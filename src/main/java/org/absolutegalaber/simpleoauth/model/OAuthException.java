package org.absolutegalaber.simpleoauth.model;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public class OAuthException extends Exception {
    public OAuthException(String message,Throwable cause) {
        super(message,cause);
    }
    public OAuthException(String message) {
        super(message);
    }
}
