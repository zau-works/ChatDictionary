/*
 * Copyright (c) 2022 zauuuw <https://github.com/zau-works>
 */

package com.github.zauworks.chatdictionary;

import com.github.zauworks.chatdictionary.command.DictionarySettingCommand;
import com.github.zauworks.chatdictionary.dictionary.DictionaryWord;
import com.github.zauworks.chatdictionary.listener.PlayerChat;
import com.github.zauworks.chatdictionary.dictionary.DictionaryData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChatDictionary extends JavaPlugin {

    private static ChatDictionary instance;

    private static Map<String, DictionaryWord> words = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

        saveDefaultConfig();
        saveConfig();

        initializeWords();

        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getCommand("dictionary").setExecutor(new DictionarySettingCommand());

        getLogger().info("ChatDictionary version " + getDescription().getVersion() + " has been loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatDictionary version " + getDescription().getVersion() + " has been un-loaded!");
        new DictionaryData().main("all", Bukkit.getConsoleSender());

    }

    public static ChatDictionary getInstance() {
        return instance;
    }

    public static Map<String, DictionaryWord> getDictionaryWords() {
        return words;
    }

    public static void initializeWords() {
        words.clear();
        for (String word : instance.getConfig().getConfigurationSection("dictionary").getKeys(false)) {
            String path = "dictionary." + word + ".";

            Object mean = instance.getConfig().get(path + "mean");
            if (mean instanceof List && ((List) mean).toArray()[0] instanceof String) {
                String ms = mean.toString();
                mean = ms.substring(1, ms.length() - 1).replaceAll(", ", "\n");
            }

            if (mean instanceof String) {
                words.put(word, new DictionaryWord(instance.getConfig().getString(path + "color"),
                        instance.getConfig().getBoolean(path + "underline"),
                        instance.getConfig().getBoolean(path + "bold"),
                        (String) mean));
            }
        }
    }

}
