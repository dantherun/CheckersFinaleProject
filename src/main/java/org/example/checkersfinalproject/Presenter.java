package org.example.checkersfinalproject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Presenter {
    public int TIMER_DURATION = 50000;
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
    private HashMap<int[], String> moves;
    private GameMode gameMode;
    private Piece playerPieceType;
    private King playerKingType;
    private PieceType hasPiece;
    private Move aiMove;
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
        playerPieceType = player % 2 == 0 ? new Piece(PieceType.WHITEPIECE) : new Piece(PieceType.REDPIECE);
        playerKingType = new King(playerPieceType.getDifferentType());

        // to make the move
        if(model.hasPiece(row, col) == PieceType.SHADOW) {
         //   if(model.canBeKing(playerType, row)){
               // model.makeKing(playerType, row, col);
               // playerType = player % 2 == 0 ? PieceType.WHITEKING : PieceType.REDKING;
          //  }

        //    else{
                model.makeMove(lastChosenPiece, row, col);
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
                // preparing for the next player's move
                player++;
                playerPieceType = new Piece(playerPieceType.getEnemyPieceType());
                playerKingType = new King(playerPieceType.getDifferentType());

                hasToEat = model.canEat(playerPieceType);
                if(gameMode == GameMode.playerVsPlayer)
                    view.setTimer(timeRemained[player % 2], TIMER_DURATION - timeRemained[player % 2], player % 2);

                if(gameMode == GameMode.playerVsAi){
                    aiMove = model.getAIMove(playerPieceType);
                    while(aiMove == null){
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

                    model.makeMove(lastChosenPiece, aiToCord[0], aiToCord[1]);

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
            for (int[] move : moves.keySet()) {
                model.addPiece(PieceType.SHADOW, move[0], move[1]);
            }

            view.updateBoard(model.getBoard());
            view.changePieceColor(lastChosenPiece.getPieceType(), row, col, lastChosenPiece.getPieceColor() == PieceType.WHITEPIECE ? "#696565" : "#6e2525");

        }
        /*
        // to make the shadows
        else if((hasPiece = model.hasPiece(row, col)) == playerPieceType.getPieceType()){
            lastChosenPiece = playerPieceType;
            model.removeAllPieces(PieceType.SHADOW);
            moves = model.getAllMoves(playerPieceType, row, col, hasToEat, true);
            for (int[] move : moves) {
                model.addPiece(PieceType.SHADOW, move[0], move[1]);
                //view.addPiece(PieceType.SHADOW, move[0], move[1]);
            }
        }

        // to make the shadows
        else if(hasPiece == playerKingType.getPieceType()){
            lastChosenPiece = playerKingType;
            model.removeAllPieces(PieceType.SHADOW);
            moves = model.getAllMoves(playerKingType, row, col, hasToEat, true);
            for (int[] move : moves) {
                model.addPiece(PieceType.SHADOW, move[0], move[1]);
                //view.addPiece(PieceType.SHADOW, move[0], move[1]);
            }
        }
         */


    }

    public void aiWon(){
        view.stopTimer(0);
        view.stopTimer(1);
        view.askTwoAnswers("AI won. Do you want to restart the game?", "Yes", "No", "restart", true);
    }
    public void playerWon(){
        view.stopTimer(0);
        view.stopTimer(1);
        view.askTwoAnswers("Player " + (player % 2 + 1) + " Won. Do you want to restart the game?", "Yes", "No", "restart", true);
    }

//    public void enemyWon(){
//
//    }

    public void playerTie(){
        view.stopTimer(0);
        view.stopTimer(1);
        System.out.println("tie");
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
                initializeGame();
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
    }

    public void updateTimer(int time){
        int[] convertedTime = convertToTime(TIMER_DURATION - time);

        if(player % 2 == 0)
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 600, false, 4); // bottom player

        else
            view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 200, false, 5); // top player

        if(TIMER_DURATION - time <= 0) {
            player++;
            playerWon();
        }

        timeRemained[player % 2] = time;
    }

    public void initializeGame(){
        player = 0;
        moves = new HashMap<int[], String>();
        hasToEat = false;
        timeRemained[0] = timeRemained[1] = 0;
        view.initializeBoard();
        view.updateBoard(model.initializeBoard());
        int[] convertedTime = convertToTime(TIMER_DURATION);

        view.message("player 1", 880, 50, false, 0); // bottom player number message
        view.message("player 2", 880, 450, false, 1); // top player number message

        view.message("Timer: ", 850, 200, true, 2); // bottom timer
        view.message("Timer: ", 850, 600, true, 3); // top timer

        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 600, false, 4); // bottom player time message
        view.message(String.format("%02d", convertedTime[0]) + ":" + String.format("%02d", convertedTime[1]), 980, 200, false, 5); // top player time message

        view.addButton("See AI Move", 812, 300);

        view.setTimer(timeRemained[0], TIMER_DURATION - timeRemained[0], 0);
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
