package Classes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    // the piece on the tile
    private ViewPiece viewPiece;

    public ViewPiece getPiece(){
        return viewPiece;
    }

    public void setPiece(ViewPiece viewPiece){
        this.viewPiece = viewPiece;
    }

    // the constructor that creates a new tile with the given light, x and y.
    // it is the white and black tiles (squares) on the board
    public Tile(boolean light, int x, int y){
        setWidth(CheckersBoard.TILE_SIZE);
        setHeight(CheckersBoard.TILE_SIZE);

        relocate(x * CheckersBoard.TILE_SIZE, y * CheckersBoard.TILE_SIZE);

        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));
    }
}
