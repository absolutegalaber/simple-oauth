package org.absolutegalaber.simpleoauth.model;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public interface ProfileAware<P extends BasicUserProfile> {
    public P loadUserProfile(INetworkToken token) throws OAuthException;

    public String getProfileUrl();
}
