package cn.idealio.oauth2.server.simple.core.token;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@SuppressWarnings("ClassCanBeRecord")
public class TokenInfo {
    @Nonnull
    private final String clientId;
    @Nonnull
    private final String userId;
    @Nullable
    private final String tenantId;
    @Nonnull
    private final Set<String> scopes;

    public TokenInfo(@Nonnull String clientId,
                     @Nonnull String userId,
                     @Nullable String tenantId,
                     @Nonnull Set<String> scopes) {
        this.clientId = clientId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.scopes = scopes;
    }
}
