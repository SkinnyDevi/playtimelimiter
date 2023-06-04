package com.skinnydevi.playtimelimiter.playtime;

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
    private static final HashMap<ServerPlayer, Integer> timePlayedData = new HashMap<>();

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

        if (ConfigManager.TRACK_TOTAL_PLAYTIME.get()) {
            setTotalPlayedTime(player, nbt.contains("timePlayed") ? nbt.getInt("timePlayed") : 0);
        }

        setRemainingTime(player, timeLeft);
    }

    public static void savePlayer(ServerPlayer player) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt("timeLeft", getRemainingTime(player));
        nbt.putInt("dayLeft", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        if (ConfigManager.TRACK_TOTAL_PLAYTIME.get()) nbt.putInt("timePlayed", getTotalPlayedTime(player));
    }

    public static void stopTrackingTimeout(ServerPlayer player) {
        data.remove(player);
    }

    public static void setRemainingTime(ServerPlayer player, int timeLeft) {
        data.put(player, timeLeft);

        if (!ConfigManager.PLAYTIME_WARN_KICK.get())
            return;


        switch (timeLeft) {
            case 60, 60 * 3, 60 * 5, 60 * 10, 60 * 15, 60 * 30 ->
                player.sendSystemMessage(Component.literal(ChatFormatting.GREEN + "You have " + ChatFormatting.RED +
                        (timeLeft / 60) + ChatFormatting.GREEN + " Minute(s) of Playtime left!")
                );

        }
    }


    public static int getRemainingTime(ServerPlayer player) {
        return data.getOrDefault(player, ConfigManager.PLAYTIME_LENGTH.get());
    }

    public static void setTotalPlayedTime(ServerPlayer player, int totalTime) {
        if (!ConfigManager.TRACK_TOTAL_PLAYTIME.get()) return;
        timePlayedData.put(player, totalTime);
    }

    public static int getTotalPlayedTime(ServerPlayer player) {
        return timePlayedData.getOrDefault(player, 0);
    }

    public static long[] getFormattedTotalPlayTime(ServerPlayer player) {
        Duration time = Duration.ofSeconds(getTotalPlayedTime(player));

        return new long[]{
                time.toDaysPart(),
                time.toHoursPart(),
                time.toMinutesPart(),
                time.toSecondsPart()
        };
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

    public static void resetTimeout(ServerPlayer playerMP) {
        stopTrackingTimeout(playerMP);
        setRemainingTime(playerMP, getRemainingTime(playerMP));
    }

    public static void resetTotalTime(ServerPlayer player) {
        if (!ConfigManager.TRACK_TOTAL_PLAYTIME.get()) return;
        timePlayedData.remove(player);
        setTotalPlayedTime(player, getTotalPlayedTime(player));
    }

    public static ArrayList<ServerPlayer> getTrackedPlayers() {
        return new ArrayList<>(data.keySet());
    }
}
