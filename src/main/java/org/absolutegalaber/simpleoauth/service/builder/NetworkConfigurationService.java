package org.absolutegalaber.simpleoauth.service.builder;

import org.absolutegalaber.simpleoauth.model.Network;
import org.absolutegalaber.simpleoauth.model.networks.NetworkIdentifier;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public interface NetworkConfigurationService {
    public Iterable<Network> loadNetworks();

    public Network getNetwork(NetworkIdentifier networkIdentifier);

}
