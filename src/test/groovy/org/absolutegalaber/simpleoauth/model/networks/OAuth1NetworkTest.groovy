package org.absolutegalaber.simpleoauth.model.networks

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.LowLevelHttpRequest
import com.google.api.client.http.LowLevelHttpResponse
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpRequest
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import org.absolutegalaber.simpleoauth.model.ClientConfig
import org.absolutegalaber.simpleoauth.model.IClient
import org.absolutegalaber.simpleoauth.model.INetworkToken
import org.absolutegalaber.simpleoauth.model.OAuthException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * @author Peter Schneider-Manzell
 */
class OAuth1NetworkTest extends Specification {

    OAuth1Network underTest
    String networkName
    String requestTokenUrl
    String authUrl
    String accessTokenUrl
    IClient clientConfig

    def setup() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        networkName = "dummy"
        clientConfig = new ClientConfig();
        requestTokenUrl = "http://localhost/oauth1/request_token"
        authUrl = "http://localhost/oauth1/auth"
        accessTokenUrl = "http://localhost/oauth1/access_token"
        underTest = new OAuth1Network(networkName, clientConfig, requestTokenUrl, authUrl, accessTokenUrl)
    }

    def "AuthorizationRedirect"() {
        given:

        String oauthToken = "dummy_oauth_token"
        String oauthTokenSecret = "dummy_oauth_token_secret"
        HttpServletRequest req = Mock(HttpServletRequest)
        HttpSession mockSession = Mock(HttpSession)
        underTest.httpTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContent("oauth_token=" + oauthToken + "&oauth_token_secret=" + oauthTokenSecret + "&oauth");
                        return response;
                    }
                };
            }
        }

        when:
        String result = underTest.authorizationRedirect(req)

        then:
        req.getSession() >> mockSession
        1 * mockSession.setAttribute(networkName + "_req_token", oauthToken)
        1 * mockSession.setAttribute(networkName + "_req_token_secret", oauthTokenSecret)
        result == "http://localhost/oauth1/auth?oauth_token=" + oauthToken
    }

    def "AccessToken"() {
        given:
        HttpServletRequest mockReq = Mock(HttpServletRequest)
        HttpSession mockSession = Mock(HttpSession)
        String requestToken = "1234_token"
        String requestTokenSecret = "1234_token_secret"
        String oauthVerifier = "verifyer"
        String oauthToken = "6253282-eWudHldSbIaelX7swmsiHImEL4KinwaGloHANdrY"
        String oauthTokenSecret = "2EEfA6BG3ly3sR3RjE0IBSnlQu4ZrUzPiYKmrkVU"
        underTest.httpTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContent("oauth_token=" + oauthToken + "&oauth_token_secret=" + oauthTokenSecret + "&user_id=6253282&scr");
                        return response;
                    }
                };
            }
        }

        when:
        INetworkToken result = underTest.accessToken(mockReq)

        then:
        1 * mockReq.getSession() >> mockSession
        1 * mockSession.getAttribute(networkName + "_req_token") >> requestToken
        1 * mockSession.getAttribute(networkName + "_req_token_secret") >> requestTokenSecret
        1 * mockReq.getParameter("oauth_verifier") >> oauthVerifier
        result != null
        result.network == networkName
        result.accessToken == oauthToken
        result.tokenSecret == oauthTokenSecret
    }

    def "RefreshToken"() {
        when:
        underTest.refreshToken(null)
        then:
        OAuthException ex = thrown()
    }

    def "ExecuteGet"() {
        given:
        INetworkToken networkToken = Mock(INetworkToken)
        boolean withJSONParser = true
        String requestedUrl = "http://api.twitter.com/statuses/user_timeline?screen_name=absolut1978"
        underTest.httpTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {

                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContent("[]");
                        return response;
                    }


                };
            }
        }

        when:
        HttpResponse result = underTest.executeGet(requestedUrl, networkToken, withJSONParser)

        then:
        result != null
    }
}
