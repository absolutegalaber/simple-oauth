package org.absolutegalaber.simpleoauth.model.networks.v1;

import org.absolutegalaber.simpleoauth.model.ClientConfig;
import org.absolutegalaber.simpleoauth.model.networks.ProfileAwareOAuth1Network;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class Twitter extends ProfileAwareOAuth1Network<TwitterProfile> {

    public static final String NAME = "twitter";
    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String AUTH_URL = "https://api.twitter.com/oauth/authorize";
    private static final String PROFILE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";

    public Twitter(ClientConfig clientConfig) {
        super(
                NAME,
                clientConfig,
                REQUEST_TOKEN_URL,
                AUTH_URL,
                ACCESS_TOKEN_URL,
                PROFILE_URL,
                TwitterProfile.class
        );
    }
}
