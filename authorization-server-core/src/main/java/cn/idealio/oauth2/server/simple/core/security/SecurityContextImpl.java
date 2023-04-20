package cn.idealio.oauth2.server.simple.core.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
@SuppressWarnings("ClassCanBeRecord")
public class SecurityContextImpl implements SecurityContext {
    @Nonnull
    private final String userId;
    @Nullable
    private final String tenantId;

    public SecurityContextImpl(@Nonnull String userId,
                               @Nullable String tenantId) {
        this.userId = userId;
        this.tenantId = tenantId;
    }

    @Nonnull
    @Override
    public String userId() {
        return userId;
    }

    @Nullable
    @Override
    public String tenantId() {
        return tenantId;
    }
}
