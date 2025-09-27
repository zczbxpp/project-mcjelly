package pl.zczb.redis;

import lombok.Data;

public final @Data class RedisCredential {
    private final String hostname;
    private final int port;
}