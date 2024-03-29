package com.tm.calemiutils.inventory;

import com.tm.calemiutils.init.InitContainerTypes;
import com.tm.calemiutils.init.InitItems;
import com.tm.calemiutils.inventory.base.ContainerBase;
import com.tm.api.calemicore.inventory.SlotIInventoryFilter;
import com.tm.calemiutils.item.ItemCoin;
import com.tm.calemiutils.util.helper.CurrencyHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerWallet extends ContainerBase {

    private final IInventory stackInv;

    public ContainerWallet(final int windowID, final PlayerInventory playerInventory, IInventory stackInv) {
        super(InitContainerTypes.WALLET.get(), windowID, playerInventory, null, 8, 94);

        isItemContainer = true;
        size = 1;

        this.stackInv = stackInv;

        //New Inventory
        addSlot(new SlotIInventoryFilter(stackInv, 0, 17, 42, InitItems.COIN_COPPER.get(), InitItems.COIN_SILVER.get(), InitItems.COIN_GOLD.get(), InitItems.COIN_PLATINUM.get()));
    }

    public static ContainerWallet createClientWallet (final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        data.readVarInt();
        return new ContainerWallet(windowId, playerInventory, new Inventory(1));
    }

    private ItemStack getCurrentWalletStack () {
        return CurrencyHelper.getCurrentWalletStack(playerInventory.player);
    }

    /**
     * Called when a slot is clicked.
     * Handles adding money to Wallet.
     */
    @Override
    public ItemStack slotClick (int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {

        ItemStack returnStack = super.slotClick(slotId, dragType, clickTypeIn, player);
        ItemStack stackInInv = stackInv.getStackInSlot(0);
        ItemStack walletStack = getCurrentWalletStack();

        //Checks if the Stack in the Wallet is a Coin.
        if (stackInInv.getItem() instanceof ItemCoin) {

            ItemCoin currency = ((ItemCoin) stackInInv.getItem());

            int amountToAdd = 0;
            int stacksToRemove = 0;

            //Iterates through every count of the Stack. Ex: a stack of 32 will iterate 32 times.
            for (int i = 0; i < stackInInv.getCount(); i++) {

                //Checks if the Wallet can fit the added money.
                if (CurrencyHelper.canDepositToWallet(walletStack, currency.value)) {
                    amountToAdd += currency.value;
                    stacksToRemove++;
                }

                else break;
            }

            CurrencyHelper.depositToWallet(walletStack, amountToAdd);
            stackInv.decrStackSize(0, stacksToRemove);
        }

        return returnStack;
    }

    @Override
    public void onContainerClosed (PlayerEntity player) {
        super.onContainerClosed(player);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize () {
        return size;
    }
}
