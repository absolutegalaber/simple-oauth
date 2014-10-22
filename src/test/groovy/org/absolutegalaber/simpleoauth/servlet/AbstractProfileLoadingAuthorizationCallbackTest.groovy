package org.absolutegalaber.simpleoauth.servlet

import org.absolutegalaber.simpleoauth.model.BasicUserProfile
import org.absolutegalaber.simpleoauth.model.INetworkToken
import org.absolutegalaber.simpleoauth.model.OAuthException
import org.absolutegalaber.simpleoauth.service.UserProfileService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Peter Schneider-Manzell
 */
class AbstractProfileLoadingAuthorizationCallbackTest extends Specification {

    AbstractProfileLoadingAuthorizationCallback underTest
    UserProfileService userProfileServiceMock
    Map<String,Object> onErrorCallParameters = [:]
    Map<String,Object> onProfileLoadedCallParameters =[:]

    def "setup"() {
        underTest = new AbstractProfileLoadingAuthorizationCallback(){

            @Override
            void onProfileLoaded(INetworkToken accessToken, BasicUserProfile userProfile, HttpServletRequest req, HttpServletResponse resp) throws OAuthException, IOException {
                onProfileLoadedCallParameters = ['accessToken':accessToken,'userProfile':userProfile,'req':req,'resp':resp]
            }

            @Override
            void onError(Exception authException, HttpServletRequest req, HttpServletResponse resp) {
                onErrorCallParameters = ['authException':authException,'req':req,'resp':resp]
            }
        }
        userProfileServiceMock = Mock(UserProfileService)
        underTest.userProfileService = userProfileServiceMock
    }


    def "OnAuthorizationSuccess"() {
        given:
        INetworkToken accessToken = Mock(INetworkToken)
        HttpServletRequest req = Mock(HttpServletRequest)
        HttpServletResponse resp = Mock(HttpServletResponse)
        BasicUserProfile userProfile = new BasicUserProfile() {
            @Override
            String getNetworkName() {
                return null
            }

            @Override
            void setNetworkName(String networmName) {

            }

            @Override
            String getNetworkId() {
                return null
            }

            @Override
            String getEmail() {
                return null
            }

            @Override
            String getName() {
                return null
            }

            @Override
            String getFirstName() {
                return null
            }

            @Override
            String getLastName() {
                return null
            }

            @Override
            String getGender() {
                return null
            }

            @Override
            String getLocale() {
                return null
            }

            @Override
            String getPictureUrl() {
                return null
            }
        }

        when:
        underTest.onAuthorizationSuccess(accessToken, req, resp)

        then:
        1 * userProfileServiceMock.userProfile(accessToken) >> userProfile
        !onProfileLoadedCallParameters.isEmpty()
        onProfileLoadedCallParameters.accessToken == accessToken
        onProfileLoadedCallParameters.userProfile == userProfile
        onErrorCallParameters.isEmpty()
    }

    def "OnAuthorizationSuccessWithProfileLoadingError"() {
        given:
        INetworkToken accessToken = Mock(INetworkToken)
        HttpServletRequest req = Mock(HttpServletRequest)
        HttpServletResponse resp = Mock(HttpServletResponse)
        OAuthException throwable =  new OAuthException("Could not load profile!")
        userProfileServiceMock.userProfile(accessToken) >> {throw throwable}

        when:
        underTest.onAuthorizationSuccess(accessToken, req, resp)

        then:
        onProfileLoadedCallParameters.isEmpty()
        !onErrorCallParameters.isEmpty()
        onErrorCallParameters.authException == throwable
    }
}
