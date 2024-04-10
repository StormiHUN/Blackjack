package com.example.blackjack;

import javafx.application.Platform;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Server {

    private DatagramSocket socket;
    private ArrayList<Client> clients = new ArrayList<>();
    private Stack<String> cards = new Stack<>();

    public Server() {
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        });
        t.setDaemon(true);
        t.start();
        for(int i = 2; i < 11; i++){
            for (int j = 0; j < "CDHS".split("").length; j++){
                cards.push(i + "" + "CDHS".split("")[j]);
            }
        }
        for (int i = 0; i < "JQKA".split("").length; i++){
            for (int j = 0; j < "CDHS".split("").length; j++){
                cards.push("JQKA".split("")[i]  + "" + "CDHS".split("")[j]);
            }
        }

        /*for (int i = 0; i < cards.size(); i++){
            System.out.println(cards.get(i));
        }*/

        Collections.shuffle(cards);

        /*for (int i = 0; i < cards.size(); i++){
            System.out.println(cards.get(i));
        }*/
    }

    private void send(String s, int index){
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        try {
            InetAddress ip = Inet4Address.getByName(clients.get(index).ip);
            int port = clients.get(index).port;
            DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void receive(){
        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true) {
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
    }

    private void onReceive(String message, String ip, int port){
        Client c = new Client();
        c.ip = ip;
        c.port = port;
        clients.add(c);
        if (message.equals("Kártya?")) {
            String s = String.format("%s;%s", cards.pop(), cards.pop());
            send(s, clients.indexOf(c));
        }
    }

}
