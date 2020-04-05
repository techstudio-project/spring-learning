package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractSendPacket;

import java.io.*;

/**
 * @author lj
 * @since 2020/4/4
 */
public class FileSendPacket extends AbstractSendPacket<FileInputStream> {

    public FileSendPacket(File file) {
        super(file.length());
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    public FileInputStream createStream() {
        return null;
    }


}
