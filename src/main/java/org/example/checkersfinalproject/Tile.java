package org.example.checkersfinalproject;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private ViewPiece viewPiece;
    private double mouseX, mouseY;
    public boolean hasPiece(){
        return viewPiece != null;
    }

    public ViewPiece getPiece(){
        return viewPiece;
    }

    public void setPiece(ViewPiece viewPiece){
        this.viewPiece = viewPiece;
    }

    public Tile(boolean light, int x, int y){
        setWidth(CheckersBoard.TILE_SIZE);
        setHeight(CheckersBoard.TILE_SIZE);

        relocate(x * CheckersBoard.TILE_SIZE, y * CheckersBoard.TILE_SIZE);

        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));

        setOnMouseClicked(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
    }
}
