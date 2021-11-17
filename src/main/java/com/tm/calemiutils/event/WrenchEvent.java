package com.tm.calemiutils.event;

import com.tm.calemiutils.tileentity.base.ICurrencyNetworkBank;
import com.tm.calemiutils.tileentity.base.TileEntityCUBase;
import com.tm.api.calemicore.util.Location;
import com.tm.api.calemicore.util.helper.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WrenchEvent {

    /**
     * Called when a Block is taken by a Security Wrench.
     * Also saves the currency of the Block within the drop.
     */
    public static void onBlockWrenched (World world, Location location) {

        TileEntity tileEntity = location.getTileEntity();
        ItemStack stack = new ItemStack(location.getBlock().asItem(), 1);

        if (!world.isRemote) {
            ItemHelper.spawnStackAtLocation(world, location, stack);
        }

        //Handles currency saving.
        if (tileEntity instanceof ICurrencyNetworkBank) {

            ICurrencyNetworkBank currencyNetwork = (ICurrencyNetworkBank) tileEntity;

            if (currencyNetwork.getStoredCurrency() > 0) {
                ItemHelper.getNBT(stack).putInt("currency", currencyNetwork.getStoredCurrency());
            }
        }

        location.setBlockToAir();
    }

    /**
     * Handles transforming the Item's NBT into currency for the Block.
     */
    @SubscribeEvent
    public void onBlockPlace (BlockEvent.EntityPlaceEvent event) {

        //Checks if the entity is a Player.
        if (event.getEntity() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) event.getEntity();
            TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());

            if (tileEntity instanceof TileEntityCUBase) {
                ItemStack mainStack = player.getHeldItem(Hand.MAIN_HAND);
                ItemStack offStack = player.getHeldItem(Hand.OFF_HAND);

                if (transferCurrencyToBlock(mainStack, (TileEntityCUBase) tileEntity));
                else transferCurrencyToBlock(offStack, (TileEntityCUBase) tileEntity);
            }
        }
    }

    /**
     * Used to check if a stack hold currency, and if it does, transfer it to the block.
     * Called twice for main & off hand.
     */
    private boolean transferCurrencyToBlock(ItemStack stack, TileEntityCUBase tileEntity) {

        //Checks if the held Item is a Block.
        if (stack.getItem() instanceof BlockItem) {

            //Handles restoring currency.
            if (tileEntity instanceof ICurrencyNetworkBank) {

                ICurrencyNetworkBank currencyNetwork = (ICurrencyNetworkBank) tileEntity;

                if (ItemHelper.getNBT(stack).getInt("currency") != 0) {
                    currencyNetwork.setCurrency(ItemHelper.getNBT(stack).getInt("currency"));
                    tileEntity.markForUpdate();
                }

                return true;
            }
        }

        return false;
    }
}
