package org.absolutegalaber.simpleoauth.service.builder.v2

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Peter Schneider-Manzell
 */
class SystemPropertiesOAuth2ClientConfigTest extends Specification {

    @Shared
    String networkName = "testnetwork"


    SystemPropertiesOAuth2ClientConfig underTest



    @Unroll
    def "Get ClientId from System with systemPropertiesPrefix #systemPropertiesPrefix"() {
       given:
       underTest = new SystemPropertiesOAuth2ClientConfig(networkName,systemPropertiesPrefix)
       String clientId = "123321"
       setupSystemProperty(systemPropertiesPrefix,"clientId",clientId)

       expect:
       clientId == underTest.clientId()

       cleanup:
       clearSystemProperty(systemPropertiesPrefix,"clientId")

       where:
       systemPropertiesPrefix|_
       "test"|_
       null|_


    }

    @Unroll
    def "Get Secret from System with systemPropertiesPrefix #systemPropertiesPrefix"() {
        given:
        underTest = new SystemPropertiesOAuth2ClientConfig(networkName,systemPropertiesPrefix)
        String secret = "123321"
        setupSystemProperty(systemPropertiesPrefix,"secret",secret)

        expect:
        secret == underTest.secret()

        cleanup:
        clearSystemProperty(systemPropertiesPrefix,"secret")

        where:
        systemPropertiesPrefix|_
        "test"|_
        null|_
    }

    @Unroll
    def "Get CallbackUrl from System with systemPropertiesPrefix #systemPropertiesPrefix"() {
        given:
        underTest = new SystemPropertiesOAuth2ClientConfig(networkName,systemPropertiesPrefix)
        String callbackUrl = "123321"
        setupSystemProperty(systemPropertiesPrefix,"callbackUrl",callbackUrl)

        expect:
        callbackUrl == underTest.callbackUrl()

        cleanup:
        clearSystemProperty(systemPropertiesPrefix,"callbackUrl")

        where:
        systemPropertiesPrefix|_
        "test"|_
        null|_
    }

    def "Get State from System with systemPropertiesPrefix #systemPropertiesPrefix"() {
        given:
        underTest = new SystemPropertiesOAuth2ClientConfig(networkName,systemPropertiesPrefix)
        String state = "123321"
        setupSystemProperty(systemPropertiesPrefix,"state",state)

        expect:
        state == underTest.state()

        cleanup:
        clearSystemProperty(systemPropertiesPrefix,"state")

        where:
        systemPropertiesPrefix|_
        "test"|_
        null|_
    }

    @Unroll
    def "Get scope from System with systemPropertiesPrefix #systemPropertiesPrefix"() {
        given:
        underTest = new SystemPropertiesOAuth2ClientConfig(networkName,systemPropertiesPrefix)
        List<String> scope = ["111","222"]
        setupSystemProperty(systemPropertiesPrefix,"scope","111 222")

        expect:
        scope == underTest.scope()

        cleanup:
        clearSystemProperty(systemPropertiesPrefix,"scope")

        where:
        systemPropertiesPrefix|_
        "test"|_
        null|_
    }

    private setupSystemProperty(String systemPropertiesPrefix, String variableName, String value){
        System.setProperty(getKey(systemPropertiesPrefix,variableName),value)
    }
    private clearSystemProperty(String systemPropertiesPrefix, String variableName){
        System.clearProperty(getKey(systemPropertiesPrefix,variableName))
    }




    private String getKey(String systemPropertiesPrefix, String variableName){
        String key = networkName + "."+variableName
        if(systemPropertiesPrefix != null){
            key = systemPropertiesPrefix + "."+key
        }
        return key
    }
}
