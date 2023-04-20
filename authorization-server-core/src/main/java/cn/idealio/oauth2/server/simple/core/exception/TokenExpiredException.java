package cn.idealio.oauth2.server.simple.core.exception;

import java.io.Serial;

/**
 * 无效的客户端密钥异常
 *
 * @author 宋志宗 on 2023/4/20
 */
public class TokenExpiredException extends OAuthException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TokenExpiredException() {
        super(401, "token_expired", "Token已过期");
    }
}
