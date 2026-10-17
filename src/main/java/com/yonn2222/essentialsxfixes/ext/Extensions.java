package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.route.Twin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Extensions {
    private Extensions() {
    }

    public static int run(Twin twin, Call call) {
        return switch (twin) {
            case TP -> Teleport.run(call);
            case KILL -> Kill.run(call);
            case CLEAR -> Clear.run(call);
            case ENCHANT -> Enchant.run(call);
            case GIVE -> Give.run(call);
            case ITEM -> Item.run(call);
            case RECIPE -> Recipes.run(call);
            case LIST -> Listing.run(call);
            case XP -> Experience.run(call);
            case TIME -> Time.run(call);
            case WEATHER -> Weather.run(call);
        };
    }

    public static List<String> keywords(Twin twin, List<String> before) {
        List<String> out = new ArrayList<>();
        String first = before.isEmpty() ? "" : before.get(0).toLowerCase(Locale.ROOT);
        switch (twin) {
            case TIME -> {
                if (before.isEmpty()) {
                    out.addAll(List.of("set", "add", "query"));
                } else if (first.equals("set") && before.size() == 1) {
                    out.addAll(List.of("day", "noon", "night", "midnight"));
                } else if (first.equals("query") && before.size() == 1) {
                    out.addAll(List.of("daytime", "gametime", "day"));
                }
            }
            case WEATHER -> {
                if (before.isEmpty()) {
                    out.addAll(List.of("clear", "rain", "thunder"));
                }
            }
            case XP -> {
                if (before.isEmpty()) {
                    out.addAll(List.of("add", "set", "query"));
                } else if (before.size() >= 2) {
                    out.addAll(List.of("points", "levels"));
                }
            }
            case ITEM -> {
                if (before.isEmpty()) {
                    out.add("replace");
                } else if (first.equals("replace") && before.size() == 1) {
                    out.add("entity");
                } else if (first.equals("replace") && before.size() == 4) {
                    out.add("with");
                }
            }
            case RECIPE -> {
                if (before.isEmpty()) {
                    out.addAll(List.of("give", "take"));
                }
            }
            case LIST -> {
                if (before.isEmpty()) {
                    out.add("uuids");
                }
            }
            case TP -> {
                if (before.size() >= 3) {
                    out.add("facing");
                }
            }
            default -> {
            }
        }
        return out;
    }
}
