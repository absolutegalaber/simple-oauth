package org.absolutegalaber.simpleoauth.model;

/**
 * Created by Josip.Mihelko @ Gmail
 */
public interface BasicUserProfile {

    public String getNetworkName();

    public void setNetworkName(String networmName);

    public String getNetworkId();

    public String getEmail();

    public String getName();

    public String getFirstName();

    public String getLastName();

    public String getGender();

    public String getLocale();

    public String getPictureUrl();
}
