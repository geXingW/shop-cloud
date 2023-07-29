package com.gexingw.shop.auth.util;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/23 19:27
 */
@SuppressWarnings("unused")
public class RsaUtil {

    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArPCt5JguBsVO8xOSW3jjQIk7qFyjbSxdLaeb4M8P46on5dSXZPph+wfKmNK1rzPNSNzCIzM5H+wDCrxJ8QAfnNTzoQ+NxLkoVtVy/eKF1qFOGdZQn4m68hDbVsrzwaPrX/LDs+VsX39sKOLvogLw738LNgrJCQMUbQcSiPb3N675UVMaDjixzzLZGvAWVsFBp+fyQRs+GeozgPdzx4dT2AsdMghCzV/6DQYqIQ78bKs4ZaVD6D3j4JKEn6byeSYXBmVw7UwWHN7v+AzVTPO79o8ohGtxlv70awZri4qkPEoyMKvtoesy+mG9toc0+GgrM0zRBaLnV3coYOV2ZNia4QIDAQAB";

    private static final String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCs8K3kmC4GxU7zE5JbeONAiTuoXKNtLF0tp5vgzw/jqifl1Jdk+mH7B8qY0rWvM81I3MIjMzkf7AMKvEnxAB+c1POhD43EuShW1XL94oXWoU4Z1lCfibryENtWyvPBo+tf8sOz5Wxff2wo4u+iAvDvfws2CskJAxRtBxKI9vc3rvlRUxoOOLHPMtka8BZWwUGn5/JBGz4Z6jOA93PHh1PYCx0yCELNX/oNBiohDvxsqzhlpUPoPePgkoSfpvJ5JhcGZXDtTBYc3u/4DNVM87v2jyiEa3GW/vRrBmuLiqQ8SjIwq+2h6zL6Yb22hzT4aCszTNEFoudXdyhg5XZk2JrhAgMBAAECggEAR0pP5NP25UOg8AM5agvDYm8v8I9/rxmCr7dQQrQY0LjsCYpBfpbYnSrxEV6LFtY74bVfMjODcO1Rs7M6p+ZtIbGHXPWUc2wAfQswhAhFM9z63RvD0IBuWCsqv/SZ11FDTiI9GoOQ0S0t4Qzry9V6jL/KV1TK2rQyT1+C/IMSFhmSktkSnq1WoJ12Yfn1wlj8psEDYXN8Z7lzMTX4RKqXSw7NgG/bEMg7VVhmqpRyKvsolSEk+T+WtN7oWS9L+pUaPcfy/gdyVl2CJ7Xc2+gaMov9D/+Cb0kgz7oiszsvaDpWizqHo2SoPaioUUb28x86oRG16sREtZh/jNcJl8iCoQKBgQD92nh2NAwp36BzlfU9oCdrLWqQu0JyuF/JNE3Zx9cWLnfkOD1L5jX9gCSRiDnGk99CrUVSBLLmYRHOvs9LqHa54UCmYYSG7mKXwLJV8FC7/JF1S3KsG9X8sgzNRp/iNKlECJlIzqWPu7kEEUa7JmIo1XSEpXCEMXu+maiwG4sbgwKBgQCuZw06LgRNlXl3MBKnBNh2vJ2UI3XPmIViY+yZ0nr8pHAxyANBfa9xixovk38zRlzx2fDeKAd3M6SEhvBQ1RJSbdJhuWRmfnandfhIw5x/nDwrL+hMWmCvUZ9PJtd6XCMG0Ryy8e7hTwZdrGJvakLT5ORJEtphYxvity4BzlLuywKBgCdJy8EHlyZX7P5WpCkQg2lMEyUpRkR5EqISTUY6bqiJMpcIuTvo+kwsq/w2KDL6qeAo7wSdo46SUW49C9o6zX1BRn85sogrlujDsxYn5mbh1SVhMvM+L6U3bVHL72zUF6pm5kplaSxQsnbVsau/agvF1Y2+CSb8fZlnMLto1+O/AoGASzxJiH+xe4awD6q9YS/F8fMMR4vElvnFENzqhLE4PSTzAYa3YNlHhkPr1/pZNVV1YyARuMSr89BjeIh9w2EntkeKKn4/wbdsmHFb2ogIS0Sq8DaKj+WtL/SWNR8cdbypn5c2OjwEQU5GAMMM8+NzUNW9ZYisjLakoPlE76KCRGECgYEA1QQK0Ikh/aWDUIlN+PLLegRRsJDtzJYwQV2S49UB5PoaNcpRHjriy5T6OA6sqyNYiPHgxOrcx+4GHqp8cX5htNW1zQelea/JJHDjoYK8dwIOs9CPL5LU7KvY+xq/tjwnbNtld+iDNidz4nc6FBC5GXEmDVXG1vZFBWTkhYxb7oY=";

    /**
     * 公钥解密
     *
     * @param text 待解密的信息
     * @return 铭文
     * @throws Exception /
     */
    public static String decryptByPublicKey(String text) throws Exception {
        Cipher cipher = getRsaCipher();
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
        return new String(cipher.doFinal(Base64.decode(text)));
    }

    /**
     * 公钥加密
     *
     * @param text 待加密的文本
     * @return /
     */
    public static String encryptByPublicKey(String text) throws Exception {
        Cipher cipher = getRsaCipher();
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return Base64.encode(cipher.doFinal(text.getBytes()));
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText 私钥
     * @param text           待加密的信息
     * @return /
     * @throws Exception /
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        Cipher cipher = getRsaCipher();
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey());
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encode(result);
    }

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return /
     * @throws Exception /
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        Cipher cipher = getRsaCipher();
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        byte[] result = cipher.doFinal(Base64.decode(text));
        return new String(result);
    }

    public static Cipher getRsaCipher() throws Exception {
        return Cipher.getInstance("RSA");
    }

    public static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decode(publicKey));
        return KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec2);
    }

    public static PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        return KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec5);
    }

}
