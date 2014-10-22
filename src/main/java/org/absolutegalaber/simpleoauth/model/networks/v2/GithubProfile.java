package org.absolutegalaber.simpleoauth.model.networks.v2;

import com.google.api.client.util.Key;
import lombok.Data;
import org.absolutegalaber.simpleoauth.model.BasicUserProfile;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Data
public class GithubProfile implements BasicUserProfile {
    private String networkName;
    @Key("id")
    private String networkId;
    @Key("email")
    private String email;
    @Key("name")
    private String name;
    @Key("given_name")
    private String firstName;
    @Key("family_name")
    private String lastName;
    @Key("gender")
    private String gender;
    @Key("locale")
    private String locale;
    @Key("avatar_url")
    private String pictureUrl;
}
