package com.tm.calemiutils.event;

import com.tm.api.calemicore.util.Location;
import com.tm.api.calemicore.util.helper.RayTraceHelper;
import com.tm.api.calemicore.util.helper.ScreenHelper;
import com.tm.api.calemicore.util.helper.StringHelper;
import com.tm.calemiutils.item.ItemLinkBookLocation;
import com.tm.calemiutils.tileentity.TileEntityLinkPortal;
import com.tm.calemiutils.tileentity.TileEntityPortalProjector;
import com.tm.calemiutils.util.helper.CurrencyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class LinkPortalOverlayEvent {

    /**
     * Handles the Trading Post overlay when the cursor is over it.
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void render (RenderGameOverlayEvent.Post event) {

        //Checks if the current render is on the "HOTBAR" layer, so we can use transparency.
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT) {

            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            ClientPlayerEntity player = mc.player;

            //Checks if the World and Player exists.
            if (world != null && player != null) {

                int scaledWidth = mc.getMainWindow().getScaledWidth();
                int scaledHeight = mc.getMainWindow().getScaledHeight();
                int midX = scaledWidth / 2;
                int midY = scaledHeight / 2;

                RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.rayTraceBlock(world, player, Hand.MAIN_HAND);

                //Checks if the trace hit a block.
                if (blockTrace != null) {

                    Location hit = blockTrace.getHit();

                    //Check if the hit was a Trading Post
                    if (hit.getTileEntity() instanceof TileEntityLinkPortal) {

                        TileEntityLinkPortal portal = (TileEntityLinkPortal) hit.getTileEntity();

                        TileEntityPortalProjector projector = portal.getProjector();

                        if (projector.isProjecting()) {

                            Location linkedLocation = ItemLinkBookLocation.getLinkedLocation(world, projector.getLinkBook());

                            if (linkedLocation != null) {

                                int travelCost = ItemLinkBookLocation.getCostForTravel(world, linkedLocation, player);

                                if (travelCost > 0) {

                                    String name = projector.getLinkBook().getDisplayName().getString();
                                    String dimName = ItemLinkBookLocation.getLinkedDimensionName(projector.getLinkBook());
                                    String locationString = dimName.substring(dimName.indexOf(":") + 1).toUpperCase() + " " + linkedLocation;
                                    String costString = "Total Travel Cost: " + CurrencyHelper.printCurrency(travelCost);

                                    List<String> list = new ArrayList<>();
                                    list.add(name);
                                    list.add(locationString);
                                    list.add(costString);

                                    ScreenHelper.bindGuiTextures();
                                    ScreenHelper.drawTextBox(event.getMatrixStack(), midX - 3, midY + 12, 0, true, StringHelper.getArrayFromList(list));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
