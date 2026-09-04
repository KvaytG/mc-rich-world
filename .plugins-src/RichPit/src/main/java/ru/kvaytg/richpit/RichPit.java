package ru.kvaytg.richpit;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richpit.command.UpCommand;
import ru.kvaytg.richpit.item.ItemImprover;
import ru.kvaytg.richpit.handler.HandlerManager;
import java.util.Objects;

public class RichPit extends JavaPlugin {

    private ItemImprover itemImprover;

    @Override
    public void onEnable() {
        itemImprover = new ItemImprover();
        HandlerManager.INSTANCE.registerAll(this);
        Objects.requireNonNull(getCommand("up")).setExecutor(new UpCommand(this));
    }

    public ItemImprover getItemImprover() {
        return itemImprover;
    }

}