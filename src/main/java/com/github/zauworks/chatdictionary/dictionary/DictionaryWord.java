/*
 * Copyright (c) 2022 zauuuw <https://github.com/zau-works>
 */

package com.github.zauworks.chatdictionary.dictionary;

public class DictionaryWord {

    private String colorCode;
    private boolean underline;
    private boolean bold;
    private String mean;

    public DictionaryWord(String colorCode, boolean underline, boolean bold, String mean) {
        this.colorCode = colorCode;
        this.underline = underline;
        this.bold = bold;
        this.mean = mean;
    }

    public String getColorCode() {
        return colorCode;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isBold() {
        return bold;
    }

    public String getMean() {
        return mean;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
