package sample;

import javafx.scene.shape.Rectangle;

public class Hero extends Rectangle {
    double power = 0;
    double fading = 0.21;
    double maxFallenSpeed = 6;
    double consY;
    Hero(int width, int height){
        this.setWidth(width);
        this.setHeight(height);
        this.consY = GameSession.gHeight * 0.8 - this.getHeight();
        this.setX(45);
        this.setY(consY);
    }
    public void move(){
        if (power < maxFallenSpeed) {
            power += fading;
        }

        this.setY(this.getY() + power);
        if (this.getY() > this.consY){
            this.setY(this.consY);
            power = 0;
        }
    }
    public void powerInc(){
        if (power == 0){
            power = -8;
        }
    }

}
