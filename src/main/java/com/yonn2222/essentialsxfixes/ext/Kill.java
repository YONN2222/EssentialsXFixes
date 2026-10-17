package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

final class Kill {
    private Kill() {
    }

    static int run(Call call) {
        call.require("essentials.kill");
        List<Entity> targets;
        if (call.tokens().isEmpty()) {
            if (call.executor() == null) {
                throw new Failure("no-target");
            }
            targets = List.of(call.executor());
        } else {
            targets = call.entities(0);
        }
        int count = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                player.setHealth(0);
            } else if (entity instanceof LivingEntity living) {
                living.setHealth(0);
            } else {
                entity.remove();
            }
            count++;
        }
        call.reply("killed", Messages.text("count", String.valueOf(count)));
        return count;
    }
}
