package com.skinnydevi.playtimelimiter;

import com.mojang.logging.LogUtils;
import com.skinnydevi.playtimelimiter.config.ConfigManager;
import com.skinnydevi.playtimelimiter.server.PlayerJoinListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(PlaytimeLimiter.MODID)
public class PlaytimeLimiter
{
    public static final String MODID = "playtimelimiter";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static PlayerJoinListener playerJoinListener;

    public PlaytimeLimiter()
    {
        playerJoinListener = new PlayerJoinListener();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigManager.SPEC, "playtimelimiter-server.toml");

        MinecraftForge.EVENT_BUS.register(playerJoinListener);
    }
}
