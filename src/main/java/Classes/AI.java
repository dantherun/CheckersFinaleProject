package Classes;

import Enums.AIDifficulty;
import Enums.PieceType;

import java.io.File;
import java.util.*;

public class AI {
    public static String filePath = "src/main/java/TextFiles/openings.txt";
    public int numberOfRecursion;// counts the number of recursions that the evaluation function did
    public static int maxNumberOfRecursion; // the maximum number of recursions that the evaluation function did
    private boolean openingStage; // checks if the game is in the opening stage

    // the list of the openings of the opponent
    public static HashMap<String, Move> openingList = new HashMap<>();
    static {
        try{
            // creates a new model
            Model model = new Model();

            // initializes the board
            model.initializeBoard();

            // opens the file that has the openings
            File openingListFile = new File(filePath);
            Scanner scanner = new Scanner(openingListFile);

            // reads the first line of the file that has the opening move of the AI (if the AI is the first player)
            String line = scanner.nextLine();

            // gets the cords of the opening move
            int[] aiFromCords = new int[]{Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1])};
            int[] aiToCords = new int[]{Integer.parseInt(line.split(",")[2]), Integer.parseInt(line.split(",")[3])};

            // puts the opening move in the opening list when the key is the current position of the board
            // and the value is the move that the AI will do
            openingList.put(11163050L + "," + 6172839697753047040L, new Move(aiFromCords, aiToCords));

            // makes the move so that the AI can get the opponent's opening move
            model.makeMove(new Piece(PieceType.WHITEPIECE), aiFromCords[0], aiFromCords[1], aiToCords[0], aiToCords[1], false);

            // gets the opponent's opening move after the AI's opening move
            String[] moveAndResponse = scanner.nextLine().split(";");

            // adds the entire opening from the beginning to the end
            addOpening(moveAndResponse, new Piece(PieceType.REDPIECE), model);

