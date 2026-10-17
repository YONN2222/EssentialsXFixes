package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

final class Listing {
    private Listing() {
    }

    static int run(Call call) {
        call.require("essentials.list");
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            call.reply("uuid-line", Messages.text("name", player.getName()), Messages.text("uuid", player.getUniqueId().toString()));
            count++;
        }
        return count;
    }
}
