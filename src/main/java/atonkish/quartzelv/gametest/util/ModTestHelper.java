package atonkish.quartzelv.gametest.util;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.authlib.GameProfile;

import io.netty.channel.embedded.EmbeddedChannel;

public class ModTestHelper {
    private static AtomicInteger playerId = new AtomicInteger(1);

    public static ServerPlayerEntity createMockServerPlayer(TestContext context, GameMode gameMode) {
        ConnectedClientData data = ConnectedClientData
                .createDefault(
                        new GameProfile(UUID.randomUUID(), String.format("mock-player-%d", playerId.getAndIncrement())),
                        false);
        ServerPlayerEntity player = new ServerPlayerEntity(context.getWorld().getServer(), context.getWorld(),
                data.gameProfile(), data.syncedOptions());
        ClientConnection connection = new ClientConnection(NetworkSide.SERVERBOUND);
        new EmbeddedChannel(connection);
        context.getWorld().getServer().getPlayerManager().onPlayerConnect(connection, player, data);

        player.changeGameMode(gameMode);
        player.setOnGround(true);

        return player;
    }

    public static void destroyMockServerPlayer(TestContext context, ServerPlayerEntity player) {
        player.discard();
        player.getInventory().clear();
        player.networkHandler.disconnect(Text.of(String.format("%s (%s) left the game",
                player.getGameProfile().getName(), player.getUuidAsString())));
    }
}
