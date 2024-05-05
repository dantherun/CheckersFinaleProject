package org.example.checkersfinalproject;

import java.io.File;
import java.util.*;

public class AI {

    private boolean isFirst; // checks if this is the first time that the ai checks the board position

    public int numberOfRecursion;// counts the number of recursions that the evaluation function did

    public static int maxNumberOfRecursion; // the maximum number of recursions that the evaluation function did
    private boolean openingStage; // checks if the game is in the opening stage

    public static HashMap<String, Move> openingList = new HashMap<>(); // the list of the openings of the opponent
    static {
        try{
            Model model = new Model();
            model.initializeBoard();
            File openingListFile = new File("src/main/java/org/example/checkersfinalproject/openings.txt");
            Scanner scanner = new Scanner(openingListFile);
            //float evaluation, int[] pieceFromCord, int[] pieceToCord, boolean becomesAKing, PieceType piece, boolean willEat

            String line = scanner.nextLine();

            int[] aiFromCords = new int[]{Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1])};
            int[] aiToCords = new int[]{Integer.parseInt(line.split(",")[2]), Integer.parseInt(line.split(",")[3])};

            openingList.put(11163050L + "," + 6172839697753047040L, new Move(aiFromCords, aiToCords));
            model.makeMove(new Piece(PieceType.WHITEPIECE), aiFromCords[0], aiFromCords[1], aiToCords[0], aiToCords[1], false);

            String[] moveAndResponse = scanner.nextLine().split(";");

            addOpening(moveAndResponse, new Piece(PieceType.REDPIECE), model);

            // add every opponent's opening move to the opening list
            while(scanner.hasNextLine()){
                model.initializeBoard();
                moveAndResponse = scanner.nextLine().split(";");

                addOpening(moveAndResponse, new Piece(PieceType.WHITEPIECE), model);
            }

