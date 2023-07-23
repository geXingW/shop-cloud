package com.gexingw.shop.auth.service;

import org.springframework.jdbc.core.*;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.sql.Types;
import java.util.List;
import java.util.function.Function;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/22 14:48
 */
public class CustomOAuth2AuthorizationConsentService extends JdbcOAuth2AuthorizationConsentService {

    private final JdbcOperations jdbcOperations;

    private final RowMapper<OAuth2AuthorizationConsent> authorizationConsentRowMapper;

    private final Function<OAuth2AuthorizationConsent, List<SqlParameterValue>> authorizationConsentParametersMapper;

    // @formatter:off
    private static final String COLUMN_NAMES = "registered_client_id, "
            + "principal_name, "
            + "authorities";
    // @formatter:on

    private static final String TABLE_NAME = "auth_authorization_consent";

    private static final String PK_FILTER = "registered_client_id = ? AND principal_name = ?";

    // @formatter:off
    private static final String LOAD_AUTHORIZATION_CONSENT_SQL = "SELECT " + COLUMN_NAMES
            + " FROM " + TABLE_NAME
            + " WHERE " + PK_FILTER;
    // @formatter:on

    // @formatter:off
    private static final String SAVE_AUTHORIZATION_CONSENT_SQL = "INSERT INTO " + TABLE_NAME
            + " (" + COLUMN_NAMES + ") VALUES (?, ?, ?)";
    // @formatter:on

    // @formatter:off
    private static final String UPDATE_AUTHORIZATION_CONSENT_SQL = "UPDATE " + TABLE_NAME
            + " SET authorities = ?"
            + " WHERE " + PK_FILTER;
    // @formatter:on

    private static final String REMOVE_AUTHORIZATION_CONSENT_SQL = "DELETE FROM " + TABLE_NAME + " WHERE " + PK_FILTER;

    /**
     * Constructs a {@code JdbcOAuth2AuthorizationConsentService} using the provided parameters.
     *
     * @param jdbcOperations             the JDBC operations
     * @param registeredClientRepository the registered client repository
     */
    public CustomOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        super(jdbcOperations, registeredClientRepository);
        this.jdbcOperations = jdbcOperations;
        this.authorizationConsentRowMapper = new OAuth2AuthorizationConsentRowMapper(registeredClientRepository);
        this.authorizationConsentParametersMapper = new OAuth2AuthorizationConsentParametersMapper();
    }


    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        OAuth2AuthorizationConsent existingAuthorizationConsent = findById(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        if (existingAuthorizationConsent == null) {
            insertAuthorizationConsent(authorizationConsent);
        } else {
            updateAuthorizationConsent(authorizationConsent);
        }
    }

    private void updateAuthorizationConsent(OAuth2AuthorizationConsent authorizationConsent) {
        List<SqlParameterValue> parameters = this.authorizationConsentParametersMapper.apply(authorizationConsent);
        SqlParameterValue registeredClientId = parameters.remove(0);
        SqlParameterValue principalName = parameters.remove(0);
        parameters.add(registeredClientId);
        parameters.add(principalName);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(UPDATE_AUTHORIZATION_CONSENT_SQL, pss);
    }

    private void insertAuthorizationConsent(OAuth2AuthorizationConsent authorizationConsent) {
        List<SqlParameterValue> parameters = this.authorizationConsentParametersMapper.apply(authorizationConsent);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(SAVE_AUTHORIZATION_CONSENT_SQL, pss);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        SqlParameterValue[] parameters = new SqlParameterValue[]{
                new SqlParameterValue(Types.VARCHAR, authorizationConsent.getRegisteredClientId()),
                new SqlParameterValue(Types.VARCHAR, authorizationConsent.getPrincipalName())
        };
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters);
        this.jdbcOperations.update(REMOVE_AUTHORIZATION_CONSENT_SQL, pss);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        SqlParameterValue[] parameters = new SqlParameterValue[]{
                new SqlParameterValue(Types.VARCHAR, registeredClientId),
                new SqlParameterValue(Types.VARCHAR, principalName)};
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters);
        List<OAuth2AuthorizationConsent> result = this.jdbcOperations.query(LOAD_AUTHORIZATION_CONSENT_SQL, pss, this.authorizationConsentRowMapper);
        return !result.isEmpty() ? result.get(0) : null;
    }


}
