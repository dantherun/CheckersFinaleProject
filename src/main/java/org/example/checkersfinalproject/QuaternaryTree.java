package org.example.checkersfinalproject;

public class QuaternaryTree{
    private int[] enemyToKill;
    private int[] playerCords;
    private String playerType;
    private QuaternaryTree topLeft;
    private QuaternaryTree topRight;
    private QuaternaryTree buttomLeft;
    private QuaternaryTree buttomRight;
    private QuaternaryTree father;
    public QuaternaryTree(int[] enemyToKill, int[] playerCords, String playerType, QuaternaryTree father){
        this.enemyToKill = new int[]{enemyToKill[0], enemyToKill[1]};
        this.playerCords = new int[]{playerCords[0], playerCords[1]};
        this.playerType = playerType;
        this.topLeft = null;
        this.topRight = null;
        this.buttomLeft = null;
        this.buttomRight = null;
        this.father = father;
    }

    public QuaternaryTree getTopLeft() {
        return topLeft;
    }

    public int[] getPlayerCords() {
        return playerCords;
    }

    public QuaternaryTree getFather() {
        return father;
    }

    public QuaternaryTree getTopRight() {
        return topRight;
    }

    public QuaternaryTree getButtomLeft() {
        return buttomLeft;
    }

    public QuaternaryTree getButtomRight() {
        return buttomRight;
    }

    public int[] getEnemyToKill() {
        return enemyToKill;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setTopLeft(QuaternaryTree topLeft) {
        this.topLeft = topLeft;
    }

    public void setTopRight(QuaternaryTree topRight) {
        this.topRight = topRight;
    }

    public void setButtomLeft(QuaternaryTree buttomLeft) {
        this.buttomLeft = buttomLeft;
    }

    public void setButtomRight(QuaternaryTree buttomRight) {
        this.buttomRight = buttomRight;
    }
}