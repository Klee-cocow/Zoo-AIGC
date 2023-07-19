package com.animal.product.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.animal.product.common.CommonToolUtils;
import com.animal.product.common.ErrorCode;
import com.animal.product.constant.IdentityEnum;
import com.animal.product.constant.UserConstant;
import com.animal.product.exception.BusinessException;
import com.animal.product.mapper.ZooUsersMapper;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.service.ZooUsersService;
import com.animal.product.strategy.UserStrategyContent;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


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
    private RedisTemplate<String,String> redisTemplate;



    @Override
    public Integer userRegister(UserRegisterRequest userRegisterRequest, String registerIdentity , HttpServletRequest request)
    {
        //统一处理 邮箱注册和手机号注册的情况
        ZooUsers user = UserStrategyContent.doUserRegister(IdentityEnum.valueOf(registerIdentity)).doEmailOrPhone(userRegisterRequest, registerIdentity);

        QueryWrapper<ZooUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        long count = zooUsersMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "当前账户已存在");
        }

        //获取用户ip
        String ipAddr = CommonToolUtils.getIpAddr(request);
        Long timeMillis = System.currentTimeMillis();
        Date date = DateUtil.date();

        user.setIp(ipAddr);
        user.setUpdateTime(date);
        user.setCreateTime(date);
        user.setName(timeMillis.toString());

        try {
            this.save(user);
        } catch (Exception e) {

            log.error("保存用户失败了 时间：" + date, user);
            throw new BusinessException(ErrorCode.NO_SAVE);
        }

        return user.getId();
    }

    @Override
    public UserDTO userLogin(String userAccount, String password, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "邮箱或密码不能为空");
        }
        Pattern compile = Pattern.compile(validPattern);
        //判断是否拥有特殊字符
        boolean m = compile.matcher(userAccount).matches();
        if (m) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "邮箱不能拥有特殊字符");
        }
        m = compile.matcher(password).matches();
        if (m) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "密码不能拥有特殊字符");
        }
        String entryPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());

        QueryWrapper<ZooUsers> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("email", userAccount);
        queryWrapper.eq("password", entryPassword);
        ZooUsers user = zooUsersMapper.selectOne(queryWrapper);
        UserDTO userResult = new UserDTO();
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_QUERY, "此用户不存在");
        }
        BeanUtils.copyProperties(user, userResult);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, userResult);

        return userResult;
    }

    @Override
    public UserDTO getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "未登录");
        }
        return (UserDTO) userObj;
    }

    @Override
    //生成验证码发往用户邮箱
    public String generateCodeToEmail(String email) {
        //校验邮箱是否合法
        if(email == null) throw new BusinessException(ErrorCode.PARAMETER_ERROR,"邮箱不合法");
        //redis中查询是否已经有这个邮箱存在
        String judgeCode = redisTemplate.opsForValue().get(UserConstant.USER_LOGIN_STATE);
        if(!judgeCode.isEmpty()){
            return judgeCode;
        }
        String code = RandomUtil.randomNumbers(4);
        String text = "你正在我们的网站进行注册操作，如果不是您所操作的请无视掉这条消息，验证码是: "+code+"请在5分钟内输入";
        redisTemplate.opsForValue().set(UserConstant.USER_LOGIN_STATE + email,code,5, TimeUnit.MINUTES);
        try {
            commonToolUtils.sendMail(email,text);
        }catch (MessagingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发送邮件失败");
        }

        return code;
    }


}




