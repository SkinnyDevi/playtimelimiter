package com.skinnydevi.playtimelimiter.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.skinnydevi.playtimelimiter.playtime.PlaytimeDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class CommandPlaytime{
    private static final String CMD_PREFIX = "playtime";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(CMD_PREFIX).requires(csource -> csource.hasPermission(2))
                    .then(playtimeLeft())
                    .then(playtimeReset())
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeLeft() {
        return Commands.literal("left").executes(ctx -> {
            Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(Component.literal(ChatFormatting.GREEN + "You have "
                    + ChatFormatting.BOLD + PlaytimeDataManager.getRemainingFormattedTime(ctx.getSource().getPlayer()) + ChatFormatting.GREEN + " left"));

            return 1;
        });
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeReset() {
        return Commands.literal("reset").requires(csource -> csource.hasPermission(4))
                .then(Commands.argument("targetplayer", EntityArgument.players()).executes(ctx -> {
                    ServerPlayer targetPlayer = EntityArgument.getPlayer(ctx, "targetplayer");

                    targetPlayer.getPersistentData().remove("timeout");
                    targetPlayer.getPersistentData().remove("timeLeft");
                    PlaytimeDataManager.resetTime(targetPlayer);
                    Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(
                            Component.literal(
                                    ChatFormatting.GREEN + "You have successfully reset the Playtime and Timeout for "
                                            + ChatFormatting.AQUA + targetPlayer.getName().getString()
                            )
                    );

                    return 1;
                }));
    }
}
