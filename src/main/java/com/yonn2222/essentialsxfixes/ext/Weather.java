package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.World;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

final class Weather {
    private Weather() {
    }

    static int run(Call call) {
        call.require("essentials.weather");
        World world = call.origin().getWorld();
        String mode = call.token(0).toLowerCase(Locale.ROOT);
        int duration = call.has(1) ? (int) Args.duration(call.token(1)) : ThreadLocalRandom.current().nextInt(6000, 18000);
        switch (mode) {
            case "clear" -> {
                world.setStorm(false);
                world.setThundering(false);
                world.setClearWeatherDuration(duration);
            }
            case "rain" -> {
                world.setStorm(true);
                world.setThundering(false);
                world.setWeatherDuration(duration);
            }
            case "thunder" -> {
                world.setStorm(true);
                world.setThundering(true);
                world.setWeatherDuration(duration);
                world.setThunderDuration(duration);
            }
            default -> throw new Failure("bad-arguments");
        }
        call.reply("weather-set", Messages.text("world", world.getName()), Messages.text("weather", mode));
        return 1;
    }
}
