package client;

import mutual.HelperFunctions;
import message.Message;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Client implements Runnable {
    private final int clientPort;
    private final String teamName = "shahar & the roaches";
    private final int timeOut = 15000;
    private final int serverPort = 3117;
    private HelperFunctions helperFunctions;
    private int NumOfServerOffers;

    public Client(int port) {
        this.clientPort = port;
        helperFunctions = new HelperFunctions();
    }


    @Override
    public void run() {
        System.out.println("Welcome to " + teamName + ". Please enter the hash:");
        Scanner scanner = new Scanner(System.in);
        String hash = scanner.next();
        System.out.println("Please enter the input string length:");
        int length = scanner.nextInt();
        try (DatagramSocket socket = new DatagramSocket(clientPort)) {
            sendDiscover(socket);
            LinkedList<InetAddress> ServerOffers = WaitForOffer(socket);
            sendRequests(ServerOffers,socket,length,hash);
            waitforAcks(socket,length);

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private void sendDiscover(DatagramSocket socket) {
        char discover = 1;
        Message msg = new Message(teamName.toCharArray(), discover, "".toCharArray(), discover, "".toCharArray(), "".toCharArray());
        try {
            byte[] send = HelperFunctions.toByteArray(msg);
            DatagramPacket packet = new DatagramPacket(send, send.length,InetAddress.getByName("255.255.255.255") , serverPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LinkedList<InetAddress> WaitForOffer(DatagramSocket socket) {
        LinkedList<InetAddress> ServerAnswers = new LinkedList<>();
        Boolean moreOffers = true;
        // now we wait for response
        while (moreOffers) {
            try {
                socket.setSoTimeout(1000);
                byte[] receive = new byte[65000];
                DatagramPacket packet = new DatagramPacket(receive, 0, receive.length);
                socket.receive(packet);
                Message offer = (Message) helperFunctions.toObject(packet.getData());
                if (offer.getType() == 2)
                    ServerAnswers.add(packet.getAddress());

            } catch (SocketTimeoutException e){
                moreOffers=false;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ServerAnswers;
    }
    private void sendRequests(LinkedList<InetAddress> serverOffers,DatagramSocket socket,int length,String hash){
        NumOfServerOffers=serverOffers.size();
        char request=3;
        char strLength=(char)length;
        String[] strings=helperFunctions.divideToDomains(length,serverOffers.size());
        for(int i=0;i<strings.length;i+=2){
            Message msg=new Message(teamName.toCharArray(),request,hash.toCharArray(),strLength,strings[i].toCharArray(),strings[i+1].toCharArray());
            try {
                byte[] send=helperFunctions.toByteArray(msg);
                DatagramPacket packet=new DatagramPacket(send,send.length,serverOffers.remove(),serverPort);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitforAcks(DatagramSocket socket,int length){
        char ack=4;
        char nack=5;
        Boolean moreAcks=true;
        while (moreAcks){
            try {
                socket.setSoTimeout(timeOut);
                byte[] receive =new byte[65000];
                DatagramPacket packet=new DatagramPacket(receive,0,receive.length);
                socket.receive(packet);
                Message msg=(Message)helperFunctions.toObject(packet.getData());
                if(msg.getType()==ack){
                    System.out.println("server: "+packet.getAddress().toString()+" found the hash original string!!");
                    System.out.println("the original string is:");
                    for(int i=0;i<length;i++){
                        System.out.print(msg.getOriginalStringStart()[i]);
                    }
                    moreAcks=false;
                }
                if(msg.getType()==nack){
                    System.out.println("server: "+packet.getAddress().toString()+" has not found the hash original string");
                    NumOfServerOffers--;
                }
            } catch (SocketTimeoutException e){
                System.out.println("server timeout");
                e.printStackTrace();
                NumOfServerOffers--;
                if(NumOfServerOffers == 0)
                    moreAcks=false;

            }
            catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
