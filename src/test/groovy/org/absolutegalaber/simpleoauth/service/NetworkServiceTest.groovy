package org.absolutegalaber.simpleoauth.service

import com.google.api.client.http.HttpResponse
import org.absolutegalaber.simpleoauth.model.AccessToken
import org.absolutegalaber.simpleoauth.model.INetworkToken
import org.absolutegalaber.simpleoauth.model.Network
import org.absolutegalaber.simpleoauth.model.OAuthException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * @author Peter Schneider-Manzell
 */
class NetworkServiceTest extends Specification {

    NetworkService underTest
    String networkName = "testnetwork"
    Network dummyNetwork
    final String SESSION_KEY = "com.simple.oauth.model.Network"
    final String REQUEST_KEY = "network"

    void setup() {
        underTest = new NetworkService()
        dummyNetwork = new Network(networkName,null){
            @Override
            String authorizationRedirect(HttpServletRequest request) throws OAuthException {
                return null
            }

            @Override
            AccessToken accessToken(HttpServletRequest callbackRequest) throws OAuthException {
                return null
            }

            @Override
            AccessToken refreshToken(INetworkToken token) throws OAuthException {
                return null
            }

            @Override
            HttpResponse post(String url, INetworkToken token) throws OAuthException {
                return null
            }

            @Override
            String getProfileUrl() {
                return null
            }

            @Override
            protected HttpResponse executeGet(String url, INetworkToken token, boolean withJsonParser) throws OAuthException {
                return null
            }
        }
        underTest.configureUnmutableNetworks([dummyNetwork]);
    }

    def "Add network and get from name"() {
       expect:
       underTest.fromName(networkName) != null
    }


    def "Add network and FromRequestParam"() {
      setup:
      HttpServletRequest mockRequest = Mock(HttpServletRequest)
      mockRequest.getParameter(REQUEST_KEY) >> networkName

      expect:
      underTest.fromRequestParam(mockRequest) != null
    }

    def "FromRequestParam with NULL param"() {
        setup:
        HttpServletRequest mockRequest = Mock(HttpServletRequest)
        mockRequest.getParameter(REQUEST_KEY) >> null

        when:
        underTest.fromRequestParam(mockRequest)

        then:
        OAuthException e = thrown()
        e != null
    }

    def "ToSession"() {
       setup:
       HttpServletRequest mockRequest = Mock(HttpServletRequest)
       HttpSession mockSession = Mock(HttpSession)
       mockRequest.getSession() >> mockSession

       when:
       underTest.toSession(mockRequest,dummyNetwork)

       then:
       1* mockSession.setAttribute(SESSION_KEY,networkName)
    }

    def "Add network and FromSession"() {
        setup:
        HttpServletRequest mockRequest = Mock(HttpServletRequest)
        HttpSession mockSession = Mock(HttpSession)
        mockRequest.getSession() >> mockSession
        mockSession.getAttribute(SESSION_KEY) >> networkName

        when:
        Network network = underTest.fromSession(mockRequest)

        then:
        network != null
    }
    def "FromSession with NULL attribute"() {
        setup:
        HttpServletRequest mockRequest = Mock(HttpServletRequest)
        HttpSession mockSession = Mock(HttpSession)
        mockRequest.getSession() >> mockSession
        mockSession.getAttribute(SESSION_KEY) >> null

        when:
        underTest.fromSession(mockRequest)

        then:
        OAuthException e = thrown()
        e != null
    }
}
