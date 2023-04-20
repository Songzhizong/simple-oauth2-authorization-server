package cn.idealio.oauth2.server.simple.core.exception;

import javax.annotation.Nonnull;
import java.io.Serial;

/**
 * 无效的客户端密钥异常
 *
 * @author 宋志宗 on 2023/4/20
 */
public class ClientNotFoundException extends OAuthException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ClientNotFoundException(@Nonnull String clientId) {
        super(400, "client_not_found", "client not found: " + clientId);
    }
}
