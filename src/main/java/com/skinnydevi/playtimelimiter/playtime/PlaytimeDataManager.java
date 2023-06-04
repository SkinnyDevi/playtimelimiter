package com.skinnydevi.playtimelimiter.playtime;

import com.skinnydevi.playtimelimiter.PlaytimeLimiter;
import com.skinnydevi.playtimelimiter.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PlaytimeDataManager {
    private static final HashMap<ServerPlayer, Integer> data = new HashMap<>();

    public static void loadPlayer(ServerPlayer player) {
        CompoundTag nbt = player.getPersistentData();

        int timeLeft = 0;

        if (ConfigManager.PLAYTIME_RESET_MIDNIGHT.get()) {
            boolean loginSameDay = nbt.contains("dayLeft") && nbt.getInt("dayLeft") == Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (loginSameDay)
                timeLeft = nbt.contains("timeLeft") ? nbt.getInt("timeLeft") : timeLeft;
            else timeLeft = ConfigManager.PLAYTIME_LENGTH.get();
        }

        if (ConfigManager.PLAYTIME_RESET_RECONNECT.get()) {
            timeLeft = ConfigManager.PLAYTIME_LENGTH.get();
        }

        setRemainingTime(player, timeLeft);
    }

    public static void savePlayer(ServerPlayer player) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt("timeLeft", getRemainingTime(player));
        nbt.putInt("dayLeft", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static void stopTracking(ServerPlayer player) {
        data.remove(player);
    }

    public static void setRemainingTime(ServerPlayer player, int timeLeft) {
        data.put(player, timeLeft);

        if (!ConfigManager.PLAYTIME_WARN_KICK.get())
            return;


        switch (timeLeft) {
            case 60, 60 * 3, 60 * 5, 60 * 10, 60 * 15, 60 * 30 ->
                player.sendSystemMessage(Component.literal(ChatFormatting.GREEN + "You have " + ChatFormatting.RED +
                        (timeLeft / 60) + ChatFormatting.GREEN + " Minutes of Playtime left!")
                );

        }
    }

    public static int getRemainingTime(ServerPlayer player) {
        return data.getOrDefault(player, ConfigManager.PLAYTIME_LENGTH.get());
    }

    public static String getRemainingSeconds(ServerPlayer player) {
        return new DecimalFormat("00").format(Duration.ofSeconds(getRemainingTime(player)).toSecondsPart());
    }

    public static String getRemainingMinutes(ServerPlayer player) {
        return new DecimalFormat("00").format(Duration.ofSeconds(getRemainingTime(player)).toMinutesPart());
    }

    public static String getRemainingHours(ServerPlayer player) {
        return new DecimalFormat("00").format(Duration.ofSeconds(getRemainingTime(player)).toHoursPart());
    }

    public static String getRemainingFormattedTime(ServerPlayer player) {
        return ChatFormatting.RED + getRemainingHours(player) + ChatFormatting.GREEN + " hours, "
                + ChatFormatting.RED +  getRemainingMinutes(player) + ChatFormatting.GREEN + " minutes and "
                + ChatFormatting.RED +  getRemainingSeconds(player) + ChatFormatting.GREEN + " seconds";
    }

    public static void resetTime(ServerPlayer playerMP) {
        stopTracking(playerMP);
        setRemainingTime(playerMP, PlaytimeDataManager.getRemainingTime(playerMP));
    }

    public static ArrayList<ServerPlayer> getTrackedPlayers() {
        return new ArrayList<>(data.keySet());
    }
}
