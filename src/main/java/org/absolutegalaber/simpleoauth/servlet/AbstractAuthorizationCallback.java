package org.absolutegalaber.simpleoauth.servlet;

import lombok.extern.slf4j.Slf4j;
import org.absolutegalaber.simpleoauth.model.INetworkToken;
import org.absolutegalaber.simpleoauth.model.Network;
import org.absolutegalaber.simpleoauth.model.OAuthException;
import org.absolutegalaber.simpleoauth.service.INetworkService;
import org.absolutegalaber.simpleoauth.service.NetworkService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Slf4j
public abstract class AbstractAuthorizationCallback extends HttpServlet {
    protected final INetworkService networkService;


    public AbstractAuthorizationCallback() {
        this(NetworkService.getNetworkSerice());
    }

    protected AbstractAuthorizationCallback(INetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.info("Trying to detect network stored in session...");
            Network network = networkService.fromSession(req);
            log.info("Trying to extract access token...");
            INetworkToken accessToken = network.accessToken(req);
            log.info("Detected access token...");
            onAuthorizationSuccess(accessToken, req, resp);
        } catch (OAuthException e) {
            onError(e, req, resp);
        }
    }

    public abstract void onError(Exception authException, HttpServletRequest req, HttpServletResponse resp);

    public abstract void onAuthorizationSuccess(INetworkToken accessToken, HttpServletRequest req, HttpServletResponse resp);

}
