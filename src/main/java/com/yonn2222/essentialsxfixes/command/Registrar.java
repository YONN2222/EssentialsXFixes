package com.yonn2222.essentialsxfixes.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.yonn2222.essentialsxfixes.bridge.Router;
import com.yonn2222.essentialsxfixes.ext.Extensions;
import com.yonn2222.essentialsxfixes.route.Tokens;
import com.yonn2222.essentialsxfixes.route.Twin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.logging.Logger;

public final class Registrar {
    private static final List<String> SELECTORS = List.of("@a", "@p", "@r", "@s", "@e", "@n");

    private final Router router;
    private final Plugin essentials;
    private final Predicate<String> skipped;
    private final Logger logger;

    public Registrar(Router router, Plugin essentials, Predicate<String> skipped, Logger logger) {
        this.router = router;
        this.essentials = essentials;
        this.skipped = skipped;
        this.logger = logger;
    }

    public int registerAll(Commands commands) {
        int count = 0;
        for (String label : router.catalog().labels()) {
            if (skipped.test(label) || !ownedByEssentials(label)) {
                continue;
            }
            commands.register(node(label).build());
            count++;
        }
        logger.info("Took over " + count + " EssentialsX command labels");
        return count;
    }

    private boolean ownedByEssentials(String label) {
        Command root = Bukkit.getCommandMap().getCommand(label);
        return root instanceof PluginCommand plugin && plugin.getPlugin() == essentials;
    }

    private LiteralArgumentBuilder<CommandSourceStack> node(String label) {
        return Commands.literal(label)
                .requires(stack -> visible(label, stack.getSender()))
                .executes(context -> router.run(context.getSource(), label, ""))
                .then(Commands.argument("args", StringArgumentType.greedyString())
                        .suggests((context, builder) -> suggest(context, builder, label))
                        .executes(context -> router.run(context.getSource(), label, StringArgumentType.getString(context, "args"))));
    }

    private boolean visible(String label, CommandSender sender) {
        Command command = Bukkit.getCommandMap().getCommand("essentials:" + label);
        return command == null || command.testPermissionSilent(sender);
    }

    private CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder, String label) {
        CommandSender sender = context.getSource().getSender();
        String remaining = builder.getRemaining();
        int cut = remaining.lastIndexOf(' ') + 1;
        String current = remaining.substring(cut);
        SuggestionsBuilder tail = builder.createOffset(builder.getStart() + cut);
        Set<String> options = new LinkedHashSet<>();
        Command command = Bukkit.getCommandMap().getCommand("essentials:" + label);
        if (command != null) {
            try {
                options.addAll(command.tabComplete(sender, label, remaining.split(" ", -1)));
            } catch (RuntimeException e) {
                logger.fine("Tab completion failed for " + label + ": " + e.getMessage());
            }
        }
        if (router.selectorsAllowed(sender) && (current.isEmpty() || current.startsWith("@"))) {
            options.addAll(SELECTORS);
        }
        List<String> before = new ArrayList<>(Tokens.split(remaining.substring(0, cut)));
        Twin.of(label).ifPresent(twin -> options.addAll(Extensions.keywords(twin, before)));
        String lower = current.toLowerCase(Locale.ROOT);
        List<String> filtered = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                filtered.add(option);
            }
        }
        filtered.forEach(tail::suggest);
        return tail.buildFuture();
    }
}
