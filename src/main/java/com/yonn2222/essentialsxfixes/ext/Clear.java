package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import com.yonn2222.essentialsxfixes.route.Tokens;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

final class Clear {
    private Clear() {
    }

    static int run(Call call) {
        call.require("essentials.clearinventory");
        List<Player> players;
        int itemIndex;
        if (call.tokens().isEmpty()) {
            players = call.executorAsPlayers();
            itemIndex = 0;
        } else if (Tokens.selector(call.token(0)) || Bukkit.getPlayerExact(call.token(0)) != null) {
            players = call.players(0);
            itemIndex = 1;
        } else {
            players = call.executorAsPlayers();
            itemIndex = 0;
        }
        Predicate<ItemStack> match = call.has(itemIndex) ? predicate(call.token(itemIndex)) : stack -> true;
        int limit = call.has(itemIndex + 1) ? Args.integer(call.token(itemIndex + 1)) : -1;
        if (limit < -1) {
            throw new Failure("bad-arguments");
        }
        int total = 0;
        for (Player player : players) {
            int amount = clear(player, match, limit);
            total += amount;
            call.reply(limit == 0 ? "counted" : "cleared", Messages.text("who", player.getName()), Messages.text("count", String.valueOf(amount)));
        }
        return total;
    }

    private static int clear(Player player, Predicate<ItemStack> match, int limit) {
        int removed = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack stack = contents[slot];
            if (stack == null || stack.getType().isAir() || !match.test(stack)) {
                continue;
            }
            if (limit == 0) {
                removed += stack.getAmount();
                continue;
            }
            int take = limit < 0 ? stack.getAmount() : Math.min(stack.getAmount(), limit - removed);
            if (take <= 0) {
                break;
            }
            removed += take;
            if (take >= stack.getAmount()) {
                player.getInventory().setItem(slot, null);
            } else {
                stack.setAmount(stack.getAmount() - take);
            }
            if (limit > 0 && removed >= limit) {
                break;
            }
        }
        return removed;
    }

    private static Predicate<ItemStack> predicate(String token) {
        if (token.startsWith("#")) {
            NamespacedKey key = NamespacedKey.fromString(token.substring(1));
            Tag<Material> tag = key == null ? null : Bukkit.getTag(Tag.REGISTRY_ITEMS, key, Material.class);
            if (tag == null) {
                throw new Failure("bad-arguments");
            }
            return stack -> tag.isTagged(stack.getType());
        }
        ItemStack template;
        try {
            template = Bukkit.getItemFactory().createItemStack(token.toLowerCase().contains(":") ? token : "minecraft:" + token);
        } catch (IllegalArgumentException e) {
            throw new Failure("bad-arguments");
        }
        if (token.indexOf('[') < 0) {
            return stack -> stack.getType() == template.getType();
        }
        return stack -> {
            ItemStack one = stack.clone();
            one.setAmount(template.getAmount());
            return one.isSimilar(template);
        };
    }
}
