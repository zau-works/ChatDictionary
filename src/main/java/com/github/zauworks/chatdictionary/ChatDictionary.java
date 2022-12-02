/*
 * Copyright (c) 2020 zauuuw <https://github.com/zau-works>
 */

package com.github.zauworks.chatdictionary;

import com.github.zauworks.chatdictionary.listener.PlayerChat;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatDictionary extends JavaPlugin {

    private static ChatDictionary instance;

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

        getConfig().options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(new PlayerChat(), this);

        getLogger().info("ChatDictionary version " + getDescription().getVersion() + " has been loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatDictionary version " + getDescription().getVersion() + " has been un-loaded!");
    }

    public static ChatDictionary getInstance() {
        return instance;
    }

}
