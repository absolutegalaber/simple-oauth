package org.absolutegalaber.simpleoauth.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Peter Schneider-Manzell
 */
public interface IClient extends Serializable{

    String clientId();

    String secret();

    String callbackUrl();

    String state();

    Collection<String> scope();
}
