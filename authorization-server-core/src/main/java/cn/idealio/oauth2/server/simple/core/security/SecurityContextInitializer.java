package cn.idealio.oauth2.server.simple.core.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * SecurityContext初始化器接口
 *
 * @author 宋志宗 on 2023/4/20
 */
public interface SecurityContextInitializer {

    /**
     * 初始化 SecurityContext
     *
     * @param queryString
     * @param headers     HTTP请求头
     */
    @Nullable
    SecurityContext initialize(@Nonnull String queryString,
                               @Nonnull Map<String, List<String>> headers);
}
