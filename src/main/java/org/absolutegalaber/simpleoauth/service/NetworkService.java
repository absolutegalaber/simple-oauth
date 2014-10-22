package org.absolutegalaber.simpleoauth.service;

import lombok.extern.slf4j.Slf4j;
import org.absolutegalaber.simpleoauth.model.INetworkToken;
import org.absolutegalaber.simpleoauth.model.Network;
import org.absolutegalaber.simpleoauth.model.OAuthException;
import org.absolutegalaber.simpleoauth.model.networks.NetworkIdentifier;
import org.absolutegalaber.simpleoauth.service.builder.NetworkConfigurationService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Slf4j
public class NetworkService implements INetworkService {
    protected static final Map<String, Network> networks = new HashMap<>();

    protected NetworkConfigurationService configurationService;

    private static NetworkService networkService;

    private NetworkService() {
    }

    public static NetworkService getNetworkSerice() {
        if(networkService == null){
            networkService = new NetworkService();
        }
        return networkService;
    }


    public static void configureUnmutableNetworks(Iterable<Network> configuredNetworks) {
        log.info("Configuring unmutable networks");
        for (Network configuredNetwork : configuredNetworks) {
            if (!networks.containsKey(configuredNetwork.getName())) {
                networks.put(configuredNetwork.getName(), configuredNetwork);
            }
        }
        log.info("Configured Networks: {}", networks);
    }


    public void configureDynamicNetworks(NetworkConfigurationService configurationService) {
        log.info("Setting configurationService to " + configurationService.getClass());
        this.configurationService = configurationService;
    }

    public Network fromName(String name) throws OAuthException {
        if (name == null) {
            throw new OAuthException("Cannot load network for name NULL!");
        }

        Network network = networks.get(name);

        if (network == null && configurationService != null) {
            NetworkIdentifier identifier = NetworkIdentifier.byKey(name);
            if (identifier != null) {
                network = configurationService.getNetwork(identifier);
            }
        } else if (configurationService == null) {
            log.info("No configuration service defined!");
        }
        if (network == null) {
            log.warn("No network for name {} found", name);
            throw new OAuthException(name + " is not configured");
        }
        log.info("Found network {} for name {}", network, name);
        return network;
    }

    public Network fromAccessToken(INetworkToken accessToken) throws OAuthException {
        return fromName(accessToken.getNetwork());
    }

    public Network fromRequestParam(HttpServletRequest request) throws OAuthException {
        String networkName = request.getParameter("network");
        log.info("Detected network name {} in request under parameter name ", networkName, "network");
        return fromName(networkName);
    }

    public void toSession(HttpServletRequest request, Network network) {
        log.info("Storing network name {} in session under key {}", network.getName(), "com.simple.oauth.model.Network");
        request.getSession().setAttribute("com.simple.oauth.model.Network", network.getName());
    }

    public Network fromSession(HttpServletRequest request) throws OAuthException {
        String networkName = (String) request.getSession().getAttribute("com.simple.oauth.model.Network");
        log.info("Detected network name {} in session under key {}", networkName, "com.simple.oauth.model.Network");
        return fromName(networkName);
    }

}
