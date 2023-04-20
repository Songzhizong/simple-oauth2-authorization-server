package cn.idealio.oauth2.server.simple.core.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class InMemoryRegisteredClientRepository implements RegisteredClientRepository, AsyncRegisteredClientRepository {
    @Nonnull
    private final Map<String, RegisteredClient> clientMap;

    public InMemoryRegisteredClientRepository(@Nonnull Map<String, RegisteredClient> clientMap) {
        this.clientMap = clientMap;
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }


    @Nullable
    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientMap.get(clientId);
    }

    @Nonnull
    @Override
    public CompletableFuture<RegisteredClient> findByClientIdAsync(String clientId) {
        return CompletableFuture.completedFuture(findByClientId(clientId));
    }

    public static class Builder {
        @Nonnull
        private final Map<String, RegisteredClient> clientMap = new HashMap<>();

        @Nonnull
        public Builder addClient(@Nonnull RegisteredClient registeredClient) {
            clientMap.put(registeredClient.getClientId(), registeredClient);
            return this;
        }

        @Nonnull
        public InMemoryRegisteredClientRepository build() {
            return new InMemoryRegisteredClientRepository(clientMap);
        }
    }
}
