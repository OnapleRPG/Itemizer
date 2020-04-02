package com.onaple.itemizer;

import com.onaple.itemizer.data.serializers.HiddenFlagsAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.format.TextColor;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
@ConfigSerializable
public class GlobalConfig {

    public enum Flags {
        Enchantments,
        Attributes_modifiers,
        Unbreakable,
        CanDestroy,
        CanPlaceOn,
        Others;
    }

    public enum RewriteFlag {
        attributesModifiersNegavite,
        attributesModifiersPositive,
        canDestroy,
        canPlaceOn,
        canDestroyMention,
        enchantments,
        lore,
        unbreakable,
        others
    }

    @Setting("DescriptionRewrite")
    private boolean descriptionRewrite;

    @Setting("RewriteParts")
    @CustomAdapter(HiddenFlagsAdapter.class)
    private Map<Flags, Boolean> hiddenFlags;

    @Setting("EnchantRewrite")
    private Map<EnchantmentType, String> enchantRewrite;

    @Setting("ModifierRewrite")
    private Map<String, String> modifierRewrite;

    @Setting("UnbreakableRewrite")
    private String unbreakableRewrite;

    @Setting("CanMineRewrite")
    private String CanMineRewrite;

    @Setting("DefaultColor")
    private Map<RewriteFlag, TextColor> colorMap;

    private int hiddenFlagsValue;

    public Map<EnchantmentType, String> getEnchantRewrite() {
        return enchantRewrite;
    }

    public Map<String, String> getModifierRewrite() {
        return modifierRewrite;
    }

    public Map<RewriteFlag, TextColor> getColorMap() {
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

    public Map<Flags, Boolean> getHiddenFlags() {
        return hiddenFlags;
    }

    public void setHiddenFlagsValue(int hiddenFlagsValue) {
        this.hiddenFlagsValue = hiddenFlagsValue;
    }
}
