package Classes;

import Interface.IView;
import Enums.PieceType;

public class View implements IView {
    // The presenter class
    private Presenter presenter;

    // The visual board (CheckersBoard class)
    private CheckersBoard visualBoard;
    public View(CheckersBoard visualBoard){
        this.visualBoard = visualBoard;
        this.presenter = new Presenter(this);
    }

    /**
     * Updates the board with the updated board
     * @param board the updated board
     */
    @Override
    public void updateBoard(PieceType[][] board) {
        // Removes all pieces from the board
        visualBoard.removePieces();

        // Iterates all over the board and puts all the pieces in it
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

    /**
     * Sets a timer on the board
     * @param setSeconds the time to set the timer to
     * @param duration the duration of the timer
     * @param timerNumber the number of the timer
     */
    @Override
    public void setTimer(int setSeconds, int duration, int timerNumber) {
        visualBoard.setTimer(setSeconds, duration, timerNumber);
    }

    /**
     * Stops a timer on the board
     * @param timerId - the number of the timer to stop
     */
    public void stopTimer(int timerId){
        visualBoard.stopTimer(timerId);
    }

    /**
     * Displays a message on the board
     * @param message the message to display
     * @param x the x coordinate of the message
     * @param y the y coordinate of the message
     * @param bold whether the message should be bold
     * @param textId the id of the text
     */
    @Override
    public void message(String message, int x, int y, boolean bold, int textId) {
        visualBoard.message(message, x, y, bold, textId);
    }

    /**
     * Takes input from the board into the presenter
     * @param x the x coordinate of the input
     * @param y the y coordinate of the input
     */
    public void takeInput(int x, int y) {
        presenter.getUpdateFromBoard(x, y);
    }

    /**
     * Asks a question with two answers
     * @param message the message to ask
     * @param ans1 the first answer
     * @param ans2 the second answer
     * @param questionType the type of question
     */
    public void askTwoAnswers(String message, String ans1, String ans2, String questionType) {
        visualBoard.askTwoAnswers(message, ans1, ans2, questionType);
    }

    /**
     * Asks a question with three answers
     * @param message the message to ask
     * @param ans1 the first answer
     * @param ans2 the second answer
     * @param ans3 the third answer
     * @param questionType the type of question
     */
    public void askThreeAnswers(String message, String ans1, String ans2, String ans3, String questionType) {
        visualBoard.askThreeAnswers(message, ans1, ans2, ans3, questionType);
    }

    /**
     * Pushes a button on the board and sends the answer to the presenter
     * @param message the message to push
     * @param buttonContent the content of the button
     * @param questionType the type of question
     */
    public void buttonPushed(String message, String buttonContent, String questionType){
        presenter.answer(message, buttonContent, questionType);
    }

    /**
     * If a button is pushed on the board, it will call the answer method in the presenter
     * @param message - the message that was on the button
     */
    public void buttonPushed(String message){
        presenter.answer(message, "", "");
    }

    /**
     * Initializes the board by calling the initializeBoard method in the visualBoard
     */
    public void initializeBoard(){
        visualBoard.initializeBoard();
    }

    /**
     * Updates the timer on the board and sends the time to the presenter
     * @param time the time to update the timer to
     */
    public void updateTimer(int time){
        presenter.updateTimer(time);
    }

    /**
     * Quits the game by calling the quit method in the CheckersBoard class
     */
    public void quit(){
        visualBoard.quit();
    }

    /**
     * Adds a button to the board
     * @param message the message on the button
     * @param x the x coordinate of the button
     * @param y the y coordinate of the button
     * @param buttonId the id of the button
     */
    public void addButton(String message, int x, int y, int buttonId){
        visualBoard.addButton(message, x, y, buttonId);
    }

    /**
     * Removes a button from the board
     * @param buttonId the id of the button to remove
     */
    public void removeButton(int buttonId){
        visualBoard.removeButton(buttonId);
    }

    /**
     * Changes the color of a piece on the board
     * @param row the row of the piece
     * @param col the column of the piece
     * @param color the color to change the piece to
     */
    public void changePieceColor(int row, int col, String color){
        visualBoard.changePieceColor(col, row, color);
    }

    /**
     * when a key is pressed on the board, it will call the keyPressed method in the presenter
     * @param keyType the type of key that was pressed
     */
    public void onKeyPressed(String keyType){
        presenter.keyPressed(keyType);
    }
}
