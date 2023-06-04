package com.skinnydevi.playtimelimiter.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.skinnydevi.playtimelimiter.config.ConfigManager;
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
                Commands.literal(CMD_PREFIX).requires(src -> src.hasPermission(0))
                    .then(playtimeLeft())
                    .then(playtimeReset())
                    .then(playtimeTotalTime())
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
        return Commands.literal("reset").requires(src -> src.hasPermission(2))
                .then(playtimeResetTimeout())
                .then(playtimeResetTotalTime())
                .then(playtimeResetAll());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeResetAll() {
        return Commands.literal("all")
                .then(Commands.argument("targetPlayer", EntityArgument.players()).executes(ctx -> {
                    resetTimeout(ctx);
                    resetTotalTime(ctx);

                    return 1;
                }
            ));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeResetTimeout() {
        return Commands.literal("timeout")
                .then(Commands.argument("targetPlayer", EntityArgument.players()).executes(ctx -> {
                    resetTimeout(ctx);

                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeResetTotalTime() {
        return Commands.literal("totalTime")
                .then(Commands.argument("targetPlayer", EntityArgument.players()).executes(ctx -> {
                    resetTotalTime(ctx);

                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playtimeTotalTime() {
        return Commands.literal("total").executes(ctx -> {
            if (!ConfigManager.TRACK_TOTAL_PLAYTIME.get()) {
                Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(
                        Component.literal(ChatFormatting.AQUA + "Total play time tracking is disabled.")
                );

                return 1;
            }

            ServerPlayer player = ctx.getSource().getPlayer();

            if (player == null) return 0;

            long[] timeplayed = PlaytimeDataManager.getFormattedTotalPlayTime(player);

            player.sendSystemMessage(
                    Component.literal(
                            ChatFormatting.GREEN + "You have played a total of "
                                    + ChatFormatting.RED + timeplayed[0] + ChatFormatting.GREEN + " days, "
                                    + ChatFormatting.RED + timeplayed[1] + ChatFormatting.GREEN + " hours, "
                                    + ChatFormatting.RED + timeplayed[2] + ChatFormatting.GREEN + " minutes and "
                                    + ChatFormatting.RED + timeplayed[3] + ChatFormatting.GREEN + " seconds"
                    )
            );

            return 1;
        });
    }

    private static void resetTimeout(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(ctx, "targetPlayer");

        targetPlayer.getPersistentData().remove("timeout");
        targetPlayer.getPersistentData().remove("timeLeft");
        PlaytimeDataManager.resetTimeout(targetPlayer);
        Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(
                Component.literal(
                        ChatFormatting.GREEN + "You have successfully reset the Timeout for "
                                + ChatFormatting.AQUA + targetPlayer.getName().getString()
                )
        );
    }

    private static void resetTotalTime(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!ConfigManager.TRACK_TOTAL_PLAYTIME.get()) {
            Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(
                    Component.literal(ChatFormatting.AQUA + "Total play time tracking is disabled.")
            );

            return;
        }

        ServerPlayer targetPlayer = EntityArgument.getPlayer(ctx, "targetPlayer");

        targetPlayer.getPersistentData().remove("timePlayed");
        PlaytimeDataManager.resetTotalTime(targetPlayer);
        Objects.requireNonNull(ctx.getSource().getPlayer()).sendSystemMessage(
                Component.literal(
                        ChatFormatting.GREEN + "You have successfully reset the Playtime for "
                                + ChatFormatting.AQUA + targetPlayer.getName().getString()
                )
        );
    }
}
