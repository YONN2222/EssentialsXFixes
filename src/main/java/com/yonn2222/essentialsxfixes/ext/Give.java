package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

final class Give {
    private Give() {
    }

    static int run(Call call) {
        call.require("essentials.give");
        ItemStack template = stack(call.token(1));
        int amount = call.has(2) ? Args.integer(call.token(2)) : 1;
        if (amount < 1) {
            throw new Failure("bad-arguments");
        }
        int count = 0;
        for (Player player : call.players(0)) {
            give(player, template, amount);
            count++;
            call.reply("given", Messages.text("who", player.getName()), Messages.text("count", String.valueOf(amount)), Messages.text("item", call.token(1)));
        }
        return count;
    }

    static ItemStack stack(String spec) {
        try {
            return Bukkit.getItemFactory().createItemStack(spec.contains(":") ? spec : "minecraft:" + spec);
        } catch (IllegalArgumentException e) {
            throw new Failure("bad-arguments");
        }
    }

    private static void give(Player player, ItemStack template, int amount) {
        int remaining = amount;
        int maxStack = Math.max(1, template.getMaxStackSize());
        while (remaining > 0) {
            ItemStack part = template.clone();
            part.setAmount(Math.min(maxStack, remaining));
            remaining -= part.getAmount();
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(part);
            for (ItemStack rest : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), rest);
            }
        }
    }
}
