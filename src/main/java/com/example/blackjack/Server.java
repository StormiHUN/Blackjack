package com.example.blackjack;

import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    private DatagramSocket socket;
    private ArrayList<Client> clients = new ArrayList<Client>();

    public Server() {
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private void send(String s){

    }

    private void receive(){
        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
            String message = new String(data, 0, packet.getLength());
            String ip = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            Platform.runLater(() -> onReceive(message, ip, port));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onReceive(String message, String ip, int port){

    }

}
