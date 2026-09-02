package ru.kvaytg.richdonate.velocity;

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import ru.kvaytg.richdonate.Channel;

/*
*
* Хранилище идентификатора канала на стороне Velocity
*
*/
public class Identifier {

    private static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from(Channel.NAME);

    public static MinecraftChannelIdentifier get() {
        return IDENTIFIER;
    }

}