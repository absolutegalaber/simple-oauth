package org.absolutegalaber.simpleoauth.model.networks;

import com.google.api.client.auth.oauth.*;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.extern.slf4j.Slf4j;
import org.absolutegalaber.simpleoauth.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Slf4j
public class OAuth1Network extends Network {
    private final String requestTokenUrl;
    private final String authUrl;
    private final String accessTokenUrl;

    protected final Map<String, String> defaultQueryParams = new HashMap<>();
    protected final Map<String, String> defaultHeaders = new HashMap<>();
    protected HttpTransport httpTransport;


    public OAuth1Network(String name, IClient clientConfig, String requestTokenUrl, String authUrl, String accessTokenUrl) {
        super(name, clientConfig);
        this.requestTokenUrl = requestTokenUrl;
        this.authUrl = authUrl;
        this.accessTokenUrl = accessTokenUrl;
        httpTransport = new NetHttpTransport();

    }


    @Override
    public String authorizationRedirect(HttpServletRequest request) throws OAuthException {
        try {
            //signer
            OAuthHmacSigner signer = new OAuthHmacSigner();
            signer.clientSharedSecret = clientConfig.secret();
            //get a request token
            OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(requestTokenUrl);
            temporaryToken.signer = signer;
            temporaryToken.callback = clientConfig.callbackUrl();
            temporaryToken.transport = httpTransport;
            temporaryToken.consumerKey = clientConfig.clientId();
            OAuthCredentialsResponse temporaryTokenResponse = temporaryToken.execute();
            OAuthAuthorizeTemporaryTokenUrl accessTempToken = new OAuthAuthorizeTemporaryTokenUrl(authUrl);
            accessTempToken.temporaryToken = temporaryTokenResponse.token;
            request.getSession().setAttribute(name + "_req_token", temporaryTokenResponse.token);
            request.getSession().setAttribute(name + "_req_token_secret", temporaryTokenResponse.tokenSecret);
            return accessTempToken.build();
        } catch (IOException e) {
            log.error("Could not generate authorizationRedirect!", e);
            throw new OAuthException("Could not generate authorizationRedirect!", e);
        }
    }

    @Override
    public INetworkToken accessToken(HttpServletRequest callbackRequest) throws OAuthException {
        try {
            HttpSession session = callbackRequest.getSession();
            String requestToken = (String) session.getAttribute(name + "_req_token");
            String requestTokenSecret = (String) session.getAttribute(name + "_req_token_secret");
            String oauthVerifier = callbackRequest.getParameter("oauth_verifier");

            OAuthHmacSigner signer = new OAuthHmacSigner();
            signer.clientSharedSecret = clientConfig.secret();
            signer.tokenSharedSecret = requestTokenSecret;

            OAuthGetAccessToken getAccessToken = new OAuthGetAccessToken(accessTokenUrl);
            getAccessToken.signer = signer;
            getAccessToken.temporaryToken = requestToken;
            getAccessToken.transport = httpTransport;
            getAccessToken.verifier = oauthVerifier;
            getAccessToken.consumerKey = clientConfig.clientId();
            OAuthCredentialsResponse accessTokenResponse = getAccessToken.execute();
            return AccessToken.oAuth1Token(name, accessTokenResponse.token, accessTokenResponse.tokenSecret);
        } catch (IOException e) {
            log.error("Could not load accessToken!", e);
            throw new OAuthException("Could not load accessToken!", e);
        }
    }

    @Override
    public INetworkToken refreshToken(INetworkToken token) throws OAuthException {
        throw new OAuthException("Refreshing not part of OAuth 1.0");
    }

    @Override
    protected HttpResponse executeGet(String url, INetworkToken token, boolean withJsonParser) throws OAuthException {
        try {
            OAuthHmacSigner signer = new OAuthHmacSigner();
            signer.clientSharedSecret = clientConfig.secret();
            signer.tokenSharedSecret = token.getTokenSecret();

            OAuthParameters parameters = new OAuthParameters();
            parameters.consumerKey = clientConfig.clientId();
            parameters.token = token.getAccessToken();
            parameters.signer = signer;

            HttpRequestFactory factory = httpTransport.createRequestFactory(parameters);
            HttpRequest req = factory.buildGetRequest(new GenericUrl(url));
            addDefaultParamsAndHeaders(req);
            if (withJsonParser) {
                req.setParser(jsonObjectParser);
            }
            return req.execute();
        } catch (IOException e) {
            log.error("Could not execute GET request!", e);
            throw new OAuthException("Could not execute GET request!", e);
        }
    }


    @Override
    public HttpResponse post(String url, INetworkToken token) throws OAuthException {
        return null;
    }

    private void addDefaultParamsAndHeaders(HttpRequest httpRequest) {
        for (Map.Entry<String, String> paramToAdd : defaultQueryParams.entrySet()) {
            httpRequest.getUrl().put(paramToAdd.getKey(), paramToAdd.getValue());
        }
        for (Map.Entry<String, String> headerToAdd : defaultHeaders.entrySet()) {
            httpRequest.getHeaders().put(headerToAdd.getKey(), headerToAdd.getValue());
        }
    }

    public void headerDefaults(Map<String, String> defaults) {
        defaultHeaders.putAll(defaults);
    }

    public void queryParamsDefaults(Map<String, String> defaults) {
        defaultQueryParams.putAll(defaults);
    }


}
