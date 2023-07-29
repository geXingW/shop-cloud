package com.gexingw.shop.service.auth.service;

import org.springframework.jdbc.core.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/22 14:56
 */
public class CustomRegisteredClientRepository extends JdbcRegisteredClientRepository {
    // @formatter:off
    private static final String COLUMN_NAMES = "id, "
            + "client_id, "
            + "client_id_issued_at, "
            + "client_secret, "
            + "client_secret_expires_at, "
            + "client_name, "
            + "client_authentication_methods, "
            + "authorization_grant_types, "
            + "redirect_uris, "
            + "scopes, "
            + "client_settings,"
            + "token_settings";
    // @formatter:on

    private static final String TABLE_NAME = "auth_registered_client";

    private static final String PK_FILTER = "id = ?";

    private static final String LOAD_REGISTERED_CLIENT_SQL = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME + " WHERE ";

    // @formatter:off
    private static final String INSERT_REGISTERED_CLIENT_SQL = "INSERT INTO " + TABLE_NAME
            + "(" + COLUMN_NAMES + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // @formatter:on

    // @formatter:off
    private static final String UPDATE_REGISTERED_CLIENT_SQL = "UPDATE " + TABLE_NAME
            + " SET client_name = ?, client_authentication_methods = ?, authorization_grant_types = ?,"
            + " redirect_uris = ?, scopes = ?, client_settings = ?, token_settings = ?"
            + " WHERE " + PK_FILTER;
    // @formatter:on

    private final JdbcOperations jdbcOperations;
    private final RowMapper<RegisteredClient> registeredClientRowMapper;
    private final Function<RegisteredClient, List<SqlParameterValue>> registeredClientParametersMapper;

    /**
     * Constructs a {@code JdbcRegisteredClientRepository} using the provided parameters.
     *
     * @param jdbcOperations the JDBC operations
     */
    public CustomRegisteredClientRepository(JdbcOperations jdbcOperations) {
        super(jdbcOperations);
        this.jdbcOperations = jdbcOperations;
        this.registeredClientRowMapper = new RegisteredClientRowMapper();
        this.registeredClientParametersMapper = new RegisteredClientParametersMapper();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        RegisteredClient existingRegisteredClient = findBy(PK_FILTER,
                registeredClient.getId());
        if (existingRegisteredClient != null) {
            updateRegisteredClient(registeredClient);
        } else {
            insertRegisteredClient(registeredClient);
        }
    }

    private void updateRegisteredClient(RegisteredClient registeredClient) {
        List<SqlParameterValue> parameters = new ArrayList<>(this.registeredClientParametersMapper.apply(registeredClient));
        SqlParameterValue id = parameters.remove(0);
        parameters.remove(0); // remove client_id
        parameters.remove(0); // remove client_id_issued_at
        parameters.remove(0); // remove client_secret
        parameters.remove(0); // remove client_secret_expires_at
        parameters.add(id);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(UPDATE_REGISTERED_CLIENT_SQL, pss);
    }

    private void insertRegisteredClient(RegisteredClient registeredClient) {
        List<SqlParameterValue> parameters = this.registeredClientParametersMapper.apply(registeredClient);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(INSERT_REGISTERED_CLIENT_SQL, pss);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return findBy("id = ?", id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return findBy("client_id = ?", clientId);
    }

    private RegisteredClient findBy(String filter, Object... args) {
        List<RegisteredClient> result = this.jdbcOperations.query(
                LOAD_REGISTERED_CLIENT_SQL + filter, this.registeredClientRowMapper, args);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
