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

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Playtime Limiter configuration");
        PLAYTIME_LENGTH = builder.comment("The Length (IN SECONDS) which a Player can play on your Server before getting Kicked with a Timeout. Default is 3 Hours").defineInRange("playtime_length",  60 * 60 * 3, 1, 60 * 60 * 24 * 7);
        PLAYTIME_TIMEOUT = builder.comment("The Length (IN SECONDS) which a Player has to wait, after being kicked from the server to be able to join again. Default is 12 Hours").defineInRange("playtime_timeout", 60 * 60 * 12, 1, 60 * 60 * 24 * 7);
        PLAYTIME_WARN_KICK = builder.comment("If Players should get warned before they get kicked. \nThey would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked!").define("playtime_warn_kick", true);
        PLAYTIME_RESET_RECONNECT = builder.comment("If the Playtime should be reset after the Player Reconnects").define("playtime_reset_reconnect", false);
        PLAYTIME_RESET_MIDNIGHT = builder.comment("If the Playtime should be reset after once the Server detects that a new (IRL) Day has started since the last time the Player has joined").define("playtime_reset_midnight", true);

        SPEC = builder.build();
    }
}
