package cn.idealio.oauth2.server.simple.core.token;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * OAuth2 Token
 *
 * @author 宋志宗 on 2023/4/20
 */
@Getter
public class OAuthToken {
    public static final String TOKEN_TYPE = "Bearer";

    /** token类型 */
    @Nonnull
    private final String token_type = TOKEN_TYPE;

    /** token值 */
    @Nonnull
    private final String access_token;

    @Nullable
    private final String scope;

    /** 过期时间, 单位秒 */
    private final int expires_in;

    public OAuthToken(@Nonnull String accessToken,
                      @Nullable String scope,
                      int expiresIn) {
        this.access_token = accessToken;
        this.scope = scope;
        this.expires_in = expiresIn;
    }
}
