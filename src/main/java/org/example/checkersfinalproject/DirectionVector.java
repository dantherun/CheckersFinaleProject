package org.example.checkersfinalproject;

public enum DirectionVector {
    north(8),
    south(-8),
    west(-1),
    east(1),
    northeast(7),
    southeast(-9),
    northwest(9),
    southwest(-7),
    all(0);
    final int moveDirection;
    DirectionVector(int moveDir){
        this.moveDirection = moveDir;
    }
    public int getDirectionType(){
        return this.moveDirection;
    }
}
