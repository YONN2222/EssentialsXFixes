package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Recipes {
    private Recipes() {
    }

    static int run(Call call) {
        call.require("essentials.recipe");
        String mode = call.token(0).toLowerCase();
        if (!mode.equals("give") && !mode.equals("take")) {
            throw new Failure("bad-arguments");
        }
        List<NamespacedKey> keys = keys(call.token(2));
        int count = 0;
        for (Player player : call.players(1)) {
            if (mode.equals("give")) {
                player.discoverRecipes(keys);
            } else {
                player.undiscoverRecipes(keys);
            }
            count++;
            call.reply("recipes", Messages.text("who", player.getName()), Messages.text("count", String.valueOf(keys.size())));
        }
        return count;
    }

    private static List<NamespacedKey> keys(String token) {
        List<NamespacedKey> out = new ArrayList<>();
        if (token.equals("*")) {
            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof Keyed keyed) {
                    out.add(keyed.getKey());
                }
            }
            return out;
        }
        NamespacedKey key = NamespacedKey.fromString(token.toLowerCase());
        if (key == null || Bukkit.getRecipe(key) == null) {
            throw new Failure("bad-arguments");
        }
        out.add(key);
        return out;
    }
}
