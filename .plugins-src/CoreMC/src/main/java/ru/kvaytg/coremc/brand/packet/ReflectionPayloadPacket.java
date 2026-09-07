package ru.kvaytg.coremc.brand.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import ru.kvaytg.coremc.CoreMc;

public class ReflectionPayloadPacket {

   private final CoreMc plugin;
   private final String channel;

   public ReflectionPayloadPacket(CoreMc plugin) {
      this.plugin = plugin;
      this.channel = "minecraft:brand";
      Messenger messenger = Bukkit.getMessenger();
      try {
         Method method = messenger.getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
         method.setAccessible(true);
         method.invoke(messenger, plugin, channel);
      } catch (Exception ex) {
         plugin.getLogger().log(Level.WARNING, "Failed to register channel!", ex);
      }
   }

   @SuppressWarnings("unchecked")
   private void checkPlayerChannels(Player player) {
      try {
         Field playerChannels = player.getClass().getDeclaredField("channels");
         playerChannels.setAccessible(true);
         Set<String> channels = (Set<String>) playerChannels.get(player);
         channels.add(channel);
      } catch (Exception ex) {
         plugin.getLogger().log(Level.WARNING, "Failed to add channel to player!", ex);
      }
   }

   public void send(Player player, String brand) {
      checkPlayerChannels(player);
      player.sendPluginMessage(plugin, channel, new PacketSerializer(brand).toArray());
   }

}