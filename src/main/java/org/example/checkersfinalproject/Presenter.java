package org.example.checkersfinalproject;

import java.util.*;

public class Presenter {
    public int TIMER_DURATION = 500;
    private final IView view;
    private final Model model;
    private int player;
    private boolean gameOn;
    private boolean hasToEat;
    private String moveDirection;
    private int row;
    private int col;
    private int[] cordinations;
    private int[] timeRemained;
    private Piece lastChosenPiece;
    private HashMap<String ,String> moves;
    private GameMode gameMode;
    private Piece playerPieceType;
    private King playerKingType;
    private PieceType hasPiece;
    private Move aiMove;
    private Stack<Model> positionsBefore;
    private Stack<Model> positionsAfter;

    public Presenter(IView view){
        timeRemained = new int[2];
        this.view = view;
        model = new Model();
        player = 0;
        String moveDirection = "";
        lastChosenPiece = null;
        moves = new HashMap<>();
        hasToEat = false;
        game();
    }

    public void game(){
        gameOn = true;
        view.askTwoAnswers("Who do you want to play against?", "Player", "AI", "playerOrAI", true);

        //view.askTwoAnswers("Do you wanna start?", "Yes", "No, the game sucks");
       // while(gameOn){
//            cordinations = view.takeInput();
//            row = cordinations[0];
//            col = cordinations[1];
//            if(model.hasPiece(row, col) == player){
//                moves = model.getAllMoves(player == 0 ? PieceType.WHITE : PieceType.RED, row, col);
//                for (int[] move : moves) {
//                    view.addPiece(PieceType.SHADOW, move[0], move[1]);
//                }
//            }
       // }
    }

    public void getUpdateFromBoard(int col, int row){
        if(!positionsAfter.isEmpty())
            return;

        playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
        playerKingType = new King(playerPieceType.getDifferentType());

        // to make the move
        if(model.hasPiece(row, col) == PieceType.SHADOW) {
         //   if(model.canBeKing(playerType, row)){
               // model.makeKing(playerType, row, col);
               // playerType = player % 2 == 0 ? PieceType.WHITEKING : PieceType.REDKING;
          //  }

        //    else{
            model.makeMove(lastChosenPiece, row, col, moves);

               // model.makeMove(player % 2 == 0 ? PieceType.WHITEKING : PieceType.REDKING, row, col);
       //    }

            model.removeAllPieces(PieceType.SHADOW);
            if(gameMode == GameMode.playerVsPlayer)
                view.stopTimer(player % 2);

            if(model.hasWon(lastChosenPiece)){
                playerWon();
            }

            else if(model.isTie()){
                playerTie();
            }

            else{

                positionsBefore.add(model.clone());
                // preparing for the next player's move
                player++;
                view.removeButton(player % 2 == 0 ? 1 : 0); // remove resign button
                view.removeButton(player % 2 == 0 ? 3 : 2); // remove ai move button

                view.addButton("Resign", 870, player % 2 == 0 ? 600 : 200, player % 2 == 0 ? 0 : 1); // add resign button
                view.addButton("See AI Move", 812, player % 2 == 0 ? 700 : 300, player % 2 == 0 ? 2 : 3); // add ai move button

                playerPieceType = new Piece(playerPieceType.getEnemyPieceType());
                playerKingType = new King(playerPieceType.getDifferentType());

                hasToEat = model.canEat(playerPieceType);
                if(gameMode == GameMode.playerVsPlayer)
                    view.setTimer(timeRemained[player % 2], TIMER_DURATION - timeRemained[player % 2], player % 2);

                if(gameMode == GameMode.playerVsAi){
                    aiMove = model.getAIMove(playerPieceType);
                    while(aiMove == null || aiMove.getPieceFromCord() == null || aiMove.getPieceToCord() == null){
                        System.out.println("asus");
                        aiMove = model.getAIMove(playerPieceType);
                    }

                    int[] aiFromCord = aiMove.getPieceFromCord();
                    int[] aiToCord = aiMove.getPieceToCord();

                    if((hasPiece = model.hasPiece(aiFromCord[0], aiFromCord[1])) == playerPieceType.getPieceType()){
                        lastChosenPiece = playerPieceType;
                        moves = model.getAllMoves(playerPieceType, aiFromCord[0], aiFromCord[1], hasToEat, true);
                    }

                    else if(hasPiece == playerKingType.getPieceType()){
                        lastChosenPiece = playerKingType;
                        moves = model.getAllMoves(playerKingType, aiFromCord[0], aiFromCord[1], hasToEat, true);
                    }

                    model.makeMove(lastChosenPiece, aiToCord[0], aiToCord[1], moves);
                    positionsBefore.add(model.clone());

                    if(model.hasWon(lastChosenPiece)){
                        aiWon();
                    }

                    else if(model.isTie()){
                        playerTie();
                    }

                    player++;
                    playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
                    hasToEat = model.canEat(playerPieceType);

                }
            }

            view.updateBoard(model.getBoard());
        }

        // to make the shadows
        else{
            if((hasPiece = model.hasPiece(row, col)) == playerPieceType.getPieceType())
                lastChosenPiece = playerPieceType;

            else if(hasPiece == playerKingType.getPieceType())
                lastChosenPiece = playerKingType;

            else
                return;

            model.removeAllPieces(PieceType.SHADOW);
            moves = model.getAllMoves(lastChosenPiece, row, col, hasToEat, true);
            for (String move : moves.keySet()) {
                model.addPiece(PieceType.SHADOW, Integer.parseInt(move.split(",")[0]), Integer.parseInt(move.split(",")[1]));
            }

            view.updateBoard(model.getBoard());
            view.changePieceColor(lastChosenPiece.getPieceType(), row, col, lastChosenPiece.getPieceColor() == PieceType.WHITEPIECE ? "#696565" : "#6e2525");

        }

    }

