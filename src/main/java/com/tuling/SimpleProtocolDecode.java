package com.tuling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class SimpleProtocolDecode extends ByteToMessageDecoder {

    /**
     * 基本长度，4个字节的包头，和4个字节的长度字段
     */
    private static int BASE_LENGTH = 8;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 如果数据不够长的话，就 return 掉再等一等
        if (in.readableBytes() <= BASE_LENGTH) {
            return;
        }
        int bufferIndex = 0;
        while (true) {
            // 记录下索引，后面会用到
            bufferIndex = in.readerIndex();
            in.markReaderIndex();
            // 如果读到包头就可以break了
            if (in.readInt() == SimpleProtocol.PROTOCOL_HEAD) {
                break;
            }
            // 读不到包头的话还原一下标记，然后丢掉一个字节
            in.resetReaderIndex();
            in.readByte();
            if (in.readableBytes() <= BASE_LENGTH) {
                return;
            }
        }

        // 刚才   read 了一个int，是包头
        // 现在再 read 一个 int ，就是长度了
        int length = in.readInt();
        // 这个时候可能包的长度还没有到齐，我们要再判断一下
        if (in.readableBytes() < length) {
            // 没到齐的话就重置一下标记，然后等到齐了再说
            in.readerIndex(bufferIndex);
            return ;
        }
        byte[] data = new byte[length];
        in.readBytes(data);
        SimpleProtocol simpleProtocol = new SimpleProtocol(data);
        out.add(simpleProtocol);
    }
}