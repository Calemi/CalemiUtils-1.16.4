package com.tm.api.calemicore.tileentity;

import com.tm.api.calemicore.inventory.SlotFilter;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CCItemHandler extends ItemStackHandler {

    private final List<Slot> containerSlots;

    public CCItemHandler(int size, List<Slot> containerSlots, ItemStack... stacks) {
        super(size);

        this.containerSlots = containerSlots;

        for (int index = 0; index < stacks.length; index++) {
            this.stacks.set(index, stacks[index]);
        }
    }

    @Override
    public boolean isItemValid (int slot, @Nonnull ItemStack stack) {

        if (containerSlots != null && containerSlots.get(slot) != null && containerSlots.get(slot) instanceof SlotFilter) {
            return containerSlots.get(slot).isItemValid(stack);
        }

        return true;
    }

    public void clear() {

        for (int index = 0; index < this.getSlots(); index++) {
            this.stacks.set(index, ItemStack.EMPTY);
            this.onContentsChanged(index);
        }
    }

    public boolean isEmpty() {
        return stacks.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack decrStackSize (int index, int count) {
        return ItemStackHelper.getAndSplit(stacks, index, count);
    }

    public ItemStack removeStackFromSlot (int index) {
        return ItemStackHelper.getAndRemove(stacks, index);
    }

    public NonNullList<ItemStack> toNonNullList() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(stacks);
        return items;
    }

    public void setNonNullList(NonNullList<ItemStack> items) {

        if (items.size() == 0) {
            return;
        }

        if (items.size() != this.getSlots()) {
            throw new IndexOutOfBoundsException("NonNullList must be same size as ItemStackHandler!");
        }

        for (int index = 0; index < items.size(); index++) {
            stacks.set(index, items.get(index));
        }
    }

    @Override
    public String toString() {
        return stacks.toString();
    }
}
