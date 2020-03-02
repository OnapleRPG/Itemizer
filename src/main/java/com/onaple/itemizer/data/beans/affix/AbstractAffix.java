package com.onaple.itemizer.data.beans.affix;

import com.onaple.itemizer.probability.Probable;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextFormat;

@ConfigSerializable
public abstract class AbstractAffix implements AffixFactory {

    @Setting("suffix")
    private String suffix;
    @Setting("prefix")
    private String prefix;
    @Setting("probability")
    private double probability;

    public AbstractAffix() {
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public ItemStack apply(ItemStack itemStack) {
        String name = itemStack.get(Keys.DISPLAY_NAME).map(Text::toPlain).orElse(itemStack.getType().getTranslation().get());
        TextFormat textFormat = itemStack.get(Keys.DISPLAY_NAME).map(text -> text.getFormat()).orElse(TextFormat.NONE);
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(getPrefix()+ " " + name +" " + getSuffix(),textFormat));
        return itemStack;
    }
}
