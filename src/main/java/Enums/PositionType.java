package Enums;

public enum PositionType {
    forward(1),
    backward(-1);
    final int positionType;
    PositionType(int positionType){
        this.positionType = positionType;
    }
}
