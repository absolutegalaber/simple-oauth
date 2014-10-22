package org.absolutegalaber.simpleoauth.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Unroll
class AccessTokenTest extends Specification {
    def "OAuth1Token with exception"() {
        when:
        AccessToken.oAuth1Token(name, accesstoken, tokenSecret)
        then:
        thrown(NullPointerException)
        where:
        name   | accesstoken   | tokenSecret
        null   | 'accessToken' | 'tokenSecret'
        'name' | null          | 'tokenSecret'
        'name' | 'AccessToken' | null
    }

    def "OAuth1Token successfull"() {
        when:
        AccessToken token = AccessToken.oAuth1Token('naem', 'accessToken', 'tokenSecret')
        then:
        token != null
        token.version == AccessTokenVersion.OAUTH_1

    }

    def "OAuth2Token with exception"() {
        when:
        AccessToken.oAuth2Token(name, accesstoken, refreshToken, expiresIn)
        then:
        thrown(NullPointerException)
        where:
        name   | accesstoken   | refreshToken  | expiresIn
        null   | 'accessToken' | 'tokenSecret' | null
        'name' | null          | 'tokenSecret' | null
    }

    def "OAuth2Token with optionals null"() {
        when:
        AccessToken token = AccessToken.oAuth2Token('name', 'accesstoken', null, null)
        then:
        token.version == AccessTokenVersion.OAUTH_2
        !token.getRefreshToken()
        !token.getExpiresAt()
    }

    def "OAuth2Token with optionals not null"() {
        when:
        AccessToken token = AccessToken.oAuth2Token('name', 'accesstoken', 'refreshToken', 3600)
        then:
        token.version == AccessTokenVersion.OAUTH_2
        token.getRefreshToken()
        token.getExpiresAt()
        token.getExpiresAt().after(new Date())
    }

    def "oAuth1: Equals and hashCode and toString"() {
        when:
        AccessToken first = AccessToken.oAuth1Token(network1, accesstoken1, tokenSecret1)
        AccessToken second = AccessToken.oAuth1Token(network2, accesstoken2, tokenSecret2)

        then:
        first.toString() //check that there are no nullpointers in toString
        second.toString()
        if (shouldEqual) {
            first.equals(second)
            first.hashCode() == second.hashCode()
        } else {
            !first.equals(second)
            !first.hashCode() == second.hashCode()
        }

        where:
        network1  | network2  | accesstoken1 | accesstoken2 | tokenSecret1 | tokenSecret2 | shouldEqual
        'network' | 'network' | 'token'      | 'token'      | 'secret'     | 'secret'     | true
        'network' | 'network' | 'token'      | 'token'      | 'value'      | 'otherValue' | false
        'network' | 'network' | 'token'      | 'otherToken' | 'value'      | 'value'      | false
        'network' | 'other'   | 'token'      | 'token'      | 'value'      | 'value'      | false
    }

    def "oAuth2: Equals and hashCode and toString"() {
        when:
        AccessToken first = AccessToken.oAuth2Token(network1, accesstoken1, refreshToken1, expiresIn1)
        AccessToken second = AccessToken.oAuth2Token(network2, accesstoken2, refreshToken2, expiresIn2)

        then:
        first.toString() //check that there are no nullpointers in toString
        second.toString()
        if (shouldEqual) {
            first.equals(second)
            first.hashCode() == second.hashCode()
        } else {
            !first.equals(second)
            !first.hashCode() == second.hashCode()
        }

        where:
        network1  | network2  | accesstoken1 | accesstoken2 | refreshToken1 | refreshToken2 | expiresIn1 | expiresIn2 | shouldEqual
        'network' | 'network' | 'token'      | 'token'      | 'refresh'     | 'refresh'     | 10         | 10         | true
        'network' | 'network' | 'token'      | 'token'      | 'value'       | 'otherValue'  | 10         | 10         | false
        'network' | 'network' | 'token'      | 'token'      | 'value'       | null          | 10         | 10         | false
        'network' | 'network' | 'token'      | 'token'      | null          | 'value'       | 10         | 10         | false
        'network' | 'network' | 'token'      | 'otherToken' | 'value'       | 'value'       | 10         | 10         | false
        'network' | 'other'   | 'token'      | 'token'      | 'value'       | 'value'       | 10         | 10         | false
        'network' | 'other'   | 'token'      | 'token'      | 'value'       | 'value'       | 10         | 20         | false
        'network' | 'other'   | 'token'      | 'token'      | 'value'       | 'value'       | null       | 20         | false
        'network' | 'other'   | 'token'      | 'token'      | 'value'       | 'value'       | 10         | null       | false
    }
}
