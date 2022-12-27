package com.github.zauworks.chatdictionary.dictionary;

import com.github.zauworks.chatdictionary.ChatDictionary;
import com.github.zauworks.chatdictionary.dictionary.DictionaryWord;
import com.github.zauworks.chatdictionary.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Map;

public class DictionaryData {

    private FileConfiguration config = ChatDictionary.getInstance().getConfig();
    private ChatDictionary instance = ChatDictionary.getInstance();

    public void main(String type, CommandSender sender) {
        if (type.equalsIgnoreCase("all")) {
            for (Map.Entry<String, DictionaryWord> entry : ChatDictionary.getDictionaryWords().entrySet()) {
                save(entry.getKey());
            }
        } else if (!ChatDictionary.getDictionaryWords().containsKey(type)) {
            sender.sendMessage(new Messages().error("[Error]:Config save error. Not contains the value."));
        } else {
            save(type);
        }
        sender.sendMessage(new Messages().success("[Save]:Complete to save the configuration."));
    }

    private void save(String word) {
        DictionaryWord dict = ChatDictionary.getDictionaryWords().get(word);
        String mean = dict.getMean();
        String color = dict.getColorCode();
        boolean bold = dict.isBold();
        boolean underline = dict.isUnderline();
        String savePath = "dictionary." + word + ".";

        config.set(savePath + "color", color);
        config.set(savePath + "bold", bold);
        config.set(savePath + "underline", underline);
        if (mean.contains("\n")) {
            config.set(savePath + "mean", Arrays.asList(mean.split("\n")));
        } else {
            config.set(savePath + "mean", mean);
        }
        instance.saveConfig();
    }

    public void wordReload(String word) {
        //word Reload
        FileConfiguration config = ChatDictionary.getInstance().getConfig();
        String path = "dictionary." + word + ".";
        String color = config.getString(path + "color");
        boolean underline = config.getBoolean(path + "underline");
        boolean bold = config.getBoolean(path + "bold");
        String mean;
        if (config.isList(path + "mean")) {
            mean = String.join("\n", config.getStringList(path + "mean"));
        } else {
            mean = config.getString(path + "mean");
        }
        DictionaryWord dict = new DictionaryWord(color, underline, bold, mean);
        ChatDictionary.getDictionaryWords().put(word, dict);
    }


}
