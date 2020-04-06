package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractSendPacket;

import java.io.*;

/**
 * @author lj
 * @since 2020/4/4
 */
public class FileSendPacket extends AbstractSendPacket<FileInputStream> {

    private final File file;

    public FileSendPacket(File file) {
        super(file.length());
        this.file = file;
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    public FileInputStream createStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


}
