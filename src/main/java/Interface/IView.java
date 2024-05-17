package Interface;

import Enums.PieceType;

public interface IView {
    /**
     * This method is used to set a timer
     * @param setSeconds - the time to set the timer to
     * @param duration - the duration of the timer
     * @param timerId - the number of the timer
     */
    void setTimer(int setSeconds, int duration, int timerId);

    /**
     * This method is used to stop a timer
     * @param timerId - the number of the timer to stop
     */
    void stopTimer(int timerId);

    /**
     * This method is used to display a message on the board
     * @param message - the message to display
     * @param x - the x position of the message
     * @param y - the y position of the message
     * @param bold - whether the message should be bold
     * @param textId - the id of the text
     */
    void message(String message, int x, int y, boolean bold, int textId);

    /**
     * This method is used to initialize the board
     */
    void initializeBoard();

    /**
     * This method is used to update the board
     * @param board - the updated board
     */
    void updateBoard(PieceType[][] board);

    /**
     * This method is used to ask a question with two answers
     * @param message - the message to ask
     * @param asn1 - the first answer
     * @param ans2 - the second answer
     * @param questionType - the type of question
     */
    void askTwoAnswers(String message, String asn1, String ans2, String questionType);

    /**
     * This method is used to ask a question with three answers
     * @param message - the message to ask
     * @param asn1 - the first answer
     * @param ans2 - the second answer
     * @param ans3 - the third answer
     * @param questionType - the type of question
     */
    void askThreeAnswers(String message, String asn1, String ans2, String ans3, String questionType);

    /**
     * This method is used to add a button to the board
     * @param message - the message to display on the button
     * @param x - the x position of the button
     * @param y - the y position of the button
     * @param buttonId - the id of the button
     */
    void addButton(String message, int x, int y, int buttonId);

    /**
     * This method is used to remove a button from the board
     * @param buttonId - the id of the button to remove
     */
    void removeButton(int buttonId);

    /**
     * This method is used to change the color of a piece
     * @param row - the row of the piece
     * @param col - the column of the piece
     * @param color - the color to change the piece to
     */
    void changePieceColor(int row, int col, String color);

    /**
     * This method is used to quit the game
     */
    void quit();
}
