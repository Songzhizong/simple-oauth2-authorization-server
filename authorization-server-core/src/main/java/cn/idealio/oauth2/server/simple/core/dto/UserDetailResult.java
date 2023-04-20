package cn.idealio.oauth2.server.simple.core.dto;

import cn.idealio.oauth2.server.simple.core.user.OAuthUserDetail;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@Setter
public class UserDetailResult extends OAuthResult {

    /**
     * 用户名, 即用户唯一标识
     * <p>
     * 可以是UserID、OpenID 或者其他唯一标识
     */
    @Nullable
    private String userId;

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

    /** 租户ID, 可选 */
    @Nullable
    private String tenantId;

    @Nonnull
    public static UserDetailResult success(@Nonnull OAuthUserDetail userDetail) {
        UserDetailResult result = new UserDetailResult();
        result.setUserId(userDetail.getUserId());
        result.setNickname(userDetail.getNickname());
        result.setPhone(userDetail.getPhone());
        result.setEmail(userDetail.getEmail());
        result.setTenantId(userDetail.getTenantId());
        return result;
    }

    @Nonnull
    public static UserDetailResult error(@Nonnull String error,
                                         @Nullable String errorDescription) {
        UserDetailResult result = new UserDetailResult();
        result.setError(error);
        result.setError_description(errorDescription);
        return result;
    }

}
