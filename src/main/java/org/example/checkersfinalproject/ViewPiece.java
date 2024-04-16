package org.example.checkersfinalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import static org.example.checkersfinalproject.CheckersBoard.TILE_SIZE;
public class ViewPiece extends StackPane {
    private PieceType type;
    private String color;
    private Ellipse ellipse;
    private double mouseX, mouseY;
    private double oldX, oldY;
    public PieceType getType() {
        return type;
    }

    public double getOldY(){
        return oldY;
    }
    public double getOldX(){
        return oldX;
    }

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

//            if(type == PieceType.WHITEPIECE){
//                ellipse.setFill(Color.valueOf("#fff9f4"));
//                getChildren().addAll(bg, ellipse);
//            }
//
//            else if(type == PieceType.REDPIECE){
//                ellipse.setFill(Color.valueOf("#c40003"));
//                getChildren().addAll(bg, ellipse);
//            }

            ImageView imageView = null;
            if(type == PieceType.WHITEKING || type == PieceType.REDKING){
                //ImageView imageView = createImageView("D:/YG/CheckersFinalProject-master/src/main/java/org/example/checkersfinalproject/crown.png", 50, 50);
                imageView = createImageView("src\\main\\java\\org\\example\\checkersfinalproject\\crown.png", 50, 50);
                if(imageView != null){
                    imageView.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
                    imageView.setTranslateY((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
                }

                //getChildren().addAll(bg, ellipse, imageView);

                ellipse.setStroke(Color.GOLD);
                bg.setFill(Color.GOLD);
                bg.setStroke(Color.GOLD);
            }

            getChildren().addAll(bg, ellipse);
            if(imageView != null)
                getChildren().addAll(imageView);
           // }

//            else{
//                ellipse.setFill(Color.valueOf("#c40003"));
//                ImageView imageView = createImageView("D:\\YG\\CheckersFinalProject-master\\src\\main\\java\\org\\example\\checkersfinalproject\\crown.png", 50, 50);
//                imageView.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
//                imageView.setTranslateY((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
//                getChildren().addAll(bg, ellipse, imageView);
//            }
        }




        setOnMousePressed(e -> {

        });

//        setOnMouseDragged(e -> {
//            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
//        });
    }

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

    private int toBoard(double pixel){
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public void move(int x, int y){
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;

        relocate(oldX, oldY);
    }

    public void setColor(String color){
        ellipse.setFill(Color.valueOf(color));
        //ellipse.setFill(Color.BLACK);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }
}
