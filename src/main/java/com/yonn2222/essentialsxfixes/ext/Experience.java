package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.entity.Player;

import java.util.Locale;

final class Experience {
    private Experience() {
    }

    static int run(Call call) {
        call.require("essentials.exp");
        String mode = call.token(0).toLowerCase(Locale.ROOT);
        return switch (mode) {
            case "add", "set" -> change(call, mode.equals("add"));
            case "query" -> query(call);
            default -> throw new Failure("bad-arguments");
        };
    }

    private static boolean levels(Call call, int index) {
        if (!call.has(index)) {
            return false;
        }
        String unit = call.token(index).toLowerCase(Locale.ROOT);
        if (unit.equals("levels")) {
            return true;
        }
        if (unit.equals("points")) {
            return false;
        }
        throw new Failure("bad-arguments");
    }

    private static int change(Call call, boolean add) {
        int amount = Args.integer(call.token(2));
        boolean levels = levels(call, 3);
        if (!add && amount < 0) {
            throw new Failure("bad-arguments");
        }
        int count = 0;
        for (Player player : call.players(1)) {
            if (levels) {
                if (add) {
                    player.giveExpLevels(amount);
                } else {
                    player.setLevel(amount);
                }
            } else if (add) {
                player.giveExp(amount);
            } else {
                player.setExp(0);
                player.setLevel(0);
                player.setTotalExperience(0);
                player.giveExp(amount);
            }
            count++;
            call.reply("xp-changed", Messages.text("who", player.getName()), Messages.text("value", describe(player, levels)));
        }
        return count;
    }

    private static int query(Call call) {
        boolean levels = levels(call, 2);
        Player player = call.players(1).get(0);
        call.reply("xp-query", Messages.text("who", player.getName()), Messages.text("value", describe(player, levels)));
        return levels ? player.getLevel() : points(player);
    }

    private static int points(Player player) {
        return (int) (player.getExp() * player.getExpToLevel());
    }

    private static String describe(Player player, boolean levels) {
        return levels ? player.getLevel() + " levels" : points(player) + " points";
    }
}
