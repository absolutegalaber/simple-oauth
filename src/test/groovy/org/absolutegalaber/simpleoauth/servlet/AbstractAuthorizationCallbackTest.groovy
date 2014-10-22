package org.absolutegalaber.simpleoauth.servlet

import org.absolutegalaber.simpleoauth.model.INetworkToken
import org.absolutegalaber.simpleoauth.model.Network
import org.absolutegalaber.simpleoauth.model.OAuthException
import org.absolutegalaber.simpleoauth.service.INetworkService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Peter Schneider-Manzell
 */
class AbstractAuthorizationCallbackTest extends Specification {

    AbstractAuthorizationCallback underTest
    Map<String, Object> onErrorParameters = [:]
    Map<String, Object> onAuthorizationSuccessParameters = [:]
    INetworkService networkServiceMock

    def setup() {
        networkServiceMock = Mock(INetworkService)
        underTest = new AbstractAuthorizationCallback(networkServiceMock) {
            @Override
            void onError(Exception authException, HttpServletRequest req, HttpServletResponse resp) {
                onErrorParameters = ['authException': authException, 'req': req, 'resp': resp]
            }

            @Override
            void onAuthorizationSuccess(INetworkToken accessToken, HttpServletRequest req, HttpServletResponse resp) {
                onAuthorizationSuccessParameters = ['accessToken': accessToken, 'req': req, 'resp': resp]
            }
        }
    }

    def "DoGet"() {
        given:
        HttpServletRequest reqMock = Mock(HttpServletRequest)
        HttpServletResponse respMock = Mock(HttpServletResponse)
        Network network = Mock(Network,constructorArgs: ["dummynetwork",null])
        INetworkToken accessToken = Mock(INetworkToken)

        when:
        underTest.doGet(reqMock, respMock)

        then:
        1 * networkServiceMock.fromSession(reqMock) >> network
        1* network.accessToken(reqMock) >> accessToken
        onAuthorizationSuccessParameters.accessToken == accessToken
        onAuthorizationSuccessParameters.req == reqMock
        onAuthorizationSuccessParameters.resp == respMock
        onErrorParameters.isEmpty()
    }

    def "DoGetWithException"() {
        given:
        HttpServletRequest reqMock = Mock(HttpServletRequest)
        HttpServletResponse respMock = Mock(HttpServletResponse)

        when:
        underTest.doGet(reqMock, respMock)

        then:
        networkServiceMock.fromSession(reqMock) >> {throw new OAuthException("Dummy exception")}
        onAuthorizationSuccessParameters.isEmpty()
        !onErrorParameters.isEmpty()
    }
}
