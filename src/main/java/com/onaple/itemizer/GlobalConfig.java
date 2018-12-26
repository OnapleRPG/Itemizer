package com.onaple.itemizer;

import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.format.TextColor;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {

    public GlobalConfig() {

    }

    public void setDescriptionRewrite(boolean descriptionRewrite) {
        this.descriptionRewrite = descriptionRewrite;
    }

    public void setHiddenFlagsValue(int hiddenFlagsValue) {
        this.hiddenFlagsValue = hiddenFlagsValue;
    }

    public Map<String, String> getRewriteChoice() {
        return rewriteChoice;
    }

    public void setEnchantRewrite(Map<EnchantmentType, String> enchantRewrite) {
        this.enchantRewrite = enchantRewrite;
    }

    public void setModifierRewrite(Map<String, String> modifierRewrite) {
        this.modifierRewrite = modifierRewrite;
    }

    public void setUnbreakableRewrite(String unbreakableRewrite) {
        this.unbreakableRewrite = unbreakableRewrite;
    }

    public void setCanMineRewrite(String canMineRewrite) {
        CanMineRewrite = canMineRewrite;
    }

    public void setHiddenFlags(Map<String, Boolean> hiddenFlags) {
        this.hiddenFlags = hiddenFlags;
    }

    public void setColorMap(Map<RewriteFlagColorList, TextColor> colorMap) {
        this.colorMap = colorMap;
    }

    private enum flags {
        Enchantments,
        Attributes_modifiers,
        Unbreakable,
        CanDestroy,
        CanPlaceOn,
        Others
    }
    public enum RewriteFlagColorList{
        attributesModifiersNegavite,
        attributesModifiersPositive,
        canDestroy,
        canPlaceOn,
        canDestroyMention,
        enchantments,
        lore,
        unbreakable,
    }
    private Map<String,String> rewriteChoice = new HashMap<String,String>(){{
        put("unbreakable","UnbreakableRewrite");
        put("canMine","CanMineRewrite");
    }};


    private boolean descriptionRewrite;
    private int hiddenFlagsValue;
    private Map<EnchantmentType, String> enchantRewrite;
    private Map<String, String> modifierRewrite;
    private String unbreakableRewrite;

    private String CanMineRewrite;

    private final HashMap flagsMap = new HashMap<flags, Integer>() {{
        put(flags.Enchantments, 1);
        put(flags.Attributes_modifiers, 2);
        put(flags.Unbreakable, 4);
        put(flags.CanDestroy, 8);
        put(flags.CanPlaceOn, 16);
        put(flags.Others, 32);
    }};

    private Map<String, Boolean> hiddenFlags;

    public Map<EnchantmentType, String> getEnchantRewrite() {
        return enchantRewrite;
    }

    public Map<String, String> getModifierRewrite() {
        return modifierRewrite;
    }

    private Map<RewriteFlagColorList, TextColor> colorMap;

    public GlobalConfig(  String unbreakableRewrite,
                          String CanMineRewrite,
                        Map<String, Boolean> flagToHide,
                        Map<EnchantmentType, String> enchantRewrite,
                        Map<String, String> modifierRewrite,
                        Map<RewriteFlagColorList, TextColor> colors ) {


        int flagsValue = 0;
        for (Map.Entry<String, Boolean> flag : flagToHide.entrySet()) {

            if (flag.getValue()) {
                flagsValue += (int) flagsMap.get(flags.valueOf(flag.getKey()));
            }
        }


        this.unbreakableRewrite = unbreakableRewrite;
        this.CanMineRewrite = CanMineRewrite;

        this.hiddenFlagsValue = flagsValue;
        this.hiddenFlags = flagToHide;
        this.enchantRewrite = enchantRewrite;
        this.modifierRewrite = modifierRewrite;


        this.colorMap = colors;


    }

    public Map<RewriteFlagColorList, TextColor> getColorMap() {
        return colorMap;
    }

    public String getCanMineRewrite() {
        return CanMineRewrite;
    }

    public String getUnbreakableRewrite() {
        return unbreakableRewrite;
    }

    public int getHiddenFlagsValue() {
        return hiddenFlagsValue;
    }

    public Map<String, Boolean> getHiddenFlags() {
        return hiddenFlags;
    }


}
