package csx55.overlay.transport;

import java.net.Socket;
import java.io.DataOutputStream;

import java.io.IOException;

public class TCPSender {

    private DataOutputStream dout;

    public TCPSender(Socket socket) throws IOException {
        this.dout = new DataOutputStream(socket.getOutputStream());
    }

    public void sendData(byte[] dataToSend) throws IOException {
        int dataLength = dataToSend.length;
        this.dout.writeInt(dataLength);
        this.dout.write(dataToSend, 0, dataLength);
        this.dout.flush();
    }
    
}