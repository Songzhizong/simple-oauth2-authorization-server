package cn.idealio.oauth2.server.simple.core.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;

/**
 * OAuth2异常
 *
 * @author 宋志宗 on 2023/4/4
 */
public class OAuthException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int httpStatus;

    @Nonnull
    private final String error;

    @Nullable
    private final String errorDescription;

    public OAuthException(int httpStatus, @Nonnull String error, @Nullable String errorDescription) {
        super(error + ": " + errorDescription);
        this.httpStatus = httpStatus;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public OAuthException(@Nonnull Throwable throwable) {
        super(throwable);
        this.httpStatus = 500;
        this.error = "server_error";
        String name = throwable.getClass().getName();
        this.errorDescription = name + ": " + throwable.getMessage();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Nonnull
    public String getError() {
        return error;
    }

    @Nullable
    public String getErrorDescription() {
        return errorDescription;
    }
}
