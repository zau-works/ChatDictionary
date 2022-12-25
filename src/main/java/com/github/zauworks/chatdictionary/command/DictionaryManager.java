package com.github.zauworks.chatdictionary.command;

import com.github.zauworks.chatdictionary.ChatDictionary;
import com.github.zauworks.chatdictionary.dictionary.DictionaryWord;
import com.github.zauworks.chatdictionary.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command,String label,String[] args){

        Player player = (Player) sender;
        if(args.length<2){
            player.sendMessage(new Messages().error("Invalid arguments."));
            return false;
        }

        String type = args[0];
        if(type.equalsIgnoreCase("add") && args.length >= 3){
            // command type : add
            String word = args[1];
            if(word.contains(" "))word.replace(" ","+");
            DictionaryWord dictWord = new DictionaryWord("none",false,false,getMean(args,2));
            ChatDictionary.getDictionaryWords().put(word,dictWord);
        }else if(type.equalsIgnoreCase("remove")){
            //command type : remove
            if(!ChatDictionary.getDictionaryWords().containsKey(args[1])){
                player.sendMessage(new Messages().error("[Word remove Error]:Not contains word."));
                return false;
            }
            ChatDictionary.getDictionaryWords().remove(args[1]);
            return true;
        }else if(type.equalsIgnoreCase("options")){
            //command type : options
            String word = args[1];
            if(word.contains(" "))word.replace(" ","+");
            if(!ChatDictionary.getDictionaryWords().containsKey(word)){
                // the words(map) does not contain entered word
                player.sendMessage(new Messages().error("[Word modify Error]:Not contains word."));
                return false;
            }
            List<String> modifyType = new ArrayList<>(Arrays.asList("bold","underline","color","mean"));
            String option = args[2].toLowerCase();
            if(!modifyType.contains(option)){
                // an unknown argument entered
                player.sendMessage(new Messages().error("[Word modify Error]:Invalid option-type\nArguments: <bold | underline | color | mean>"));
                return false;
            }
            DictionaryWord dict = ChatDictionary.getDictionaryWords().get(word);
            if(option.equalsIgnoreCase("bold")){
                //set bold
                boolean bool = Boolean.valueOf(args[3]);
                dict.setBold(bool);
                sendSuccessMessage(String.valueOf(bool),word,"bold",player);
                return true;
            }else if(option.equalsIgnoreCase("underline")){
                //set underline
                boolean bool = Boolean.valueOf(args[3]);
                dict.setUnderline(bool);
                sendSuccessMessage(String.valueOf(bool),word,"underline",player);
            }else if(option.equalsIgnoreCase("color")){
                //set color
                String color = args[3];
                if(!isColorCode(color)){
                    player.sendMessage(new Messages().error("[Word modify Error]:Invalid option argument. Have to enter an color code without '&'."));
                    return false;
                }
                dict.setColorCode(color);
                sendSuccessMessage(color,word,"color",player);
            }else if(option.equalsIgnoreCase("mean")){
                //set mean
                String mean = getMean(args,3);
                dict.setMean(mean);
                sendSuccessMessage(mean,word,"mean",player);
            }else{
                //invalid argument
                player.sendMessage(new Messages().error("[Word modify Error]:invalid option argument."));
                return false;
            }
            return true;
        }else if(type.equalsIgnoreCase("config")
                && args[1].equalsIgnoreCase("reload")){
            //command type : config reload
            if(args.length == 3
                    && ChatDictionary.getDictionaryWords().containsKey(args[2])){
                //word reload
                String word = args[2];
                wordReload(word);
                return true;

            }else if(args.length == 2){
                //config reload
                configReload();
            }
        }else{
            player.sendMessage(new Messages().error("Command Usage: /dictionary <add|remove|options|config reload> ..."));
        }

        return false;
    }

    private boolean isColorCode(String string){
        if(string.equalsIgnoreCase("none")){
            return true;
        }else if(string.length()!=1){
            return false;
        }
        Pattern p = Pattern.compile("[1-9|a-g|k-o|r]");
        Matcher m = p.matcher(string);
        return m.find();
    }

    private void sendSuccessMessage(String value,String word,String param,Player player){
        player.sendMessage(new Messages().success("[Word modify Success]: Word:"+word+" | "+param+":"+value));
    }

    private String getMean(String[] args,int start){
        StringBuilder builder = new StringBuilder();
        for(int i=start;i<args.length;i++){
            builder.append(args[i]+" ");
        }
        return builder.toString();
    }

    private void wordReload(String word){
        //word Reload
        ChatDictionary.getDictionaryWords().remove(word);
        FileConfiguration config = ChatDictionary.getInstance().getConfig();
        String path = "dictionary."+word+".";
        String color = config.getString(path+"color");
        boolean underline = config.getBoolean(path+"underline");
        boolean bold = config.getBoolean(path+"bold");
        String mean;
        if(config.isList(path+"mean")){
            mean = String.join("\n",config.getStringList(path+"mean"));
        }else{
            mean = config.getString(path+"mean");
        }
        DictionaryWord dict = new DictionaryWord(color,underline,bold,mean);
        ChatDictionary.getDictionaryWords().put(word,dict);
    }

    private void configReload(){
        // reload config
        ChatDictionary.initializeWords();
    }
}
