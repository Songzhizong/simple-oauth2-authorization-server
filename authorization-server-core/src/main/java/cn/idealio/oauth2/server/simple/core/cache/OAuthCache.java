package cn.idealio.oauth2.server.simple.core.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
public interface OAuthCache {

    void saveCode(@Nonnull CodeInfo codeInfo);

    @Nullable
    CodeInfo loadCode(@Nonnull String code);
}
