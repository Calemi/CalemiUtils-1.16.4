package com.tm.calemiutils.item;

import com.tm.calemiutils.main.CalemiUtils;
import com.tm.calemiutils.block.BlockBlueprint;
import com.tm.calemiutils.item.base.ItemBase;
import com.tm.api.calemicore.util.Location;
import com.tm.api.calemicore.util.VeinScan;
import com.tm.api.calemicore.util.helper.LoreHelper;
import com.tm.api.calemicore.util.helper.SoundHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEraser extends ItemBase {

    public ItemEraser () {
        super(new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation (ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {
        LoreHelper.addInformationLore(tooltipList, "Destroys Blueprint", true);
        LoreHelper.addControlsLore(tooltipList, "Erases one Blueprint", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltipList, "Erases all connected Blueprint", LoreHelper.Type.SNEAK_USE);
    }

    /**
     * Handles erasing.
     */
    @Override
    public ActionResultType onItemUse (ItemUseContext context) {

        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();

        Location location = new Location(world, pos);

        //Checks if the Player exists.
        if (player != null) {

            //Checks if the block clicked is a Blueprint.
            if (location.getBlock() instanceof BlockBlueprint) {

                SoundHelper.playAtPlayer(player, SoundEvents.ENTITY_SLIME_ATTACK, SoundCategory.PLAYERS, 0.9F, 1.0F);

                //If the Player is not crouching, remove only one Blueprint.
                if (!player.isCrouching()) {
                    location.setBlockToAir();
                }

                //If the Player is crouching, remove multiple Blueprints.
                else {

                    //Starts a scan of all connected Blueprint.
                    VeinScan scan = new VeinScan(location, location.getForgeBlockState());
                    scan.startScan();

                    //Iterates through all scanned Blueprints and removes them.
                    for (Location nextLocation : scan.buffer) {
                        nextLocation.setBlockToAir();
                    }
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }

    /**
     * Used to increase the mining speed on Blueprint.
     */
    @Override
    public float getDestroySpeed (ItemStack stack, BlockState state) {

        if (state.getBlock() instanceof BlockBlueprint) {
            return 9F;
        }

        return super.getDestroySpeed(stack, state);
    }
}
