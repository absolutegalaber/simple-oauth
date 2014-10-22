package org.absolutegalaber.simpleoauth.service;

import lombok.extern.slf4j.Slf4j;
import org.absolutegalaber.simpleoauth.model.*;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Slf4j
public class UserProfileService {
    private NetworkService networkService = NetworkService.getNetworkSerice();

    public BasicUserProfile userProfile(INetworkToken accessToken) throws OAuthException {
        Network network = networkService.fromAccessToken(accessToken);
        if (!network.isProfileAware()) {
            throw new OAuthException(network.getName() + " is not configured to load profiles");
        }
        ProfileAware profileAware = (ProfileAware) network;
        BasicUserProfile userProfile = profileAware.loadUserProfile(accessToken);
        userProfile.setNetworkName(network.getName());
        return userProfile;
    }
}
