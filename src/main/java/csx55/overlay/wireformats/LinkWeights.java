package csx55.overlay.wireformats;

import java.util.*;
import java.io.*;

public class LinkWeights implements Event {

    private int messageType = Protocol.LINK_WEIGHTS;
    private int numberOfLinks;
    private List<LinkInfo> linkInfoList;

    public LinkWeights(List<LinkInfo> linkInfoList) {
        this.linkInfoList = linkInfoList;
        this.numberOfLinks = linkInfoList.size();
    }

    public LinkWeights(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt();

        this.numberOfLinks = din.readInt();

        this.linkInfoList = new ArrayList<>();
        for (int i = 0; i < this.numberOfLinks; i++) {
            int currentInfoLength = din.readInt();
            byte[] currentInfoBytes = new byte[currentInfoLength];
            din.readFully(currentInfoBytes);
            this.linkInfoList.add(new LinkInfo(currentInfoBytes));
        }

        bArrayInputStream.close();
        din.close();
    }

    public List<LinkInfo> getLinkInfoList() {
        return this.linkInfoList;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);
        dout.writeInt(this.numberOfLinks);

        for (LinkInfo linkInfo : this.linkInfoList) {
            byte[] infoBytes = linkInfo.getBytes();
            int elementLength = infoBytes.length;
            dout.writeInt(elementLength);
            dout.write(infoBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    @Override
    public String toString() {
        String rtn = "";
        for (LinkInfo info : this.linkInfoList) {
            rtn += info.toString() + "\n";
        }
        return rtn;
    }

    public int getType() {
        return this.messageType;
    }

}