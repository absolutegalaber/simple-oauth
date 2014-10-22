package org.absolutegalaber.simpleoauth.model.networks.v2;

import com.google.api.client.auth.oauth2.Credential;
import org.absolutegalaber.simpleoauth.model.IClient;
import org.absolutegalaber.simpleoauth.model.networks.OAuth2Network;
import org.absolutegalaber.simpleoauth.model.v2.ConfigurableQueryParameterAccessMethod;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class FourSquare extends OAuth2Network {

    public static final String NAME = "foursquare";
    private static final String AUTH_URL = "https://foursquare.com/oauth2/authorize";
    private static final String ACCESS_TOKEN_URL = "https://foursquare.com/oauth2/access_token";
    private static final String PROFILE_URL = "https://api.foursquare.com/v2/users/self";

    public FourSquare(IClient config) {
        super(
                NAME,
                config,
                AUTH_URL,
                ACCESS_TOKEN_URL
        );
        defaultQueryParams.put("v", "20140701");
    }

    @Override
    protected Credential.AccessMethod myAccessMethod() {
        return new ConfigurableQueryParameterAccessMethod("oauth_token");
    }
}
