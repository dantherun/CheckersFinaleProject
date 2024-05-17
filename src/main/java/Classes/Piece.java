package Classes;

import Enums.DirectionVector;
import Enums.PieceType;

import java.util.HashSet;
import java.util.Set;

public class Piece {
    // The piece type of the piece
    protected PieceType pieceType;

    // The piece type of the other kind (king or piece)
    protected PieceType differentPieceType;

    // The directions the piece can move to eat
    protected Set<DirectionVector> directionsToMove = new HashSet<>();

    // The piece type of the enemy pieces
    protected PieceType enemyPieces;

    // The piece type of the enemy kings
    protected PieceType enemyKings;

    protected Piece(){}
    public Piece(PieceType pieceType){
        this.pieceType = pieceType;

        differentPieceType = pieceType == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;


        enemyPieces = pieceType == PieceType.WHITEPIECE ? PieceType.REDPIECE : PieceType.WHITEPIECE;
        enemyKings = pieceType == PieceType.WHITEPIECE ? PieceType.REDKING : PieceType.WHITEKING;

        if (pieceType == PieceType.WHITEPIECE) {
            directionsToMove.add(DirectionVector.northwest);
            directionsToMove.add(DirectionVector.northeast);
        }

        else {
            directionsToMove.add(DirectionVector.southwest);
            directionsToMove.add(DirectionVector.southeast);
        }
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceType getPieceColor() {
        return pieceType;
    }

    public PieceType getEnemyPieceType(){
        return enemyPieces;
    }

    public PieceType getEnemyKingType(){
        return enemyKings;
    }

    public PieceType getDifferentType(){
        return differentPieceType;
    }

    public Set<DirectionVector> getEatingDirections(){
        return directionsToMove;
    }

    /**
     * Returns the row the piece needs to be in to be king
     */
    public int rowToBeKing(){
        return pieceType == PieceType.WHITEPIECE ? 0 : 7;
    }
}
