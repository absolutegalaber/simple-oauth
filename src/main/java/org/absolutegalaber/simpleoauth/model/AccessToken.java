package org.absolutegalaber.simpleoauth.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class AccessToken implements INetworkToken {
    @Getter
    protected final String network;
    @Getter
    protected final AccessTokenVersion version;
    @Getter
    protected final String accessToken;
    protected Optional<String> tokenSecret = Optional.absent();
    protected Optional<String> refreshToken = Optional.absent();
    protected Optional<Date> expiresAt = Optional.absent();

    private AccessToken(String network, String accessToken, String tokenSecret) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(accessToken);
        Preconditions.checkNotNull(tokenSecret);
        this.network = network;
        this.accessToken = accessToken;
        this.tokenSecret = Optional.of(tokenSecret);
        this.version = AccessTokenVersion.OAUTH_1;
    }

    private AccessToken(String network, String accessToken, String refreshToken, Long expiresInSeconds) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(accessToken);
        this.network = network;
        this.accessToken = accessToken;
        this.refreshToken = Optional.fromNullable(refreshToken);
        if (expiresInSeconds != null) {
            Calendar expiryTime = Calendar.getInstance();
            expiryTime.add(Calendar.SECOND, expiresInSeconds.intValue());
            this.expiresAt = Optional.of(expiryTime.getTime());
        }
        this.version = AccessTokenVersion.OAUTH_2;
    }

    public static AccessToken oAuth1Token(String network, String accessToken, String tokenSecret) {
        return new AccessToken(network, accessToken, tokenSecret);
    }

    public static AccessToken oAuth2Token(String network, String accessToken, String refreshToken, Long expiresInSeconds) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(accessToken);
        return new AccessToken(network, accessToken, refreshToken, expiresInSeconds);
    }


    @Override
    public String getTokenSecret() {
        return tokenSecret.orNull();
    }

    @Override
    public String getRefreshToken() {
        return refreshToken.orNull();
    }

    @Override
    public Date getExpiresAt() {
        return expiresAt.orNull();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return Objects.equal(accessToken, that.accessToken)
                && Objects.equal(expiresAt, that.expiresAt)
                && Objects.equal(network, that.network)
                && Objects.equal(refreshToken, that.refreshToken)
                && Objects.equal(tokenSecret, that.tokenSecret)
                && Objects.equal(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(network, accessToken, network, refreshToken, tokenSecret, expiresAt, version);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("network", network)
                .add("version", version)
                .add("accessToken", accessToken)
                .toString();
    }
}
