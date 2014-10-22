package org.absolutegalaber.simpleoauth.service.builder.v2;

import org.absolutegalaber.simpleoauth.model.ClientConfig;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class HardcodedOAuth2ClientConfig extends ClientConfig {
    private final String clientId;
    private final String secret;
    private final String callbackUrl;
    private final String state;
    private final Collection<String> scope;

    public HardcodedOAuth2ClientConfig(String clientId, String secret, String callbackUrl, String state, String... scopes) {
        this.clientId = clientId;
        this.secret = secret;
        this.callbackUrl = callbackUrl;
        this.state = state;
        this.scope = Arrays.asList(scopes);
    }

    @Override
    public String clientId() {
        return clientId;
    }

    @Override
    public String secret() {
        return secret;
    }

    @Override
    public String callbackUrl() {
        return callbackUrl;
    }

    @Override
    public Collection<String> scope() {
        return scope;
    }

    @Override
    public String state() {
        return state;
    }
}