            scanner.close();
        }

        catch (Exception e){
            openingList = null;
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addOpening(String[] moveAndResponse, Piece piece, Model model){
        for (int i = 0; i < moveAndResponse.length; i+=2) {
            String info = moveAndResponse[i];
            int[] enemyFromCords = new int[]{Integer.parseInt(info.split(",")[0]), Integer.parseInt(info.split(",")[1])};
            int[] enemyToCords = new int[]{Integer.parseInt(info.split(",")[2]), Integer.parseInt(info.split(",")[3])};

            info = moveAndResponse[i + 1];
            int[] aiFromCords = new int[]{Integer.parseInt(info.split(",")[0]), Integer.parseInt(info.split(",")[1])};
            int[] aiToCords = new int[]{Integer.parseInt(info.split(",")[2]), Integer.parseInt(info.split(",")[3])};

            model.makeMove(piece, enemyFromCords[0], enemyFromCords[1], enemyToCords[0], enemyToCords[1], false);
            openingList.put(model.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," + model.getBitBoard().getPieces(PieceType.REDPIECE), new Move(aiFromCords, aiToCords));
            model.makeMove(new Piece(piece.getEnemyPieceType()), aiFromCords[0], aiFromCords[1], aiToCords[0], aiToCords[1], false);
        }
    }
    public AI(AI other){
        isFirst = other.isFirst;
        numberOfRecursion = other.numberOfRecursion;
    }

    public AI(){
    }

    public void restart(){

    }

    public void newGame(){
        openingStage = true;
    }

    public boolean isSidePiece(int[] piece){
        return piece[1] == 0 || piece[1] == 7;
    }

    // this function calls the takeMove function and returns the best move.
    // It also checks if the game is in the opening stage and if it is, it will return the opening move
    public Move chooseMove(Piece pieceType, Model model, boolean isResponse, AIDifficulty level){
        isFirst = true;
        numberOfRecursion = 0;


        // checks if the game is in the opening stage
        if(openingStage){
            // checks if the opening list has the current position
            if(openingList.containsKey(model.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," + model.getBitBoard().getPieces(PieceType.REDPIECE)))
                // returns the opening move
                return openingList.get(model.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," + model.getBitBoard().getPieces(PieceType.REDPIECE));

            // if the opening list does not have the current position, then the game is not in the opening stage
            else
                openingStage = false;
        }

        Move move = takeMove(pieceType, model, isResponse, new HashMap<>(), level);

        // --------------------------------- this part for debugging
        System.out.println(numberOfRecursion);
        if(numberOfRecursion > maxNumberOfRecursion)
            maxNumberOfRecursion = numberOfRecursion;
        System.out.printf("Max number of recursions: %d\n", maxNumberOfRecursion);
        // --------------------------------- end of debugging part

        return move;
    }

    /**
     * This function is the main function of the AI, it takes the best move that the AI can do
     * @param pieceType the type of the piece that the AI will check
     * @param model the current model of the game
     * @param isResponse if the move is a response to the player's move
     * @param viewedPositions the positions that the AI already viewed
     * @param level the difficulty level of the AI
     * @return the best move that the AI can do
     */
    private Move takeMove(Piece pieceType, Model model, boolean isResponse, HashMap<String, Object> viewedPositions, AIDifficulty level){
        boolean hasToEat;
        Model newModel = model.clone(); // makes new model so that it does not change the original model
        Set<Move> moves; // will have the possible moves for all the pieces of that pieceType
        Move bestMove; // will have the best move that the AI can do
        hasToEat = newModel.canEat(pieceType); // checks if there is a piece that can eat
        moves = getMoves(pieceType, model); // gets all the possible eatings for that pieceType

        // iterates over all the possible moves
        for (Move move : moves){
            Piece newPiece;

            // checks if the piece  isa king
            if(move.getPiece() == PieceType.WHITEKING || move.getPiece() == PieceType.REDKING)
                newPiece = new King(move.getPiece()); // if it is a king, then it will be a king
            else
                newPiece = new Piece(move.getPiece()); // if it is not a king, then it will be a piece

            Model evalModel = newModel.clone(); // makes a new model so that it does not change the model

            // for every piece that can move it takes all the possible moves into a HashMap
            HashMap<String, String> allMoves = evalModel.getAllMoves(newPiece, move.getPieceFromCord()[0], move.getPieceFromCord()[1], hasToEat, true);

            // makes the move
            evalModel.makeMove(newPiece, move.getPieceToCord()[0], move.getPieceToCord()[1], allMoves);
            if(!viewedPositions.containsKey(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDKING))){

                viewedPositions.put(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDKING), null);

                evaluate(newPiece, move, evalModel, isResponse, viewedPositions, level);
            }
        }

        bestMove = null;

        for (Move move : moves){
            if(bestMove == null || move.getEvaluation() > bestMove.getEvaluation())
                bestMove = move;
        }

        numberOfRecursion++;
        return bestMove;
    }

    public void evaluate(Piece pieceType, Move move, Model model, boolean isResponse, HashMap<String, Object> viewedPositions, AIDifficulty level){
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
            move.addEvaluation(3.5F);

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

                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    move.addEvaluation(1);
                break;

            case REDPIECE:
                // checks if the piece is essential (if on the first row of the player)
                if((move.getPieceFromCord()[0] == 0) && (move.getPieceFromCord()[1] == 1 || move.getPieceFromCord()[1] == 5))
                    move.subtractEvaluation(1.5F);

                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    move.addEvaluation(1);
                break;
        }

        // checks counter-attack (only if not the easiest level)
        if((level == AIDifficulty.HARDEST && (enemyCanEat || !isResponse)) || (level == AIDifficulty.MEDIUM && enemyCanEat)){
            Move enemyBestMove = takeMove(new Piece(pieceType.getEnemyPieceType()), model.clone(), true, viewedPositions, level);
            move.subtractEvaluation(enemyBestMove.getEvaluation());
        }

    }

    /**
     * This function gets all the possible moves for a pieceType
     * @param pieceType the type of the piece that the AI will check
     * @param model the current model of the game
     * @return a set of all the possible moves for that pieceType
     */
    private Set<Move> getMoves(Piece pieceType, Model model){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        int evaluation;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        HashMap<String, String> possibleMoves;
        hasToEat = newModel.canEat(pieceType);
        Piece kingType = new King(pieceType.getDifferentType());
        long pieces = board.getPieces(pieceType.getPieceType());
        long kings = board.getPieces(kingType.getPieceType());
        int[] leftestPiece;

        while ((leftestPiece = board.getFirstPiece(pieces))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(pieceType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, String> move : possibleMoves.entrySet()) {
                    evaluation = Integer.parseInt(move.getValue().split(",")[0]);
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, Boolean.parseBoolean(move.getValue().split(",")[1]), pieceType.getPieceType(), Boolean.parseBoolean(move.getValue().split(",")[2])));
                }
            }

            pieces = board.removeFirstPiece(pieces);
        }

        while ((leftestPiece = board.getFirstPiece(kings))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(kingType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, String> move : possibleMoves.entrySet()) {
                    evaluation = Integer.parseInt(move.getValue().split(",")[0]);
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, Boolean.parseBoolean(move.getValue().split(",")[1]), kingType.getPieceType(), Boolean.parseBoolean(move.getValue().split(",")[2])));
                }
            }
            //players.removeFirstPiece(piece);
            kings = board.removeFirstPiece(kings);
        }

        return moves;
    }

    public int[] getClosestEnemy(Piece pieceType, Model model, int row, int col){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
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
