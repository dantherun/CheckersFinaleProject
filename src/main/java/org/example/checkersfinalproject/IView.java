package org.example.checkersfinalproject;

public interface IView {
    public void deletePiece(int x, int y);
    public void addPiece(PieceType type, int x, int y);
    public void deleteKing(int x, int y);
    public void addKing(int x, int y);
    public void setTimer(int setSeconds, int duration, int timerId);
    public void stopTimer(int timerId);
    public void message(String message, int x, int y, boolean bold, int timerId);
    //public void alertMessage(String message);
    public void initializeBoard();
    public void restart();
    public void updateBoard(PieceType[][] board);
   // public void alertAskTwoAnswers(String message, String asn1, String ans2, String restart);
    //public void message(String message, int x, int y, boolean bold, int timerId, boolean alert);

    public void askTwoAnswers(String message, String asn1, String ans2, String restart, boolean alert);
    public void quit();
}
