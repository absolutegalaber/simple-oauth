package org.absolutegalaber.simpleoauth.model.networks.v1;

import com.google.api.client.util.Key;
import lombok.Data;
import org.absolutegalaber.simpleoauth.model.BasicUserProfile;

/**
 * Created by Josip.Mihelko @ Gmail
 */
@Data
public class TwitterProfile implements BasicUserProfile{
    private String networkName;
    @Key("id_str")
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
    @Key("lang")
    private String locale;
    @Key("profile_image_url")
    private String pictureUrl;
}
