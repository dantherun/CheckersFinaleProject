package org.example.checkersfinalproject;

public class Move {
    private int evaluation;
    private int[] pieceFromCord;
    private int[] pieceToCord;

    public Move(int evaluation, int[] pieceFromCord, int[] pieceToCord){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = pieceToCord;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public int[] getPieceFromCord() {
        return pieceFromCord;
    }

    public int[] getPieceToCord() {
        return pieceToCord;
    }

    public Move clone(){
        return new Move(evaluation, pieceFromCord, pieceToCord);
    }
}
