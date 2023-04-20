package cn.idealio.oauth2.server.simple.core.exception;

import javax.annotation.Nonnull;
import java.io.Serial;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class ForbiddenException extends OAuthException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenException(@Nonnull String scope) {
        super(403, "permission_denied", "No permissions are specified: " + scope);

    }
}
