package cn.idealio.oauth2.server.simple.core.client;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
public interface RegisteredClientRepository {

    /**
     * 根据客户端ID获取客户端信息
     *
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    @Nullable
    RegisteredClient findByClientId(String clientId);
}
