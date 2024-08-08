package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application {

    static int speed=5;
    static int foodcolor=0;
    static int width=25;
    static int height=25;
    static int foodX=20;
    static int foodY=20;
    static int cornerSize=25;
    static List<Corner> snake=new ArrayList<>();
    static Dir direction=Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();
    public enum Dir{
        left,right,up,down
    }

    public static class Corner{
        int x;
        int y;
        public Corner(int x, int y){
            this.x=x;
            this.y=y;
        }
    }

    @Override

    public void start(Stage primaryStage) throws Exception{
        try {
            newFood();
            VBox root=new VBox();
            Canvas c=new Canvas(width*cornerSize,height*cornerSize);
            GraphicsContext gc=c.getGraphicsContext2D();
            root.getChildren().add(c);
            new AnimationTimer(){
                long lastStick=0;
                @Override
                public void handle(long now) {
                    if (lastStick==0) {
                        lastStick = now;
                        tick(gc);
                        return;
                    }
                    if (now-lastStick>1000000000/speed){
                        lastStick=now;
                        tick(gc);

                    }

                }
            }.start();
            primaryStage.setTitle("JIMI'S SNAKE GAME");
            Scene scene=new Scene(root, width*cornerSize, width*height);
            scene.addEventFilter(KeyEvent.KEY_PRESSED,key->{
                if (key.getCode()==KeyCode.UP){
                    direction=Dir.up;
                }
                if (key.getCode()==KeyCode.LEFT){
                    direction=Dir.left;
                }
                if (key.getCode()==KeyCode.RIGHT){
                    direction=Dir.right;
                }
                if (key.getCode()==KeyCode.DOWN){
                    direction=Dir.down;
                }

            });
            primaryStage.setScene(scene);

            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));


            primaryStage.show();



        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));



    }
    public static void tick(GraphicsContext gc){
        if (gameOver){
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("GAME OVER JIMI",150,300);


            return;
        }
        for (int i=snake.size()-1;i>=1;i--){
            snake.get(i).x=snake.get(i-1).x;
            snake.get(i).y=snake.get(i-1).y;
        }

        switch (direction){
            case up:
                snake.get(0).y--;
                if (snake.get(0).y<0){
                    gameOver=true;
                }
                break;

            case down:
                snake.get(0).y++;
                if (snake.get(0).y>height){
                    gameOver=true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x<0){
                    gameOver=true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x>width){
                    gameOver=true;
                }
                break;
        }
            if (foodX==snake.get(0).x && foodY==snake.get(0).y){
                snake.add(new Corner(-1,-1));
                newFood();
            }
            for (int i=1;i<snake.size();i++){
                if (snake.get(0).x==snake.get(i).x && snake.get(0).y==snake.get(i).y){
                    gameOver=true;
                }
            }

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width*cornerSize,height*cornerSize);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score:"+(speed-6),10,30);

        Color cc=Color.WHITE;

        switch (foodcolor){
            case 0:cc=Color.PURPLE;
            case 1:cc=Color.LIGHTBLUE;
            case 2:cc=Color.YELLOW;
            case 3:cc=Color.PINK;
            case 4:cc=Color.ORANGE;

        }
        gc.setFill(cc);
        gc.fillOval(foodX*cornerSize,foodY*cornerSize,cornerSize,cornerSize);

        for (Corner c:snake){
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x*cornerSize,c.y*cornerSize,cornerSize-1,cornerSize-1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x*cornerSize,c.y*cornerSize,cornerSize-2,cornerSize-2);
        }

    }


    public static void newFood(){

        start:while (true){
            foodX=rand.nextInt(width);
            foodY=rand.nextInt(height);
            for (Corner c:snake){
                if (c.x==foodX && c.y==foodY){
                    continue start;
                }
            }
            foodcolor=rand.nextInt(5);
            speed++;
            break;
        }
    }

    public static void main(String[] args) {
               launch(args);
    }
}
