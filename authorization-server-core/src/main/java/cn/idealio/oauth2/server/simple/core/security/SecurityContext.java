package cn.idealio.oauth2.server.simple.core.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
public interface SecurityContext {

    @Nonnull
    static SecurityContext of(@Nonnull String userId, @Nullable String tenantId) {
        return new SecurityContextImpl(userId, tenantId);
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return 当前用户
     */
    @Nonnull
    String userId();

    /**
     * 获取当前登录用户的租户ID
     *
     * @return 当前租户ID
     */
    @Nullable
    String tenantId();

}
