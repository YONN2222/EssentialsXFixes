package com.yonn2222.essentialsxfixes.ext;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class Failure extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String key;
    private final transient TagResolver[] resolvers;

    public Failure(String key, TagResolver... resolvers) {
        super(key, null, false, false);
        this.key = key;
        this.resolvers = resolvers;
    }

    public String key() {
        return key;
    }

    public TagResolver[] resolvers() {
        return resolvers;
    }
}
