package org.absolutegalaber.simpleoauth.service.builder;

import com.google.common.base.Preconditions;
import org.absolutegalaber.simpleoauth.model.IClientWritable;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Peter Schneider-Manzell
 */
public class ClientBuilder {

    String clientId;
    String secret;
    String callbackUrl;
    String state;
    Collection<String> scope;

    public ClientBuilder() {
    }

    public ClientBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ClientBuilder secret(String secret) {
        this.secret = secret;
        return this;
    }

    public ClientBuilder callbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public ClientBuilder state(String state) {
        this.state = state;
        return this;
    }

    public ClientBuilder withAdditionalScope(String scope) {
        if (this.scope == null) {
            this.scope = new HashSet<>();
        }
        this.scope.add(scope);
        return this;
    }

    public <T extends IClientWritable> T build(T clientObject) {
        Preconditions.checkNotNull(clientId, "ClientID required");
        Preconditions.checkNotNull(secret, "ClientSecret required");
        Preconditions.checkNotNull(callbackUrl, "CallbackUrl required");
        clientObject.setClientId(clientId);
        clientObject.setSecret(secret);
        clientObject.setCallbackUrl(callbackUrl);
        clientObject.setState(state);
        clientObject.setScope(scope);
        return clientObject;
    }
}
