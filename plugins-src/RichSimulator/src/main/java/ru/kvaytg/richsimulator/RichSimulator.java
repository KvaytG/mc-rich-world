package ru.kvaytg.richsimulator;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richsimulator.handler.*;
import ru.kvaytg.richsimulator.handler.trader.ArcherHandler;
import ru.kvaytg.richsimulator.handler.trader.BuyerHandler;
import ru.kvaytg.richsimulator.handler.trader.MerchantHandler;
import ru.kvaytg.richsimulator.trader.TraderController;

public class RichSimulator extends JavaPlugin {

    private BlockHandler blockHandler;

    @Override
    public void onEnable() {
        blockHandler = new BlockHandler(this);
        new EffectHandler(this);
        new EntityHandler(this);
        new ItemHandler(this);
        new JoinHandler(this);
        new VillagerHandler(this);
        new ArcherHandler(this);
        new BuyerHandler(this);
        new MerchantHandler(this);
        TraderController.INSTANCE.renameTraders();
    }

    @Override
    public void onDisable() {
        blockHandler.restoreAllBlocks();
    }

}