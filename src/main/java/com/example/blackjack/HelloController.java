package com.example.blackjack;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HelloController {

    public Pane playerCards;
    public Label players;
    public ImageView serversCard;

    public class Client {

        String serverIP = "";
        int money = 1000;
        String lastCommand = "";
        boolean needRefresh = false;

        String knownCard = "";
        ArrayList<String> cards = new ArrayList<String>();

        Thread reciveThread = null;
        DatagramSocket socket = null;


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
            reciveThread.setDaemon(true);
            reciveThread.start();
        }

        public void join(String startmoney){
            byte[] adat = ("join:"+startmoney).getBytes(StandardCharsets.UTF_8);
            cards.clear();
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
            System.out.printf(got+"\n");
            String[] msg = got.split(":");
            lastCommand = msg[0];
            needRefresh = true;
            if (msg[0].equals("joined")){
                money = Integer.parseInt(msg[1]);
                bank.setText(money+"");
            }if(msg[0].equals("start")){
                cards.clear();
                knownCard = "";
                serversCard.setImage(new Image(getClass().getResourceAsStream("cards/gray_back.png")));
                playerCards.getChildren().clear();
                players.setText("Csatlakozott játékosok: "+msg[1]);
            }if(msg[0].equals("paid")){
                money = Integer.parseInt(msg[1]);
                bank.setText(money+"");
            }if(msg[0].equals("s")){
                knownCard = msg[1];
                serversCard.setImage(new Image(getClass().getResourceAsStream("cards/"+knownCard+".png")));
            }if(msg[0].equals("k")){
                cards.add(msg[1]);
                ImageView card = new ImageView(new Image(getClass().getResourceAsStream("cards/"+msg[1]+".png")));
                card.setY(50);
                card.setFitWidth(200);
                card.setFitHeight(300);
                card.setX(10+60*(cards.size()-1));
                playerCards.getChildren().add(card);
            }if(msg[0].equals("balance")){
                money = Integer.parseInt(msg[1]);
                bank.setText(money+"");
            }
        }

        public void send(byte[] adat){
            InetAddress ip = null;
            try {
                ip = Inet4Address.getByName(serverIP);
            } catch (Exception e) {
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
            money-=bet;
            bank.setText(money+"");
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

    }

    public Label bank;
    public TextField serverIp;
    public Button hitButton;
    public Button standButton;
    public Button allInButton;
    public Button leaveButton;
    public VBox main;
    public TextField startmoney;

    Client player = null;

    public void initialize(){

        player = new Client();
        player.setServerIP(serverIp.getText());

    }

    public void stake5() {
        player.stake(5);
    }

    public void stake25() {
        player.stake(25);

    }

    public void stake50() {
        player.stake(50);

    }

    public void stake100() {
        player.stake(100);

    }

    public void onHitClick() {
        player.hit();
    }

    public void onStandClick() {
        player.stand();
    }

    public void onLeaveClick() {
        player.leave();
    }

    public void onAllInClick() {
        player.stake(player.money);
    }


    public void onJoinClick() {
        player.setServerIP(serverIp.getText());
        player.join(startmoney.getText());

    }
}