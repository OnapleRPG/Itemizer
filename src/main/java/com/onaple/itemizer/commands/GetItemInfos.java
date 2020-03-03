package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.managers.ItemLoreManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.item.UseLimitProperty;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.LIGHT_PURPLE;
import static org.spongepowered.api.text.format.TextColors.RED;


public class GetItemInfos implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Optional<ItemStack> objectInHandOpt = ((Player) src).getItemInHand(HandTypes.MAIN_HAND);
            if (!objectInHandOpt.isPresent()) {
                src.sendMessage(Text.of("You don't have any item in hand, to analyse a item put it in your main hand"));
                return CommandResult.empty();
            }
            ItemStack objectToAnalyse = objectInHandOpt.get();
            Optional<ItemBean> itemizerElementOptional = Itemizer.getItemDAO().getItem(ItemBean.getId(objectToAnalyse));
            if (!itemizerElementOptional.isPresent()) {
                src.sendMessage(Text.of("This object is not register in Itemizer. use /register first"));
                return CommandResult.empty();
            }
            ItemBean bean = itemizerElementOptional.get();

            ItemLoreManager loreManager = ItemLoreManager.of(bean);

            Itemizer.getLogger().info("[potion type ={}]", objectToAnalyse.supports(Keys.POTION_TYPE));

            Itemizer.getLogger().info("[{}]", objectToAnalyse.getContainers());

            List<Text> content = new ArrayList<>();
            content.add(displayName(objectToAnalyse));
            if (loreManager.supports()) {
                content.add(loreManager.showResume());
            }
            content.add(displayEnchantmentsList(objectToAnalyse));
            content.add(displayModifier(bean));
            content.add(displayDurability(objectToAnalyse));
            PaginationList.builder()
                    .title(Text.of(TextColors.GOLD, TextStyles.BOLD, bean.getId()))
                    .header(Text.of("Object Characteristics"))
                    .contents(content)
                    .linesPerPage(3)
                    .footer(
                            Text.builder().append(
                                    loreManager.showModifyButton(),
                                    Text.builder("[Modifiers]").color(TextColors.RED).onHover(TextActions.showText(Text.of("Add or Remove enchantments.")))
                                            .onClick(TextActions.suggestCommand("/suggestion"))
                                            .build(),
                                    Text.builder("[Miners]").color(TextColors.GRAY).onHover(TextActions.showText(Text.of("Add or Remove enchantments.")))
                                            .onClick(TextActions.suggestCommand("/suggestion"))
                                            .build(),
                                    Text.builder("[Durability]").color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Add or Remove enchantments.")))
                                            .onClick(TextActions.suggestCommand("/suggestion"))
                                            .build()
                            ).build()
                    )
                    .build().sendTo(src);

            return CommandResult.success();

        }
        return CommandResult.empty();
    }

    private Text displayEnchantmentsList(ItemStack input) {
        Optional<List<Enchantment>> enchantments = input.get(Keys.ITEM_ENCHANTMENTS);
        Text.Builder enchantmentsBuilder = Text.builder().append(Text.of(LIGHT_PURPLE, TextStyles.UNDERLINE, "Enchantments : "));
        if (enchantments.isPresent()) {
            List<Text> names =
                    enchantments.get().stream().map(enchantment -> Text.of(LIGHT_PURPLE, enchantment.getType().getTranslation().get(Locale.ENGLISH) + " " + enchantment.getLevel())).collect(Collectors.toList());
            enchantmentsBuilder.append(names);
        }
        return enchantmentsBuilder.build();
    }

    private Text displayName(ItemStack input) {
        Optional<Text> name = input.get(Keys.DISPLAY_NAME);
        Text.Builder textBuilder = Text.builder().append(Text.of(GREEN, "Item name : "));
        textBuilder.append(name.orElse(Text.of(input.getType().getName())));
        return textBuilder.build();
    }

    private Text displayModifier(ItemBean input) {
        List<AttributeBean> attributes = input.getItemAttribute();
        Text.Builder textBuilder = Text.builder().append(Text.of(RED, "Modifiers : "));
        List<Text> names =
                attributes.stream().map(attribute -> Text.of(attribute.getName() + " " + attribute.getCompiledAmount())).collect(Collectors.toList());
        textBuilder.append(names);
        return textBuilder.build();
    }

    private Text displayDurability(ItemStack input) {
        Optional<UseLimitProperty> maxDurability = input.getProperty(UseLimitProperty.class);
        if (maxDurability.isPresent()) {
            Integer durability = input.get(Keys.ITEM_DURABILITY).orElse(0);

            Boolean unbreakable = input.get(Keys.UNBREAKABLE).orElse(false);
            Text.Builder textBuilder = Text.builder().append(Text.of(TextColors.YELLOW, "Durability : "));
            textBuilder.append(Text.of(durability + "/" + maxDurability.get().getValue() + (unbreakable ? "Unbreakable" : "")));

            return textBuilder.build();
        }
        return Text.EMPTY;
    }
}
