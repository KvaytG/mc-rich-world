package ru.kvaytg.richdonate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Безопасное кодирование данных plugin messaging.
 * Формат пакета:
 * magic(2) + version(1) + command(1) + UUID(16) + txId(UTF) +
 * amount(long) + text(UTF)
 */
public final class ByteUtils {

    private static final short MAGIC = (short) 0x5244; // "RD"
    private static final byte VERSION = 2;

    private ByteUtils() {}

    public static byte[] encode(ChannelCommand command,
                                 java.util.UUID playerId,
                                 String transactionId,
                                 long amount,
                                 String text) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(128);
            DataOutputStream out = new DataOutputStream(buffer);
            out.writeShort(MAGIC);
            out.writeByte(VERSION);
            out.writeByte(command.getId());
            out.writeLong(playerId.getMostSignificantBits());
            out.writeLong(playerId.getLeastSignificantBits());
            out.writeUTF(transactionId == null ? "" : transactionId);
            out.writeLong(amount);
            out.writeUTF(text == null ? "" : text);
            out.flush();
            return buffer.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to encode RichDonate packet", ex);
        }
    }

    public static Packet decode(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        if (in.readShort() != MAGIC) {
            throw new IOException("Unknown RichDonate packet");
        }
        int version = in.readUnsignedByte();
        if (version != VERSION) {
            throw new IOException("Unsupported RichDonate protocol version: " + version);
        }

        ChannelCommand command = ChannelCommand.fromId(in.readUnsignedByte());
        java.util.UUID playerId = new java.util.UUID(in.readLong(), in.readLong());
        String transactionId = in.readUTF();
        long amount = in.readLong();
        String text = in.readUTF();

        return new Packet(command, playerId, transactionId, amount, text);
    }

    public record Packet(ChannelCommand command, UUID playerId, String transactionId, long amount, String text) {}

}