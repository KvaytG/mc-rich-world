package ru.kvaytg.richworld.limits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.world.StructureGrowEvent;
import ru.kvaytg.richworld.RichWorld;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class PhysicsHandler extends LimitHandler {

    public PhysicsHandler(RichWorld plugin) {
        super(plugin, "physics");
    }

    private static final Set<Material> FALLING_BLOCKS = EnumSet.of(
            Material.SAND, Material.GRAVEL, Material.ANVIL
    );
    static {
        Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("_CONCRETE_POWDER"))
                .forEach(FALLING_BLOCKS::add);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (getWorlds().contains(block.getWorld()) &&
                FALLING_BLOCKS.contains(block.getType())) {
            Location belowBlockLoc = block.getLocation().subtract(0.0, 1.0, 0.0).clone();
            Block belowBlock = block.getWorld().getBlockAt(belowBlockLoc);
            if (belowBlock.getType() != Material.AIR) return;
            belowBlock.setType(Material.BARRIER);
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> belowBlock.setType(Material.AIR), 2L);
        }
    }

    private void cancelEvent(Event event, World world) {
        if (getWorlds().contains(world) && event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(true);
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        cancelEvent(event, event.getWorld());
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        cancelEvent(event, event.getBlock().getWorld());
    }

}