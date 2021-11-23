package com.tm.calemiutils.enchantment;

import com.tm.calemiutils.main.CUReference;
import com.tm.calemiutils.item.ItemSledgehammer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CUReference.MOD_ID)
public class EnchantmentCrushing extends Enchantment {

    private static final EnchantmentType HAMMER = EnchantmentType.create("weapons", (item) -> (item instanceof ItemSledgehammer));

    public EnchantmentCrushing () {
        super(Rarity.UNCOMMON, HAMMER, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel () {
        return 2;
    }

    public int getMinEnchantability (int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    public int getMaxEnchantability (int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
