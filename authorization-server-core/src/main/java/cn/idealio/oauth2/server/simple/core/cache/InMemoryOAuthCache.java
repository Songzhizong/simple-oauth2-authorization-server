package cn.idealio.oauth2.server.simple.core.cache;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 宋志宗 on 2023/4/20
 */
public class InMemoryOAuthCache implements OAuthCache {
    private final ConcurrentHashMap<String, CodeInfoWrapper> codeMap = new ConcurrentHashMap<>();
    private final long timeoutMills;

    public InMemoryOAuthCache(@Nonnull Duration timeout) {
        this.timeoutMills = timeout.toMillis();
        @SuppressWarnings("resource")
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(() ->
            codeMap.forEach((code, wrapper) -> {
                    if (System.currentTimeMillis() - wrapper.getCreateTime() > timeoutMills) {
                        codeMap.remove(code);
                    }
                }
            ), 60, 60, TimeUnit.SECONDS
        );
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
    }

    @Override
    public void saveCode(@Nonnull CodeInfo codeInfo) {
        codeMap.put(codeInfo.getCode(), new CodeInfoWrapper(codeInfo));
    }

    @Nullable
    @Override
    public CodeInfo loadCode(@Nonnull String code) {
        CodeInfoWrapper wrapper = codeMap.get(code);
        if (wrapper == null) {
            return null;
        }
        long createTime = wrapper.getCreateTime();
        if (System.currentTimeMillis() - createTime > timeoutMills) {
            codeMap.remove(code);
            return null;
        }
        return wrapper.getCodeInfo();
    }

    @Getter
    private static class CodeInfoWrapper {
        private final long createTime = System.currentTimeMillis();
        @Nonnull
        private final CodeInfo codeInfo;

        private CodeInfoWrapper(@Nonnull CodeInfo codeInfo) {
            this.codeInfo = codeInfo;
        }
    }
}
