package org.absolutegalaber.simpleoauth.model;


import java.util.Collection;

/**
 * @author Peter Schneider-Manzell
 */
public interface IClientWritable extends IClient {
    void setClientId(String clientId);

    void setSecret(String secret);

    void setCallbackUrl(String callbackUrl);

    void setState(String state);

    void setScope(Collection<String> scope);
}
