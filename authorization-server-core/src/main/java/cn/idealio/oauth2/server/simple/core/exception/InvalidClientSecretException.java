package cn.idealio.oauth2.server.simple.core.exception;

import java.io.Serial;

/**
 * 无效的客户端密钥异常
 *
 * @author 宋志宗 on 2023/4/20
 */
public class InvalidClientSecretException extends OAuthException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidClientSecretException() {
        super(400, "invalid_client_secret", "invalid client secret");
    }
}
