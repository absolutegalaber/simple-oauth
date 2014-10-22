package org.absolutegalaber.simpleoauth.service.builder.v2

import spock.lang.Specification

/**
 * @author Peter Schneider-Manzell
 */
class HardcodedOAuth2ClientConfigTest extends Specification {

    HardcodedOAuth2ClientConfig config;
    String clientId = "dummy_client_id"
    String secret = "dummy_client_secret"
    String callbackUrl = "dummy_callback_url"
    String state = "dummy_state"
    String[] scopes = ["email"]

    def setup() {
        config = new HardcodedOAuth2ClientConfig(clientId, secret, callbackUrl, state, scopes)
    }


    def "ClientId"() {
        expect:
        config.clientId() == clientId
    }

    def "Secret"() {
        expect:
        config.secret() == secret

    }

    def "CallbackUrl"() {
        expect:
        config.callbackUrl() == callbackUrl
    }

    def "Scope"() {
        expect:
        config.scope() == scopes
    }

    def "State"() {
        expect:
        config.state() == state
    }
}
