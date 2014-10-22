package org.absolutegalaber.simpleoauth.model.networks.v2;

import org.absolutegalaber.simpleoauth.model.IClient;
import org.absolutegalaber.simpleoauth.model.networks.ProfileAwareOAuth2Network;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class Facebook extends ProfileAwareOAuth2Network<FacebookProfile> {

    public static final String NAME = "facebook";
    private static final String AUTH_URL = "https://graph.facebook.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";
    private static final String PROFILE_URL = "https://graph.facebook.com/v2.0/me";

    public Facebook(IClient config) {
        super(
                NAME,
                config,
                AUTH_URL,
                ACCESS_TOKEN_URL,
                PROFILE_URL,
                FacebookProfile.class
        );
        isAccessTokenResponseJson = false;
    }
}
