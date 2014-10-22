package org.absolutegalaber.simpleoauth.servlet;

import org.absolutegalaber.simpleoauth.model.Network;
import org.absolutegalaber.simpleoauth.model.OAuthException;
import org.absolutegalaber.simpleoauth.service.NetworkService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public abstract class AbstractAuthorizationRedirect extends HttpServlet {
    private NetworkService networkService = NetworkService.getNetworkSerice();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Network network = networkService.fromRequestParam(req);
            networkService.toSession(req, network);
            String authorizationRedirect = network.authorizationRedirect(req);
            beforeRedirect(req,resp,network);
            resp.sendRedirect(authorizationRedirect);
        } catch (Exception e) {
            onError(e, req, resp);
        }
    }


    public void beforeRedirect(HttpServletRequest req, HttpServletResponse resp, Network network) throws OAuthException {
    }



    public abstract void onError(Exception authException, HttpServletRequest req, HttpServletResponse resp);
}
