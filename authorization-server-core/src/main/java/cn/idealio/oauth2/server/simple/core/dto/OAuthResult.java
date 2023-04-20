package cn.idealio.oauth2.server.simple.core.dto;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@Setter
public class OAuthResult {

    @Nullable
    private String error;

    @Nullable
    private String error_description;

    @Nonnull
    public static OAuthResult error(@Nonnull String error,
                                    @Nullable String errorDescription) {
        OAuthResult result = new OAuthResult();
        result.setError(error);
        result.setError_description(errorDescription);
        return result;
    }
}
