package org.example.checkersfinalproject;

import java.util.*;

public class AI {
//    private final int pieceEvaluation = 1;
//    private final int kingEvaluation = 2;
//    private int numberOfPieces;
//    private int numberOfEnemyPieces;
//    private int numberOfKings;
//    private int numberOfEnemyKings;
//    private boolean isWhite;
//   // private BitBoard bitBoard;
//    private Model model;
//    private String[][] board;
//    private int gameState;
    private boolean isFirst;
    private Set<Move> defaultMoves ;
    public int numberOfRecurtiosn;
    public static int maxNumberOfRecurtiosn;
    private Set<Move> asus;
    //private PieceType pieceColor;
    public AI(AI other){
        isFirst = other.isFirst;
        defaultMoves = other.defaultMoves;
        numberOfRecurtiosn = other.numberOfRecurtiosn;
        asus = new HashSet<>(other.asus);
    }

    public AI(){
        asus = new HashSet<>();
    }

    public void restart(){

    }
//    public String[][] getPieces(PieceType piece){
//   //     return bitBoard.convertToMatrix();
//    }
    public boolean isSidePiece(int[] piece){
        return piece[1] == 0 || piece[1] == 7;
    }
    public Move chooseMove(Piece pieceType, Model model, boolean isResponse, AIDifficulty level){
        isFirst = true;
        defaultMoves = getEatings(pieceType, model);
        numberOfRecurtiosn = 0;
        Move move = takeMove(pieceType, model, isResponse, new HashMap<>(), level);
        System.out.println(numberOfRecurtiosn);
        if(numberOfRecurtiosn > maxNumberOfRecurtiosn)
            maxNumberOfRecurtiosn = numberOfRecurtiosn;
        System.out.printf("Max number of recursions: %d\n", maxNumberOfRecurtiosn);
        return move;
    }
    private Move takeMove(Piece pieceType, Model model, boolean isResponse, HashMap<String, Object> viewdPositions, AIDifficulty level){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        HashMap<int[], String> possibleMoves;
        int evaluation = 0;
        Move bestMove;
        HashMap<String, long[]> eatingPathPointer;
        hasToEat = newModel.canEat(pieceType);
        Piece kingType = new King(pieceType.getDifferentType());
        long pieces = board.getPieces(pieceType.getPieceType());
        long kings = board.getPieces(kingType.getPieceType());
        int[] leftestPiece;

        if(!isFirst)
            moves = getEatings(pieceType, model);

        else{
            isFirst = false;
            moves = defaultMoves;
        }

        int maxEatings = 0;
        int currentEatings = 0;
        for (Move move : moves){
            if(defaultMoves.contains(move)){
                int a = 0;
                a++;
            }
            Piece newPiece;
            if(move.getPiece() == PieceType.WHITEKING || move.getPiece() == PieceType.REDKING)
                newPiece = new King(move.getPiece());
            else
                newPiece = new Piece(move.getPiece());

            Model evalModel = newModel.clone();
            HashMap<String, String> allMoves = evalModel.getAllMoves(newPiece, move.getPieceFromCord()[0], move.getPieceFromCord()[1], hasToEat, true);
            evalModel.makeMove(newPiece, move.getPieceToCord()[0], move.getPieceToCord()[1], allMoves);
            if(!viewdPositions.containsKey(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDKING))){

                viewdPositions.put(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDKING), null);

                evaluate(newPiece, move, evalModel, isResponse, viewdPositions, level);
            }

