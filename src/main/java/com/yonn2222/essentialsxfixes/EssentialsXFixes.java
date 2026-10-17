package com.yonn2222.essentialsxfixes;

import com.mojang.brigadier.Command;
import com.yonn2222.essentialsxfixes.bridge.Catalog;
import com.yonn2222.essentialsxfixes.bridge.Router;
import com.yonn2222.essentialsxfixes.command.Registrar;
import com.yonn2222.essentialsxfixes.config.Settings;
import com.yonn2222.essentialsxfixes.metrics.Stats;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class EssentialsXFixes extends JavaPlugin {
    private volatile Settings settings;
    private volatile Messages messages;

    @Override
    public void onEnable() {
        Plugin essentials = getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null || !essentials.isEnabled()) {
            getLogger().severe("EssentialsX is not enabled, disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        load();
        Router router = new Router(this, () -> settings, () -> messages, new Catalog(essentials));
        Registrar registrar = new Registrar(router, essentials, this::skipped, getLogger());
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            registrar.registerAll(commands);
            commands.register(admin("essentialsxfixes").build(), "EssentialsXFixes administration", List.of("exfixes"));
        });
        Stats.start(this, () -> settings);
    }

    private boolean skipped(String label) {
        return label.equals("return") || settings.excluded().contains(label);
    }

    private void load() {
        reloadConfig();
        settings = Settings.read(getConfig());
        messages = new Messages(this, settings.language());
    }

    private com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> admin(String name) {
        return Commands.literal(name)
                .then(Commands.literal("reload")
                        .requires(stack -> stack.getSender().hasPermission("essentialsxfixes.reload"))
                        .executes(context -> {
                            load();
                            context.getSource().getSender().sendMessage(messages.get("reloaded"));
                            return Command.SINGLE_SUCCESS;
                        }));
    }
}
