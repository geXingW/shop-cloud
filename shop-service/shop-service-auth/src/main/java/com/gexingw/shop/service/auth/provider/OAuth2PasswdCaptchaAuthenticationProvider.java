package com.gexingw.shop.service.auth.provider;

import com.gexingw.shop.service.auth.token.OAuth2PasswdCaptchaAuthenticationToken;
import com.gexingw.shop.service.auth.util.RsaUtil;
import com.gexingw.shop.common.captcha.CaptchaUtil;
import com.gexingw.shop.common.core.util.RespCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.ArrayList;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:51
 */
public class OAuth2PasswdCaptchaAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

    public final static String GRANT_TYPE_PASSWORD_CAPTCHA = "password_captcha";

    public final static String PARAM_USERNAME = "username";

    public final static String PARAM_PASSWORD = "password";

    public OAuth2PasswdCaptchaAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super(authorizationService, tokenGenerator, userDetailsService, passwordEncoder);
    }

    @Override
    protected Authentication getAuthenticatedInfo(Authentication authentication) {
        OAuth2PasswdCaptchaAuthenticationToken passwdCaptchaAuthenticationToken = (OAuth2PasswdCaptchaAuthenticationToken) authentication;

        // 查询登录信息并校验
        Map<String, Object> parameters = passwdCaptchaAuthenticationToken.getAdditionalParameters();
        String captchaCode = (String) parameters.getOrDefault("code", "");
        if (StringUtils.isBlank(captchaCode)) {
            throwError(RespCode.INVALID_CAPTCHA);
        }

        String uuid = (String) parameters.getOrDefault("key", "");
        if (StringUtils.isBlank(uuid)) {
            throwError(RespCode.INVALID_CAPTCHA);
        }

        if (!CaptchaUtil.checkCode(uuid, captchaCode)) {
            throwError(RespCode.INVALID_CAPTCHA);
        }

        String username = (String) parameters.getOrDefault(PARAM_USERNAME, "");
        if (StringUtils.isBlank(username)) {
            throwError(RespCode.INVALID_USERNAME_OR_PASSWORD);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throwError(RespCode.INVALID_USERNAME_OR_PASSWORD);
        }

        String decodePassword = this.decodePassword((String) parameters.getOrDefault(PARAM_PASSWORD, ""));
        //noinspection DataFlowIssue
        if (!passwordEncoder.matches(decodePassword, userDetails.getPassword())) {
            throwError(RespCode.INVALID_USERNAME_OR_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), new ArrayList<>());
    }

    @Override
    protected String getGrantType() {
        return GRANT_TYPE_PASSWORD_CAPTCHA;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswdCaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public String decodePassword(String encodePassword) {
        try {
            return RsaUtil.decryptByPrivateKey(encodePassword);
        } catch (Exception e) {
            throwError(RespCode.INVALID_USERNAME_OR_PASSWORD);
            return "";
        }
    }

}
