package com.example.blackjack;

import javafx.animation.AnimationTimer;
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

public class HelloController {

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

        pane.getChildren().add(makeCircle(0,"#000000"));
        player = new Client();
        player.setServerIP(serverIp.getText());

        if(player != null){
            System.out.printf(getColor(clientColor));
            pane.getChildren().add(makeCircle(1,getColor(clientColor)));

        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                refreshScreen();
            }
        };
        timer.start();

    }


    public void refreshScreen(){
        if(!player.needRefresh) return;
        player.needRefresh = false;
        String com = player.lastCommand;
        if (com.equals("joined")){
            bank.setText(player.money+"");
        }if(com.equals("start")){
            makeCircle(0,"#000000");
            ImageView card = new ImageView(new Image(getClass().getResourceAsStream("cards/"+player.knownCard+".png")));
            card.setX(posX[0]-30);
            card.setY(posY[0]+120);
            card.setFitHeight(120);
            card.setFitWidth(60);
            pane.getChildren().add(card);
            card = new ImageView(new Image(getClass().getResourceAsStream("cards/gray_back.png")));
            card.setX(posX[0]+30);
            card.setY(posY[0]+120);
            card.setFitHeight(120);
            card.setFitWidth(60);
            pane.getChildren().add(card);
            if(player.players > 1){
                for(int i = 2; i < player.players; i++){
                    makeCircle(i,"#000000");
                    card = new ImageView(new Image(getClass().getResourceAsStream("cards/gray_back.png")));
                    card.setX(posX[i]);
                    card.setY(posY[i]);
                    card.setRotate(30);
                    card.setFitHeight(60);
                    card.setFitWidth(30);
                    pane.getChildren().add(card);
                    card = new ImageView(new Image(getClass().getResourceAsStream("cards/gray_back.png")));
                    card.setX(posX[i]);
                    card.setY(posY[i]);
                    card.setRotate(-30);
                    card.setFitHeight(60);
                    card.setFitWidth(30);
                    pane.getChildren().add(card);
                }
            }
        }if(com.equals("paid")){
            bank.setText(player.money+"");
        }if(com.equals("s")){
            ImageView card = new ImageView(new Image(getClass().getResourceAsStream("cards/"+player.knownCard+".png")));
            card.setX(posX[0]-20);
            card.setY(posY[0]+40);
            card.setFitHeight(60);
            card.setFitWidth(30);
            pane.getChildren().add(card);
            card = new ImageView(new Image(getClass().getResourceAsStream("cards/gray_back.png")));
            card.setX(posX[0]+20);
            card.setY(posY[0]+40);
            card.setFitHeight(60);
            card.setFitWidth(30);
            pane.getChildren().add(card);
        }if(com.equals("k")){
            for(int i = 0; i < player.cards.size(); i++){
                ImageView card = new ImageView(new Image(getClass().getResourceAsStream("cards/"+player.cards.get(i)+".png")));
                card.setX(-40+posX[1]+20*i);
                card.setY(posY[1]-100);
                card.setFitHeight(120);
                card.setFitWidth(60);
                //card.setRotate(40*i);
                pane.getChildren().add(card);
            }
        }
    }

    public void stake5() {
        if(isClient.isSelected()){
            player.stake(5);
        }
        refreshScreen();
    }

    public void stake25() {
        if(isClient.isSelected()){
            player.stake(25);
        }
        refreshScreen();

    }

    public void stake50() {
        if(isClient.isSelected()){
            player.stake(50);
        }
        refreshScreen();

    }

    public void stake100() {
        if(isClient.isSelected()){
            player.stake(100);
        }
        refreshScreen();

    }

    public void onHitClick() {
        if(isClient.isSelected()){
            player.hit();
        }
        refreshScreen();

    }

    public void onStandClick() {
        if (isClient.isSelected()){
            player.stand();
        }
        refreshScreen();

    }

    public void onLeaveClick() {
        if (isClient.isSelected()){
            player.leave();
        }
        refreshScreen();

    }

    public void onAllInClick() {
        if(isClient.isSelected()){
            player.stake(player.money);
        }
        refreshScreen();
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