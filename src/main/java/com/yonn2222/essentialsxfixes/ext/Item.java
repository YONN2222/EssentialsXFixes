package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

final class Item {
    private Item() {
    }

    static int run(Call call) {
        call.require("essentials.item");
        if (!call.token(0).equalsIgnoreCase("replace") || !call.token(1).equalsIgnoreCase("entity") || !call.has(5) || !call.token(4).equalsIgnoreCase("with")) {
            throw new Failure("bad-arguments");
        }
        String slot = call.token(3).toLowerCase(Locale.ROOT);
        ItemStack template = Give.stack(call.token(5));
        if (call.has(6)) {
            template.setAmount(Math.max(1, Args.integer(call.token(6))));
        }
        int count = 0;
        for (Entity entity : call.entities(2)) {
            if (apply(entity, slot, template)) {
                count++;
                call.reply("slot-set", Messages.text("who", entity.getName()), Messages.text("slot", slot));
            }
        }
        if (count == 0) {
            throw new Failure("no-target");
        }
        return count;
    }

    private static boolean apply(Entity entity, String slot, ItemStack stack) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        EntityEquipment equipment = living.getEquipment();
        EquipmentSlot equipmentSlot = switch (slot) {
            case "weapon", "weapon.mainhand" -> EquipmentSlot.HAND;
            case "weapon.offhand" -> EquipmentSlot.OFF_HAND;
            case "armor.head" -> EquipmentSlot.HEAD;
            case "armor.chest" -> EquipmentSlot.CHEST;
            case "armor.legs" -> EquipmentSlot.LEGS;
            case "armor.feet" -> EquipmentSlot.FEET;
            default -> null;
        };
        if (equipmentSlot != null && equipment != null) {
            equipment.setItem(equipmentSlot, stack.clone());
            return true;
        }
        if (entity instanceof Player player) {
            int index = inventoryIndex(slot);
            if (index < 0) {
                throw new Failure("bad-arguments");
            }
            player.getInventory().setItem(index, stack.clone());
            return true;
        }
        return false;
    }

    private static int inventoryIndex(String slot) {
        int dot = slot.indexOf('.');
        if (dot < 0) {
            return -1;
        }
        String kind = slot.substring(0, dot);
        int number;
        try {
            number = Integer.parseInt(slot.substring(dot + 1));
        } catch (NumberFormatException e) {
            return -1;
        }
        return switch (kind) {
            case "hotbar" -> number >= 0 && number < 9 ? number : -1;
            case "inventory" -> number >= 0 && number < 27 ? number + 9 : -1;
            case "container" -> number >= 0 && number < 36 ? number : -1;
            default -> -1;
        };
    }
}
