package org.example.checkersfinalproject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AI {
    private final int pieceEvaluation = 1;
    private final int kingEvaluation = 2;
    private int numberOfPieces;
    private int numberOfEnemyPieces;
    private int numberOfKings;
    private int numberOfEnemyKings;
    private boolean isWhite;
   // private BitBoard bitBoard;
    private Model model;
    private String[][] board;
    private int gameState;
    //private PieceType pieceColor;
    public AI(Model model, BitBoard bitBoard){
        this.model = model;
   //     this.bitBoard = bitBoard;
    }

    public void restart(){

    }
//    public String[][] getPieces(PieceType piece){
//   //     return bitBoard.convertToMatrix();
//    }
    public boolean isSidePiece(int[] piece){
        return piece[1] == 0 || piece[1] == 7;
    }
    public Move chooseMove(Piece pieceType, Model model){
        BitBoard board = model.getBitBoard();
        boolean hasToEat;
        Model newModel = model.clone();
        Set<Move> moves = new HashSet<>();
        Set<int[]> eatings;
        int evaluation = 0;

        HashMap<String, long[]> eatingPathPointer;
        hasToEat = newModel.canEat(pieceType);
        Piece kingType = new King(pieceType.getDifferentType());
        long pieces = board.getPieces(pieceType.getPieceType());
        long kings = board.getPieces(kingType.getPieceType());
        int[] leftestPiece;


        while ((leftestPiece = board.getFirstPiece(pieces))[0] != -1) {
            newModel.getEatings(pieceType, leftestPiece[0], leftestPiece[1], 0, 0);
            if (!(eatings = newModel.getAllMoves(pieceType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (int[] cord : eatings) {
                    moves.add(new Move(1, leftestPiece, cord));
                }
            }

            pieces = board.removeFirstPiece(pieces);
        }

        while ((leftestPiece = board.getFirstPiece(kings))[0] != -1) {
            if (!(eatings = newModel.getAllMoves(kingType, leftestPiece[0], leftestPiece[1], hasToEat, true)).isEmpty()) {
                for (int[] cord : eatings) {
                    moves.add(new Move(1, leftestPiece, cord));
                }
            }
            //players.removeFirstPiece(piece);
            kings = board.removeFirstPiece(kings);

        }


        if(hasToEat){
            int maxEatings = 0;
            int currentEatings = 0;
            for (Move move : moves){
                //currentEatings = countEatings(move);
            }
        }
            for (Move move : moves){
                return move;
            }
        return null;
    }

    //public

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
