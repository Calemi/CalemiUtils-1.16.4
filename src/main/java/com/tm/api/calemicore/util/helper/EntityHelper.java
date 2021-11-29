package com.tm.api.calemicore.util.helper;

import com.tm.api.calemicore.util.Location;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;
import java.util.Set;

public class EntityHelper {

    public static void teleportPlayer(PlayerEntity player, Location location) {
        teleportPlayer(player, location, 0, 0);
    }

    public static void teleportPlayer(PlayerEntity player, Location location, float yaw) {
        teleportPlayer(player, location, yaw, 0);
    }

    public static void teleportPlayer(PlayerEntity player, Location location, float yaw, float pitch) {

        World world = player.world;

        if (world.isRemote()) return;

        Set<SPlayerPositionLookPacket.Flags> set = EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class);

        if (player instanceof ServerPlayerEntity) {

            ServerWorld serverWorld = (ServerWorld) world;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            serverPlayer.teleport(serverWorld, location.x + 0.5D, location.y + 0.2D, location.z + 0.5D, yaw, pitch);
        }

        else {

            player.setPositionAndUpdate(location.x + 0.5D, location.y + 0.2D, location.z + 0.5D);
            player.setRotationYawHead(yaw);
            player.setMotion(0, 0, 0);
            player.fallDistance = 0;
            player.setOnGround(true);
        }
    }

    public static boolean canTeleportAt(Location legLocation) {

        Location groundLocation = new Location(legLocation, Direction.DOWN);
        Location headLocation = new Location(legLocation, Direction.UP);

        return groundLocation.doesBlockHaveCollision() && !legLocation.doesBlockHaveCollision() && !headLocation.doesBlockHaveCollision();
    }
}
