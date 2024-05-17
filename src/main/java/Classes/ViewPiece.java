package Classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import Enums.PieceType;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import static Classes.CheckersBoard.TILE_SIZE;
public class ViewPiece extends StackPane {
    // the image path
    public static String imagePath = "src\\main\\java\\Images\\crown.png";

    // the type of the piece
    private PieceType type;

    // the color of the piece
    private String color;

    // the ellipse of the piece
    private Ellipse ellipse;

    // the type of the piece
    public PieceType getType() {
        return type;
    }


    /**
     * the constructor that creates a new piece with the given type, x, y and color
      */
    public ViewPiece(PieceType type, int x, int y, String color){
        this.type = type;
        this.color = color;
        move(x, y);


        ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);

        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        if(type == PieceType.SHADOW){
            ellipse.setFill(Color.gray(0.2));
            getChildren().addAll(ellipse);
        }

        else{

            Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
            bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
            bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);
            bg.setFill(Color.BLACK);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(TILE_SIZE * 0.03);

            ellipse.setStroke(Color.BLACK);
            ellipse.setStrokeWidth(TILE_SIZE * 0.03);
            ellipse.setFill(Color.valueOf(color));

            ImageView imageView = null;
            if(type == PieceType.WHITEKING || type == PieceType.REDKING){
                imageView = createImageView(imagePath, 50, 50);
                if(imageView != null){
                    imageView.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
                    imageView.setTranslateY((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
                }


                ellipse.setStroke(Color.GOLD);
                bg.setFill(Color.GOLD);
                bg.setStroke(Color.GOLD);
            }

            getChildren().addAll(bg, ellipse);
            if(imageView != null)
                getChildren().addAll(imageView);
        }

    }

    /**
     * This method is used to create an image view
     * @param imagePath - the path of the image
     * @param width - the width of the image
     * @param height - the height of the image
     * @return - the image view
     */
    private ImageView createImageView(String imagePath, double width, double height) {
        ImageView imageView = null;
        try {
            Image image = new Image(new FileInputStream(imagePath));
            imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }

        catch (FileNotFoundException e){
        }

        return imageView;
    }

    /**
     * This method is used to move the piece to the given x and y
     * @param x - the x position
     * @param y - the y position
     */
    public void move(int x, int y){
        int newX = x * TILE_SIZE;
        int newY = y * TILE_SIZE;

        relocate(newX, newY);
    }

    /**
     * This method is used to change the color of the piece
     * @param color - the color to change the piece to
     */
    public void setColor(String color){
        ellipse.setFill(Color.valueOf(color));
    }
}
