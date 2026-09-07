package ru.kvaytg.coremc.limits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.BukkitUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockHandler extends LimitHandler {

    private Set<Material> interactableMaterials;

    public BlockHandler(CoreMc plugin) {
        super(plugin, "blocks");
    }

    @Override
    public void onInit() {
        interactableMaterials = new HashSet<>(Arrays.asList(
                Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE,
                Material.ANVIL, Material.FLOWER_POT,
                Material.PAINTING, Material.BEACON,
                Material.RED_BED, Material.BIRCH_DOOR,
                Material.BIRCH_FENCE_GATE, Material.OAK_BOAT,
                Material.BREWING_STAND, Material.COMMAND_BLOCK,
                Material.CHEST, Material.DARK_OAK_DOOR,
                Material.SPRUCE_DOOR, Material.DARK_OAK_FENCE_GATE,
                Material.DAYLIGHT_DETECTOR, Material.DAYLIGHT_DETECTOR,
                Material.DISPENSER, Material.DROPPER,
                Material.ENCHANTING_TABLE, Material.ENDER_CHEST,
                Material.OAK_FENCE_GATE, Material.FURNACE,
                Material.HOPPER, Material.HOPPER_MINECART,
                Material.ITEM_FRAME, Material.JUNGLE_DOOR,
                Material.JUNGLE_FENCE_GATE, Material.LEVER,
                Material.MINECART, Material.NOTE_BLOCK,
                Material.MINECART, Material.COMPARATOR,
                Material.ACACIA_SIGN, Material.BIRCH_SIGN,
                Material.DARK_OAK_SIGN, Material.JUNGLE_SIGN,
                Material.OAK_SIGN, Material.CHEST_MINECART,
                Material.OAK_DOOR, Material.OAK_TRAPDOOR,
                Material.TRAPPED_CHEST, Material.OAK_BUTTON,
                Material.OAK_DOOR
        ));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!getWorlds().contains(player.getWorld())) return;
        if (BukkitUtils.isAdmin(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!getWorlds().contains(player.getWorld())) return;
        if (BukkitUtils.isAdmin(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!getWorlds().contains(event.getBlock().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (!getWorlds().contains(event.getBlock().getWorld())) return;
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent event) {
        if (!getWorlds().contains(event.getBlock().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!getWorlds().contains(entity.getWorld())) return;
        if (event.getRemover() instanceof Player player && BukkitUtils.isAdmin(player)) return;
        if (entity instanceof Painting || entity instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        if (!getWorlds().contains(entity.getWorld())) return;
        if (event.getDamager() instanceof Player player && BukkitUtils.isAdmin(player)) return;
        if (entity instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!getWorlds().contains(entity.getWorld())) return;
        if (BukkitUtils.isAdmin(event.getPlayer())) return;
        if (entity instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!getWorlds().contains(player.getWorld())) return;
        if (BukkitUtils.isAdmin(event.getPlayer())) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (final Material material : interactableMaterials) {
                if (block.getType() == material || block.getType().toString().contains("POTTED")) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getAction() == Action.PHYSICAL && block.getType() == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }

}