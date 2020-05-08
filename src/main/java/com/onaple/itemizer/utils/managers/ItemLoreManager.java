package com.onaple.itemizer.utils.managers;

import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemDataManager;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ItemLoreManager implements ItemDataManager {

    private ItemLoreManager(ItemBean itemBean, List<Text> lore) {
        this.itemBean = itemBean;
        this.lore = lore;
    }

    public ItemBean getItemBean() {
        return itemBean;
    }

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public List<Text> getLore() {
        return lore;
    }

    public void setLore(List<Text> lore) {
        this.lore = lore;
    }

    private ItemBean itemBean;
    private ItemLoreManager(ItemBean itemBean){
        this.itemBean = itemBean;
        if(itemBean.getItemStack().supports(Keys.ITEM_LORE)){
            this.lore = itemBean.getItemStack().get(Keys.ITEM_LORE).orElse(new ArrayList<>());
        } else {
           this.lore = new ArrayList<>();
        }
    }

    public static ItemLoreManager of(ItemBean item){
        return new ItemLoreManager(item);
    }
    public static ItemLoreManager of(ItemBean item,List<Text> lore){
        return new ItemLoreManager(item,lore);
    }
    private List<Text> lore;

    @Override
    public Text showResume() {
        Text.Builder textBuilder = Text.builder().append(Text.of(TextColors.DARK_GREEN, "Lore : "));
        textBuilder.append(lore);
        return textBuilder.build();
    }

    @Override
    public Text showModifyButton() {
        String joinnedLore = lore.stream().map(Text::toPlain).collect(Collectors.joining(" "));
        return Text.builder("[Lore]").color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Set item lore")))
                .onClick(TextActions.suggestCommand("/set-lore " + itemBean.getId() + " " + joinnedLore))
                .build();
    }

    @Override
    public void apply() {
       /*ItemStack newStack = itemBean.getItemStack();
       newStack.offer(Keys.ITEM_LORE,lore);
       itemBean.setItemStack(newStack);*/
    }

    @Override
    public boolean supports() {
        return itemBean.getItemStack().supports(Keys.ITEM_LORE);
    }
}
