package ru.kvaytg.richsimulator.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.richsimulator.RichSimulator;
import ru.kvaytg.richsimulator.util.PlayerUtils;

import java.util.HashMap;
import java.util.Map;

public class BlockHandler extends AbstractListener {

    private final ItemStack grass;
    private final Map<Location, Material> pendingRestores = new HashMap<>();

    public BlockHandler(RichSimulator plugin) {
        super(plugin);
        ItemStack item = new ItemStack(Material.WHEAT);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Трава")
                .color(TextColor.fromHexString("#FFFF31"))
                .decoration(TextDecoration.ITALIC, false)
        );
        item.setItemMeta(meta);
        grass = item;
    }

    public void restoreAllBlocks() {
        int restored = 0;
        int failed = 0;
        for (Map.Entry<Location, Material> entry : new HashMap<>(pendingRestores).entrySet()) {
            Location loc = entry.getKey();
            Material material = entry.getValue();
            Block block = loc.getBlock();
            try {
                if (block.getType() == Material.AIR) {
                    block.setType(material);
                    restored++;
                }
            } catch (Exception e) {
                getPlugin().getLogger().warning("Не удалось восстановить блок в " + loc + ": " + e.getMessage());
                failed++;
            }
        }
        pendingRestores.clear();
        getPlugin().getLogger().info("Восстановлено блоков: " + restored + ", не удалось: " + failed);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();
        if (tool.getType() == Material.IRON_HOE &&
                block != null &&
                (block.getType() == Material.GRASS ||
                        block.getType() == Material.FERN)) {
            event.setCancelled(true);
            Material originalType = block.getType();
            Location loc = block.getLocation();
            playCollectionEffects(player, block);
            block.setType(Material.AIR);
            PlayerUtils.addToInventory(player, grass.clone(), loc);
            if (!pendingRestores.containsKey(loc)) {
                pendingRestores.put(loc, originalType);
                getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
                    if (pendingRestores.containsKey(loc)) {
                        Block restoreBlock = loc.getBlock();
                        if (restoreBlock.getType() == Material.AIR) {
                            try {
                                restoreBlock.setType(pendingRestores.get(loc));
                                loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,
                                        loc.add(0.5, 0.5, 0.5), 10);
                            } catch (Exception e) {
                                getPlugin().getLogger().warning("Не удалось восстановить блок: " + e.getMessage());
                            }
                        }
                        pendingRestores.remove(loc);
                    }
                }, 600L);
            }
        }
    }

    private void playCollectionEffects(Player player, Block block) {
        Location loc = block.getLocation().add(0.5, 0.5, 0.5);
        World world = block.getWorld();
        world.playSound(loc, Sound.BLOCK_CROP_BREAK, 1.0f, 1.2f);
        world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 8,
                0.3, 0.3, 0.3, 0.1);
        player.swingMainHand();
    }

}