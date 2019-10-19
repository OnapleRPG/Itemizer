package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.LIGHT_PURPLE;


public class GetItemInfos implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Optional<ItemStack> objectInHandOpt = ((Player) src).getItemInHand(HandTypes.MAIN_HAND);
            if (!objectInHandOpt.isPresent()) {
                src.sendMessage(Text.of("You don't have any item in hand, to analyse a item put it in your main hand"));
                return CommandResult.empty();
            } else {
                ItemStack objectToAnalyse = objectInHandOpt.get();
                ItemBean bean = Itemizer.getItemManager().getItemBean(objectToAnalyse);

                List<Text> content = new ArrayList<>();

                Optional<List<Enchantment>> enchantments = objectToAnalyse.get(Keys.STORED_ENCHANTMENTS);
                if (enchantments.isPresent()) {
                    List<Text> names =
                            enchantments.get().stream().map(enchantment -> Text.of(LIGHT_PURPLE, enchantment.getType().getName() + " " + enchantment.getLevel())).collect(Collectors.toList());
                    ;
                    content.add(Text.builder().append(Text.of(LIGHT_PURPLE, TextStyles.UNDERLINE, "Enchantments : "))
                            .append(names)
                            .build());
                }


                PaginationList.builder()
                        .title(Text.of(TextColors.GOLD, TextStyles.BOLD, bean))
                        .header(Text.of("Object Characteristics"))
                        .contents(content)
                        .footer(
                                Text.builder().append(
                                        Text.builder("[Lore]").color(TextColors.GREEN).build(),
                                        Text.builder("[Enchantments]").color(LIGHT_PURPLE).build(),
                                        Text.builder("[Modifiers]").color(TextColors.RED).build(),
                                        Text.builder("[Miners]").color(TextColors.GRAY).build(),
                                        Text.builder("[Durability]").color(TextColors.YELLOW).build()
                                ).build()
                        )
                        .build().sendTo(src);

                return CommandResult.success();
            }
        }
        return CommandResult.empty();
    }
}
