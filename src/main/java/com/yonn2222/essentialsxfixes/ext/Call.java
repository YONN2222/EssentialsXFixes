package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import com.yonn2222.essentialsxfixes.route.Tokens;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Call(CommandSourceStack stack, List<String> tokens, Map<Integer, List<Entity>> resolved, Messages messages) {
    public CommandSender sender() {
        return stack.getSender();
    }

    public Entity executor() {
        return stack.getExecutor();
    }

    public Location origin() {
        return stack.getLocation();
    }

    public String token(int index) {
        if (index >= tokens.size()) {
            throw new Failure("bad-arguments");
        }
        return tokens.get(index);
    }

    public boolean has(int index) {
        return index < tokens.size();
    }

    public List<Entity> entities(int index) {
        String token = token(index);
        if (Tokens.selector(token)) {
            List<Entity> list = resolved.get(index);
            if (list == null || list.isEmpty()) {
                throw new Failure("no-target");
            }
            return list;
        }
        Player player = Bukkit.getPlayerExact(token);
        if (player == null) {
            throw new Failure("no-target");
        }
        return List.of(player);
    }

    public List<Player> players(int index) {
        List<Player> out = new ArrayList<>();
        for (Entity entity : entities(index)) {
            if (entity instanceof Player player) {
                out.add(player);
            }
        }
        if (out.isEmpty()) {
            throw new Failure("no-target");
        }
        return out;
    }

    public Entity single(int index) {
        List<Entity> list = entities(index);
        if (list.size() != 1) {
            throw new Failure("too-many-targets");
        }
        return list.get(0);
    }

    public List<Player> executorAsPlayers() {
        if (executor() instanceof Player player) {
            return List.of(player);
        }
        throw new Failure("no-target");
    }

    public void reply(String key, TagResolver... resolvers) {
        sender().sendMessage(messages.get(key, resolvers));
    }

    public void require(String permission) {
        if (!sender().hasPermission(permission)) {
            throw new Failure("no-permission");
        }
    }
}
