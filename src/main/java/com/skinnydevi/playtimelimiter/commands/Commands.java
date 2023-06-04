package com.skinnydevi.playtimelimiter.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.skinnydevi.playtimelimiter.PlaytimeLimiter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlaytimeLimiter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Commands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        CommandPlaytime.register(dispatcher);
    }
}
