package Classes;

import Enums.AIDifficulty;
import Enums.GameMode;
import Enums.PieceType;
import Enums.PositionType;
import Interface.IView;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Stack;

public class Presenter {
    // the duration of the timer
    public int TIMER_DURATION = 500;

    // the view interface
    private final IView view;

    // the model of the game
    private final Model model;

    // the turn of player that is playing
    private int player;

    // if the game is on or not
    private boolean gameOn;

    // if the player has to eat or not
    private boolean hasToEat;

    // the last row that the player chose
    private int lastRow;

    // the last column that the player chose
    private int lastCol;

    // the amount of time that remained for each player
    private int[] timeRemained;

    // the last piece that the player chose
    private Piece lastChosenPiece;

    // the moves that the player can make
    private HashMap<String, String> moves;

    // the game mode of the game (player vs player or player vs AI)
    private GameMode gameMode;

    // the piece type of the current player
    private Piece playerPieceType;

    // the king type of the current player
    private King playerKingType;

    // the type of the piece that is in the position
    private PieceType hasPiece;

    // the move that the AI chose
    private Move aiMove;

    // the positions before the current position
    private Stack<SimpleEntry<Model, String>> positionsBefore;

    // the positions after the current position
    private Stack<SimpleEntry<Model, String>> positionsAfter;

    // the difficulty of the AI
    private AIDifficulty aiDifficulty;

    public Presenter(IView view){
        timeRemained = new int[2];
        this.view = view;
        model = new Model();
        player = 0;
        String moveDirection = "";
        lastChosenPiece = null;
        moves = new HashMap<>();
        hasToEat = false;
        restartGame();
    }

    /**
     * This method is used to restart the game by asking the player who they want to play against
     */
    public void restartGame(){
        gameOn = true;
        view.askTwoAnswers("Who do you want to play against?", "Player", "AI", "playerOrAI");
    }

    /**
     * The main method of the game.
     * The method is used to get the update from the board
     * and to make the move if the player chose a shadow piece
     * or to make the shadows if the player chose a piece to move
     * @param col - the column of the piece
     * @param row - the row of the piece
     */
    public void getUpdateFromBoard(int col, int row){
        // --------- NOTE: shadows = possible moves for the piece ------------

        // if the game is over
        // or the board is not in the last position (happens if the player
        // chose to see previous moves)
        if(!positionsAfter.isEmpty() || !gameOn)
            return;

        playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
        playerKingType = new King(playerPieceType.getDifferentType());

        // if the player chose a shadow piece (to move)
        if(model.hasPiece(row, col) == PieceType.SHADOW) {
            lastRow = -1;
            lastCol = -1;

            // adding the current position to the stack including the shadows
            positionsBefore.add(new SimpleEntry<>(model.clone(), row + "," + col + ":" + player));

            // making the move
            model.makeMove(lastChosenPiece, row, col, moves);

            // removing all the shadows
            model.removeAllPieces(PieceType.SHADOW);

            // adding the current position to the stack without the shadows
            positionsBefore.add(new SimpleEntry<>(model.clone(), ":" + player));

            // if the player is playing against another player then stop the timer
            if(gameMode == GameMode.playerVsPlayer)
                view.stopTimer(player % 2);

            // if the player has won
            if(model.hasWon(lastChosenPiece)){
                playerWon();
            }

            // if the player didn't win
            else{
                // preparing for the next player's move
                playerPieceType = new Piece(playerPieceType.getEnemyPieceType());
                playerKingType = new King(playerPieceType.getDifferentType());
                hasToEat = model.needToEat(playerPieceType);

                // changing the player
                player++;

                // if the game mode is player vs player
                if(gameMode == GameMode.playerVsPlayer){
                    // removing the resignation and AI move buttons from the previous player
                    view.removeButton(player % 2 == 0 ? 1 : 0); // remove resign button
                    view.removeButton(player % 2 == 0 ? 3 : 2); // remove ai move button

                    // adding the resignation and AI move buttons for the next player
                    view.addButton("Resign", 870, player % 2 == 0 ? 600 : 200, player % 2 == 0 ? 0 : 1); // add resign button
                    view.addButton("See AI Move", 812, player % 2 == 0 ? 700 : 300, player % 2 == 0 ? 2 : 3); // add ai move button

                    // starting the timer for the next player
                    view.setTimer(timeRemained[player % 2], TIMER_DURATION - timeRemained[player % 2], player % 2);
                }

                // if the next player is AI
                else{
                    // moving the AI
                    moveAI();

                    // if the AI has won
                    if(model.hasWon(lastChosenPiece)){
                        aiWon();
                    }

                    // changing the player
                    player++;
                    playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);

                    // checking if the player has to eat
                    hasToEat = model.needToEat(playerPieceType);
                }
            }

            // updating the board
            view.updateBoard(model.getBoard());
        }