            // adds every opponent's opening move to the opening list
            while(scanner.hasNextLine()){
                // initializes the board
                model.initializeBoard();
                // gets the opponent's opening move and the AI's response
                moveAndResponse = scanner.nextLine().split(";");
                // adds the entire opening from the beginning to the end
                addOpening(moveAndResponse, new Piece(PieceType.WHITEPIECE), model);
            }
            // closes the scanner
            scanner.close();
        }

        // catches any exception that might occur and makes the opening list null
        catch (Exception e){
            openingList = null;
            System.out.println("Error: " + e.getMessage());
        }
    }


    /**
     * This function adds the opening to the opening list
     * @param moveAndResponse the opening move and the response of the AI
     * @param piece the piece of the enemy
     * @param model the current model of the game
     */
    public static void addOpening(String[] moveAndResponse, Piece piece, Model model){
        // iterates over the opening moves and the AI's responses
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

    // constructor of the clone
    public AI(AI other){
        numberOfRecursion = other.numberOfRecursion;
    }
    public AI(){}

    // this function makes the game in the opening stage
    public void newGame(){
        openingStage = true;
    }

    /**
     * This function chooses the move from the opening list or calls the takeMove function and returns the best move
     * @param pieceType the type of the piece that the AI will check
     * @param model the current model of the game
     * @param level the difficulty level of the AI
     * @return the best move that the AI can do
     */
    public Move chooseMove(Piece pieceType, Model model, AIDifficulty level){
        numberOfRecursion = 0;


        // checks if the game is in the opening stage
        if(openingStage){
            // checks if the opening list has the current position
            if(openingList != null && openingList.containsKey(model.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," + model.getBitBoard().getPieces(PieceType.REDPIECE)))
                // returns the opening move
                return openingList.get(model.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," + model.getBitBoard().getPieces(PieceType.REDPIECE));

            // if the opening list does not have the current position, then the game is not in the opening stage
            else
                openingStage = false;
        }

        Set<Move> moves = new HashSet<>();
        Move move = takeMove(pieceType, model, false, new HashMap<>(), level, moves);

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
    private Move takeMove(Piece pieceType, Model model, boolean isResponse, HashMap<String, Object> viewedPositions, AIDifficulty level, Set<Move> movesus){
        boolean hasToEat;
        Model newModel = model.clone(); // makes new model so that it does not change the original model
        Set<Move> moves; // will have the possible moves for all the pieces of that pieceType
        Move bestMove; // will have the best move that the AI can do
        hasToEat = newModel.needToEat(pieceType); // checks if there is a piece that can eat
        moves = getMoves(pieceType, model); // gets all the possible moves and eatings for that pieceType

        // iterates over all the possible moves
        for (Move move : moves){
            Piece newPiece;

            // checks if the piece is a king
            if(move.getPiece() == PieceType.WHITEKING || move.getPiece() == PieceType.REDKING)
                newPiece = new King(move.getPiece()); // if it is a king, then it will be a king
            else
                newPiece = new Piece(move.getPiece()); // if it is not a king, then it will be a piece

            Model evalModel = newModel.clone(); // makes a new model so that it does not change the model

            // for every piece that can move it takes all the possible moves into a HashMap
            //HashMap<String, String> allMoves = evalModel.getAllMoves(newPiece, move.getPieceFromCord()[0], move.getPieceFromCord()[1], hasToEat, true);

            // makes the move
            evalModel.makeMove(newPiece, move.getPieceFromCord()[0], move.getPieceFromCord()[1], move.getPieceToCord()[0], move.getPieceToCord()[1], move);

            // checks if the position is already viewed
            if(!viewedPositions.containsKey(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                    evalModel.getBitBoard().getPieces(PieceType.REDKING))){

                //if not viewed, then it will evaluate the position and add it to the viewed positions
                viewedPositions.put(evalModel.getBitBoard().getPieces(PieceType.WHITEPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.WHITEKING) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDPIECE) + "," +
                        evalModel.getBitBoard().getPieces(PieceType.REDKING), null);

                evaluate(newPiece, move, evalModel, isResponse, viewedPositions, level);
            }
        }

        bestMove = null;

        // iterates over all the possible moves and gets the best move
        for (Move move : moves){
            if(bestMove == null || move.getEvaluation() > bestMove.getEvaluation())
                bestMove = move;
        }

        numberOfRecursion++;

        if(movesus != null)
            movesus.addAll(moves);
        // returns the best move
        return bestMove;
    }

    /**
     * This function evaluates the move
     * @param pieceType the type of the piece that the AI will check
     * @param move the move that the AI will evaluate
     * @param model the current model of the game
     * @param isResponse if the move is a response to the player's move
     * @param viewedPositions the positions that the AI already viewed
     * @param level the difficulty level of the AI
     */
    private void evaluate(Piece pieceType, Move move, Model model, boolean isResponse, HashMap<String, Object> viewedPositions, AIDifficulty level){
        boolean enemyCanEat = false;
        boolean enemyCanBecomeKing = false;
        model.getEatingPathPointer().clear();

        // checks if the move ends the game
        if(model.hasWon(pieceType)) {
            move.setEvaluation(Integer.MAX_VALUE);
            return;
        }

        // check if there is a enemy that could eat this piece after the move
        model.needToEat(new Piece(pieceType.getEnemyPieceType()));
        for(Map.Entry<String, long[]> eatingCord : model.getEatingPathPointer().entrySet()){
            int[] pieceCords = model.getBitBoard().getFirstPiece(eatingCord.getValue()[0]);
            int[] kingCords = model.getBitBoard().getFirstPiece(eatingCord.getValue()[1]);
            if(pieceCords[0] == move.getPieceToCord()[0] && pieceCords[1] == move.getPieceToCord()[1] ||
                    kingCords[0] == move.getPieceToCord()[0] && kingCords[1] == move.getPieceToCord()[1])
                enemyCanEat = true;
        }

        // check if will be a king
        if(move.becomesAKing())
            move.addEvaluation(3.5F);

        // checks if the piece will eat
        if(!move.willEat() && !enemyCanEat){
            int[] closestEnemy = getClosestEnemy(pieceType, model, move.getPieceFromCord()[0],  move.getPieceFromCord()[1]);

            // checks if the piece will move toward the closest enemy
            if((Math.abs(closestEnemy[0] - move.getPieceToCord()[0]) < Math.abs(closestEnemy[0] - move.getPieceFromCord()[0])) &&
                    (Math.abs(closestEnemy[1] - move.getPieceToCord()[1]) < Math.abs(closestEnemy[1] - move.getPieceFromCord()[1])))
                // if the piece will move toward the closest enemy, then it will add 0.5 to the evaluation
                move.addEvaluation(0.5F);
        }

        // checks if the piece will not move toward the left and right edges (to control more of the center)
        if((move.getPieceToCord()[1] == 0 || move.getPieceToCord()[1] == 7) && !move.willEat())
            // if the piece will move toward the left or right edges, then it will subtract 0.5 from the evaluation
            move.subtractEvaluation(0.5F);

        switch (pieceType.getPieceType()){
            case PieceType.WHITEPIECE:
                // checks if the piece is essential (if on the first row of the player)
                if((move.getPieceFromCord()[0] == 7) && (move.getPieceFromCord()[1] == 2 || move.getPieceFromCord()[1] == 6) && !move.willEat())
                    // if the piece is essential, then it will subtract 1.5 from the evaluation
                    move.subtractEvaluation(1.5F);

                // if the enemy cannot eat and the move does not make the piece a king and the piece will not eat
                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    // then it will add 1 to the evaluation
                    move.addEvaluation(1F);
                break;

            case PieceType.REDPIECE:
                // checks if the piece is essential (if on the first row of the player)
                if((move.getPieceFromCord()[0] == 0) && (move.getPieceFromCord()[1] == 1 || move.getPieceFromCord()[1] == 5) && !move.willEat())
                    // if the piece is essential, then it will subtract 1.5 from the evaluation
                    move.subtractEvaluation(1.5F);

                // if the enemy cannot eat and the move does not make the piece a king and the piece will not eat
                if(!enemyCanEat && !move.becomesAKing() && !move.willEat())
                    // then it will add 1 to the evaluation
                    move.addEvaluation(1F);
                break;
        }

        // checks counter-attack and setting a trap (only if not the easiest level)
        if((level == AIDifficulty.HARDEST && (enemyCanEat || !isResponse)) || (level == AIDifficulty.MEDIUM && enemyCanEat)){
            // receives the best move of the enemy
            Move enemyBestMove = takeMove(new Piece(pieceType.getEnemyPieceType()), model.clone(), true, viewedPositions, level, null);
            // subtracts the evaluation of the enemy's best move from the evaluation of the current move
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
        float evaluation;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        HashMap<String, Move> possibleMoves;
        hasToEat = newModel.needToEat(pieceType);
        Piece kingType = new King(pieceType.getDifferentType());
        long pieces = board.getPieces(pieceType.getPieceType());
        long kings = board.getPieces(kingType.getPieceType());
        int[] leftestPiece;

        // iterates over all the pieces and adds all the possible moves to the moves set
        while ((leftestPiece = board.getFirstPiece(pieces))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(pieceType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, Move> move : possibleMoves.entrySet()) {
                    evaluation = move.getValue().getEvaluation();
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, move.getValue().becomesAKing(), pieceType.getPieceType(), move.getValue().willEat()));
                }
            }

            pieces = board.removeFirstPiece(pieces);
        }

        // iterates over all the kings and adds all the possible moves to the moves set
        while ((leftestPiece = board.getFirstPiece(kings))[0] != -1) {
            if (!(possibleMoves = newModel.getAllMoves(kingType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (Map.Entry<String, Move> move : possibleMoves.entrySet()) {
                    evaluation = move.getValue().getEvaluation();
                    int[] pieceToCord = new int[]{Integer.parseInt(move.getKey().split(",")[0]), Integer.parseInt(move.getKey().split(",")[1])};
                    moves.add(new Move(evaluation, leftestPiece, pieceToCord, move.getValue().becomesAKing(), kingType.getPieceType(), move.getValue().willEat()));
                }
            }
            //players.removeFirstPiece(piece);
            kings = board.removeFirstPiece(kings);
        }

        return moves;
    }

    /**
     * This function gets the closest enemy to a piece
     * @param pieceType the type of the piece that the AI will check
     * @param model the current model of the game
     * @param row the row of the piece
     * @param col the column of the piece
     * @return the closest enemy to the piece
     */
    private int[] getClosestEnemy(Piece pieceType, Model model, int row, int col){
        BitBoard board = model.getBitBoard();
        Model newModel = model.clone();
        Piece enemyKingType = new King(pieceType.getEnemyKingType());
        long enemyPieces = board.getPieces(pieceType.getEnemyPieceType());
        long enemyKings = board.getPieces(enemyKingType.getPieceType());
        int[] leftestPiece;
        double closestEnemyDistance = Double.MAX_VALUE;
        int[] closestEnemyCords = new int[2];

        // iterates over all the enemy pieces and gets the closest enemy to the piece
        while ((leftestPiece = board.getFirstPiece(enemyPieces))[0] != -1) {
            double distance = Math.sqrt(Math.pow(leftestPiece[0] - row, 2) + Math.pow(leftestPiece[1] - col, 2));
            if(distance < closestEnemyDistance){
                closestEnemyCords = leftestPiece;
                closestEnemyDistance = distance;
            }


            enemyPieces = board.removeFirstPiece(enemyPieces);
        }

        // iterates over all the enemy kings and gets the closest enemy to the piece
        while ((leftestPiece = board.getFirstPiece(enemyKings))[0] != -1) {
            double distance = Math.sqrt(Math.pow(leftestPiece[0] - row, 2) + Math.pow(leftestPiece[1] - col, 2));
            if(distance < closestEnemyDistance){
                closestEnemyCords = leftestPiece;
                closestEnemyDistance = distance;
            }

            //players.removeFirstPiece(piece);
            enemyKings = board.removeFirstPiece(enemyKings);
        }

        // returns the closest enemy to the piece
        return closestEnemyCords;
    }

    /**
     * This function clones the AI
     * @return the clone of the AI
     */
    public AI clone(){
        return new AI(this);
    }
}
