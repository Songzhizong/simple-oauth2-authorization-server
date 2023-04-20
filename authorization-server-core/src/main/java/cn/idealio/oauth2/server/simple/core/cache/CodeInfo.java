package cn.idealio.oauth2.server.simple.core.cache;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author 宋志宗 on 2023/4/20
 */
@Getter
@SuppressWarnings("ClassCanBeRecord")
public class CodeInfo {
    @Nonnull
    private final String code;
    @Nonnull
    private final String userId;
    @Nullable
    private final String tenantId;

    public CodeInfo(@Nonnull String code,
                    @Nonnull String userId,
                    @Nullable String tenantId) {
        this.code = code;
        this.userId = userId;
        this.tenantId = tenantId;
    }

    @Nonnull
    public static CodeInfo of(@Nonnull String userId,
                              @Nullable String tenantId) {
        String code = UUID.randomUUID().toString().replace("-", "");
        return new CodeInfo(code, userId, tenantId);
    }
}
