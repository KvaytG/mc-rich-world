package ru.kvaytg.richanarchy;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richanarchy.command.BuyCommand;
import ru.kvaytg.richanarchy.command.InfoCommand;
import ru.kvaytg.richanarchy.command.SellCommand;
import ru.kvaytg.richanarchy.handler.EndermanHandler;
import ru.kvaytg.richanarchy.handler.FirstJoinHandler;
import ru.kvaytg.richanarchy.handler.MonsterDamageHandler;
import ru.kvaytg.richanarchy.handler.PlayerRespawnHandler;

@SuppressWarnings("unused")
public class RichAnarchy extends JavaPlugin {

    @Override
    public void onEnable() {
        new PlayerRespawnHandler(this);
        new MonsterDamageHandler(this);
        new EndermanHandler(this);
        new FirstJoinHandler(this);
        new InfoCommand(this);
        new SellCommand(this);
        new BuyCommand(this);
    }

}