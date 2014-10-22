package org.absolutegalaber.simpleoauth.model.networks;

import com.google.common.base.Optional;
import org.absolutegalaber.simpleoauth.model.*;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class ProfileAwareOAuth1Network<P extends BasicUserProfile> extends OAuth1Network implements ProfileAware<P> {
    private Class<P> profileClass;
    private final Optional<String> profileUrl;

    public ProfileAwareOAuth1Network(String name, IClient clientConfig, String requestTokenUrl, String authUrl, String accessTokenUrl, String profileUrl, Class<P> profileClass) {
        super(name, clientConfig, requestTokenUrl, authUrl, accessTokenUrl);
        this.profileClass = profileClass;
        this.profileUrl = Optional.fromNullable(profileUrl);
    }

    @Override
    public String getProfileUrl() {
        return profileUrl.get();
    }

    @Override
    public P loadUserProfile(INetworkToken token) throws OAuthException {
        return getAs(getProfileUrl(), token, profileClass);
    }
}
