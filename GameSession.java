package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class GameSession implements Runnable {
    ///Major elements
    static boolean gameCur = true;
    static boolean jump = false;
    static int gWidth = 580;
    static int gHeight = 260;
    int timeCount = -1;
    Timeline timeline;
    Stage gameStage;
    Scene gameScene;
    Pane gamePane;
    Random random = new Random();
    int difficult = 1;
    int enemiesCount = 0;
    ///Major elements
    ///
    ///Game's elements
    Line ground = new Line();
    Label label = new Label();
    class Score extends Label{
        public double dbScore = 0;
        Score(String text, String style){
            setText(text);
            try {
                setStyle(style);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    Score score;
    Hero hero;
    ArrayList<Enemies> enemies = new ArrayList<>();
    ///Game's elements
    GameSession(){
        init();
    }
    public void init(){
        gameStage = new Stage();
        gameStage.setTitle("DragonBorn");
        gameStage.setResizable(false);
        gamePane = new Pane();
        gamePane.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        gameScene = new Scene(gamePane,gWidth,gHeight);
        //gameScene.setFill(Color.BLACK);
        gameStage.setScene(gameScene);
        //gamePane's elements configurations
        hero = new Hero(35,35);
        hero.setFill(Color.GREEN);
        score = new Score("0","-fx-font: 18 Arial;");
        score.setLayoutX(GameSession.gWidth - score.getWidth());
        score.setTextFill(Color.GREEN);
        label.setTextFill(Color.GREEN);
        label.setStyle("-fx-font: 30 Arial;" +
                "-fx-fond-weight: bold;");
        ground.setStartX(0);
        ground.setEndX(gWidth);
        ground.setStartY(gHeight*0.8);
        ground.setEndY(gHeight*0.8);
        ground.setFill(Color.BLACK);

        gamePane.getChildren().add(hero);
        gamePane.getChildren().add(score);
        gamePane.getChildren().add(label);
        gamePane.getChildren().add(ground);
        //gamePane's elements configurations
        gameStage.show();
    }

    @Override
    public void run() {
        Animation();
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                //System.out.println(event.getCode().getName());
                if (event.getCode().getName().equals("Space")){
                    GameSession.jump = true;
                }
                if (event.getCode().getName().equals("Space") && gameCur == false){
                    gameCur = true;
                    //score.setStyle("-fx-font: 18 Arial");
                    score.dbScore = 0;
                    label.setVisible(false);
                    //score.setLayoutX(GameSession.gWidth - score.getWidth());
                   //score.setLayoutY(0 +  score.getHeight());
                    gamePane.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
                    for(int i = 0;i < enemies.size();i++){
                        enemies.get(i).setX(gWidth + enemies.get(i).getWidth());
                    }
                    enemies.clear();

                }
            }
        });
        gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timeline.stop();
                Main.game = true;
            }
        });

    }
    public void Animation(){
         timeline = new Timeline(new KeyFrame(Duration.millis(15),
                new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (gameCur) {
                    gamePane.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
                    if (jump) {
                        hero.powerInc();
                        jump = !jump;
                    }
                    hero.move();
                    //***Enemies
                    if ((random.nextInt(100) == difficult)) {
                        enemies.add(new Enemies(2));
                        gamePane.getChildren().add(enemies.get(enemies.size() - 1));
                        //System.out.println(enemies.size());
                    }

                    if (enemies.size() > 0) {
                        for (int i = 0; i < enemies.size(); i++) {
                            enemies.get(i).move();

                            if ((hero.getX() + hero.getWidth() >= enemies.get(i).getX()) &&
                                    (hero.getX() <= enemies.get(i).getX()) &&
                                    (hero.getY() + hero.getHeight() >= enemies.get(i).getY()) &&
                                    (hero.getY() <= enemies.get(i).getY() + enemies.get(i).getHeight())) {
                                gameCur = false;
                            }

                            if (enemies.get(i).getX() <= -10) enemies.remove(i);
                        }
                    }
                    //***Enemies
                    //***Score
                    score.setText(String.valueOf((int) score.dbScore));
                    score.setLayoutX(GameSession.gWidth - score.getWidth());
                    score.dbScore += 0.1;
                    //***Score
                }
                else {
                    label.setVisible(true);
                    label.setText("Your shoot: " + String.valueOf((int)score.dbScore));
                    label.setLayoutX(GameSession.gWidth/2 - label.getWidth()/2);
                    label.setLayoutY(GameSession.gHeight/2 - label.getHeight());
                }
            }
                }));
        timeline.setCycleCount(timeCount);
        timeline.play();
    }

}
