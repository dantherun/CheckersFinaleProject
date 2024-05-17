package Enums;

public enum MoveType {
    move(1),
    eat(2),
    none(3);

    final int moveType;
    MoveType(int moveType){
        this.moveType = moveType;
    }
}
