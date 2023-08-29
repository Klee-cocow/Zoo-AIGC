package com.animal.product.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.animal.product.utils.CommonToolUtils;
import com.animal.product.common.ErrorCode;
import com.animal.product.constant.IdentityEnum;
import com.animal.product.constant.UserConstant;
import com.animal.product.exception.BusinessException;
import com.animal.product.mapper.ZooUsersMapper;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserLoginRequest;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.model.vo.UserVO;
import com.animal.product.service.ZooUsersService;
import com.animal.product.strategy.UserStrategyContent;
import com.animal.product.utils.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author 咏鹅、AllianceTing
 * @description 针对表【zoo_users】的数据库操作Service实现
 * @createDate 2023-07-19 17:58:01
 */
@Service
public class ZooUsersServiceImpl extends ServiceImpl<ZooUsersMapper, ZooUsers>
        implements ZooUsersService {

    //邮箱校验
    public final String validPattern = ".*[[ _`=|\\[\\]~……——+|{}‘]|\\n|\\r|\\t].*";

    private final Logger log = LoggerFactory.getLogger("ZooUserService");

    @Resource
    private ZooUsersMapper zooUsersMapper;

    @Resource
    private CommonToolUtils commonToolUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Integer userRegister(UserRegisterRequest userRegisterRequest, String registerIdentity, HttpServletRequest request) {

        //查找用户是否已经注册
        String res = redisTemplate.opsForValue().get(userRegisterRequest.getEmail());
        if (res != null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "当前账户已存在");
        }
        QueryWrapper<ZooUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userRegisterRequest.getEmail());
        long count = zooUsersMapper.selectCount(queryWrapper);
        if (count > 0) {
            int userOut = RandomUtil.randomInt(1, 9);
            redisTemplate.opsForValue().set(userRegisterRequest.getEmail(), "1", userOut, TimeUnit.MINUTES);
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "当前账户已存在");
        }

        //判断验证码
        String invite_code = userRegisterRequest.getInvite_code();
        String code = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE + userRegisterRequest.getEmail());
        if (code == null) {
            throw new BusinessException(ErrorCode.NO_QUERY, "邮箱验证码不存在");
        }

        if (invite_code == null || (code == null && invite_code == null) || !invite_code.equals(code)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "请输入正确的邮箱验证码");
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userRegisterRequest, userDTO);
        //统一处理 邮箱注册和手机号注册的情况
        ZooUsers user = UserStrategyContent.doUserRegister(IdentityEnum.valueOf(registerIdentity)).doEmailOrPhone(userDTO, registerIdentity);

        //获取用户ip
        String ipAddr = CommonToolUtils.getIpAddr(request);
        Long timeMillis = System.currentTimeMillis();
        Timestamp currentDate = CommonToolUtils.getCurrentDate();
        user.setIp(ipAddr);
        user.setUpdateTime(currentDate);
        user.setCreateTime(currentDate);
        user.setName(timeMillis.toString());
        user.setDescription(UserConstant.USER_DESCRIPTION);  //默认设置

        try {
            this.save(user);
        } catch (Exception e) {

            log.error("保存用户失败了 时间：" + currentDate, user);
            throw new BusinessException(ErrorCode.NO_SAVE);
        }

        return user.getId();
    }

    @Override
    public String userLogin(UserLoginRequest userLoginRequest, String loginIdentity, HttpServletRequest request) {

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userLoginRequest, userDTO);
        //统一处理 邮箱注册和手机号注册的情况
        ZooUsers user = UserStrategyContent.doUserLogin(IdentityEnum.valueOf(loginIdentity)).doEmailOrPhone(userDTO, loginIdentity);


        QueryWrapper<ZooUsers> queryWrapper = new QueryWrapper<>();

        if (user.getEmail() != null)
            queryWrapper.eq("email", userLoginRequest.getEmail());

        if(user.getPhone() != null){
            queryWrapper.eq("phone", userLoginRequest.getEmail());
        }

        String phone_code = user.getPhone_code();
        //如果是手机登录，拿到手机验证码，然后去查询是否对应上了 如果正确对应则放行

        if (user.getPassword() != null){
            queryWrapper.eq("password", user.getPassword());
        }


        user = zooUsersMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_QUERY, "此用户不存在");
        }
        UserVO userResult = new UserVO();

        //赋值传递脱敏
        BeanUtils.copyProperties(user, userResult);
        //登录成功后设置jwt令牌
        String token = JwtUtil.generateToken(userResult);
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE+token,token,7,TimeUnit.DAYS);


        return token;
    }

    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        Boolean flag = JwtUtil.checkToken(token);
        if(!flag){
            throw new BusinessException(ErrorCode.NO_AUTH,"token过期");
        }
        String t = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE + token);
        if(!token.equals(t)) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }

        DecodedJWT userJwt = JwtUtil.getToken(token);
        UserVO user = new UserVO();
        String name = userJwt.getClaim("Name").asString();
        String phone = userJwt.getClaim("Phone").asString();
        String email = userJwt.getClaim("Email").asString();
        String avatar = userJwt.getClaim("Avatar").asString();
        String money = userJwt.getClaim("Money").asString();
        String description = userJwt.getClaim("Description").asString();
        Integer id = userJwt.getClaim("Id").asInt();
        user.setId(id);
        user.setEmail(email);
        user.setName(name);
        user.setAvatar(avatar);
        user.setRemember_token(null);  //暂无邀请码
        user.setPhone(phone);
        user.setDescription(description);
        BigDecimal moneyDecimal = new BigDecimal(money);
        user.setMoney(moneyDecimal);
        return user;
    }

    @Override
    //生成验证码发往用户邮箱
    public String generateCodeToEmail(String email) {
        //校验邮箱是否合法
        if (email == null) throw new BusinessException(ErrorCode.PARAMETER_ERROR, "邮箱不合法");
        //redis中查询是否已经有这个邮箱存在
        String judgeCode = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE + email);
        if (!(judgeCode == null)) {
            return judgeCode;
        }
        //避免恶意调用
        String res = redisTemplate.opsForValue().get(email);
        if (res != null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "当前账户已存在");
        }

        //查找用户是否已经注册
        QueryWrapper<ZooUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        long count = zooUsersMapper.selectCount(queryWrapper);
        if (count > 0) {
            int userOut = RandomUtil.randomInt(1, 9);
            redisTemplate.opsForValue().set(email, "1", userOut, TimeUnit.MINUTES);
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "当前账户已存在");
        }

        String code = RandomUtil.randomNumbers(4);
        String text = "你正在我们的网站进行注册操作，如果不是您所操作的请无视掉这条消息，验证码是: " + code + "请在5分钟内输入";
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE + email, code, 5, TimeUnit.MINUTES);
        try {
            commonToolUtils.sendMail(email, text);
        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送邮件失败");
        }

        return code;
    }

    @Override
    public Boolean logoutUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE + token,"",30,TimeUnit.MINUTES);
        return true;
    }


}




