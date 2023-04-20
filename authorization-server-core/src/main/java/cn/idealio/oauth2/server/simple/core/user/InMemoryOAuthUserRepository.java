package cn.idealio.oauth2.server.simple.core.user;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class InMemoryOAuthUserRepository implements OAuthUserRepository {
    private final Map<String, OAuthUser> userIdMap = new HashMap<>();
    private final Map<String, OAuthUser> usernameMap = new HashMap<>();

    public InMemoryOAuthUserRepository(@Nonnull List<OAuthUser> users) {
        for (OAuthUser user : users) {
            this.userIdMap.put(user.userId(), user);
            this.usernameMap.put(user.username(), user);
        }
    }

    @Nullable
    @Override
    public OAuthUser findByUserId(@Nonnull String userId) {
        return userIdMap.get(userId);
    }

    @Nullable
    @Override
    public OAuthUser findByUsername(@Nonnull String username) {
        return usernameMap.get(username);
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<OAuthUser> users = new ArrayList<>();


        @Nonnull
        public Builder add(@Nonnull OAuthUser user) {
            users.add(user);
            return this;
        }

        @Nonnull
        public InMemoryOAuthUserRepository build() {
            return new InMemoryOAuthUserRepository(users);
        }
    }
}
