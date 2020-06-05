package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
    static boolean game = true;
    //moveSun
    boolean move = false,click = false;
    //moveSun
    LightThread lightThread = new LightThread(108,171,205);
    static public int width = 325;
    static public int height = 475;
    public double top, bottom, left,right;
    public BasicButton[] butMenuStorage = new BasicButton[5];
    GameSession gameSession;
    class LightThread {
        int red,green,blue;
        Color color ;
        LightThread(double r,double g, double b){
            red = (int)(r * 255);
            green = (int)(g * 255);
            blue = (int)(b * 255);
            color = Color.rgb(red,green,blue);
        }
        LightThread(int r, int g, int b){
            color = Color.rgb(r,g,b);
        }
        private void colorCheck(int rValue,int gValue,int bValue){
            if(red > rValue) red --;
            else if (red < rValue) red++;
            if (green > gValue) green--;
            else if (green < gValue) green++;
            if (blue > bValue) blue--;
            else if (blue < bValue) blue++;
        }
        void ChangeColor(StackPane root,ImageView img){
            if (img.getX()+img.getFitWidth()/2 < root.getWidth()*0.4){//morning 109 171 205
                colorCheck(109,171,205);
            }
            else if ((img.getX()+img.getFitWidth()/2 >  root.getWidth()*0.4) &&//yellow 255 247 89
                    (img.getX()+img.getFitWidth()/2 < root.getWidth()*0.6)){
                colorCheck(255,250,153);
            }
            else if ((img.getX()+img.getFitWidth()/2 > root.getWidth()*0.6))//evening sun 254 180 137
            colorCheck(254,180,137);
            color = Color.rgb(red,green,blue);
            root.setBackground(new Background(new BackgroundFill(color,null,null)));
        }

    }
    static class BasicButton extends Button{
        BasicButton(String style,String textSize,String name,int width,int height){
            setText(name);
            setWidth(width);
            setHeight(height);
            try{
            setStyle("-fx-font: "+textSize+" "+ style+";"+
                    "-fx-font-weight: "+"bold;");
            }
            catch (Exception e){
            e.printStackTrace();
            }
        }
        BasicButton(String name,int width,int height){
            setText(name);
            setWidth(width);
            setHeight(height);
        }
        void reaction(){
            setOnMouseEntered(event -> {
                this.changeColor();
            });
            setOnMouseExited(event -> {
                this.changeColor();
            });
        }

        void changeColor(){
            if (this.getTextFill().equals(Color.GREEN))
                setTextFill(Color.BLACK);
            else
                setTextFill(Color.GREEN);
        }
    }
    public static void main(String[] args){ launch(args); }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("DragonBorn");
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setResizable(false);
        //ColorStuff

        Image image = new Image(getClass().getResourceAsStream("Sun1.png"));
        ImageView img = new ImageView(image);
        img.setFitWidth(75);
        img.setFitHeight(72);
        img.setVisible(true);
        //ColorStuff
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(lightThread.color,null,null)));
        Scene scene = new Scene(root,width,height,Color.RED);
        //Layout manager
        StackPane.setAlignment(img,Pos.BOTTOM_CENTER);
        StackPane.setMargin(img,new Insets(0,width-img.getFitWidth()*2,height*0.2,0));
        butMenuStorage[0] = new BasicButton("Arial","16","Start",70,40);
        StackPane.setAlignment(butMenuStorage[0],Pos.TOP_CENTER);
        StackPane.setMargin(butMenuStorage[0],new Insets(100,75,310,75));
        butMenuStorage[0].setMaxWidth(Double.MAX_VALUE);
        butMenuStorage[0].setMaxHeight(Double.MAX_VALUE);
        butMenuStorage[0].reaction();
        butMenuStorage[0].setOnAction(event -> {

            if (game == true){
                gameSession = new GameSession();
                gameSession.run();
                game = false;
            }
            /*
            else if (game == false){
                gameSession.timeline.stop();
                gameSession.gameStage.hide();
                gameSession = null;
                game = true;
            }
            */
            System.out.println(game);
        });
        butMenuStorage[1] = new BasicButton("Arial","16","Settings",70,40);
        StackPane.setAlignment(butMenuStorage[1],Pos.CENTER);
        StackPane.setMargin(butMenuStorage[1],new Insets(150,75,260,75));
        butMenuStorage[1].setMaxWidth(Double.MAX_VALUE);
        butMenuStorage[1].setMaxHeight(Double.MAX_VALUE);
        butMenuStorage[1].reaction();

        butMenuStorage[2] = new BasicButton("Arial","16","X",70,40);
        StackPane.setAlignment(butMenuStorage[2],Pos.CENTER);
        StackPane.setMargin(butMenuStorage[2],new Insets(200,75,210,195));
        butMenuStorage[2].setMaxWidth(Double.MAX_VALUE);
        butMenuStorage[2].setMaxHeight(Double.MAX_VALUE);
        butMenuStorage[2].reaction();
        butMenuStorage[2].setOnAction(e -> {
            primaryStage.close();
            System.exit(0);
        });
        Pane pane = new Pane();
        StackPane.setAlignment(pane,Pos.BOTTOM_CENTER);
        StackPane.setMargin(pane,new Insets(235,0,0,0));
        pane.getChildren().add(img);
        //layout manager
        root.getChildren().add(pane);
        root.getChildren().addAll(
                butMenuStorage[0],butMenuStorage[1],butMenuStorage[2]);
        //Timeline
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lightThread.ChangeColor(root,img);
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        //Timeline
        //Action

        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click = false;
                //System.out.print(click);
            }
        });

        pane.setOnMousePressed(new EventHandler<MouseEvent>() {//FIX
            @Override
            public void handle(MouseEvent event) {
                if ((event.getX() <= (img.getX()+img.getFitWidth())) &&
                        (event.getX() >= img.getX()) &&
                        (event.getY() >= img.getY()) &&
                        (event.getY() <= img.getY()+img.getFitHeight()) ){
                    click = true;
                }
            }
        });
        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getX() < 0 || event.getY() < 0 ||
                        (event.getX() > pane.getWidth() || event.getY() > pane.getHeight())) click = false;
                if (click) {
                    /*
                    if (iter >= 1) {
                        if ((img.getX() + img.getFitWidth() / 2 > pane.getWidth() / 2)
                                && backColor.getRed() < 0.9) {
                            backColor = Color.rgb( (int)(backColor.getRed()*255 + 1),  (int)(backColor.getGreen()*255), (int) (backColor.getBlue()*255-1), 1.0);
                            root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                        } else if ((img.getX() + img.getFitWidth() / 2 < pane.getWidth() / 2)
                                &&  backColor.getBlue() < 0.9) {
                            backColor = Color.rgb((int)(backColor.getRed()*255-1), (int)(backColor.getGreen()*255), (int)(backColor.getBlue()*255+1), 1.0);
                            root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                            //System.out.println(backColor.getRed() + " " + backColor.getGreen() + " " + backColor.getBlue());
                        }
                        System.out.println(backColor.getRed() + " " + backColor.getGreen() + " " + backColor.getBlue());
                        iter = 0;
                    }
                     */
                    /*
                    if (iter >= 20) {
                        if (pane.getWidth() * 0.4 > img.getX() + img.getFitWidth() / 2 && backColor.getBlue() * 255 < 255) {
                            backColor = Color.rgb((int) (backColor.getRed() * 255 - 1), (int) (backColor.getGreen() * 255 - 1), (int) (backColor.getBlue() * 255 + 1), 1.0);
                            root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                        } else if (pane.getWidth() * 0.4 <= img.getX() + img.getFitWidth() / 2 && pane.getWidth()  * 0.6 > img.getX() + img.getFitWidth()
                                && backColor.getRed() * 255 < 255 && backColor.getGreen() * 255 < 255 && backColor.getBlue() < 254) {
                            backColor = Color.rgb((int) (backColor.getRed() * 255 + 1), (int) (backColor.getGreen() * 255 + 1), (int) (backColor.getBlue() * 255)+1, 1.0);
                            root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                        } else if (pane.getWidth()  * 0.6 <= img.getX() + img.getFitWidth() / 2 && backColor.getRed() < 254 && backColor.getBlue() > 0) {
                            backColor = Color.rgb((int) (backColor.getRed() * 255 + 1), (int) (backColor.getGreen() * 255), (int) (backColor.getBlue() * 255 - 1), 1.0);
                            root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                        }
                    }

                     */
                    img.setX(Math.abs(event.getX()) - img.getFitWidth() / 2);
                    img.setY(Math.abs(event.getY()) - img.getFitHeight() / 2);
                }
                //System.out.println(backColor.getRed() + " " + backColor.getGreen() + " " + backColor.getBlue());
            }
        });
        /*
        img.addEventHandler(MouseDragEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((img.getX() < event.getX() && img.getX() + img.getX() + img.getFitWidth() > event.getX()) &&
                        (img.getY() < event.getY() && img.getY() + img.getFitHeight() > img.getY())){
                    backColor = Color.rgb(0, 0, 0, 1.0);
                    root.setBackground(new Background(new BackgroundFill(backColor, null, null)));
                }

            }
        });
         */

        //Action

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
