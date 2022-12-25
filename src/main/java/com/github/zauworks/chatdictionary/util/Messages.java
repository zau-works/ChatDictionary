package com.github.zauworks.chatdictionary.util;

public class Messages {
    public String error(String gist){
        String color = "§c";
        return color+gist;
    }

    public String success(String gist){
        String color = "§a";
        return color+gist;
    }

    public String notice(String gist){
        String color = "§7";
        return color+gist;
    }
}
