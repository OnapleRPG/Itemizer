package com.onaple.itemizer;

import com.onaple.itemizer.service.IItemService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.mctester.api.junit.MinecraftRunner;
import org.spongepowered.mctester.internal.BaseTest;
import org.spongepowered.mctester.internal.event.StandaloneEventListener;
import org.spongepowered.mctester.junit.TestUtils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

//@RunWith(MinecraftRunner.class)
public class ItemizerTest extends BaseTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public ItemizerTest(TestUtils testUtils) {
        super(testUtils);
    }

    /**
     * Command /reload-itemizer should returns confirmation messages
     */
    /*@Test
    public void testReloadItemizer() throws Throwable {
        String itemsReloadedString = "Items configuration successfully reloaded";
        String minersReloadedString = "Miners configuration successfully reloaded";
        String poolsReloadedString = "Pools configuration successfully reloaded";
        String craftsReloadedString = "Crafts configuration successfully reloaded";
        AtomicBoolean itemsReloadedBool = new AtomicBoolean(false), minersReloadedBool = new AtomicBoolean(false), poolsReloadedBool = new AtomicBoolean(false), craftsReloadedBool = new AtomicBoolean(false);
        this.testUtils.listenOneShot(() -> {
            this.testUtils.getClient().sendMessage("/reload-itemizer");
        }, new StandaloneEventListener<>(MessageChannelEvent.class, (MessageChannelEvent event) -> {
            if (event.getMessage().toPlain().contains(itemsReloadedString)) {
                itemsReloadedBool.set(true);
            }
            if (event.getMessage().toPlain().contains(minersReloadedString)) {
                minersReloadedBool.set(true);
            }
            if (event.getMessage().toPlain().contains(poolsReloadedString)) {
                poolsReloadedBool.set(true);
            }
            if (event.getMessage().toPlain().contains(craftsReloadedString)) {
                craftsReloadedBool.set(true);
            }
        }));
        Assert.assertTrue(itemsReloadedBool.get() && minersReloadedBool.get() && poolsReloadedBool.get() && craftsReloadedBool.get());
    }*/

    /**
     * Trying to retrieve a configured item
     */
    /*@Test
    public void testRetrieveItem() throws Throwable {
        this.testUtils.getClient().sendMessage("/retrieve 1");
        this.testUtils.waitForInventoryPropagation();
        this.testUtils.runOnMainThread(() -> {
            Optional<IItemService> optionalItemService = Sponge.getServiceManager().provide(IItemService.class);
            Assert.assertTrue(optionalItemService.isPresent());
            Optional<ItemStack> item = optionalItemService.get().retrieve("1");
            Assert.assertTrue(item.isPresent());
            Assert.assertTrue(this.testUtils.getThePlayer().getInventory().contains(item.get()));
        });
    }*/
}
