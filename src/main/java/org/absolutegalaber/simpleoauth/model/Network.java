package org.absolutegalaber.simpleoauth.model;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public abstract class Network {
    protected final JacksonFactory jacksonFactory = new JacksonFactory();
    protected JsonObjectParser jsonObjectParser = new JsonObjectParser(jacksonFactory);

    @Getter
    protected final String name;
    @Getter
    protected final IClient clientConfig;

    protected Network(String name, IClient clientConfig) {
        this.name = name;
        this.clientConfig = clientConfig;
    }

    public abstract String authorizationRedirect(HttpServletRequest request) throws OAuthException;

    public abstract INetworkToken accessToken(HttpServletRequest callbackRequest) throws OAuthException;

    public abstract INetworkToken refreshToken(INetworkToken token) throws OAuthException;

    public final HttpResponse getRaw(String url, INetworkToken token) throws OAuthException {
        return executeGet(url, token, false);
    }

    public final GenericJson getAsGenericJson(String url, INetworkToken token) throws OAuthException {
        return getAs(url, token, GenericJson.class);
    }

    public final <R> R getAs(String url, INetworkToken token, Class<R> dataClass) throws OAuthException {
        try {
            return executeGet(url, token, true).parseAs(dataClass);
        } catch (IOException e) {
            throw new OAuthException(e.getMessage());
        }
    }

    public abstract HttpResponse post(String url, INetworkToken token) throws OAuthException;


    protected abstract HttpResponse executeGet(String url, INetworkToken token, boolean withJsonParser) throws OAuthException;

    public boolean isProfileAware() {
        return ProfileAware.class.isAssignableFrom(getClass());
    }
}
