package ru.kvaytg.richdonate;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/*
*
* ОБЩИЙ КЛАСС
*
* Хранит методы перевода байтов в строки и наоборот
*
*/
public class ByteUtils {

    public static byte[] stringToBytes(String message) {
        @SuppressWarnings("UnstableApiUsage")
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(message);
        return out.toByteArray();
    }

    public static String bytesToString(byte[] bytes) {
        @SuppressWarnings("UnstableApiUsage")
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        return in.readUTF();
    }

}