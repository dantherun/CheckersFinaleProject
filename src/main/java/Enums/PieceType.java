package Enums;

public enum PieceType {
    REDPIECE(1),
    WHITEPIECE(-1),
    WHITEKING(-2),
    REDKING(2),
    None(3),
    SHADOW(0);
    final int piece;
    PieceType(int moveDir){
        this.piece = moveDir;
    }
    public int getPiece(){
        return this.piece;
    }
}
