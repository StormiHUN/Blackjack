package com.example.blackjack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
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

    Server dealer = null;
    Client player = null;

    int[] posX = {300, 300};
    int[] posY = {0, 300};

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

        onRadioClick();
        pane.getChildren().add(makeCircle(0,"#000000"));
        if(player != null){
            System.out.printf(getColor(clientColor));
            pane.getChildren().add(makeCircle(1,getColor(clientColor)));

        }

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

    public void onRadioClick() {
        if (isClient.isSelected()){

            player = new Client();
            player.setServerIP(serverIp.getText());
            dealer = null;

            standButton.setDisable(false);
            hitButton.setDisable(false);
            allInButton.setDisable(false);
            serverIp.setDisable(false);

        }else{

            dealer = new Server();
            player = null;

            standButton.setDisable(true);
            hitButton.setDisable(true);
            allInButton.setDisable(true);
            serverIp.setDisable(true);
        }

    }

    public void onSzinClick() {
        pane.getChildren().remove(pane.getChildren().size()-1);
        pane.getChildren().add(makeCircle(1,getColor(clientColor)));
        player.changeColor(getColor(clientColor));
    }
}