    public void aiWon(){
        view.stopTimer(0);
        view.stopTimer(1);
        view.removeButton(0);
        view.removeButton(1);
        view.removeButton(2);
        view.removeButton(3);
        view.askTwoAnswers("AI won. Do you want to restart the game?", "Yes", "No", "restart", true);
    }
    public void playerWon(){
        view.stopTimer(0);
        view.stopTimer(1);
        view.removeButton(0);
        view.removeButton(1);
        view.removeButton(2);
        view.removeButton(3);
        view.askTwoAnswers("Player " + (player % 2 + 1) + " Won. Do you want to restart the game?", "Yes", "No", "restart", true);
    }

//    public void enemyWon(){
//
//    }

    public void playerTie(){
        view.stopTimer(0);
        view.stopTimer(1);
        view.removeButton(0);
        view.removeButton(1);
        view.removeButton(2);
        view.removeButton(3);
        view.askTwoAnswers("Tie. Do you want to restart the game?", "Yes", "No", "restart", true);
    }

//    public void enemyTie(){
//
//    }

    public void answer(String question, String ans, String questionType){
        if(questionType.equals("playerOrAI")){
            if(ans.equals("Player")){
                gameMode = GameMode.playerVsPlayer;

            }

            else if(ans.equals("AI")){
                gameMode = GameMode.playerVsAi;
            }

            initializeGame();
        }

        else if(questionType.equals("restart")){
            if(ans.equals("Yes")){
                game();
//                view.setTimer(100, 1);
            }

            else{
                view.quit();
            }
        }

        else if(question.equals("See AI Move")){
            playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
            playerKingType = new King(playerPieceType.getDifferentType());

            hasToEat = model.canEat(playerPieceType);

            aiMove = model.getAIMove(playerPieceType);
            while(aiMove == null){
                System.out.println("asus");
                aiMove = model.getAIMove(playerPieceType);
            }

            int[] aiFromCord = aiMove.getPieceFromCord();
            int[] aiToCord = aiMove.getPieceToCord();

            if((hasPiece = model.hasPiece(aiFromCord[0], aiFromCord[1])) == playerPieceType.getPieceType())
                lastChosenPiece = playerPieceType;

            else if(hasPiece == playerKingType.getPieceType())
                lastChosenPiece = playerKingType;

            else
                return;

            model.removeAllPieces(PieceType.SHADOW);
            moves = model.getAllMoves(lastChosenPiece, aiFromCord[0], aiFromCord[1], hasToEat, true);
            model.addPiece(PieceType.SHADOW, aiToCord[0], aiToCord[1]);

            view.updateBoard(model.getBoard());
            view.changePieceColor(lastChosenPiece.getPieceType(), aiFromCord[0], aiFromCord[1], lastChosenPiece.getPieceColor() == PieceType.WHITEPIECE ? "#696565" : "#6e2525");
        }

        else if(question.equals("Resign")){
            player++;

            if(gameMode == GameMode.playerVsAi && player % 2 == 1)
                aiWon();
            else
                playerWon();
        }
    }

    public void keyPressed(String key){
        if(key.equals("Right")){
            changePositions(PositionType.forward);
        }

        else if(key.equals("Left")){
            changePositions(PositionType.backward);
        }
    }

    public void changePositions(PositionType positionType){
        Model position = null;
        if(positionType == PositionType.forward && !positionsAfter.isEmpty()){
            positionsBefore.add(positionsAfter.pop());
            position = positionsBefore.peek();
        }

        else if(positionType == PositionType.backward && positionsBefore.size() > 1) {
            positionsAfter.add(positionsBefore.pop());
            if(!positionsBefore.isEmpty())
                position = positionsBefore.peek();
        }

        if(position != null)
            view.updateBoard(position.getBoard());
    }

    public void updateTimer(int time){
        int[] convertedTime = convertToTime(TIMER_DURATION - time);

        if(player % 2 == 0)
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 520, false, 4); // bottom player

        else
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 120, false, 5); // top player

        if(TIMER_DURATION - time <= 0) {
            player++;
            playerWon();
        }

        timeRemained[player % 2] = time;
    }

    public void initializeGame(){
        player = 0;
        moves = new HashMap<>();
        hasToEat = false;
        timeRemained[0] = timeRemained[1] = 0;
        view.initializeBoard();
        view.updateBoard(model.initializeBoard());
        int[] convertedTime = convertToTime(TIMER_DURATION);

        view.message("player 1", 880, 50, false, 0); // top player number message
        view.message("player 2", 880, 450, false, 1); // bottom player number message

        view.message("Timer: ", 850, 120, true, 2); // top timer
        view.message("Timer: ", 850, 520, true, 3); // bottom timer

        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 520, false, 4); // bottom player time message
        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 120, false, 5); // top player time message

        view.addButton("Resign", 870, 600, 0); // bottom resign button

        positionsBefore = new Stack<>();
        positionsAfter = new Stack<>();
        //view.addButton("Resign", 870, 200, 1); // top resign button

       // view.removeButton(1);

        view.addButton("See AI Move", 812, 700, 2); // bottom see ai move button
       // view.addButton("See AI Move", 812, 300, 3); // top see ai move button

       // view.removeButton(3);

        view.setTimer(timeRemained[0], TIMER_DURATION - timeRemained[0], 0);
        positionsBefore.add(model.clone());
    }
    // returns time in minutes and seconds
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
