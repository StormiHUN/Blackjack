package com.example.blackjack;

import javafx.application.Platform;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {

    String serverIP = "";
    public int port;
    public String card;
    public int money;
    String color;

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
