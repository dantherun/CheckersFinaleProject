package org.example.checkersfinalproject;

public class Move {
    private int evaluation;
    private int[] pieceFromCord;
    private int[] pieceToCord;

    private boolean becomesAKing;

    public Move(int evaluation, int[] pieceFromCord){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = null;
        this.becomesAKing = false;
    }

    public Move(int evaluation, int[] pieceFromCord, int[] pieceToCord, boolean becomesAKing){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = pieceToCord;
        this.becomesAKing = becomesAKing;
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

    public void subtructEvaluation(int evaluation){
        this.evaluation -= evaluation;
    }
    public Move clone(){
        return new Move(evaluation, pieceFromCord, pieceToCord, becomesAKing);
    }
    public boolean becomesAKing(){
        return becomesAKing;
    }
}
