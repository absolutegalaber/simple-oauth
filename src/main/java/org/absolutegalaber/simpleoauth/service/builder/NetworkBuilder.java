package org.absolutegalaber.simpleoauth.service.builder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.absolutegalaber.simpleoauth.model.BasicUserProfile;
import org.absolutegalaber.simpleoauth.model.ClientConfig;
import org.absolutegalaber.simpleoauth.model.networks.OAuth1Network;
import org.absolutegalaber.simpleoauth.model.networks.OAuth2Network;
import org.absolutegalaber.simpleoauth.model.networks.ProfileAwareOAuth1Network;
import org.absolutegalaber.simpleoauth.model.networks.ProfileAwareOAuth2Network;
import org.absolutegalaber.simpleoauth.model.networks.v1.Twitter;
import org.absolutegalaber.simpleoauth.model.networks.v2.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class NetworkBuilder {
    private String name;
    private String authUrl;
    private String requestTokenUrl;
    private String accessTokenUrl;
    private String profileUrl;
    private Class<? extends BasicUserProfile> profileClass;
    private Map<String, String> defaultQueryParams = new HashMap<>();
    private Map<String, String> defaultHeaders = new HashMap<>();
    private Boolean isAccessTokenResponseJson = true;


    public NetworkBuilder() {
    }

    public NetworkBuilder name(String name) {
        this.name = name;
        return this;
    }

    public NetworkBuilder authorizeUrl(String authorizeUrl) {
        this.authUrl = authorizeUrl;
        return this;
    }

    public NetworkBuilder requestTokenUrl(String requestTokenUrl) {
        this.requestTokenUrl = requestTokenUrl;
        return this;
    }

    public NetworkBuilder accessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
        return this;
    }

    public NetworkBuilder profileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    public NetworkBuilder profileClass(Class<? extends BasicUserProfile> profileClass) {
        this.profileClass = profileClass;
        return this;
    }

    public NetworkBuilder addDefaultHeader(String name, String value) {
        defaultHeaders.put(name, value);
        return this;
    }

    public NetworkBuilder addDefaultQueryParam(String name, String value) {
        defaultQueryParams.put(name, value);
        return this;
    }

    public NetworkBuilder isAccessTokenResponseJson(Boolean isAccessTokenResponseJson) {
        this.isAccessTokenResponseJson = isAccessTokenResponseJson;
        return this;
    }

    public OAuth2Network buildOauth2Network(ClientConfig clientConfig) {
        Preconditions.checkNotNull(name, "Name required");
        Preconditions.checkNotNull(authUrl, "AuthorizationUrl required");
        Preconditions.checkNotNull(accessTokenUrl, "AccessTokenUrl required");
        Preconditions.checkNotNull(clientConfig, "ClientConfig required");
        OAuth2Network toReturn = new OAuth2Network(name, clientConfig, authUrl, accessTokenUrl);
        toReturn.headerDefaults(defaultHeaders);
        toReturn.queryParamsDefaults(defaultQueryParams);
        toReturn.setIsAccessTokenResponseJson(isAccessTokenResponseJson);
        return toReturn;
    }

    public ProfileAwareOAuth2Network buildProfileAwareOauth2Network(ClientConfig clientConfig) {
        Preconditions.checkNotNull(name, "Name required");
        Preconditions.checkNotNull(authUrl, "AuthorizationUrl required");
        Preconditions.checkNotNull(accessTokenUrl, "AccessTokenUrl required");
        Preconditions.checkNotNull(clientConfig, "ClientConfig required");
        Preconditions.checkNotNull(profileUrl, "Profile URL required");
        Preconditions.checkNotNull(profileClass, "Profile class required");
        ProfileAwareOAuth2Network toReturn = new ProfileAwareOAuth2Network(name, clientConfig, authUrl, accessTokenUrl, profileUrl, profileClass);
        toReturn.headerDefaults(defaultHeaders);
        toReturn.queryParamsDefaults(defaultQueryParams);
        toReturn.setIsAccessTokenResponseJson(isAccessTokenResponseJson);
        return toReturn;
    }

    public OAuth1Network buildOauth1Network(ClientConfig clientConfig) {
        Preconditions.checkNotNull(name, "Name required");
        Preconditions.checkNotNull(authUrl, "AuthorizationUrl required");
        Preconditions.checkNotNull(accessTokenUrl, "AccessTokenUrl required");
        Preconditions.checkNotNull(requestTokenUrl, "RequestTokenUrl required");
        Preconditions.checkNotNull(clientConfig, "ClientConfig required");
        OAuth1Network toReturn = new OAuth1Network(name, clientConfig, requestTokenUrl, authUrl, accessTokenUrl);
        toReturn.headerDefaults(defaultHeaders);
        toReturn.queryParamsDefaults(defaultQueryParams);
        return toReturn;
    }

    public OAuth1Network buildProfileAwareOauth1Network(ClientConfig clientConfig) {
        Preconditions.checkNotNull(name, "Name required");
        Preconditions.checkNotNull(authUrl, "AuthorizationUrl required");
        Preconditions.checkNotNull(accessTokenUrl, "AccessTokenUrl required");
        Preconditions.checkNotNull(requestTokenUrl, "RequestTokenUrl required");
        Preconditions.checkNotNull(clientConfig, "ClientConfig required");
        Preconditions.checkNotNull(profileUrl, "Profile URL required");
        Preconditions.checkNotNull(profileClass, "Profile class required");
        ProfileAwareOAuth1Network toReturn = new ProfileAwareOAuth1Network(name, clientConfig, requestTokenUrl, authUrl, accessTokenUrl, profileUrl, profileClass);
        toReturn.headerDefaults(defaultHeaders);
        toReturn.queryParamsDefaults(defaultQueryParams);
        return toReturn;
    }

    public Google google(ClientConfig clientConfig) {
        return new Google(clientConfig);
    }

    public Google google(String clientId, String secret, String callback, String state, String... scope) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClientId(clientId);
        clientConfig.setSecret(secret);
        clientConfig.setCallbackUrl(callback);
        clientConfig.setState(Optional.fromNullable(state));
        clientConfig.setScope(Optional.of(Arrays.asList(scope)));
        return new Google(clientConfig);
    }

    public Facebook facebook(ClientConfig clientConfig) {
        return new Facebook(clientConfig);
    }

    public Facebook facebook(String clientId, String secret, String callback, String state, String... scope) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClientId(clientId);
        clientConfig.setSecret(secret);
        clientConfig.setCallbackUrl(callback);
        clientConfig.setState(Optional.fromNullable(state));
        clientConfig.setScope(Optional.of(Arrays.asList(scope)));
        return new Facebook(clientConfig);
    }

    public OAuth2Network linkedIn(ClientConfig clientConfig) {
        return new LinkedIn(clientConfig);
    }

    public OAuth2Network fourSquare(ClientConfig clientConfig) {
        return new FourSquare(clientConfig);
    }

    public ProfileAwareOAuth2Network<GithubProfile> github(ClientConfig clientConfig) {
        return new Github(clientConfig);
    }

    public OAuth1Network twitter(ClientConfig clientConfig) {
        return new Twitter(clientConfig);
    }
}
