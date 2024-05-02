package org.example.checkersfinalproject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Piece {
    protected PieceType pieceType;
    protected PieceType differentPieceType;
    protected Set<DirectionVector> directionsToMove = new HashSet<>();
    protected PieceType enemyPieces;
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

    public int rowToBeKing(){
        return pieceType == PieceType.WHITEPIECE ? 0 : 7;
    }
}
