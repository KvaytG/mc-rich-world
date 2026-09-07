package ru.kvaytg.coremc.antiwdl;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.coremc.CoreMc;

public enum AntiWorldDownloader implements PluginMessageListener {

    INSTANCE;

    public static final String CHANNEL_INIT = "wdl:init";
    public static final String CHANNEL_CONTROL = "wdl:control";

    private CoreMc plugin;
    private Messenger messenger;

    public void init(CoreMc plugin) {
        this.plugin = plugin;
        this.messenger = plugin.getServer().getMessenger();
        messenger.registerIncomingPluginChannel(plugin, CHANNEL_INIT, this);
        messenger.registerOutgoingPluginChannel(plugin, CHANNEL_CONTROL);
    }

    public void stop() {
        if (plugin == null) return;
        messenger.unregisterIncomingPluginChannel(plugin, CHANNEL_INIT);
        messenger.unregisterOutgoingPluginChannel(plugin, CHANNEL_CONTROL);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel,
                                        @NotNull Player player,
                                        byte @NotNull [] data) {
        @SuppressWarnings("UnstableApiUsage")
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(0);
        out.writeBoolean(false);
        player.sendPluginMessage(plugin, CHANNEL_CONTROL, out.toByteArray());
    }

}