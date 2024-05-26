package Classes;

import Enums.PieceType;

public class Move {
    // The evaluation of the move
    private float evaluation;

    // The coordinates of the piece that will be moved
    private int[] pieceFromCord;

    // The coordinates of the piece that will be moved to
    private int[] pieceToCord;

    // If the piece becomes a king
    private boolean becomesAKing;

    // If the piece will eat
    private boolean willEat;

    // The type of the piece
    private PieceType piece;


    public Move(int[] pieceFromCord, int[] pieceToCord){
        this.evaluation = 0;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = pieceToCord;
        this.becomesAKing = false;
        this.piece = PieceType.None;
    }

    public Move(int evaluation, boolean becomesAKing, boolean willEat){
        this.evaluation = evaluation;
        this.pieceFromCord = null;
        this.pieceToCord = null;
        this.becomesAKing = becomesAKing;
        this.piece = PieceType.None;
        this.willEat = willEat;
    }

    public Move(float evaluation, int[] pieceFromCord, int[] pieceToCord, boolean becomesAKing, PieceType piece, boolean willEat){
        this.evaluation = evaluation;
        this.pieceFromCord = pieceFromCord;
        this.pieceToCord = pieceToCord;
        this.becomesAKing = becomesAKing;
        this.piece = piece;
        this.willEat = willEat;
    }

    public float getEvaluation() {
        return evaluation;
    }

    public int[] getPieceFromCord() {
        return pieceFromCord;
    }

    public int[] getPieceToCord() {
        return pieceToCord;
    }

    public void setEvaluation(float evaluation) {
        this.evaluation = evaluation;
    }

    public void addEvaluation(float evaluation){
        this.evaluation += evaluation;
    }

    public void subtractEvaluation(float evaluation){
        this.evaluation -= evaluation;
    }
    public boolean becomesAKing(){
        return becomesAKing;
    }

    public PieceType getPiece(){
        return piece;
    }
    public Move clone(){
        return new Move(evaluation, pieceFromCord, pieceToCord, becomesAKing, piece, willEat);
    }

    public boolean willEat() {
        return willEat;
    }
}
