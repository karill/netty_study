package com.tuling;

public class SimpleProtocol {

    public static int PROTOCOL_HEAD = 0XCAFEBABE;
    private byte[] content;


    public SimpleProtocol(byte[] content) {
        this.content = content;
    }

    public int getLength(){

        return content.length;
    }

    public byte[] getContent() {
        return content;
    }

    public SimpleProtocol setContent(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "SimpleProtocol{" +
                "content=" + new String(content) +
                '}';
    }
}