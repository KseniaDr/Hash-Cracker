package server;

import message.Message;
import mutual.HelperFunctions;

import java.io.IOException;
import java.net.*;

public class Server {
    private DatagramSocket udpSocket;
    private final int port = 3117;
    //private InetAddress ipBroadcast;
    private final ServerProtocol protocol;
    private boolean isClosed;
    private final int messageSizeInBytes = 587;
    private HelperFunctions helperFunctions;


    public Server() {
        try {
            udpSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        protocol = new ServerProtocol(this);
        isClosed = false;
        helperFunctions = new HelperFunctions();
    }


    /**
     * Listen for senders request to find the hash,
     * if the hash was found an ACK message with the found ACK will be sent
     * if the hash wasn't found a NACK message will be sent.
     */
    public void listen() {
        try {
            while (true) {
                byte[] arrayIn = new byte[messageSizeInBytes];
                DatagramPacket packet = new DatagramPacket(arrayIn, 0,arrayIn.length);
                udpSocket.receive(packet);// waits for a request message
                Message msg1 = (Message) HelperFunctions.toObject(packet.getData());
                String senderIPAddress = packet.getAddress().toString().substring(1);
                String senderPort = "" + packet.getPort();
                ServerProtocol protocolPerClient = new ServerProtocol(this, msg1, senderIPAddress, senderPort);
                Thread threadPerClient = new Thread(protocolPerClient);
                threadPerClient.start();// runs the 'run' method

            }

        } catch(SocketTimeoutException e){
            System.out.println("time is out");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an offer(type of the message = 2) message back to the sender,
     * sends an empty message so the sender could get the server IP address for later requests.
     *
     * @param offer           - the message to send back to the sender.
     * @param clientIPAddress - sender IP address
     * @param clientPort      - sender port
     */
    public void sendOffer(Message offer, String clientIPAddress, String clientPort) {
        byte[] arrayOut = new byte[257];
        try {
            arrayOut = HelperFunctions.toByteArray(offer);
            DatagramPacket offerPacket = null;
            try {
                offerPacket = new DatagramPacket(arrayOut, arrayOut.length, InetAddress.getByName(clientIPAddress), Integer.parseInt(clientPort));
                try {
                    udpSocket.send(offerPacket);//send the message back to the client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String localIP = udpSocket.getLocalAddress().toString(); //for DEBUG mode


    }

    /**
     * Sends the server answer for the hash request of the sender.
     *
     * @param answer        - the answer message to the sender.
     * @param destIPAddress - IP address of the sender.
     * @param destPort      - port of the sender.
     */
    public void send(Message answer, String destIPAddress, String destPort) {
        byte[] toSend = new byte[0];
        try {
            toSend = HelperFunctions.toByteArray(answer);
            InetAddress ipToSend;
            try {
                ipToSend = InetAddress.getByName(destIPAddress);
                DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, ipToSend, Integer.parseInt(destPort));
                try {
                    udpSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Broadcast socket, waits for DISCOVER messages,
     * when a message is received sends an OFFER message back to the sender.
     * The method is blocked till a message is received.
     */
    //@Override
    /*public void run() {
        while (isClosed == false) {//for broadcast requests
            byte[] arrayIn = new byte[messageSizeInBytes];
            DatagramPacket broadcastPacket = new DatagramPacket(arrayIn, arrayIn.length);
            try {//block till there is a a broadcast request
                udpSocket.receive(broadcastPacket);
                Message discoverMsg = new Message(arrayIn);
                if (discoverMsg != null)
                    sendOffer(protocol.process(discoverMsg), broadcastPacket.getAddress(), broadcastPacket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

/*
    private Message createMessage(byte[] inPacket) {
        Message message = null;
        String byteToString;
        try {
            byteToString = new String(inPacket, "UTF-8");
            char[] teamName = getCharArray(byteToString, 30, false);
            char type = getChar(byteToString);
            char[] hash = getCharArray(byteToString, 40, false);
            char length = getChar(byteToString);
            char[] originalStringStart = getCharArray(byteToString,256,false);
            char[] originalStringEnd = getCharArray(byteToString,256,true);
            message = new Message(teamName, type, hash, length, originalStringStart, originalStringEnd);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }
    */
/*
    private char getChar(String toCut){
        char answer = toCut.charAt(0);
        toCut.substring(1);
        return answer;
    }

    private char[] getCharArray(String toCut, int size, boolean lastArray){
        char[] answer = new char[size];
        for (int i=0; i < size ; i++)
            answer[i] = toCut.charAt(i);
        if (lastArray == false)
            toCut.substring(size+1);
        return answer;
    }
    */
}