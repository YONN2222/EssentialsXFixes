package com.yonn2222.essentialsxfixes.route;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Tokens {
    private static final Pattern COORDINATE = Pattern.compile("[~^]?(-?\\d+(\\.\\d+)?|-?\\.\\d+)?");

    private Tokens() {
    }

    public static List<String> split(String input) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;
        boolean quoted = false;
        char previous = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (quoted) {
                current.append(c);
                if (c == '"' && previous != '\\') {
                    quoted = false;
                }
            } else if (c == '"' && depth > 0) {
                quoted = true;
                current.append(c);
            } else if (c == '[' || c == '{') {
                depth++;
                current.append(c);
            } else if ((c == ']' || c == '}') && depth > 0) {
                depth--;
                current.append(c);
            } else if (c == ' ' && depth == 0) {
                if (current.length() > 0) {
                    out.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
            previous = c;
        }
        if (current.length() > 0) {
            out.add(current.toString());
        }
        return out;
    }

    public static boolean selector(String token) {
        if (token.length() < 2 || token.charAt(0) != '@' || "aeprsn".indexOf(token.charAt(1)) < 0) {
            return false;
        }
        return token.length() == 2 || token.charAt(2) == '[';
    }

    public static boolean coordinate(String token) {
        return !token.isEmpty() && COORDINATE.matcher(token).matches();
    }
}
