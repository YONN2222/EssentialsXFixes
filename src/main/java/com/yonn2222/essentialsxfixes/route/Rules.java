package com.yonn2222.essentialsxfixes.route;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class Rules {
    private static final Pattern EXTENDED_TIME = Pattern.compile("\\d+(\\.\\d+)?[dst]|noon|midnight");

    private Rules() {
    }

    public static Route decide(Twin twin, List<String> tokens, Facts facts) {
        String first = tokens.isEmpty() ? "" : tokens.get(0).toLowerCase(Locale.ROOT);
        return switch (twin) {
            case TP -> teleport(tokens, facts);
            case KILL -> tokens.isEmpty() || facts.entities() ? Route.EXTENDED : Route.ESSENTIALS;
            case CLEAR -> clear(tokens);
            case ENCHANT -> facts.selectors() || facts.playerFirst() ? Route.EXTENDED : Route.ESSENTIALS;
            case GIVE -> tokens.stream().skip(1).anyMatch(Rules::components) ? Route.EXTENDED : Route.ESSENTIALS;
            case ITEM -> Set.of("replace", "modify").contains(first) ? Route.EXTENDED : Route.ESSENTIALS;
            case RECIPE -> Set.of("give", "take").contains(first) ? Route.EXTENDED : Route.ESSENTIALS;
            case LIST -> first.equals("uuids") ? Route.EXTENDED : Route.ESSENTIALS;
            case XP -> experience(tokens, first);
            case TIME -> time(tokens, first);
            case WEATHER -> Set.of("clear", "rain", "thunder").contains(first) ? Route.EXTENDED : Route.ESSENTIALS;
        };
    }

    public static int freeTextStart(String canonical) {
        return switch (canonical) {
            case "broadcast", "me", "r", "helpop", "bigtree" -> 0;
            case "msg", "kick", "ban", "nick", "sudo", "mail" -> 1;
            case "tempban", "mute" -> 2;
            default -> Integer.MAX_VALUE;
        };
    }

    public static int selfTargetAt(String canonical) {
        return switch (canonical) {
            case "gamemode", "speed" -> 1;
            case "heal", "feed", "fly", "god", "clearinventory", "vanish", "extinguish" -> 0;
            default -> -1;
        };
    }

    private static Route teleport(List<String> tokens, Facts facts) {
        if (facts.entities()) {
            return Route.EXTENDED;
        }
        for (String token : tokens) {
            String lower = token.toLowerCase(Locale.ROOT);
            if (Tokens.coordinate(token) || lower.equals("facing") || lower.equals("rotated")) {
                return Route.EXTENDED;
            }
        }
        return Route.ESSENTIALS;
    }

    private static Route clear(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.indexOf('#') >= 0 || token.indexOf('[') >= 0 || token.startsWith("minecraft:")) {
                return Route.EXTENDED;
            }
        }
        if (tokens.size() >= 3 && tokens.get(2).equals("0")) {
            return Route.EXTENDED;
        }
        return Route.ESSENTIALS;
    }

    private static Route experience(List<String> tokens, String first) {
        if (first.equals("add") || first.equals("query")) {
            return Route.EXTENDED;
        }
        if (first.equals("set") && tokens.size() >= 4) {
            String unit = tokens.get(3).toLowerCase(Locale.ROOT);
            if (unit.equals("points") || unit.equals("levels")) {
                return Route.EXTENDED;
            }
        }
        return Route.ESSENTIALS;
    }

    private static Route time(List<String> tokens, String first) {
        if (first.equals("query")) {
            return Route.EXTENDED;
        }
        if ((first.equals("set") || first.equals("add")) && tokens.size() == 2) {
            String value = tokens.get(1).toLowerCase(Locale.ROOT);
            if (EXTENDED_TIME.matcher(value).matches()) {
                return Route.EXTENDED;
            }
        }
        return Route.ESSENTIALS;
    }

    private static boolean components(String token) {
        return token.indexOf('[') >= 0 || token.indexOf('{') >= 0;
    }
}
