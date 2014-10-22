package org.absolutegalaber.simpleoauth.service.builder.v2;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import org.absolutegalaber.simpleoauth.model.ClientConfig;

import java.util.Collection;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class SystemPropertiesOAuth2ClientConfig extends ClientConfig {

    private final String networkName;
    private final Optional<String> systemPropertiesPrefix;

    public SystemPropertiesOAuth2ClientConfig(String networkName, String systemPropertiesPrefix) {
        this.networkName = networkName;
        this.systemPropertiesPrefix = Optional.fromNullable(systemPropertiesPrefix);
    }

    @Override
    public Collection<String> scope() {
        return Splitter.on(" ").trimResults().splitToList(System.getProperty(propName("scope")));
    }

    @Override
    public String clientId() {
        return System.getProperty(propName("clientId"));
    }

    @Override
    public String secret() {
        return System.getProperty(propName("secret"));
    }

    @Override
    public String callbackUrl() {
        return System.getProperty(propName("callbackUrl"));
    }

    @Override
    public String state() {
        return System.getProperty(propName("state"));
    }

    private String propName(String prop) {
        String toReturn = networkName + "." + prop;
        if (systemPropertiesPrefix.isPresent()) {
            return systemPropertiesPrefix.get() + "." + toReturn;
        }
        return toReturn;
    }
}
