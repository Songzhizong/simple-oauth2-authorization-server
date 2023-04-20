package cn.idealio.oauth2.server.simple.core.dto;

import cn.idealio.oauth2.server.simple.core.token.OAuthToken;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@Setter
public class OAuthTokenResult extends OAuthResult {

    /** token类型 */
    @Nullable
    private String token_type = null;

    /** token值 */
    @Nullable
    private String access_token = null;

    @Nullable
    private String scope = null;

    /** 过期时间, 单位秒 */
    @Nullable
    private Integer expires_in = null;


    @Nonnull
    public static OAuthTokenResult success(@Nonnull OAuthToken token) {
        OAuthTokenResult result = new OAuthTokenResult();
        result.setToken_type(token.getToken_type());
        result.setAccess_token(token.getAccess_token());
        result.setScope(token.getScope());
        result.setExpires_in(token.getExpires_in());
        return result;
    }

    @Nonnull
    public static OAuthTokenResult error(@Nonnull String error,
                                         @Nullable String errorDescription) {
        OAuthTokenResult result = new OAuthTokenResult();
        result.setError(error);
        result.setError_description(errorDescription);
        return result;
    }
}
