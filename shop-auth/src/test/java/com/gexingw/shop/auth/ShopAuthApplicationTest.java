package com.gexingw.shop.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 17:23
 */
@SpringBootTest(classes = ShopAuthApplication.class)
public class ShopAuthApplicationTest {

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Test
    public void testSaveClient(){
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-2")
                .clientSecret("{bcrypt}" + new BCryptPasswordEncoder().encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).redirectUri("http://oauth2login:8000/login/oauth2/code/itlab1024")
                .scope(OidcScopes.OPENID).scope("message.read")
                .scope("message.write")
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientRepository.save(registeredClient);
    }

}
