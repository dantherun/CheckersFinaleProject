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
    public static int calculations;
    private Set<Move> asus;
    //private PieceType pieceColor;
    public AI(Model model, BitBoard bitBoard){
        asus = new HashSet<>();
        //this.model = model;
   //     this.bitBoard = bitBoard;
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
    public Move chooseMove(Piece pieceType, Model model, boolean isResponse){
        isFirst = true;
        defaultMoves = getEatings(pieceType, model);
        calculations = 0;
        Move move = takeMove(pieceType, model, isResponse, new HashMap<>());
        System.out.println(calculations);
        return move;
    }
    private Move takeMove(Piece pieceType, Model model, boolean isResponse, HashMap<String, Object> viewdPositions){
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

                evaluate(newPiece, move, evalModel, isResponse, viewdPositions);
            }

            int a = 1;
            a++;
            //currentEatings = countEatings(move);
        }



        bestMove = new Move(Integer.MIN_VALUE, null);
        if(moves == defaultMoves){
            int a = 0;
            a++;
        }
        for (Move move : moves){
            if(move.getEvaluation() > bestMove.getEvaluation())
                bestMove = move;
        }

        asus.addAll(moves);
        return bestMove;
    }

    public void evaluate(Piece pieceType, Move move, Model model, boolean isResponse, HashMap<String, Object> viewdPositions){
        Piece newPiece = pieceType;
        boolean enemyCanEat = false;
        model.getEatingPathPointer().clear();

        // checks if the move ends the game
        if(model.hasWon(pieceType)) {
            move.setEvaluation(Integer.MAX_VALUE);
            return;
        }

        // check if will be a king
        if(move.becomesAKing())
            move.addEvaluation(3);

        if(!move.willEat()){
            int[] closestEnemy = getClosestEnemy(pieceType, model, move.getPieceFromCord()[0],  move.getPieceFromCord()[1]);
            if((Math.abs(closestEnemy[0] - move.getPieceToCord()[0]) < Math.abs(closestEnemy[0] - move.getPieceFromCord()[0])) &&
                    (Math.abs(closestEnemy[1] - move.getPieceToCord()[1]) < Math.abs(closestEnemy[1] - move.getPieceFromCord()[1])))
                move.addEvaluation(0.5F);
        }

        // checks if the piece will not move toward the left and right edges (to control more of the center)
        if(move.getPieceToCord()[1] == 0 || move.getPieceToCord()[1] == 7)
            move.subtractEvaluation(1);

        model.canEat(new Piece(pieceType.getEnemyPieceType()));
        for(Map.Entry<String, long[]> eatingCord : model.getEatingPathPointer().entrySet()){
            int[] pieceCords = model.getBitBoard().convertToCordinates(eatingCord.getValue()[0]);
            int[] kingCords = model.getBitBoard().convertToCordinates(eatingCord.getValue()[1]);
            if(pieceCords[0] == move.getPieceToCord()[0] && pieceCords[1] == move.getPieceToCord()[1] ||
                    kingCords[0] == move.getPieceToCord()[0] && kingCords[1] == move.getPieceToCord()[1])
                enemyCanEat = true;
        }

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
        // checks counter-attack
        if(enemyCanEat || !isResponse){
            Model newModel = model.clone();
            Piece piece = new Piece(pieceType.getEnemyPieceType());
            Move enemyBestMove = takeMove(new Piece(pieceType.getEnemyPieceType()), model.clone(), true, viewdPositions);
            move.subtractEvaluation(enemyBestMove.getEvaluation());
        }

        calculations++;
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

//    public int countEatings(Move move){
//
//    }

//        else{
//
//        }

//        eatingPathPointer = newModel.getEatingPosibilities();
//        if(newModel.canEat(pieceColor)){
//            for(Map.Entry<String, long[]> cords : eatingPathPointer.entrySet()){
//                long[] cordinations = cords.getValue();
//                moves.add(new Move(1, ))
//
//                newModel.makeMove(pieceColor, cordinations[0], cordinations[1]);
//                evaluate(pieceColor, newModel.clone(), 1);
//            }
//            //return evaluateNoMustKilling(pieceColor, model);
//        }
//
//        else
//            evaluate(pieceColor, newModel, 0);
//
//        Move bestMove = new Move(Integer.MIN_VALUE, new int[]{-1}, new int[]{-1});
//      //  board = bitBoard.convertToMatrix().clone();
//        Set<Move> moves = evaluate(pieceColor, newModel.clone(), 0);
//        for(Move move : moves){
//            if(move.getEvaluation() > bestMove.getEvaluation()){
//                bestMove = move.clone();
//            }
//        }
//
//        return bestMove;
//        //int evaluation = 0;
//
//    }
//
////    public Set<int[]> getEatings(PieceType pieceColor, ){
////
////    }
//
////    public Set<int[]> getEatingPieces(PieceType piece, int row, int col, Set<int[]>  ableToEatPieces){
////        if(piece == PieceType.WHITEPIECE){
////            if(col > 1 && row > 1 && model.canEat(piece, row, col, DirectionVector.northwest) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////
////            if(col < 6 && row > 1 && model.canEat(piece, row, col, DirectionVector.northeast) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////        }
////
////        else if(piece == PieceType.REDPIECE){
////            if(col > 1 && row < 6 && model.canEat(piece, row, col, DirectionVector.southwest) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////
////            if(col < 6 && row < 6 && model.canEat(piece, row, col, DirectionVector.southeast) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////        }
////
////        else{
////            if(col > 1 && row > 1 && model.canEat(piece, row, col, DirectionVector.northwest) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////
////            if(col < 6 && row > 1 && model.canEat(piece, row, col, DirectionVector.northeast) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////
////            if(col > 1 && row < 6 && model.canEat(piece, row, col, DirectionVector.southwest) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////
////            if(col < 6 && row < 6 && model.canEat(piece, row, col, DirectionVector.southeast) != PieceType.None){
////                ableToEatPieces.add(new int[]{row, col});
////            }
////        }
////
////        return ableToEatPieces;
////    }
//
//    public Set<Move> evaluate(PieceType pieceColor, Model model, int evaluation){
//        Set<Move> moves = null;
////        boolean hasToEat = model.canEat(pieceColor);
////        moves = model.getAllMoves(player % 2 == 0 ? PieceType.WHITEPIECE : PieceType.REDPIECE, row, col, hasToEat);
//        return moves;
//    }




//    public int evaluateCurrentBoard(PieceType pieceType){
//        PieceType pieceKingType = pieceColor == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
//
//        Set<int[]> aiPieces = bitBoard.getPiecesList(pieceType);
//        aiPieces.addAll(bitBoard.getPiecesList(pieceKingType));
//    }
//    public int evaluate(PieceType pieceColor, Model model){
//        int evaluation = 0;
//        int[] leftestPiece = bitBoard.getFirstPiece(bitBoard.getPieces(pieceColor));
//        Set<int[]> ableToEatPieces = new HashSet<>();
//        while(leftestPiece[0] != -1){
//            getEatingPieces(pieceColor, leftestPiece[0], leftestPiece[1], ableToEatPieces);
//        }
//
//        if(!ableToEatPieces.isEmpty()){
//            for(int[] eatingPiece : ableToEatPieces){
//                BitBoard newBoard = evaluationBoard.clone();
//                newBoard.
//                        evaluation += 1 + evaluateMoves(pieceColor, evaluationBoard);
//            }
//        }
//
//    //        if(isSidePiece(leftestPiece)){
//    //
//    //        }
//        return evaluation;
//    }
//    public int evaluateMoves(PieceType pieceColor, BitBoard evaluationBoard){
//        int evaluation = 0;
//        int[] leftestPiece = bitBoard.getFirstPiece(bitBoard.getPieces(pieceColor));
//        Set<int[]> ableToEatPieces = new HashSet<>();
//        while(leftestPiece[0] != -1){
//            getEatingPieces(pieceColor, leftestPiece[0], leftestPiece[1], ableToEatPieces);
//        }
//
//        if(!ableToEatPieces.isEmpty()){
//            for(int[] eatingPiece : ableToEatPieces){
//                BitBoard newBoard = evaluationBoard.clone();
//                newBoard.
//                evaluation += 1 + evaluateMoves(pieceColor, evaluationBoard);
//            }
//        }
//
////        if(isSidePiece(leftestPiece)){
////
////        }
//        return evaluation;
//    }
//    public Set<Move> evaluate(PieceType pieceColor, BitBoard evaluationBoard){
//        evaluationBoard.removeAllPieces();
//        int evaluation = 0;
//        int[] leftestPiece = bitBoard.getFirstPiece(bitBoard.getPieces(pieceColor));
//        Set<int[]> ableToEatPieces = new HashSet<>();
//        while(leftestPiece[0] != -1){
//            getEatingPieces(pieceColor, leftestPiece[0], leftestPiece[1], ableToEatPieces);
//        }
//
//        if(!ableToEatPieces.isEmpty()){
//            //evaluateMoveNoEatingNeeded
//        }
//        return null;
//    }

//    public void getEatings(PieceType piece, int row, int col, QuaternaryTree eatingPosibilities) {
//        int[] move;
//
//        if (piece == PieceType.WHITEPIECE) {
//
//            if (col > 1 && row > 1 && canEat(piece, row, col, DirectionVector.northwest)) {
//                move = new int[]{row - 2, col - 2};
//                moves.add(move);
//
//                if (row - 2 == 0) {
//                    eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{0, col - 2}, "king", eatingPosibilities));
//                    QuaternaryTree asus = eatingPathPointer.get(0 + "," + (col - 2));
//                    //eatingPathPointer.remove(0 + "," + (col - 2));
//                    eatingPathPointer.put((0) + "," + (col - 2), eatingPosibilities.getTopLeft());
//                    getEatings(PieceType.WHITEKING, 0, col - 2, eatingPosibilities.getTopLeft());
//                } else {
//                    eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{row - 2, col - 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
//                    getEatings(piece, row - 2, col - 2, eatingPosibilities.getTopLeft());
//                }
//
//
//                //eatingPathPointer.put(new int[]{row - 2, col - 2}, eatingPosibilities.getTopLeft());
//                //eatingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
//            }
//
//            if (col < 6 && row > 1 && canEat(piece, row, col, DirectionVector.northeast)) {
//                move = new int[]{row - 2, col + 2};
//                moves.add(move);
//                if (row - 2 == 0) {
//                    eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{0, col + 2}, "king", eatingPosibilities));
//                    //eatingPathPointer.remove(0 + "," + (col + 2));
//                    eatingPathPointer.put((0) + "," + (col + 2), eatingPosibilities.getTopRight());
//                    getEatings(PieceType.WHITEKING, 0, col + 2, eatingPosibilities.getTopRight());
//                } else {
//                    eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{row - 2, col + 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row - 2) + "," + (col + 2), eatingPosibilities.getTopRight());
//                    getEatings(piece, row - 2, col + 2, eatingPosibilities.getTopRight());
//                }
//
//                //eatingPathPointer.put((row - 2) + "," + (col + 2), eatingPosibilities.getTopRight());
//
//            }
//        } else if (piece == PieceType.REDPIECE) {
//            eatingPathPointer.put(row + "," + col, eatingPosibilities);
//            if (col > 1 && row < 6 && canEat(piece, row, col, DirectionVector.southwest)) {
//                move = new int[]{row + 2, col - 2};
//                moves.add(move);
//
//                if (row + 2 == 7) {
//                    eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{7, col - 2}, "king", eatingPosibilities));
//                    //eatingPathPointer.remove(7 + "," + (col - 2));
//                    eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
//                    getEatings(PieceType.REDKING, 7, col - 2, eatingPosibilities.getButtomLeft());
//                } else {
//                    eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{row + 2, col - 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
//                    getEatings(piece, row + 2, col - 2, eatingPosibilities.getButtomLeft());
//                }
//
//                //eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
//            }
//
//            if (col < 6 && row < 6 && canEat(piece, row, col, DirectionVector.southeast)) {
//                move = new int[]{row + 2, col + 2};
//                moves.add(move);
//
//                if (row + 2 == 7) {
//                    eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{7, col + 2}, "king", eatingPosibilities));
//                    //eatingPathPointer.remove(7 + "," + (col + 2));
//                    eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
//                    getEatings(PieceType.REDKING, 7, col + 2, eatingPosibilities.getButtomRight());
//                } else {
//                    eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{row + 2, col + 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
//                    getEatings(piece, row + 2, col + 2, eatingPosibilities.getButtomRight());
//                }
//
//                //eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
//            }
//        } else {
////            int playerRow = eatingPosibilities.getPlayerCords()[0];
////            int playerCol = eatingPosibilities.getPlayerCords()[1];
//            int playerRow = eatingPosibilities.getPlayerCords()[0];
//            int playerCol = pieceToMove[1];
//            if (col > 1 && row > 1 && !eatingPathPointer.containsKey((row - 2) + "," + (col - 2)) && canEat(piece, row, col, DirectionVector.northwest)) {
//                move = new int[]{row - 2, col - 2};
//                moves.add(move);
//                eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{row - 2, col - 2}, "king", eatingPosibilities));
//                //eatingPathPointer.put(new int[]{row - 2, col - 2}, eatingPosibilities.getTopLeft());
//                lastPathPos.put(row + "," + col, eatingPosibilities.getTopLeft());
//                //atingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
//                eatingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
//                getEatings(piece, row - 2, col - 2, eatingPosibilities.getTopLeft());
//            }
//
//            if (col < 6 && row > 1 && !eatingPathPointer.containsKey((row - 2) + "," + (col + 2)) && canEat(piece, row, col, DirectionVector.northeast)) {
//                move = new int[]{row - 2, col + 2};
//                moves.add(move);
//                eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{row - 2, col + 2}, "king", eatingPosibilities));
//                eatingPathPointer.put((row - 2) + "," + (col + 2), eatingPosibilities.getTopRight());
//                getEatings(piece, row - 2, col + 2, eatingPosibilities.getTopRight());
//            }
//
//            if (col > 1 && row < 6 && !eatingPathPointer.containsKey((row + 2) + "," + (col - 2)) && canEat(piece, row, col, DirectionVector.southwest)) {
//                move = new int[]{row + 2, col - 2};
//                moves.add(move);
//                eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{row + 2, col - 2}, "king", eatingPosibilities));
//                eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
//                getEatings(piece, row + 2, col - 2, eatingPosibilities.getButtomLeft());
//            }
//
//            if (col < 6 && row < 6 && !eatingPathPointer.containsKey((row + 2) + "," + (col + 2)) && canEat(piece, row, col, DirectionVector.southeast)) {
//                move = new int[]{row + 2, col + 2};
//                moves.add(move);
//                eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{row + 2, col + 2}, "king", eatingPosibilities));
//                eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
//                getEatings(piece, row + 2, col + 2, eatingPosibilities.getButtomRight());
//            }
//        }
//    }
}
