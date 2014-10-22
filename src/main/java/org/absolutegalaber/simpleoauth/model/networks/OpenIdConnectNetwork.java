package org.absolutegalaber.simpleoauth.model.networks;

import org.absolutegalaber.simpleoauth.model.ClientConfig;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class OpenIdConnectNetwork extends OAuth2Network {

    public OpenIdConnectNetwork(String name, ClientConfig config, String authUrl, String accessTokenUrl) {
        super(name, config, authUrl, accessTokenUrl);
    }
}
