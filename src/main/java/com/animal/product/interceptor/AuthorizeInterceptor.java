package com.animal.product.interceptor;

import com.animal.product.common.ErrorCode;
import com.animal.product.exception.BusinessException;
import com.animal.product.utils.JwtUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/20 23:21
 */
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwtToken)){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        try {
            JwtUtil.checkToken(jwtToken);
            return true;
        } catch (TokenExpiredException e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "Token已经过期");
        } catch (SignatureVerificationException e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "签名错误");
        } catch (AlgorithmMismatchException e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "加密算法不匹配");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "无效token");
        }
    }
}
