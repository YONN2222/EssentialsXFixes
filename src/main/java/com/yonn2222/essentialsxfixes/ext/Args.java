package com.yonn2222.essentialsxfixes.ext;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Args {
    private static final Pattern DURATION = Pattern.compile("(\\d+(?:\\.\\d+)?)([dst]?)");

    private Args() {
    }

    public static int integer(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new Failure("bad-arguments");
        }
    }

    public static long duration(String token) {
        Matcher matcher = DURATION.matcher(token.toLowerCase(Locale.ROOT));
        if (!matcher.matches()) {
            throw new Failure("bad-arguments");
        }
        double value = Double.parseDouble(matcher.group(1));
        double factor = switch (matcher.group(2)) {
            case "d" -> 24000;
            case "s" -> 20;
            default -> 1;
        };
        return Math.round(value * factor);
    }
}
