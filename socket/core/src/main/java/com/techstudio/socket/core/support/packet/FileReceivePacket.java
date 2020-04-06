package com.techstudio.socket.core.support.packet;

import com.techstudio.socket.core.AbstractReceivePacket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author lj
 * @since 2020/4/5
 */
public class FileReceivePacket extends AbstractReceivePacket<FileOutputStream, File> {

    private File file;

    public FileReceivePacket(long length, File file) {
        super(length);
        this.file = file;
    }

    /**
     * 从流转变为对应实体时直接返回创建时传入的File文件
     *
     * @param stream {@link FileOutputStream}
     * @return File
     */
    @Override
    protected File buildEntity(FileOutputStream stream) {
        return file;
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    public FileOutputStream createStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
