package org.absolutegalaber.simpleoauth.service;

import org.absolutegalaber.simpleoauth.model.Network;
import org.absolutegalaber.simpleoauth.model.OAuthException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Peter Schneider-Manzell
 */
public interface INetworkService {
    Network fromSession(HttpServletRequest req) throws OAuthException;
}
