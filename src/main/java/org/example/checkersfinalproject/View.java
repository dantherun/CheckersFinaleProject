package org.example.checkersfinalproject;

public class View implements IView{
    private Presenter presenter;
    private CheckersBoard visualBoard;
    public View(CheckersBoard visualBoard){
        this.visualBoard = visualBoard;
        this.presenter = new Presenter(this);
    }

    public void restart(){
       // visualBoard.initialize();
    }

    @Override
    public void updateBoard(PieceType[][] board) {for (int j = 0; j < 8; j++) {
    }
        visualBoard.removePieces();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == PieceType.WHITEPIECE)
                    visualBoard.addPiece(PieceType.WHITEPIECE, j, i);
                else if(board[i][j] == PieceType.REDPIECE)
                    visualBoard.addPiece(PieceType.REDPIECE, j, i);
                else if(board[i][j] == PieceType.WHITEKING)
                    visualBoard.addPiece(PieceType.WHITEKING, j, i);
                else if(board[i][j] == PieceType.REDKING)
                    visualBoard.addPiece(PieceType.REDKING, j, i);
                else if(board[i][j] == PieceType.SHADOW)
                    visualBoard.addPiece(PieceType.SHADOW, j, i);
            }
        }
    }

    @Override
    public void deletePiece(int x, int y) {

    }

    @Override
    public void addPiece(PieceType type, int row, int col) {
        visualBoard.addPiece(type, col , row);
    }

    @Override
    public void deleteKing(int x, int y) {

    }

    @Override
    public void addKing(int x, int y) {

    }

    @Override
    public void setTimer(int setSeconds, int duration, int timerNumber) {
        visualBoard.setTimer(setSeconds, duration, timerNumber);
    }

    public void stopTimer(int timerId){
        visualBoard.stopTimer(timerId);
    }

    @Override
    public void message(String message, int x, int y, boolean bold, int timerId) {
        visualBoard.message(message, x, y, bold, timerId);
    }

    public void takeInput(int x, int y) {
        presenter.getUpdateFromBoard(x, y);
        //return visualBoard.getInput();
    }

//    @Override
//    public void alertMessage(String message) {
//        visualBoard.alertMessage(message);
//    }

    public void askTwoAnswers(String message, String ans1, String ans2, String questionType, boolean alert) {
        visualBoard.askTwoAnswers(message, ans1, ans2, questionType, alert);
    }
//    public void alertAskTwoAnswers(String message, String ans1, String ans2, String questionType) {
//        visualBoard.alertAskTwoAnswers(message, ans1, ans2, questionType);
//    }

    public void buttonPushed(String message, String buttonContent, String questionType){
        presenter.answer(message, buttonContent, questionType);
    }

    public void initializeBoard(){
        visualBoard.initializeBoard();
    }

    public void updateTimer(int time){
        presenter.updateTimer(time);
    }

    public void quit(){
        visualBoard.quit();
    }

}
