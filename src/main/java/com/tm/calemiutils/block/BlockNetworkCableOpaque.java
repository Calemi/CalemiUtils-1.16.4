package com.tm.calemiutils.block;

import com.tm.calemiutils.block.base.BlockContainerBase;
import com.tm.calemiutils.init.InitTileEntityTypes;
import com.tm.api.calemicore.util.helper.LoreHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockNetworkCableOpaque extends BlockContainerBase {

    public BlockNetworkCableOpaque () {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2).harvestLevel(0));
    }

    BlockNetworkCableOpaque(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation (ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        LoreHelper.addInformationLore(tooltip, "Used to connect Trading Posts & Markets to Banks within a single network.", true);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity (IBlockReader worldIn) {
        return InitTileEntityTypes.NETWORK_CABLE.get().create();
    }
}
