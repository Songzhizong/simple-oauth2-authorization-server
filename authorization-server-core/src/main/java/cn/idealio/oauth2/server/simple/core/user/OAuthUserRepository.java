package cn.idealio.oauth2.server.simple.core.user;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
public interface OAuthUserRepository {

    @Nullable
    OAuthUser findByUserId(@Nonnull String userId);

    @Nullable
    OAuthUser findByUsername(@Nonnull String username);
}
