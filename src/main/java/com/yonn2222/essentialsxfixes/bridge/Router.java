package com.yonn2222.essentialsxfixes.bridge;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yonn2222.essentialsxfixes.Messages;
import com.yonn2222.essentialsxfixes.config.Settings;
import com.yonn2222.essentialsxfixes.ext.Call;
import com.yonn2222.essentialsxfixes.ext.Extensions;
import com.yonn2222.essentialsxfixes.ext.Failure;
import com.yonn2222.essentialsxfixes.route.Facts;
import com.yonn2222.essentialsxfixes.route.Route;
import com.yonn2222.essentialsxfixes.route.Rules;
import com.yonn2222.essentialsxfixes.route.Tokens;
import com.yonn2222.essentialsxfixes.route.Twin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;

public final class Router {
    private final JavaPlugin plugin;
    private final Supplier<Settings> settings;
    private final Supplier<Messages> messages;
    private final Catalog catalog;

    public Router(JavaPlugin plugin, Supplier<Settings> settings, Supplier<Messages> messages, Catalog catalog) {
        this.plugin = plugin;
        this.settings = settings;
        this.messages = messages;
        this.catalog = catalog;
    }

    public Catalog catalog() {
        return catalog;
    }

    public boolean selectorsAllowed(CommandSender sender) {
        return settings.get().selectors() && sender.hasPermission("essentialsxfixes.selectors");
    }

    public int run(CommandSourceStack stack, String label, String raw) throws CommandSyntaxException {
        Settings config = settings.get();
        CommandSender sender = stack.getSender();
        Entity executor = stack.getExecutor();
        List<String> tokens = Tokens.split(raw);
        String canonical = catalog.canonical(label);
        int limit = Math.min(tokens.size(), Rules.freeTextStart(canonical));
        Map<Integer, List<Entity>> resolved = new LinkedHashMap<>();
        if (selectorsAllowed(sender)) {
            for (int i = 0; i < limit; i++) {
                if (Tokens.selector(tokens.get(i))) {
                    resolved.put(i, Targets.resolve(stack, tokens.get(i)));
                }
            }
        }
        Optional<Twin> twin = Twin.of(label);
        if (twin.isPresent() && config.extended().contains(twin.get())) {
            boolean entities = resolved.values().stream().anyMatch(list -> list.stream().anyMatch(e -> !(e instanceof Player)));
            boolean playerFirst = !tokens.isEmpty() && Bukkit.getPlayerExact(tokens.get(0)) != null;
            Route route = Rules.decide(twin.get(), tokens, new Facts(!resolved.isEmpty(), entities, playerFirst));
            if (route == Route.EXTENDED) {
                Call call = new Call(stack, tokens, resolved, messages.get());
                try {
                    return Extensions.run(twin.get(), call);
                } catch (Failure failure) {
                    TagResolver[] extra = java.util.Arrays.copyOf(failure.resolvers(), failure.resolvers().length + 1);
                    extra[extra.length - 1] = Messages.text("label", label);
                    sender.sendMessage(messages.get().get(failure.key(), extra));
                    return 0;
                }
            }
        }
        return essentials(sender, executor, label, canonical, tokens, resolved);
    }

    private int essentials(CommandSender sender, Entity executor, String label, String canonical, List<String> tokens, Map<Integer, List<Entity>> resolved) {
        Command command = Bukkit.getCommandMap().getCommand("essentials:" + label.toLowerCase());
        if (command == null) {
            sender.sendMessage(messages.get().get("unavailable", Messages.text("label", label)));
            return 0;
        }
        if (resolved.isEmpty()) {
            return execute(command, sender, executor, label, canonical, tokens);
        }
        List<Map.Entry<Integer, List<Entity>>> entries = new ArrayList<>(resolved.entrySet());
        List<String> base = new ArrayList<>(tokens);
        for (int i = 1; i < entries.size(); i++) {
            List<Player> players = Targets.players(entries.get(i).getValue());
            if (players.size() != 1) {
                sender.sendMessage(messages.get().get(players.isEmpty() ? "no-target" : "too-many-targets"));
                return 0;
            }
            base.set(entries.get(i).getKey(), players.get(0).getName());
        }
        List<Player> targets = Targets.players(entries.get(0).getValue());
        if (targets.isEmpty()) {
            sender.sendMessage(messages.get().get("no-target"));
            return 0;
        }
        int success = 0;
        for (Player target : targets) {
            List<String> copy = new ArrayList<>(base);
            copy.set(entries.get(0).getKey(), target.getName());
            success += execute(command, sender, executor, label, canonical, copy);
        }
        return success;
    }

    private int execute(Command command, CommandSender sender, Entity executor, String label, String canonical, List<String> tokens) {
        List<String> arguments = new ArrayList<>(tokens);
        int selfAt = Rules.selfTargetAt(canonical);
        if (selfAt == arguments.size() && executor instanceof Player player && sender != player) {
            arguments.add(player.getName());
        }
        try {
            return command.execute(sender, label, arguments.toArray(new String[0])) ? 1 : 0;
        } catch (CommandException e) {
            plugin.getLogger().log(Level.WARNING, "Command " + label + " failed", e.getCause() == null ? e : e.getCause());
            sender.sendMessage(messages.get().get("internal-error", Messages.text("label", label)));
            return 0;
        }
    }
}
