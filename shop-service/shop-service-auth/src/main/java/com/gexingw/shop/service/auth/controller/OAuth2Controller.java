package com.gexingw.shop.service.auth.controller;

import cn.hutool.core.util.IdUtil;
import com.gexingw.shop.common.captcha.CaptchaUtil;
import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.core.util.RespCode;
import com.gexingw.shop.common.redis.util.RedisUtil;
import com.pig4cloud.captcha.SpecCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 15:17
 */
@RestController
@RequestMapping("oauth2")
public class OAuth2Controller {

    @GetMapping("captcha")
    public R<Map<String, String>> captcha() {
        // 生成验证码
        SpecCaptcha specCaptcha = CaptchaUtil.generate(130, 48, 4);

        // 缓存验证码
        String uuid = IdUtil.fastUUID();
        if (!CaptchaUtil.cacheCode(uuid, specCaptcha.text())) {
            return R.fail(RespCode.ERROR, "验证码生成失败！");
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("key", uuid);
        result.put("image", specCaptcha.toBase64());

        return R.ok(result);
    }

    /**
     * 退出登录
     *
     * @param accessToken accessToken
     * @return 执行结果
     */
    @DeleteMapping("logout")
    public R<Object> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return R.fail(RespCode.UN_AUTHORIZATION);
        }

        String tokenType = OAuth2AccessToken.TokenType.BEARER.getValue();
        if (!StringUtils.startsWith(accessToken, tokenType)) {
            return R.fail(RespCode.UN_AUTHORIZATION);
        }

        // 获取accessToken
        accessToken = accessToken.replaceFirst(tokenType, "").trim();
        // 删除access_token
        RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME, accessToken));
        // 删除认证信息
        RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_AUTH_INFO_CACHE_NAME, accessToken));

        return R.ok();
    }

}
