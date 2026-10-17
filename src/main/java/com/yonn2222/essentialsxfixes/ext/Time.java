package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.World;

import java.util.Locale;

final class Time {
    private Time() {
    }

    static int run(Call call) {
        World world = call.origin().getWorld();
        String mode = call.token(0).toLowerCase(Locale.ROOT);
        switch (mode) {
            case "set" -> {
                call.require("essentials.time.set");
                long ticks = named(call.token(1));
                world.setTime(ticks);
                call.reply("time-set", Messages.text("world", world.getName()), Messages.text("value", String.valueOf(world.getTime())));
                return (int) world.getTime();
            }
            case "add" -> {
                call.require("essentials.time.set");
                world.setFullTime(world.getFullTime() + Args.duration(call.token(1)));
                call.reply("time-set", Messages.text("world", world.getName()), Messages.text("value", String.valueOf(world.getTime())));
                return (int) world.getTime();
            }
            case "query" -> {
                String what = call.has(1) ? call.token(1).toLowerCase(Locale.ROOT) : "daytime";
                long value = switch (what) {
                    case "daytime" -> world.getTime();
                    case "gametime" -> world.getGameTime();
                    case "day" -> world.getFullTime() / 24000;
                    default -> throw new Failure("bad-arguments");
                };
                call.reply("time-query", Messages.text("what", what), Messages.text("value", String.valueOf(value)));
                return (int) value;
            }
            default -> throw new Failure("bad-arguments");
        }
    }

    private static long named(String token) {
        return switch (token.toLowerCase(Locale.ROOT)) {
            case "day" -> 1000;
            case "noon" -> 6000;
            case "night" -> 13000;
            case "midnight" -> 18000;
            default -> Args.duration(token);
        };
    }
}
