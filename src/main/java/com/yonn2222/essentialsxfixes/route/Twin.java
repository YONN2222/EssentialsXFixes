package com.yonn2222.essentialsxfixes.route;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public enum Twin {
    TP("tp", "essentials.tp", "tp", "teleport"),
    KILL("kill", "essentials.kill", "kill"),
    CLEAR("clear", "essentials.clearinventory", "clear"),
    ENCHANT("enchant", "essentials.enchant", "enchant"),
    GIVE("give", "essentials.give", "give"),
    ITEM("item", "essentials.item", "item"),
    RECIPE("recipe", "essentials.recipe", "recipe"),
    LIST("list", "essentials.list", "list"),
    XP("xp", "essentials.exp", "xp", "exp"),
    TIME("time", "essentials.time.set", "time"),
    WEATHER("weather", "essentials.weather", "weather");

    private final String key;
    private final String permission;
    private final Set<String> labels;

    Twin(String key, String permission, String... labels) {
        this.key = key;
        this.permission = permission;
        this.labels = Set.of(labels);
    }

    public String key() {
        return key;
    }

    public String permission() {
        return permission;
    }

    public static Optional<Twin> of(String label) {
        String lower = label.toLowerCase(Locale.ROOT);
        for (Twin twin : values()) {
            if (twin.labels.contains(lower)) {
                return Optional.of(twin);
            }
        }
        return Optional.empty();
    }
}
