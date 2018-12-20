package dvd_screensaver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.File;
import java.util.function.Function;

public class Main extends Application {

    private static final double img_width = 300;
    private static final double img_height = img_width / 1.9;
    private int lastImgN = 0;

    private ImageView imageView;

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
        Image image = new Image("file:src/dvd_screensaver/dvdlogo-0"+lastImgN+".png", img_width, img_height, false, false);
        //create vector of images...
        //create better resolution logo

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);

        Pane root = new Pane();
        root.setMinWidth(width);
        root.setMinHeight(height);
        imageView = new ImageView(image);
        root.getChildren().add(imageView);

        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-1.0);

        imageView.setEffect(blackout);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setScene(new Scene(root));
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
            final int increment_x = 2;
            final int increment_y = 2;
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
        int is = -1;
        while (is == lastImgN) {
            is = (int) Math.round(Math.random() * 7);
        }
    }
}
