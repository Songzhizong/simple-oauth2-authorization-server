package cn.idealio.oauth2.server.simple.core.user;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * OAuth用户信息接口
 *
 * @author 宋志宗 on 2023/4/20
 */
public interface OAuthUser {

    @Nonnull
    static OAuthUserImpl of(@Nonnull String userId, @Nonnull String username) {
        OAuthUserImpl user = new OAuthUserImpl();
        user.setUserId(userId);
        user.setUsername(username);
        return user;
    }

    /**
     * 用户唯一标识
     * <p>
     * 可以是UserID、OpenID 或者其他唯一标识
     *
     * @return 用户唯一标识
     */
    @Nonnull
    String userId();

    /**
     * 用户名, 可选
     * <p>
     * 用于登录系统的唯一标识, 可能是账号/邮箱/手机号
     *
     * @return 用户名
     */
    @Nullable
    String username();

    /**
     * 用户密码, 可选
     *
     * @return 用户密码
     */
    @Nullable
    String password();

    /**
     * 用户昵称, 可选
     * <p>
     * 可以是真实姓名、昵称
     *
     * @return 用户昵称
     */
    @Nullable
    String nickname();

    /**
     * 手机号码, 可选
     *
     * @return 手机号码
     */
    @Nullable
    String phone();

    /**
     * 邮箱, 可选
     *
     * @return 邮箱
     */
    @Nullable
    String email();
}
