package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.format.TextColor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GlobalConfigurationSerializer implements TypeSerializer<GlobalConfig> {
    @Override
    public GlobalConfig deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        boolean DescriptionRewrite = value.getNode("DescriptionRewrite").getBoolean();
        Map<String,Boolean> hiddenFlags = new HashMap<>();
        value.getNode("RewriteParts").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof Boolean){
                hiddenFlags.put((String) o,(Boolean) o2.getValue());
            }
        });
        Map<EnchantmentType,String> enchantMap = new HashMap<>();
        value.getNode("EnchantRewrite").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                Optional<EnchantmentType> enchant =  Sponge.getRegistry().getType(EnchantmentType.class,(String) o);
                enchant.ifPresent(enchantmentType ->   enchantMap.put(enchantmentType,(String) o2.getValue()));
            }
        });
        Map<String,String> modifierMap = new HashMap<>();
        value.getNode("ModifierRewrite").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                modifierMap.put((String) o,(String) o2.getValue());
            }
        });

        String unbreakable = value.getNode("UnbreakableRewrite").getString();

        String canMineRewrite = value.getNode("CanMineRewrite").getString();

        Map<GlobalConfig.RewriteFlagColorList, TextColor> colors = new HashMap<>();

        value.getNode("DefaultColor").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                Optional<TextColor> colorOptional = Sponge.getRegistry().getType(TextColor.class, o2.getString());

                colorOptional.ifPresent(textColor -> {
                    Itemizer.getLogger().info(textColor.toString());
                    colors.put(GlobalConfig.RewriteFlagColorList.valueOf((String)o),textColor);
                });

            }
        });


        return new GlobalConfig(unbreakable,canMineRewrite,hiddenFlags,enchantMap,modifierMap,colors);

    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable GlobalConfig obj, ConfigurationNode value) throws ObjectMappingException {

        value.getNode("RewriteParts").setValue(obj.getHiddenFlags());

        value.getNode("UnbreakableRewrite").setValue(obj.getUnbreakableRewrite());
        value.getNode("CanMineRewrite").setValue(obj.getCanMineRewrite());

        Map<String,String> EnchantMap = new HashMap<>();
        obj.getEnchantRewrite().forEach((enchant, s) -> EnchantMap
                .put(enchant.getTranslation().get(Locale.ENGLISH),s));
        value.getNode("EnchantRewrite").setValue(EnchantMap);
        value.getNode("ModifierRewrite").setValue(obj.getModifierRewrite());

        Map<String,String> colorMap = new HashMap<>();
        obj.getColorMap().forEach((s, textColor) -> colorMap.put(s.name(),textColor.getName().toUpperCase()));
        value.getNode("DefaultColor").setValue(colorMap);


    }
}
