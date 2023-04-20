package cn.idealio.oauth2.server.simple.core.user;

import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Setter
@Accessors(chain = true)
public class OAuthUserImpl implements OAuthUser {

    /**
     * 用户名, 即用户唯一标识
     * <p>
     * 可以是UserID、OpenID 或者其他唯一标识
     */
    @Nonnull
    private String userId;

    @Nullable
    private String username;

    @Nullable
    private String password;

    /**
     * 用户昵称, 可选
     * <p>
     * 可以是真实姓名、昵称
     */
    @Nullable
    private String nickname;

    /** 手机号码, 可选 */
    @Nullable
    private String phone;

    /** 邮箱, 可选 */
    @Nullable
    private String email;


    @Nonnull
    @Override
    public String userId() {
        return userId;
    }

    @Nullable
    @Override
    public String username() {
        return username;
    }

    @Nullable
    @Override
    public String password() {
        return password;
    }

    @Nullable
    @Override
    public String nickname() {
        return nickname;
    }

    @Nullable
    @Override
    public String phone() {
        return phone;
    }

    @Nullable
    @Override
    public String email() {
        return email;
    }
}
