package com.skinnydevi.playtimelimiter.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager {
    private ConfigManager() {}

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue PLAYTIME_LENGTH;
    public static final ForgeConfigSpec.IntValue PLAYTIME_TIMEOUT;
    public static final ForgeConfigSpec.BooleanValue PLAYTIME_WARN_KICK;
    public static final ForgeConfigSpec.BooleanValue PLAYTIME_RESET_RECONNECT;
    public static final ForgeConfigSpec.BooleanValue PLAYTIME_RESET_MIDNIGHT;
    public static final ForgeConfigSpec.BooleanValue TRACK_TOTAL_PLAYTIME;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Playtime Limiter configuration");
        PLAYTIME_LENGTH = builder.comment("The length (IN SECONDS) which a player can play on your server before getting kicked with a timeout. Default is 3 hours").defineInRange("playtime_length",  60 * 60 * 3, 1, 60 * 60 * 24 * 7);
        PLAYTIME_TIMEOUT = builder.comment("The length (IN SECONDS) which a player has to wait, after being kicked from the server to be able to join again. Default is 12 hours. \nDo not leave blank.").defineInRange("playtime_timeout", 60 * 60 * 12, 1, 60 * 60 * 24 * 7);
        PLAYTIME_WARN_KICK = builder.comment("If players should get warned before they get kicked. \nThey would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked!").define("playtime_warn_kick", true);
        PLAYTIME_RESET_RECONNECT = builder.comment("If the playtime should be reset after the Player Reconnects").define("playtime_reset_reconnect", false);
        PLAYTIME_RESET_MIDNIGHT = builder.comment("If enabled, omits the timeout set above. If the playtime timeout should be reset once the server detects that a new (IRL) day has started since the last time the player has joined").define("playtime_reset_midnight", true);
        TRACK_TOTAL_PLAYTIME = builder.comment("Enable or disable tracking the player's total play time.").define("track_total_playtime", true);

        SPEC = builder.build();
    }
}
