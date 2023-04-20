package cn.idealio.oauth2.server.simple.core.exception;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class UnauthorizedException extends OAuthException {

    public UnauthorizedException() {
        super(401, "unauthorized", "unauthorized");
    }

    public UnauthorizedException(@Nullable String errorDescription) {
        super(401, "unauthorized", errorDescription);
    }


}
