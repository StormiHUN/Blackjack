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
        public int port;
        int money = 1000;
        public String card;
        String color;
        String lastCommand = "";
       // int players = 1;
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
                card.setFitWidth(200);
                card.setFitHeight(300);
                card.setX(10+60*(cards.size()-1));
                playerCards.getChildren().add(card);
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

    public Arc table;
    public Label bank;
    public RadioButton isClient;
    public ToggleGroup modes;
    public TextField serverIp;
    public Button hitButton;
    public Button standButton;
    public Button allInButton;
    public Button leaveButton;
    public VBox main;
    public Pane pane;
    public ColorPicker clientColor;
    public TextField startmoney;

    Client player = null;

    int[] posX = {300, 300, 200, 100, 400, 500};
    int[] posY = {000, 300, 200, 100, 200, 300};

    public String getColor(ColorPicker color){
        return String.format("#%02x%02x%02x",(int)(color.getValue().getRed()*255),(int)(color.getValue().getGreen()*255),(int)(color.getValue().getBlue()*255));
    }

    public Circle makeCircle(int position, String color){
        Circle circle = new Circle();
        circle.setRadius(50);
        circle.setCenterX(posX[position]);
        circle.setCenterY(posY[position]);
        circle.setFill(Color.valueOf(color));
        circle.toFront();
        return circle;
    }

    public void initialize(){

        player = new Client();
        player.setServerIP(serverIp.getText());



    }



    public void stake5() {
        if(isClient.isSelected()){
            player.stake(5);
        }
    }

    public void stake25() {
        if(isClient.isSelected()){
            player.stake(25);
        }
    }

    public void stake50() {
        if(isClient.isSelected()){
            player.stake(50);
        }
    }

    public void stake100() {
        if(isClient.isSelected()){
            player.stake(100);
        }
    }

    public void onHitClick() {
        if(isClient.isSelected()){
            player.hit();
        }
    }

    public void onStandClick() {
        if (isClient.isSelected()){
            player.stand();
        }
    }

    public void onLeaveClick() {
        if (isClient.isSelected()){
            player.leave();
        }
    }

    public void onAllInClick() {
        if(isClient.isSelected()){
            player.stake(player.money);
        }
    }


    public void onSzinClick() {
        pane.getChildren().remove(pane.getChildren().size()-1);
        pane.getChildren().add(makeCircle(1,getColor(clientColor)));
        player.changeColor(getColor(clientColor));
    }

    public void onJoinClick() {
        player.setServerIP(serverIp.getText());
        player.join(startmoney.getText());

    }
}