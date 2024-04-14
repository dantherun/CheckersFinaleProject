package org.example.checkersfinalproject;

public class Move {
    private int evaluation;
    private int[] pieceFromCord;
    private int[] pieceToCord;
    private boolean becomesAKing;
    private PieceType piece;

    public Move(int evaluation, int[] pieceFromCord){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = null;
        this.becomesAKing = false;
    }

    public Move(int evaluation, int[] pieceFromCord, int[] pieceToCord, boolean becomesAKing, PieceType piece){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = pieceToCord;
        this.becomesAKing = becomesAKing;
        this.piece = piece;
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

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public void addEvaluation(int evaluation){
        this.evaluation += evaluation;
    }

    public void subtractEvaluation(int evaluation){
        this.evaluation -= evaluation;
    }
    public boolean becomesAKing(){
        return becomesAKing;
    }

    public PieceType getPiece(){
        return piece;
    }
    public Move clone(){
        return new Move(evaluation, pieceFromCord, pieceToCord, becomesAKing, piece);
    }

}
