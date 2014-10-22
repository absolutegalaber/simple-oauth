package org.absolutegalaber.simpleoauth.model.v2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.common.base.Optional;

import java.io.IOException;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class ConfigurableQueryParameterAccessMethod implements Credential.AccessMethod {
    static final String DEFAULT_PARAM_NAME = "access_token";
    private Optional<String> configuredParamName;

    public ConfigurableQueryParameterAccessMethod(String configuredParamName) {
        this.configuredParamName = Optional.fromNullable(configuredParamName);
    }

    @Override
    public void intercept(HttpRequest httpRequest, String accessToken) throws IOException {
        httpRequest.getUrl().set(configuredParamName.or(DEFAULT_PARAM_NAME), accessToken);
    }

    @Override
    public String getAccessTokenFromRequest(HttpRequest httpRequest) {
        Object param = httpRequest.getUrl().get(configuredParamName.or(DEFAULT_PARAM_NAME));
        return param == null ? null : param.toString();
    }
}
