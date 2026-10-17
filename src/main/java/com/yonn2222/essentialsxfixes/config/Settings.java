package com.yonn2222.essentialsxfixes.config;

import com.yonn2222.essentialsxfixes.route.Twin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public record Settings(String language, boolean selectors, Set<Twin> extended, Set<String> excluded) {
    public static Settings read(FileConfiguration config) {
        Set<Twin> enabled = EnumSet.noneOf(Twin.class);
        ConfigurationSection section = config.getConfigurationSection("extensions");
        for (Twin twin : Twin.values()) {
            if (section == null || section.getBoolean(twin.key(), true)) {
                enabled.add(twin);
            }
        }
        Set<String> excluded = new HashSet<>();
        for (String label : config.getStringList("exclude")) {
            excluded.add(label.toLowerCase(Locale.ROOT).trim());
        }
        return new Settings(
                config.getString("language", "en"),
                config.getBoolean("selectors.enabled", true),
                enabled,
                excluded
        );
    }
}
