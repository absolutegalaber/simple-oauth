package org.absolutegalaber.simpleoauth.model.networks

import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.LowLevelHttpRequest
import com.google.api.client.http.LowLevelHttpResponse
import com.google.api.client.json.Json
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpRequest
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import com.google.common.base.Optional
import org.absolutegalaber.simpleoauth.model.ClientConfig
import org.absolutegalaber.simpleoauth.model.IClient
import org.absolutegalaber.simpleoauth.model.INetworkToken
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

/**
 * @author Peter Schneider-Manzell
 */
class OAuth2NetworkTest extends Specification {

    OAuth2Network underTest
    String authUrl = "http://localhost/auth"
    String accessTokenUrl = "http://localhost/token"
    String clientId = "dummy_client_id"
    IClient clientConfig
    String state = "dummyState"
    def scope = ['email']
    String networkName = "dummy"

    def setup() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        clientConfig = new ClientConfig()
        clientConfig.clientId = clientId
        clientConfig.scope = Optional.of(scope)
        clientConfig.state = Optional.of(state)
        underTest = new OAuth2Network(networkName, clientConfig, authUrl, accessTokenUrl)
    }

    def "AuthorizationRedirect"() {
        given:
        HttpServletRequest mockReq = Mock(HttpServletRequest)
        when:
        def result = underTest.authorizationRedirect(mockReq)

        then:
        result == "http://localhost/auth?client_id=dummy_client_id&response_type=code&scope=email&state=dummyState"
    }

    def "AccessToken"() {
        given:
        String serverSideAccessToken = "dummy_token"
        HttpServletRequest mockReq = Mock(HttpServletRequest)
        underTest.httpTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setContent("{\"access_token\":\"" + serverSideAccessToken + "\"}");
                        return response;
                    }
                };
            }
        }

        when:
        INetworkToken result = underTest.accessToken(mockReq)

        then:
        1 * mockReq.getRequestURL() >> new StringBuffer("http://localhost/callback?code=123")
        result.accessToken == serverSideAccessToken
        result.network == networkName
    }

    def "NetworkSpecific"() {

    }

    def "RefreshToken"() {

    }

    def "ExecuteGet"() {
        given:
        String remoteURL = "http://localhost/api/profile"
        INetworkToken networkToken = Mock(INetworkToken)
        boolean withJSONParser = true
        underTest.httpTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setContent("{}");
                        return response;
                    }
                };
            }
        }

        when:
        HttpResponse result = underTest.executeGet(remoteURL,networkToken,withJSONParser)

        then:
        result != null
    }

    def "Post"() {

    }

    def "ExtractAuthCode"() {
        given:
        HttpServletRequest mockReq = Mock(HttpServletRequest)
        String code = "123"

        when:
        String result = underTest.extractAuthCode(mockReq)

        then:
        1 * mockReq.getRequestURL() >> new StringBuffer("http://localhost/callback?code=" + code)
        result == code

    }


    def "MyAccessMethod"() {
        expect:
        underTest.myAccessMethod().getClass() == BearerToken.authorizationHeaderAccessMethod().getClass()
    }

    def "HeaderDefaults"() {

    }

    def "QueryParamsDefaults"() {

    }

    def "SetIsAccessTokenResponseJson"() {

    }
}