            int a = 1;
            a++;
            //currentEatings = countEatings(move);
        }



        bestMove = null;
        if(moves == defaultMoves){
            int a = 0;
            a++;
        }

        for (Move move : moves){
            if(bestMove == null || move.getEvaluation() > bestMove.getEvaluation())
                bestMove = move;
        }

        asus.addAll(moves);
        numberOfRecurtiosn++;
        return bestMove;
    }

    public void evaluate(Piece pieceType, Move move, Model model, boolean isResponse, HashMap<String, Object> viewdPositions, AIDifficulty level){
        Piece newPiece = pieceType;
        boolean enemyCanEat = false;
        model.getEatingPathPointer().clear();

        // checks if the move ends the game
        if(model.hasWon(pieceType)) {
            move.setEvaluation(Integer.MAX_VALUE);
            return;
        }

        // check if there is a enemy that could eat this piece after the move
        model.canEat(new Piece(pieceType.getEnemyPieceType()));
        for(Map.Entry<String, long[]> eatingCord : model.getEatingPathPointer().entrySet()){
            int[] pieceCords = model.getBitBoard().convertToCordinates(eatingCord.getValue()[0]);
            int[] kingCords = model.getBitBoard().convertToCordinates(eatingCord.getValue()[1]);
            if(pieceCords[0] == move.getPieceToCord()[0] && pieceCords[1] == move.getPieceToCord()[1] ||
                    kingCords[0] == move.getPieceToCord()[0] && kingCords[1] == move.getPieceToCord()[1])
                enemyCanEat = true;
        }

        // check if will be a king
        if(move.becomesAKing())
            move.addEvaluation(3);

        if(!move.willEat() && !enemyCanEat){
            int[] closestEnemy = getClosestEnemy(pieceType, model, move.getPieceFromCord()[0],  move.getPieceFromCord()[1]);
            if((Math.abs(closestEnemy[0] - move.getPieceToCord()[0]) < Math.abs(closestEnemy[0] - move.getPieceFromCord()[0])) &&
                    (Math.abs(closestEnemy[1] - move.getPieceToCord()[1]) < Math.abs(closestEnemy[1] - move.getPieceFromCord()[1])))
                move.addEvaluation(0.5F);
        }

        // checks if the piece will not move toward the left and right edges (to control more of the center)
        if(move.getPieceToCord()[1] == 0 || move.getPieceToCord()[1] == 7)
            move.subtractEvaluation(0.5F);



        switch (pieceType.getPieceType()){
            case WHITEPIECE:
                // checks if the piece is essential (if on the first row of the player)
                if((move.getPieceFromCord()[0] == 7) && (move.getPieceFromCord()[1] == 2 || move.getPieceFromCord()[1] == 6))
                    move.subtractEvaluation(1.5F);
                // checks if close to be king
                //else if(move.getPieceFromCord()[0] == 2)
                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    move.addEvaluation(1);
                break;

            case REDPIECE:
                // checks if the piece is essential (if on the first row of the player)
                if((move.getPieceFromCord()[0] == 0) && (move.getPieceFromCord()[1] == 1 || move.getPieceFromCord()[1] == 5))
                    move.subtractEvaluation(1.5F);
                // checks if close to be king
                //else if(move.getPieceFromCord()[0] == 5)
                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    move.addEvaluation(1);
                break;

            case WHITEKING:
                break;

            case REDKING:
                break;
        }

        int a = 0;
        a++;

        // checks counter-attack (only if not the easiest level)
        if((level == AIDifficulty.HARDEST && (enemyCanEat || !isResponse)) || (level == AIDifficulty.MEDIUM && enemyCanEat)){
            Model newModel = model.clone();
            Piece piece = new Piece(pieceType.getEnemyPieceType());
            Move enemyBestMove = takeMove(new Piece(pieceType.getEnemyPieceType()), model.clone(), true, viewdPositions, level);
            move.subtractEvaluation(enemyBestMove.getEvaluation());
        }

        a++;
//        // check if will be a king
//        if((pieceType.getPieceType() == PieceType.WHITEPIECE && move.getPieceToCord()[0] == 0) ||
//                (pieceType.getPieceType() == PieceType.REDPIECE && move.getPieceToCord()[0] == 7)){
//            newPiece = new King(pieceType.getDifferentType());
//            move.addEvaluation(2);
//        }

//        // check if still can continue to eat
//        model.getEatings(pieceType, move.getPieceFromCord()[0], move.getPieceFromCord()[1], 0, 0, 0);
//        if(!model.getEatingPathPointer().isEmpty()){
//            for(String cords : model.getEatingPathPointer().keySet()){
//                model.makeMove(pieceType, Integer.parseInt(cords.split(",")[0]), Integer.parseInt(cords.split(",")[1]));
//                evaluate(pieceType, move, model.clone());
//            }
//        }

    }

    public Set<Move> getEatings(Piece pieceType, Model model){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        HashMap<String, String> possibleMoves;
        int evaluation = 0;
        hasToEat = newModel.canEat(pieceType);
        Piece kingType = new King(pieceType.getDifferentType());
        long pieces = board.getPieces(pieceType.getPieceType());
        long kings = board.getPieces(kingType.getPieceType());
        int[] leftestPiece;
        int closestEnemyDistance = Integer.MAX_VALUE;

        while ((leftestPiece = board.getFirstPiece(pieces))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(pieceType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, String> move : possibleMoves.entrySet()) {
                    evaluation = Integer.parseInt(move.getValue().split(",")[0]);
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, Boolean.parseBoolean(move.getValue().split(",")[1]), pieceType.getPieceType(), Boolean.parseBoolean(move.getValue().split(",")[2])));
                }
            }

            pieces = board.removeFirstPiece(pieces);
            evaluation = 0;
        }

        while ((leftestPiece = board.getFirstPiece(kings))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(kingType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, String> move : possibleMoves.entrySet()) {
                    evaluation = Integer.parseInt(move.getValue().split(",")[0]);
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, Boolean.parseBoolean(move.getValue().split(",")[1]), kingType.getPieceType(), Boolean.parseBoolean(move.getValue().split(",")[2])));
                }

//                for (int[] cord : possibleMoves) {
//                    moves.add(new Move(evaluation, leftestPiece, cord));
//                }
            }
            //players.removeFirstPiece(piece);
            kings = board.removeFirstPiece(kings);
            evaluation = 0;
        }

        return moves;
    }

    public int[] getClosestEnemy(Piece pieceType, Model model, int row, int col){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        HashMap<String, String> possibleMoves;
        int evaluation = 0;
        hasToEat = newModel.canEat(pieceType);
        Piece enemyKingType = new King(pieceType.getEnemyKingType());
        long enemyPieces = board.getPieces(pieceType.getEnemyPieceType());
        long enemyKings = board.getPieces(enemyKingType.getPieceType());
        int[] leftestPiece;
        double closestEnemyDistance = Double.MAX_VALUE;
        int[] closestEnemyCords = new int[2];
        while ((leftestPiece = board.getFirstPiece(enemyPieces))[0] != -1) {
            double distance = Math.sqrt(Math.pow(leftestPiece[0] - row, 2) + Math.pow(leftestPiece[1] - col, 2));
            if(distance < closestEnemyDistance){
                closestEnemyCords = leftestPiece;
                closestEnemyDistance = distance;
            }


            enemyPieces = board.removeFirstPiece(enemyPieces);
        }

        while ((leftestPiece = board.getFirstPiece(enemyKings))[0] != -1) {
            double distance = Math.sqrt(Math.pow(leftestPiece[0] - row, 2) + Math.pow(leftestPiece[1] - col, 2));
            if(distance < closestEnemyDistance){
                closestEnemyCords = leftestPiece;
                closestEnemyDistance = distance;
            }

            //players.removeFirstPiece(piece);
            enemyKings = board.removeFirstPiece(enemyKings);
        }

        return closestEnemyCords;
    }

    public AI clone(){
        return new AI(this);
    }
}
