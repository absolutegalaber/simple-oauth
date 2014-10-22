package org.absolutegalaber.simpleoauth.model;

import java.util.Date;

/**
 * @author Peter Schneider-Manzell
 */
public interface INetworkToken {
    String getAccessToken();

    String getTokenSecret();

    String getNetwork();

    String getRefreshToken();

    Date getExpiresAt();
}
