package com.yonn2222.essentialsxfixes.bridge;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class Catalog {
    private final Map<String, String> canonical = new HashMap<>();

    @SuppressWarnings("deprecation")
    public Catalog(Plugin essentials) {
        for (Map.Entry<String, Map<String, Object>> entry : essentials.getDescription().getCommands().entrySet()) {
            String name = entry.getKey().toLowerCase(Locale.ROOT);
            canonical.put(name, name);
            Object aliases = entry.getValue().get("aliases");
            if (aliases instanceof String alias) {
                canonical.put(alias.toLowerCase(Locale.ROOT), name);
            } else if (aliases instanceof List<?> list) {
                for (Object alias : list) {
                    canonical.put(String.valueOf(alias).toLowerCase(Locale.ROOT), name);
                }
            }
        }
    }

    public String canonical(String label) {
        return canonical.getOrDefault(label.toLowerCase(Locale.ROOT), label.toLowerCase(Locale.ROOT));
    }

    public Set<String> labels() {
        return canonical.keySet();
    }
}
