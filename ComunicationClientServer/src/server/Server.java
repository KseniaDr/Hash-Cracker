package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private DatagramSocket udpSocket;
    private final int port = 3117;
    private final ServerProtocol protocol;

    public Server(ServerProtocol protocol) throws SocketException, IOException {
        //this.udpSocket = new DatagramSocket(port);
        this.protocol = protocol;
    }
    private void listen() throws Exception {
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
        String msg;

        try(DatagramSocket socket = new DatagramSocket(port)){
            udpSocket = socket;
        }


        while (true) {

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // blocks until a packet is received
            udpSocket.receive(packet);
            msg = new String(packet.getData()).trim();

            System.out.println(
                    "Message from " + packet.getAddress().getHostAddress() + ": " + msg);
        }
    }