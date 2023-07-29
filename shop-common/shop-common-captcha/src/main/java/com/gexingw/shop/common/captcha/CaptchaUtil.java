package com.gexingw.shop.common.captcha;

import com.gexingw.shop.common.redis.util.RedisUtil;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;

import java.time.Duration;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/23 15:04
 */
public class CaptchaUtil {

    private static final String CAPTCHA_CACHE_KEY = "cache:captcha:%s:code";

    public static SpecCaptcha generate(int width, int height, int length) {
        SpecCaptcha specCaptcha = new SpecCaptcha();
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        specCaptcha.setHeight(height);
        specCaptcha.setWidth(width);
        specCaptcha.setLen(length);

        return specCaptcha;
    }

    /**
     * 缓存验证码
     *
     * @param uuid key
     * @param code code
     * @return 执行结果
     */
    public static boolean cacheCode(String uuid, String code) {
        return RedisUtil.set(getRedisKey(uuid), code, Duration.ofMinutes(10).getSeconds());
    }

    /**
     * 检查验证码
     *
     * @param uuid key
     * @param code code
     * @return 执行结果
     */
    public static boolean checkCode(String uuid, String code) {
        String cacheCode = RedisUtil.get(getRedisKey(uuid));

        return (cacheCode != null && cacheCode.equals(code));
    }

    private static String getRedisKey(String uuid) {
        return String.format(CAPTCHA_CACHE_KEY, uuid);
    }

}
