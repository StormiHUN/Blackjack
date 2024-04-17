package com.example.blackjack;

import javafx.application.Platform;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client {

    String serverIP = "";
    public int port;
    int money;
    public String card;
    String color;
    String lastCommand = "";
    int players = 1;

    String knownCard = "";
    ArrayList<String> cards = new ArrayList<String>();

    Thread reciveThread;
    DatagramSocket socket;


    public Client(){
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        reciveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        byte[] adat = ("join:500").getBytes(StandardCharsets.UTF_8);
        send(adat);
    }

    public void setServerIP(String targetIP){
        serverIP = targetIP;
    }

    public void fogad(){
        try{
            while (true){
                byte[] adat = new byte[256];
                DatagramPacket recived = new DatagramPacket(adat, adat.length);
                socket.receive(recived);
                String msg = new String(adat,0,recived.getLength(),"utf-8");
                Platform.runLater(() -> onFogad(msg));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onFogad(String got){
        String[] msg = got.split(":");
        lastCommand = msg[0];
        if (msg[0].equals("joined")){
            money = 500;
        }if(msg[0].equals("start")){
            cards.clear();
            knownCard = "";
            players = Integer.parseInt(msg[1]);
        }if(msg[0].equals("paid")){
            money = Integer.parseInt(msg[1]);
        }if(msg[0].equals("s")){
            knownCard = msg[1];
        }if(msg[0].equals("k")){
            cards.add(msg[1]);
        }
    }

    public void send(byte[] adat){
        InetAddress ip = null;
        try {
            ip = Inet4Address.getByName(serverIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket packet = new DatagramPacket(adat,adat.length,ip,678);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stake(int bet){
        byte[] adat = ("bet:"+bet).getBytes(StandardCharsets.UTF_8);
        send(adat);
    }

    public void hit(){
        byte[] adat = "hit".getBytes(StandardCharsets.UTF_8);
        send(adat);
    }

    public void stand(){
        byte[] adat = "stand".getBytes(StandardCharsets.UTF_8);
        send(adat);
    }

    public void leave(){
        byte[] adat = "exit".getBytes(StandardCharsets.UTF_8);
        send(adat);
    }

    public void changeColor(String newColor){
        color = newColor;
    }

}
