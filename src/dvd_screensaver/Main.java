package dvd_screensaver;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static final double img_width = 300;
    private static final double img_height = img_width / 1.9;
    private int last_img_n = (int) (Math.random()*7);

    private ImageView imageView;
    private Image[] images;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        scene.setFill(Color.GREY);
        scene.fillProperty();
        scene.
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setTitle("DVD Screensaver");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setX(0);
        primaryStage.setY(0);*/



        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();

        Canvas canvas = new Canvas(width, height);
        String current = new File(".").getCanonicalPath();
        System.out.println(current);
        images = new Image[8];

        for (int i=0;i<=7;i++) {
            images[i] = new Image("file:src/dvd_screensaver/dvdlogo_"+i+".png", img_width, img_height, false, false);
        }

        Pane root = new Pane();
        root.setMinWidth(width);
        root.setMinHeight(height);

        imageView = new ImageView();
        root.getChildren().add(imageView);

        imageView.setImage(images[0]);

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setX(0);
        primaryStage.setY(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                primaryStage.close();
                System.exit(0);
            }
        });

        new Thread(() -> {
            double x = 0;
            double y = 0;
            final int increment_x = 1;
            final int increment_y = 1;
            int direction_x = 1; // 1 to go right, -1 to go left
            int direction_y = 1; // 1 to go down, -1 to go up
            final int sleep = 10;
            while (true) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                //top or bottom invert Y direction
                if (y<0 || y+img_height>=height) {
                    direction_y *=-1;
                    bounce();
                }
                //left or right invert X direction
                if (x<0 || x+img_width>=width) {
                    direction_x *=-1;
                    bounce();
                }

                x+=increment_x*direction_x;
                y+=increment_y*direction_y;

                imageView.setX(x);
                imageView.setY(y);
            }
        }).start();
    }



    public static void main(String[] args) {
        launch(args);
    }

    private void bounce() {
        int new_n;
        do {
            new_n = (int) (Math.random() * 7);
        } while (new_n == last_img_n);
        last_img_n = new_n;
        imageView.setImage(images[new_n]);
    }
}
