package com.gexingw.shop.service.auth.controller;

import cn.hutool.core.util.IdUtil;
import com.gexingw.shop.common.captcha.CaptchaUtil;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.core.util.RespCode;
import com.pig4cloud.captcha.SpecCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
