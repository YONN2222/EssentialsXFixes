package com.yonn2222.essentialsxfixes.bridge;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class Targets {
    private Targets() {
    }

    public static List<Entity> resolve(CommandSourceStack stack, String token) throws CommandSyntaxException {
        EntitySelectorArgumentResolver resolver = ArgumentTypes.entities().parse(new StringReader(token));
        return resolver.resolve(stack);
    }

    public static List<Player> players(List<Entity> entities) {
        List<Player> out = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Player player) {
                out.add(player);
            }
        }
        return out;
    }
}
