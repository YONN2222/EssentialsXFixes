package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

final class Enchant {
    private Enchant() {
    }

    @SuppressWarnings("deprecation")
    static int run(Call call) {
        call.require("essentials.enchant");
        NamespacedKey key = NamespacedKey.fromString(call.token(1).toLowerCase());
        Enchantment enchantment = key == null ? null : Registry.ENCHANTMENT.get(key);
        if (enchantment == null) {
            throw new Failure("bad-arguments");
        }
        int level = call.has(2) ? Args.integer(call.token(2)) : 1;
        if (level < 1) {
            throw new Failure("bad-arguments");
        }
        int count = 0;
        for (Entity entity : call.entities(0)) {
            if (!(entity instanceof LivingEntity living) || living.getEquipment() == null) {
                continue;
            }
            ItemStack held = living.getEquipment().getItemInMainHand();
            if (held.getType().isAir()) {
                continue;
            }
            held.addUnsafeEnchantment(enchantment, level);
            living.getEquipment().setItemInMainHand(held);
            count++;
            call.reply("enchanted", Messages.text("who", entity.getName()), Messages.text("what", key.getKey() + " " + level));
        }
        if (count == 0) {
            throw new Failure("no-target");
        }
        return count;
    }
}
