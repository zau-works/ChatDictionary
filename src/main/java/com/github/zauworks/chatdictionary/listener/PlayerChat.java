/*
 * Copyright (c) 2022 zauuuw <https://github.com/zau-works>
 */

package com.github.zauworks.chatdictionary.listener;

import com.github.zauworks.chatdictionary.ChatDictionary;
import com.github.zauworks.chatdictionary.dictionary.DictionaryWord;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.logging.Logger;

public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        // TODO 他イベントリスナからチャットのフォーマットを設定しても、このイベントリスナでgetFormat()で呼び出せない問題があるので、解決する
        // イベントハンドラの仕様把握を含め、現在原因を調査中。

        Player player = e.getPlayer();
        String message = e.getMessage(), hoverText = null;

        for (Map.Entry<String, DictionaryWord> word : ChatDictionary.getDictionaryWords().entrySet()) {
            // メッセージに登録されたワードが含まれる場合、hoverTextにその内容を追加する
            String wordName = word.getKey();
            if (message.contains(wordName)) {
                DictionaryWord dictionaryWord = word.getValue();
                String colorCode = dictionaryWord.getColorCode();
                String mean = dictionaryWord.getMean();

                // 色、bold、underlineの設定を反映する
                String prefix = "";

                // カラーコードが正しく設定されている場合
                if (colorCode.toCharArray().length == 1) {
                    char code = colorCode.toCharArray()[0];
                    for (ChatColor cc : ChatColor.values()) {
                        if (code == cc.getChar()) {
                            prefix += ChatColor.getByChar(code);
                        }
                    }
                }

                // boldがtrueに設定されている場合
                if (dictionaryWord.isBold()) {
                    prefix += ChatColor.BOLD;
                    message = message.replaceAll(wordName, ChatColor.BOLD + wordName + ChatColor.RESET);
                }

                // underlineがtrueに設定されている場合
                if (dictionaryWord.isUnderline()) {
                    prefix += ChatColor.UNDERLINE;
                    message = message.replaceAll(wordName, ChatColor.UNDERLINE + wordName + ChatColor.RESET);
                }

                message = message.replaceAll(wordName, (wordName = prefix + wordName + ChatColor.RESET));
                hoverText = hoverText == null ? "&a&lMEANING OF WORD IN THE CHAT&r\n\n\n" + wordName + "&r\n" + mean + "&r\n"
                        : hoverText + "\n\n" + wordName + "&r\n" + mean + "&r";
            }
        }

        // hoverTextがnullじゃなかったらチャットメッセージを辞書化する
        if (hoverText != null) {
            TextComponent textComponent = new TextComponent(e.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", message));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hoverText)).create()));

            for (Player recipient : e.getRecipients()) {
                recipient.spigot().sendMessage(textComponent);
            }

            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(e.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", e.getMessage()));
            e.setCancelled(true);
        }
    }

}