        // if the player chose a piece to move (making the shadows)
        else{
            // removing all the shadows and updating the board
            model.removeAllPieces(PieceType.SHADOW);
            view.updateBoard(model.getBoard());

            // if the player chose the same piece that already has chosen
            if(row == lastRow && col == lastCol){
                lastRow = -1;
                lastCol = -1;
                return;
            }

            // if the player chose one of his pieces
            if((hasPiece = model.hasPiece(row, col)) == playerPieceType.getPieceType())
                // setting the last chosen piece to the chosen piece
                lastChosenPiece = playerPieceType;

            // if the player chose one of his kings
            else if(hasPiece == playerKingType.getPieceType())
                // setting the last chosen piece to the chosen king
                lastChosenPiece = playerKingType;

            // if the player chose an empty cell or an enemy piece
            else{
                lastRow = -1;
                lastCol = -1;
                return;
            }

            // save the last row and column
            lastRow = row;
            lastCol = col;

            // getting all the moves that the player can make
            moves = model.getAllMoves(lastChosenPiece, row, col, hasToEat, true);

            // iterating over all the moves and adding the shadows to the board
            for (String move : moves.keySet()) {
                model.addPiece(PieceType.SHADOW, Integer.parseInt(move.split(",")[0]), Integer.parseInt(move.split(",")[1]));
            }

            // updating the board
            view.updateBoard(model.getBoard());

            // changing the color of the chosen piece so the player can see it clearly
            view.changePieceColor(row, col, lastChosenPiece.getPieceColor() == PieceType.WHITEPIECE ? "#696565" : "#6e2525");
        }

    }

    /**
     * This method is used to move the AI
     */
    public void moveAI(){
        // getting the AI move
        aiMove = model.getAIMove(playerPieceType, aiDifficulty);

        // getting the coordinates of the piece that the AI wants to move
        int[] aiFromCord = aiMove.getPieceFromCord();
        int[] aiToCord = aiMove.getPieceToCord();

        // if the AI chose one of its pieces
        if((hasPiece = model.hasPiece(aiFromCord[0], aiFromCord[1])) == playerPieceType.getPieceType())
            // setting the last chosen piece to the chosen piece
            lastChosenPiece = playerPieceType;

        // if the AI chose one of its kings
        else if(hasPiece == playerKingType.getPieceType())
            // setting the last chosen piece to the chosen king
            lastChosenPiece = playerKingType;

        // getting all the moves that the pieces of the AI can make
        moves = model.getAllMoves(lastChosenPiece, aiFromCord[0], aiFromCord[1], hasToEat, true);

        // making the move with the chosen piece
        model.makeMove(lastChosenPiece, aiToCord[0], aiToCord[1], moves);

        // adding the current position to the stack
        positionsBefore.add(new SimpleEntry<>(model.clone(), ":" + player));
    }

    /**
     * This method is used when the AI wins
     */
    public void aiWon(){
        // calling the game over method
        gameOver();

        // asking the player if they want to restart the game and showing that the AI won
        view.askTwoAnswers("AI won. Do you want to restart the game?", "Yes", "No", "restart");
    }

    /**
     * This method is used when the player wins
     */
    public void playerWon(){
        // calling the game over method
        gameOver();

        // asking the player if they want to restart the game and showing the winner
        view.askTwoAnswers("Player " + (player % 2 + 1) + " Won. Do you want to restart the game?", "Yes", "No", "restart");
    }

    /**
     * This method is used when the game is over (when a player resigns or wins)
     */
    public void gameOver(){
        // stopping the timers and removing the buttons
        stopTimers();
        removeButtons();

        // setting the game to off
        gameOn = false;
    }

    /**
     * This method is used to stop the timers
     */
    public void stopTimers(){
        view.stopTimer(0);
        view.stopTimer(1);
    }

    /**
     * This method is used to remove the buttons from the board
     */
    public void removeButtons(){
        view.removeButton(0);
        view.removeButton(1);
        view.removeButton(2);
        view.removeButton(3);
    }

    /**
     * This method is used to answer the questions that the view asks
     * @param question - the question that the view asked
     * @param ans - the answer to the question
     * @param questionType - the type of the question
     */
    public void answer(String question, String ans, String questionType){
        // if the question is about whom the player wants to play against
        if(questionType.equals("playerOrAI")){
            // if the player wants to play against another player
            if(ans.equals("Player")){
                gameMode = GameMode.playerVsPlayer;
                initializeGame();
            }

            // if the player wants to play against the AI
            else if(ans.equals("AI")){
                gameMode = GameMode.playerVsAi;

                // asking the player about the difficulty of the AI
                view.askThreeAnswers("Which Game Difficulty", "Easiest", "Medium", "Hardest", "difficulty");
            }
        }

        // if the question is about restarting the game
        else if(questionType.equals("restart")){
            // if the player wants to restart the game, then restart the game
            if(ans.equals("Yes")){
                restartGame();
            }

            // if the player doesn't want to restart the game, then quit the game
            else{
                view.quit();
            }
        }

        // if the question is about the difficulty of the AI
        else if(questionType.equals("difficulty")){
            // setting the difficulty of the AI based on the player's answer
            switch (ans) {
                case "Easiest" -> aiDifficulty = AIDifficulty.EASIEST;
                case "Medium" -> aiDifficulty = AIDifficulty.MEDIUM;
                case "Hardest" -> aiDifficulty = AIDifficulty.HARDEST;
            }

            // asking the player who starts
            view.askTwoAnswers("Who starts?", "You", "AI", "who starts");
        }

        // if the question is about who starts
        else if(questionType.equals("who starts")){
            // first initialize the game
            initializeGame();

            // if the answer is that the AI starts
            if(ans.equals("AI")){
                playerPieceType = new Piece(PieceType.WHITEPIECE);
                playerKingType = new King(playerPieceType.getDifferentType());

                // make the AI move
                moveAI();

                // change the player to the next player
                player++;
                playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);

                // check if the player has to eat
                hasToEat = model.needToEat(playerPieceType);

                // update the board
                view.updateBoard(model.getBoard());
            }
        }

        // if the question is about the AI move
        else if(question.equals("See AI Move")){
            Model modelToDo;
            int playerTurn;

            // if the positions after stack is not empty
            // (which means the player is seeing the previous moves)
            if(!positionsAfter.isEmpty()){
                // take the model saved with the needed board position
                modelToDo = positionsBefore.peek().getKey().clone();

                // take the player that played in the needed board position
                playerTurn = Integer.parseInt(positionsBefore.peek().getValue().split(":")[1]);
            }

            // if the positions after stack is empty
            // (which means the player is in the last board position)
            else{
                // take the last board position model
                modelToDo = model;

                // take the player playing now
                playerTurn = player;

            }

            playerPieceType = playerTurn % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
            playerKingType = new King(playerPieceType.getDifferentType());

            // check if the AI has to eat
            hasToEat = modelToDo.needToEat(playerPieceType);

            // get the AI move
            aiMove = modelToDo.getAIMove(playerPieceType, AIDifficulty.HARDEST);

            // get the coordinates of the piece that the AI wants to move
            int[] aiFromCord = aiMove.getPieceFromCord();
            int[] aiToCord = aiMove.getPieceToCord();

            // if the AI chose one of its pieces
            if((hasPiece = modelToDo.hasPiece(aiFromCord[0], aiFromCord[1])) == playerPieceType.getPieceType())
                lastChosenPiece = playerPieceType;

            // if the AI chose one of its kings
            else if(hasPiece == playerKingType.getPieceType())
                lastChosenPiece = playerKingType;

            else
                return;

            // remove all the shadows
            modelToDo.removeAllPieces(PieceType.SHADOW);

            // get all the moves that the AI can make so that the player can later choose to take the AI move or not
            moves = modelToDo.getAllMoves(lastChosenPiece, aiFromCord[0], aiFromCord[1], hasToEat, true);

            // show the shadow that will show the end of the AI move
            modelToDo.addPiece(PieceType.SHADOW, aiToCord[0], aiToCord[1]);

            // update the board
            view.updateBoard(modelToDo.getBoard());

            // change the color of the chosen piece so the player can see it clearly
            view.changePieceColor(aiFromCord[0], aiFromCord[1], lastChosenPiece.getPieceColor() == PieceType.WHITEPIECE ? "#696565" : "#6e2525");
        }

        // if the answer is about the player resigning
        else if(question.equals("Resign")){
            // change the player
            player++;

            // if the game mode is player vs AI and the AI has won
            if(gameMode == GameMode.playerVsAi && player % 2 == 1)
                aiWon();

            // else if the game mode is player vs player
            else
                // then the other player won
                playerWon();
        }
    }

    /**
     * This method is used when a key is pressed
     * @param key - the key that was pressed
     */
    public void keyPressed(String key){
        switch (key) {
            // if the key is D or A, then the player can see the next or previous moves
            case "D" -> changePositions(PositionType.forward, false, null);
            case "A" -> changePositions(PositionType.backward, false, null);

            // if the key is W or S, then the player can see all the way to the end or the beginning
            // the next or previous moves
            case "W" -> changePositions(PositionType.forward, true, null);
            case "S" -> changePositions(PositionType.backward, true, null);
        }
    }

    /**
     * This method is used to change the positions of the board
     * @param positionType - the type of the position (forward or backward)
     * @param allTheWay - if the player wants to see all the way to the end or the beginning
     * @param position - the position of the board
     */
    public void changePositions(PositionType positionType, boolean allTheWay, Model position){
        SimpleEntry<Model, String> info;

        // if the player wants to see the next move and the positions after stack is not empty
        if(positionType == PositionType.forward && !positionsAfter.isEmpty()){
            // push the before position to the positions before stack
            // from the positions after stack
            positionsBefore.add(positionsAfter.pop());

            // get the needed board position
            info = positionsBefore.peek();
            position = info.getKey();

            // if the player wants to see all the way to the end
            if(allTheWay)
                // then call the method again until the board is in its last position
                changePositions(PositionType.forward, true, position);
        }

        // if the player wants to see the previous move and the positions before stack is not empty
        else if(positionType == PositionType.backward && positionsBefore.size() > 1) {
            // push the current position to the positions after stack
            positionsAfter.add(positionsBefore.pop());

            // if the positions before stack is not empty
            if(!positionsBefore.isEmpty()){

                // get the needed board position
                info = positionsBefore.peek();
                position = info.getKey();
            }

            // if the player wants to see all the way to the beginning
            if(allTheWay)
                // then call the method again until the board is in its first position
                changePositions(PositionType.backward, true, position);
        }

        // if has arrived to the end or the beginning
        else{
            allTheWay = false;
        }

        // update the board with the needed position
        if(position != null && !allTheWay){
            view.updateBoard(position.getBoard());
        }
    }

    /**
     * This method is used to update the timer
     * @param time - the time to update the timer to
     */
    public void updateTimer(int time){
        // convert the time to minutes and seconds from seconds
        int[] convertedTime = convertToTime(TIMER_DURATION - time);

        // update the timer for the bottom player if it's their turn
        if(player % 2 == 0)
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 520, false, 4); // bottom player

        // update the timer for the top player if needed if it's their turn
        else
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 120, false, 5); // top player

        // if the time is up
        if(TIMER_DURATION - time <= 0) {
            // then the other player won
            player++;
            playerWon();
        }

        // update the remaining time for the player
        timeRemained[player % 2] = time;
    }

    /**
     * This method is used to initialize the game
     */
    public void initializeGame(){
        // resets all the variables
        gameOn = true;
        player = 0;
        moves = new HashMap<>();
        hasToEat = false;
        timeRemained[0] = timeRemained[1] = 0;
        positionsBefore = new Stack<>();
        positionsAfter = new Stack<>();

        // initialize the board
        view.initializeBoard();

        // update the board with the initialized board
        view.updateBoard(model.initializeBoard());
        int[] convertedTime = convertToTime(TIMER_DURATION);

        // display the messages on the board that show the player number
        view.message("player 1", 880, 50, false, 0); // top player number message
        view.message("player 2", 880, 450, false, 1); // bottom player number message

        // display the timers on the board
        view.message("Timer: ", 850, 120, true, 2); // top timer
        view.message("Timer: ", 850, 520, true, 3); // bottom timer

        // display the time of the timers on the board
        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 520, false, 4); // bottom player time message
        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 120, false, 5); // top player time message

        // add the resignation and see AI move buttons to the board
        view.addButton("Resign", 870, 600, 0); // bottom resign button
        view.addButton("See AI Move", 812, 700, 2); // bottom see AI move button

        // start the timer for the first player
        view.setTimer(timeRemained[0], TIMER_DURATION - timeRemained[0], 0);

        // add the initial position to the positions before stack
        positionsBefore.add(new SimpleEntry<>(model.clone(), ":" + player));
    }

    /**
     * This method is used to convert the time from seconds to minutes and seconds
     * @param time - the time to convert
     * @return the converted time
     */
    public int[] convertToTime(int time){
        int[] convertedTime = new int[2];
        while(time >= 60){
            convertedTime[0]++;
            time -= 60;
        }
        convertedTime[1] = time;
        return convertedTime;
    }
}