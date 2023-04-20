package cn.idealio.oauth2.server.simple.core.user;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * OAuth用户详情
 *
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@SuppressWarnings("ClassCanBeRecord")
public class OAuthUserDetail {

    /**
     * 用户名, 即用户唯一标识
     * <p>
     * 可以是UserID、OpenID 或者其他唯一标识
     */
    @Nonnull
    private final String userId;

    /**
     * 用户昵称, 可选
     * <p>
     * 可以是真实姓名、昵称
     */
    @Nullable
    private final String nickname;

    /** 手机号码, 可选 */
    @Nullable
    private final String phone;

    /** 邮箱, 可选 */
    @Nullable
    private final String email;

    /** 租户ID, 可选 */
    @Nullable
    private final String tenantId;

    public OAuthUserDetail(@Nonnull String userId,
                           @Nullable String nickname,
                           @Nullable String phone,
                           @Nullable String email,
                           @Nullable String tenantId) {
        this.userId = userId;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.tenantId = tenantId;
    }
}
