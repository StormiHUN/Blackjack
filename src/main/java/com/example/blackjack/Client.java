package com.example.blackjack;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {

    String serverIP;
    public int port;
    public String card;
    public int money;
    String color;

    Thread reciveThread;
    DatagramSocket socket;


    public Client(String serverIP){
        this.serverIP = serverIP;
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

    }

    public void fogad(){

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
