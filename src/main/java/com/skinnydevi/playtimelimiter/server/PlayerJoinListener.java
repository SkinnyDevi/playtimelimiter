package com.skinnydevi.playtimelimiter.server;

import com.skinnydevi.playtimelimiter.PlaytimeLimiter;
import com.skinnydevi.playtimelimiter.config.ConfigManager;
import com.skinnydevi.playtimelimiter.playtime.PlaytimeDataManager;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


public class  PlayerJoinListener {
    @SubscribeEvent
    public void onMPPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer))
            return;

        PlaytimeLimiter.LOGGER.info("Loading Data for \"" + e.getEntity().getName().getString() + "\"");
        ServerPlayer player = (ServerPlayer) e.getEntity();
        PlaytimeDataManager.loadPlayer(player);
    }

    @SubscribeEvent
    public void onMPPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer))
            return;

        PlaytimeLimiter.LOGGER.info("Saving Data for \"" + e.getEntity().getName().getString() + "\"");
        ServerPlayer player = (ServerPlayer) e.getEntity();
        PlaytimeDataManager.savePlayer(player);
    }

    private int tick;
    private LocalTime oldTrackerTime = LocalTime.now();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (!e.side.isServer() || e.phase != TickEvent.Phase.START || e.type != TickEvent.Type.SERVER)
            return;
        tick++;

        if (tick % 20 == 0) {
            LocalTime newTrackerTime = LocalTime.now();
            float diff = Duration.between(oldTrackerTime, newTrackerTime).toMillis() / 1000f;
            oldTrackerTime = newTrackerTime;
            int workingSecond = Math.round(diff);

            PlaytimeDataManager.getTrackedPlayers().forEach(playerMP -> {
                if (playerMP.hasDisconnected()) {
                    PlaytimeDataManager.savePlayer(playerMP);
                    PlaytimeDataManager.stopTrackingTimeout(playerMP);
                    return;
                }

                CompoundTag compound = playerMP.getPersistentData();

                /*
                    Total play time Logic
                 */
                if (ConfigManager.TRACK_TOTAL_PLAYTIME.get()) {
                    PlaytimeDataManager.setTotalPlayedTime(
                            playerMP, PlaytimeDataManager.getTotalPlayedTime(playerMP) + workingSecond
                    );
                }

                /*
                    Playtime Logic
                 */
                int newTime = PlaytimeDataManager.getRemainingTime(playerMP) - workingSecond;
                PlaytimeDataManager.setRemainingTime(playerMP, newTime);

                if (!compound.contains("timeout")) {
                    if (newTime <= 0) {
                        int timeout = ConfigManager.PLAYTIME_TIMEOUT.get();
                        kickPlayer(playerMP, timeout);

                        if (!compound.contains("timeout"))
                            compound.putLong("timeout", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout));
                    }

                    return;
                }

                /*
                    Timeout Logic
                 */

                long timeLeft = compound.getLong("timeout");
                if (timeLeft >= System.currentTimeMillis()) {
                    // Add Delay to rightfully show the Kick Message each time
                    if (compound.contains("kick") && compound.getBoolean("kick")) {
                        kickPlayer(playerMP, TimeUnit.MILLISECONDS.toSeconds(timeLeft - System.currentTimeMillis()));
                        compound.remove("kick");
                    } else {
                        compound.putBoolean("kick", true);
                    }
                    return;
                }

                compound.remove("timeout");
                compound.remove("playtime");
                PlaytimeDataManager.resetTimeout(playerMP);
            });
        }
    }

    private void kickPlayer(ServerPlayer playerMP, long duration){
        if (playerMP == null)
            return;

        String seconds = new DecimalFormat("00").format(Duration.ofSeconds(duration).toSecondsPart());
        String minutes = new DecimalFormat("00").format(Duration.ofSeconds(duration).toMinutesPart());
        String hours = new DecimalFormat("00").format(Duration.ofSeconds(duration).toHoursPart());

        playerMP.connection.disconnect(Component.literal(
                ChatFormatting.RED + "Your Playtime is over!"
                        + "\n\n"
                        + ChatFormatting.YELLOW + "Sadly you have used up all of your Playtime" +
                        "\n" +
                        ChatFormatting.YELLOW + "and thus have been kicked!"
                        + "\n\n"
                        + ChatFormatting.WHITE + "You will be able to join again in:"
                        + "\n"
                        + ChatFormatting.BOLD
                        + ChatFormatting.RED + hours + ChatFormatting.WHITE + " hours, "
                        + ChatFormatting.RED +  minutes + ChatFormatting.WHITE + " minutes and "
                        + ChatFormatting.RED +  seconds + ChatFormatting.WHITE + " seconds")
        );
    }

}
