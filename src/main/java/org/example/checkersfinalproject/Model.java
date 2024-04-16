package org.example.checkersfinalproject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Model {
    private BitBoard players;
    private HashMap<String, String> moves;
    private int[] pieceToMove;
    //private QuaternaryTree eatingPossibilities;
   // private HashMap<String, QuaternaryTree> eatingPathPointer;
    private HashMap<String, long[]> eatingPathPointer;
    private HashMap<String, QuaternaryTree> lastPathPos;
    private AI ai;
   // private boolean becomesKing;
    //private boolean firstCheck;
    public static HashMap<DirectionVector, int[]> intDirectionVector;

    static {
        intDirectionVector = new HashMap<>();
        intDirectionVector.put(DirectionVector.northwest, new int[]{-1, -1});
        intDirectionVector.put(DirectionVector.northeast, new int[]{-1, 1});
        intDirectionVector.put(DirectionVector.southwest, new int[]{1, -1});
        intDirectionVector.put(DirectionVector.southeast, new int[]{1, 1});
    }
    public Model(){
      //  becomesKing = false;
        ai = new AI(this, this.getBitBoard());
        players = new BitBoard();
        //eatingPossibilities = null;
        moves = new HashMap<>();
        pieceToMove = new int[2];
        eatingPathPointer = new HashMap<>();
        lastPathPos = new HashMap<>();
    }

    public void newGame(){
        players.initialize();
    }

    public PieceType[][] initializeBoard(){
        players.initialize();
        return players.convertToMatrix();
    }

    public PieceType[][] getBoard(){
        return players.convertToMatrix();
    }

//    public DirectionVector convertToDirection(int row0, int col0, int row, int col){
//        if(row0 - row < 0){
//            if(col0 - col < 0)
//                return DirectionVector.northwest;
//            else if(col0 - col > 0)
//                return DirectionVector.northeast;
//            return DirectionVector.north;
//        }
//
//        else if(row0 - row > 0){
//            if(col0 - col < 0)
//                return DirectionVector.southwest;
//            else if(col0 - col > 0)
//                return DirectionVector.southeast;
//            return DirectionVector.south;
//        }
//
//        if(col0 - col < 0)
//            return DirectionVector.west;
//
//        return DirectionVector.east;
//    }
//    public int makeMove(int player, int row0, int col0, int row, int col){
//
//    }

    public PieceType hasPiece(int row, int col){
        return players.hasPiece(7 - row, 7 - col);
    }

//    public boolean canMove(PieceType piece, int row, int col, DirectionVector direct){
//        return players.canMove(piece, row, col, direct);
//    }

    public PieceType canEat(Piece piece, int row, int col, DirectionVector direct){
        if(direct == DirectionVector.northwest && (col <= 1 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.northeast && (col >= 6 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.southwest && (col <= 1 || row >= 6)) return PieceType.None;
        else if(direct == DirectionVector.southeast && (col >= 6 || row >= 6)) return PieceType.None;

        return players.canEat(piece, 7 - row, 7 - col, direct);
    }

    public boolean canMove(int row, int col, DirectionVector direct){
        if(direct == DirectionVector.northwest && (col < 1 || row < 1)) return false;
        else if(direct == DirectionVector.northeast && (col > 6 || row < 1)) return false;
        else if(direct == DirectionVector.southwest && (col < 1 || row > 6)) return false;
        else if(direct == DirectionVector.southeast && (col > 6 || row > 6)) return false;

        return players.canMove(7 - row, 7 - col, direct);
    }

//    public boolean canEat(PieceType piece, int row0, int col0, int row, int col){
//        if(row >= 8 || col >= 8 || row < 0 || col < 0) return false;
//        //if(players.canMoveOrEat(player, row, col))
//        return false;
//    }

    public void addPiece(PieceType piece, int row, int col){
        players.addPiece(piece, 7 - row, 7 - col);
    }

//    public void removePiece(PieceType piece, int row, int col){
//        players.removePiece(piece, 7 - row, 7 - col);
//    }

    public void removeAllPieces(PieceType piece){
        players.removeAllPieces(piece);
    }

    /*public void makeMove(PieceType piece, int row0, int col0, int row, int col){
        PieceType enemyPiece;
        PieceType enemyKing;
        //QuaternaryTree destination = eatingPathPointer.get(row + "," + col);

        if(piece == PieceType.WHITEPIECE){
            enemyPiece = PieceType.REDPIECE;
            enemyKing = PieceType.REDKING;
        }

        else if(piece == PieceType.WHITEKING){
            enemyPiece = PieceType.REDPIECE;
            enemyKing = PieceType.REDKING;
        }

        else if(piece == PieceType.REDPIECE) {
            enemyPiece = PieceType.WHITEPIECE;
            enemyKing = PieceType.WHITEKING;
        }

        else{
            enemyPiece = PieceType.WHITEPIECE;
            enemyKing = PieceType.WHITEKING;
        }

        if(becomesKing){
            players.removePiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
            piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
            players.addPiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
        }

        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            players.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);


        players.movePieces(piece, 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);


    }

     */

//    public void makeMove(Piece piece, int row, int col, ){
//        PieceType enemyPiece;
//
//        enemyPiece = piece.getEnemyPieceType();
//        if(becomesKing){
//            players.removePiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
//            //  piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
//            piece = new King(piece.getDifferentType());
//            players.addPiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
//        }
//
//        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
//        if(piecesToRemove != null)
//            players.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);
//
//        players.movePieces(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
//
//
//    }

    public void makeMove(Piece piece, int row, int col, boolean becomesKing){
        PieceType enemyPiece;
        //QuaternaryTree destination = eatingPathPointer.get(row + "," + col);

        enemyPiece = piece.getEnemyPieceType();

        if(becomesKing){
            players.removePiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
            //  piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
            piece = new King(piece.getDifferentType());
            players.addPiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
        }

        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            players.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);


        players.movePieces(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);


    }

    public void makeMove(Piece piece, int row, int col, HashMap<String, String> move){
        PieceType enemyPiece;

        enemyPiece = piece.getEnemyPieceType();

        if(move.get(row + "," + col).split(",")[1].equals("true")){
            players.removePiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
            //  piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
            piece = new King(piece.getDifferentType());
            players.addPiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
        }

        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            players.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);

        players.movePieces(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);


    }

//    public void makeMove(Piece piece, int row, int col){
//        PieceType enemyPiece;
//        //QuaternaryTree destination = eatingPathPointer.get(row + "," + col);
//
//        enemyPiece = piece.getEnemyPieceType();
////        if(piece == PieceType.WHITEPIECE){
////            enemyPiece = PieceType.REDPIECE;
////        }
////
////        else if(piece == PieceType.WHITEKING){
////            enemyPiece = PieceType.REDPIECE;
////        }
////
////        else if(piece == PieceType.REDPIECE) {
////            enemyPiece = PieceType.WHITEPIECE;
////        }
////
////        else{
////            enemyPiece = PieceType.WHITEPIECE;
////        }
//
//        // not killing
////        if(piecesToEat + kingsToEat == 0){
////            if(piece == PieceType.WHITEPIECE){
////                if(row == 0){
////                    players.removePiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////                    piece = PieceType.WHITEKING;
////                    players.addPiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////                }
////
//////                else
//////                    players.movePieces(piece, 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
////            }
////
////
////            else if(piece == PieceType.REDPIECE) {
////                if(row == 7){
////                    players.removePiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////                    piece = PieceType.REDKING;
////                    players.addPiece(PieceType.REDKING, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////                }
////
//////                else
//////                    players.movePieces(piece, 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
////            }
////
////            players.movePieces(piece, 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
////        }
//
//
//        //PieceType enemyPiece = piece == PieceType.WHITEPIECE ? (PieceType.REDPIECE | PieceType.REDKING): PieceType.WHITEPIECE;
//        //players.movePieces(piece, 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
//        //QuaternaryTree destination = findDestination(eatingPossibilities, row, col);
//
//       // int[] asus = new int[]{row, col};
//
//        //DirectionVector direct = convertToDirection(row, col, pieceToMove[0],  pieceToMove[1]);
////        if(hasSon(eatingPosibilities)){
////            if(direct == DirectionVector.northwest)
////                removeEatenEnemies(eatingPosibilities.getTopLeft(), enemyPiece, row, col);
////
////            else if(direct == DirectionVector.northeast)
////                removeEatenEnemies(eatingPosibilities.getTopRight(), enemyPiece, row, col);
////
////            else if(direct == DirectionVector.southwest)
////                removeEatenEnemies(eatingPosibilities.getButtomLeft(), enemyPiece, row, col);
////
////            else if(direct == DirectionVector.southeast)
////                removeEatenEnemies(eatingPosibilities.getButtomRight(), enemyPiece, row, col);
////        }
//
//        // killing
//       // else{
//            if(becomesKing){
//                players.removePiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
//              //  piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
//                piece = new King(piece.getDifferentType());
//                players.addPiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
//            }
//
//            long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
//            if(piecesToRemove != null)
//                players.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);
//
//            //removeEatenEnemies(destination, enemyPiece, enemyKing, row, col);
////            if(destination.getPlayerType().equals("king") && !(piece == PieceType.WHITEKING || piece == PieceType.REDKING)){
////                players.removePiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////                piece = piece == PieceType.WHITEPIECE ? PieceType.WHITEKING : PieceType.REDKING;
////                players.addPiece(piece, 7 - pieceToMove[0], 7 - pieceToMove[1]);
////            }
//
//            players.movePieces(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
//      //  }
//
//
////        for (int[] enemies : eatingPathPointer) {
////            players.removePiece(enemyPiece, 7 - enemies[0], 7 - enemies[1]);
////        }
//
//
//    }
//
////    public QuaternaryTree findDestination(QuaternaryTree eatingPosibilities, int row, int col){
////        QuaternaryTree pos = null;
////        if(eatingPosibilities != null){
////            if(eatingPosibilities.getPlayerPosition()[0] == row && eatingPosibilities.getPlayerPosition()[1] == col)
////                return pos = eatingPosibilities;
////
////            if((pos = findDestination(eatingPosibilities.getTopLeft(), row, col)) != null) return pos;
////            if((pos = findDestination(eatingPosibilities.getTopRight(), row, col)) != null) return pos;
////            if((pos = findDestination(eatingPosibilities.getButtomLeft(), row, col)) != null) return pos;
////            if((pos = findDestination(eatingPosibilities.getButtomRight(), row, col)) != null) return pos;
////        }
////
////        return pos;
////    }
////    public void removeEatenEnemies(QuaternaryTree eatingPosibilities, PieceType enemyPiece, PieceType enemyKing, int row, int col){
//////        if(eatingPosibilities != null){
//////            players.removePiece(enemyPiece, 7 - eatingPosibilities.getEnemyToKill()[0], 7 - eatingPosibilities.getEnemyToKill()[1]);
//////            if(eatingPosibilities.getPlayerPosition()[0] == row && eatingPosibilities.getPlayerPosition()[1] == col)
//////                return;
//////
//////            removeEatenEnemies(eatingPosibilities.getTopLeft(), enemyPiece, row, col);
//////            removeEatenEnemies(eatingPosibilities.getTopRight(), enemyPiece, row, col);
//////            removeEatenEnemies(eatingPosibilities.getButtomLeft(), enemyPiece, row, col);
//////            removeEatenEnemies(eatingPosibilities.getButtomRight(), enemyPiece, row, col);
//////        }
////
////        int killRow = eatingPosibilities.getEnemyToKill()[0];
////        int killCol = eatingPosibilities.getEnemyToKill()[1];
//////        while(eatingPosibilities != null && eatingPosibilities.getEnemyToKill()[0] != -1 && !(killRow == pieceToMove[0] && killCol == pieceToMove[1])){
//////            players.removePiece(enemyPiece, 7 - killRow, 7 - killCol);
//////            players.removePiece(enemyKing, 7 - killRow, 7 - killCol);
//////            eatingPosibilities = eatingPosibilities.getFather();
//////            killRow = eatingPosibilities.getEnemyToKill()[0];
//////            killCol = eatingPosibilities.getEnemyToKill()[1];
//////        }
////
////
////    }
    public void getEatings(Piece piece, int row, int col, long piecesToEat, long kingsToEat, int totalJumpValue, boolean alreadyBecameKing){
        String move;
        PieceType pieceToEat;
        long lastPiecesToEat = piecesToEat;
        long lastKingsToEat = kingsToEat;
        int lastTotalJumpValue = totalJumpValue;
        int jumpValue;
        boolean becomesKing = false;
//        piecesToEat = 0;
//        kingsToEat = 0;

        for(DirectionVector direction : piece.getEatingDirections()) {
            jumpValue = 4;
            int[] tempMove = intDirectionVector.get(direction);
            if ((pieceToEat = canEat(piece, row, col, direction)) != PieceType.None &&
                    !eatingPathPointer.containsKey((row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2))) {


                if(!(piece instanceof King) && row + tempMove[0] * 2 == piece.rowToBeKing()){
                    becomesKing = true;
                    piece = new King(piece.getDifferentType());
                }

                if (pieceToEat == PieceType.WHITEKING || pieceToEat == PieceType.REDKING){
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));
                    jumpValue = 8;
                }

                else
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));

                totalJumpValue += jumpValue;
                move = (row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2);
                moves.put(move, totalJumpValue + "," + (becomesKing || alreadyBecameKing) + "," + true);



                eatingPathPointer.put((row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2), new long[]{piecesToEat, kingsToEat});

//                if (piece.getPieceType() == PieceType.WHITEPIECE && row + tempMove[0] * 2 == 0) {
//                    becomesKing = true;
//                    piece = new King(PieceType.WHITEKING);
//                }
//
//                else if (piece.getPieceType() == PieceType.REDPIECE && row + tempMove[0] * 2 == 7) {
//                    becomesKing = true;
//                    piece = new King(PieceType.REDKING);
//                }


                getEatings(piece, row + tempMove[0] * 2, col + tempMove[1] * 2, piecesToEat, kingsToEat, totalJumpValue, (becomesKing || alreadyBecameKing));
            }

            piecesToEat = lastPiecesToEat;
            kingsToEat = lastKingsToEat;
            totalJumpValue = lastTotalJumpValue;
            becomesKing = false;
        }

        /*if(piece == PieceType.WHITEPIECE){
            pieceToEat = canEat(piece, row, col, DirectionVector.northwest);
            if (pieceToEat != PieceType.None) {
                move = new int[]{row - 2, col - 2};
                moves.add(move);

                if(pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col - 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row - 1), 7 - (col - 1));

                eatingPathPointer.put((row - 2) + "," + (col - 2), new long[]{piecesToEat, kingsToEat});

                if(row - 2 == 0){
                    becomesKing = true;
                    //playerType = "king";
                    //eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{0, col - 2}, "king", eatingPosibilities));
                    //eatingPathPointer.remove(0 + "," + (col - 2));
                    //eatingPathPointer.put((0) + "," + (col - 2), eatingPosibilities.getTopLeft());
                    getEatings(PieceType.WHITEKING, 0, col - 2, piecesToEat, kingsToEat);
                   // kingsToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col - 1));
                }

                else{
                    //playerType = "piece";
                    //eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{0, col - 2}, "piece", eatingPosibilities));
                    //eatingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
                    getEatings(piece, row - 2, col - 2, piecesToEat, kingsToEat);
                    //piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col - 1));
                }

                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
                //getEatings(PieceType.WHITEKING, 0, col - 2);

                //eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{row - 2, col - 2}, playerType, eatingPosibilities));



                //eatingPathPointer.put(new int[]{row - 2, col - 2}, eatingPosibilities.getTopLeft());
                //eatingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
            }

            pieceToEat = canEat(piece, row, col, DirectionVector.northeast);
            if (pieceToEat != PieceType.None) {
                move = new int[]{row - 2, col + 2};
                moves.add(move);

                if(pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col + 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row - 1), 7 - (col + 1));

                eatingPathPointer.put((row - 2) + "," + (col + 2), new long[]{piecesToEat, kingsToEat});

                if(row - 2 == 0){
                    becomesKing = true;
                   // playerType = "king";
                    //eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{0, col + 2}, "king", eatingPosibilities));
                    //eatingPathPointer.remove(0 + "," + (col + 2));
                    //eatingPathPointer.put((0) + "," + (col + 2), eatingPosibilities.getTopRight());
                    getEatings(PieceType.WHITEKING, 0, col + 2, piecesToEat, kingsToEat);
                    //kingsToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col + 1));
                }

                else{
                    //playerType = "piece";
                    //eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{row - 2, col + 2}, "piece", eatingPosibilities));
                    //eatingPathPointer.put((row - 2) + "," + (col + 2), eatingPosibilities.getTopRight());
                    getEatings(piece, row - 2, col + 2, piecesToEat, kingsToEat);
                    //piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1),  7 - (col + 1));
                }

                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
                //getEatings(piece, row - 2, col + 2);

                //eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{0, col + 2}, playerType, eatingPosibilities));

                //eatingPathPointer.put((row - 2) + "," + (col + 2), eatingPosibilities.getTopRight());

            }
        }

        else if(piece == PieceType.REDPIECE){
            pieceToEat = canEat(piece, row, col, DirectionVector.southwest);
            //eatingPathPointer.put(row + "," + col, eatingPosibilities);
            if (pieceToEat != PieceType.None) {
                move = new int[]{row + 2, col - 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col - 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row + 1), 7 - (col - 1));

                eatingPathPointer.put((row + 2) + "," + (col - 2), new long[]{piecesToEat, kingsToEat});

                if(row + 2 == 7){
                    becomesKing = true;
                    //playerType = "king";
                    //eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{7, col - 2}, "king", eatingPosibilities));
                    //eatingPathPointer.remove(7 + "," + (col - 2));
                    //eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
                    getEatings(PieceType.REDKING, 7, col - 2, piecesToEat, kingsToEat);
                    //kingsToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col - 1));
                }

                else{
                   // playerType = "piece";
//                    eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{row + 2, col - 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
                    getEatings(piece, row + 2, col - 2, piecesToEat, kingsToEat);
//                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col - 1));
                }

                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
               // getEatings(piece, row + 2, col - 2);

                //eatingPathPointer.put((row + 2) + "," + (col - 2), eatingPosibilities.getButtomLeft());
            }

            pieceToEat = canEat(piece, row, col, DirectionVector.southeast);
            if (pieceToEat != PieceType.None) {
                move = new int[]{row + 2, col + 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col + 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row + 1), 7 - (col + 1));

                eatingPathPointer.put((row + 2) + "," + (col + 2), new long[]{piecesToEat, kingsToEat});

                if(row + 2 == 7){
                    becomesKing = true;
//                    eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{7, col + 2}, "king", eatingPosibilities));
//                    //eatingPathPointer.remove(7 + "," + (col + 2));
//                    eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
                    getEatings(PieceType.REDKING, 7, col + 2, piecesToEat, kingsToEat);
//                    kingsToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col + 1));
                }

                else{
//                    eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{row + 2, col + 2}, "piece", eatingPosibilities));
//                    eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
                    getEatings(piece, row + 2, col + 2, piecesToEat, kingsToEat);
//                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col + 1));
                }

                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
                //eatingPathPointer.put((row + 2) + "," + (col + 2), eatingPosibilities.getButtomRight());
            }
        }

        else{
//            int playerRow = eatingPosibilities.getPlayerCords()[0];
//            int playerCol = eatingPosibilities.getPlayerCords()[1];
//            int playerRow = eatingPosibilities.getPlayerCords()[0];
//            int playerCol = pieceToMove[1];
            //if (col > 1 && row > 1 && !eatingPathPointer.containsKey((row - 2) + "," + (col - 2)) && canEat(piece, row, col, DirectionVector.northwest)) {
            if(!firstCheck)
                eatingPathPointer.put((row) + "," + (col), new long[]{piecesToEat, kingsToEat});
            pieceToEat = canEat(piece, row, col, DirectionVector.northwest);
            firstCheck = false;
            if (!eatingPathPointer.containsKey((row - 2) + "," + (col - 2)) && pieceToEat != PieceType.None) {
                move = new int[]{row - 2, col - 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE || pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col - 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row - 1), 7 - (col - 1));

                //eatingPosibilities.setTopLeft(new QuaternaryTree(new int[]{row - 1, col - 1}, new int[]{row - 2, col - 2}, "king", eatingPosibilities));
                //eatingPathPointer.put(new int[]{row - 2, col - 2}, eatingPosibilities.getTopLeft());
                //lastPathPos.put(row + "," + col, eatingPosibilities.getTopLeft());
                //atingPathPointer.put((row - 2) + "," + (col - 2), eatingPosibilities.getTopLeft());
                //eatingPathPointer.put((row - 2) + "," + (col - 2), new long[]{piecesToEat, kingsToEat});
                //piecesToEat = players.addPiece(piecesToEat, row - 1, col - 1);
                getEatings(piece, row - 2, col - 2, piecesToEat, kingsToEat);
                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
            }

            pieceToEat = canEat(piece, row, col, DirectionVector.northeast);
            if (!eatingPathPointer.containsKey((row - 2) + "," + (col + 2)) && pieceToEat != PieceType.None) {
                move = new int[]{row - 2, col + 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE || pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row - 1), 7 - (col + 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row - 1), 7 - (col + 1));


                //eatingPosibilities.setTopRight(new QuaternaryTree(new int[]{row - 1, col + 1}, new int[]{row - 2, col + 2}, "king", eatingPosibilities));
                //eatingPathPointer.put((row - 2) + "," + (col + 2), new long[]{piecesToEat, kingsToEat});
                //piecesToEat = players.addPiece(piecesToEat, row - 1, col + 1);
                getEatings(piece, row - 2, col + 2, piecesToEat, kingsToEat);
                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
            }

            pieceToEat = canEat(piece, row, col, DirectionVector.southwest);
            if (!eatingPathPointer.containsKey((row + 2) + "," + (col - 2)) && pieceToEat != PieceType.None) {
                move = new int[]{row + 2, col - 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE || pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col - 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row + 1), 7 - (col - 1));

                //eatingPosibilities.setButtomLeft(new QuaternaryTree(new int[]{row + 1, col - 1}, new int[]{row + 2, col - 2}, "king", eatingPosibilities));
                //eatingPathPointer.put((row + 2) + "," + (col - 2), new long[]{piecesToEat, kingsToEat});
                //piecesToEat = players.addPiece(piecesToEat, row + 1, col - 1);
                getEatings(piece, row + 2, col - 2, piecesToEat, kingsToEat);
                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
            }

            pieceToEat = canEat(piece, row, col, DirectionVector.southeast);
            if (!eatingPathPointer.containsKey((row + 2) + "," + (col + 2)) && pieceToEat != PieceType.None) {
                move = new int[]{row + 2, col + 2};
                moves.add(move);

                if(pieceToEat == PieceType.WHITEPIECE || pieceToEat == PieceType.REDPIECE)
                    piecesToEat = players.addPiece(piecesToEat, 7 - (row + 1), 7 - (col + 1));
                else
                    kingsToEat = players.addPiece(kingsToEat, 7 - (row + 1), 7 - (col + 1));

                //eatingPosibilities.setButtomRight(new QuaternaryTree(new int[]{row + 1, col + 1}, new int[]{row + 2, col + 2}, "king", eatingPosibilities));
                //eatingPathPointer.put((row + 2) + "," + (col + 2), new long[]{piecesToEat, kingsToEat});
                //piecesToEat = players.addPiece(piecesToEat, row + 1, col + 1);
                getEatings(piece, row + 2, col + 2, piecesToEat, kingsToEat);
                piecesToEat = lastPiecesToEat;
                kingsToEat = lastKingsToEat;
            }
        }

        */
    }

    public HashMap<String, String> getMoves(){
        return moves;
    }

        public void getMoves(Piece piece, int row, int col){
        boolean becomesKing = false;
        String move;

        for(DirectionVector direction : piece.getEatingDirections()) {
            int[] tempMove = intDirectionVector.get(direction);
            if(canMove(row, col, direction)){
                if(piece.getPieceType() == PieceType.WHITEPIECE && row + tempMove[0] == 0)
                    becomesKing = true;

                else if(piece.getPieceType() == PieceType.REDPIECE && row + tempMove[0] == 7)
                    becomesKing = true;

                move = (row + tempMove[0]) + "," + (col + tempMove[1]);
                moves.put(move, 0 + "," + becomesKing + "," + false);
            }
        }

        /*
        if(piece == PieceType.WHITEPIECE){
            if(col > 0 && row > 0 && players.canMove(7 -  row, 7 - col, DirectionVector.northwest)){
                if(row - 1 == 0)
                    becomesKing = true;
                move = new int[]{row - 1, col - 1};
                moves.add(move);
            }

            if(col < 7 && row > 0 && players.canMove(7 - row, 7 - col, DirectionVector.northeast)){
                if(row - 1 == 0)
                    becomesKing = true;
                move = new int[]{row - 1, col + 1};
                moves.add(move);
            }
        }

        else if(piece == PieceType.REDPIECE){
            if(row + 1 == 7)
                becomesKing = true;
            if(col > 0 && row < 7 && players.canMove(7 - row, 7 - col, DirectionVector.southwest)){
                move = new int[]{row + 1, col - 1};
                moves.add(move);
            }

            if(col < 7 && row < 7 && players.canMove(7 - row, 7 - col, DirectionVector.southeast)){
                if(row + 1 == 7)
                    becomesKing = true;
                move = new int[]{row + 1, col + 1};
                moves.add(move);
            }
        }

        else {
            if(col > 0 && row > 0 && players.canMove(7 -  row, 7 - col, DirectionVector.northwest)){
                move = new int[]{row - 1, col - 1};
                moves.add(move);
            }

            if(col < 7 && row > 0 && players.canMove(7 - row, 7 - col, DirectionVector.northeast)){
                move = new int[]{row - 1, col + 1};
                moves.add(move);
            }

            if(col > 0 && row < 7 && players.canMove(7 - row, 7 - col, DirectionVector.southwest)){
                move = new int[]{row + 1, col - 1};
                moves.add(move);
            }

            if(col < 7 && row < 7 && players.canMove(7 - row, 7 - col, DirectionVector.southeast)){
                move = new int[]{row + 1, col + 1};
                moves.add(move);
            }
        }
//        if(piece == PieceType.WHITEPIECE){
//            if(col > 0 && row > 0 && players.canMoveOrEat(piece, 7 -  row, 7 - col, DirectionVector.northwest) == MoveType.move){
//                move = new int[]{row - 1, col - 1};
//                moves.add(move);
//            }
//
//            if(col < 7 && row > 0 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.northeast) == MoveType.move){
//                move = new int[]{row - 1, col + 1};
//                moves.add(move);
//            }
//        }
//
//        else if(piece == PieceType.REDPIECE){
//            if(col > 0 && row < 7 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.southwest) == MoveType.move){
//                move = new int[]{row + 1, col - 1};
//                moves.add(move);
//            }
//
//            if(col < 7 && row < 7 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.southeast) == MoveType.move){
//                move = new int[]{row + 1, col + 1};
//                moves.add(move);
//            }
//        }
//
//        else {
//            if(col > 0 && row > 0 && players.canMoveOrEat(piece, 7 -  row, 7 - col, DirectionVector.northwest) == MoveType.move){
//                move = new int[]{row - 1, col - 1};
//                moves.add(move);
//            }
//
//            if(col < 7 && row > 0 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.northeast) == MoveType.move){
//                move = new int[]{row - 1, col + 1};
//                moves.add(move);
//            }
//
//            if(col > 0 && row < 7 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.southwest) == MoveType.move){
//                move = new int[]{row + 1, col - 1};
//                moves.add(move);
//            }
//
//            if(col < 7 && row < 7 && players.canMoveOrEat(piece, 7 - row, 7 - col, DirectionVector.southeast) == MoveType.move){
//                move = new int[]{row + 1, col + 1};
//                moves.add(move);
//            }
//        }*/
    }



    public boolean hasSon(QuaternaryTree eatingPosibilities){
        return eatingPosibilities.getTopLeft() != null || eatingPosibilities.getTopRight() != null ||
                eatingPosibilities.getButtomLeft() != null || eatingPosibilities.getButtomRight() != null;
    }

    public boolean canEat(Piece piece){
        eatingPathPointer.clear();
        PieceType kingType = piece.getDifferentType();
        long pieces = players.getPieces(piece.getPieceType());
        long kings = players.getPieces(kingType);
        int[] leftestPiece;
    //    eatingPossibilities = new QuaternaryTree(new int[]{-1, -1}, new int[]{-1, -1}, "", null);
   //     eatingPathPointer.clear();
        while((leftestPiece = players.getFirstPiece(pieces))[0] != -1){
           // firstCheck = true;
            getEatings(piece, leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
//            if(hasSon(eatingPossibilities)){
//                //players.setPieces(piece, pieces);
//                return true;
//            }

            //players.removeFirstPiece(piece);
            pieces = players.removeFirstPiece(pieces);
        }

        while((leftestPiece = players.getFirstPiece(kings))[0] != -1){
           // firstCheck = true;
            getEatings(new King(kingType), leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
//            if(hasSon(eatingPossibilities)){
//                //players.setPieces(piece, pieces);
//                return true;
//            }

            //players.removeFirstPiece(piece);
            kings = players.removeFirstPiece(kings);
        }
        //players.setPieces(piece, pieces);
   //     return hasSon(eatingPossibilities);
        return !eatingPathPointer.isEmpty();
    }

    public HashMap<String, String> getAllMoves(Piece piece, int row, int col, boolean hasToEat, boolean clear){
        if(clear){
            moves.clear();
            eatingPathPointer.clear();
        }

        //eatingPossibilities = new QuaternaryTree(new int[]{-1, -1}, new int[]{-1, -1}, "", null);
       // firstCheck = true;
        getEatings(piece, row, col, 0, 0, 0, false);
        if(!hasToEat)
            getMoves(piece, row, col);
        pieceToMove = new int[]{row, col};
        return moves;
    }

    private HashMap<String, String> getAllMovesWithOutClearing(Piece piece, int row, int col){
        pieceToMove = new int[]{row, col};
        PieceType kingType = piece.getDifferentType();
        //eatingPossibilities = new QuaternaryTree(new int[]{-1, -1}, new int[]{-1, -1}, "", null);
       // firstCheck = true;
        getEatings(piece, row, col, 0, 0, 0, false);
        getMoves(piece, row, col);
        getMoves(new Piece(kingType), row, col);
        return moves;
    }

    public boolean isTie() {
//        long pieces = players.getPieces(piece);
//        int[] leftestPiece;
//        eatingPosibilities = new QuaternaryTree(new int[]{-1, -1}, new int[]{-1, -1}, null);
//        while((leftestPiece = players.getFirstPiece(pieces))[0] != -1){
//            getEatings(piece, leftestPiece[0], leftestPiece[1], eatingPosibilities);
//            if(hasSon(eatingPosibilities)){
//                //players.setPieces(piece, pieces);
//                return true;
//            }
//
//            //players.removeFirstPiece(piece);
//            pieces = players.removeFirstPiece(pieces);
//        }
        moves.clear();

        long whitePieces = players.getPieces(PieceType.WHITEPIECE);
        long blackPieces = players.getPieces(PieceType.REDPIECE);
        long whiteKings = players.getPieces(PieceType.WHITEKING);
        long blackKings = players.getPieces(PieceType.REDKING);
        int[] leftestWhitePiece;
        int[] leftestBlackPiece;
        int[] leftestWhiteKing;
        int[] leftestBlackKing;
        int numberOfWhitePieces = 0;
        int numberOfWhiteKings = 0;
        int numberOfBlackPieces = 0;
        int numberOfBlackKings = 0;



        while ((leftestWhitePiece = players.getFirstPiece(whitePieces))[0] != -1) {
            getAllMoves(new Piece(PieceType.WHITEPIECE), leftestWhitePiece[0], leftestWhitePiece[1], false, false);

            //players.removeFirstPiece(PieceType.WHITE);
            //players.removeFirstPiece(PieceType.RED);
            numberOfWhitePieces++;
            whitePieces = players.removeFirstPiece(whitePieces);
        }

        while ((leftestWhiteKing = players.getFirstPiece(whiteKings))[0] != -1) {
            getAllMoves(new King(PieceType.WHITEKING), leftestWhiteKing[0], leftestWhiteKing[1], false, false);

            //players.removeFirstPiece(PieceType.WHITE);
            //players.removeFirstPiece(PieceType.RED);
            numberOfWhiteKings++;
            whiteKings = players.removeFirstPiece(whiteKings);
        }

        while ((leftestBlackPiece = players.getFirstPiece(blackPieces))[0] != -1) {
            getAllMoves(new Piece(PieceType.REDPIECE), leftestBlackPiece[0], leftestBlackPiece[1], false, false);

            //players.removeFirstPiece(PieceType.WHITE);
            //players.removeFirstPiece(PieceType.RED);
            numberOfBlackPieces++;
            blackPieces = players.removeFirstPiece(blackPieces);
        }

        while ((leftestBlackKing = players.getFirstPiece(blackKings))[0] != -1) {
            getAllMoves(new King(PieceType.REDKING), leftestBlackKing[0], leftestBlackKing[1], false, false);

            //players.removeFirstPiece(PieceType.WHITE);
            //players.removeFirstPiece(PieceType.RED);
            numberOfBlackKings++;
            blackKings = players.removeFirstPiece(blackKings);
        }

//        if(numberOfWhitePieces == 0 && numberOfWhiteKings != 0 && numberOfBlackPieces == 0 && numberOfBlackKings != 0)
//            return true;
        //players.setPieces(PieceType.WHITE, whitePieces);
       // players.setPieces(PieceType.RED, blackPieces);

        return moves.isEmpty();
    }

    public boolean hasWon(Piece piece){
        moves.clear();
        PieceType enemyPieceType;
        PieceType enemyKingType;

//        if(piece == PieceType.WHITEPIECE || piece == PieceType.WHITEKING){
//            enemyPieceType = PieceType.REDPIECE;
//            enemyKingType = PieceType.REDKING;
//        }
//
//        else {
//            enemyPieceType = PieceType.WHITEPIECE;
//            enemyKingType = PieceType.WHITEKING;
//        }

        enemyPieceType = piece.enemyPieces;
        enemyKingType = piece.enemyKings;


        //PieceType enemyPieceType = piece == PieceType.WHITEPIECE ? PieceType.REDPIECE : PieceType.WHITEPIECE;
        long enemyPieces = players.getPieces(enemyPieceType);
        long enemyKings = players.getPieces(enemyKingType);

        int[] leftestPiece;

        if(enemyPieces == 0 && enemyKings == 0)
            return true;


        while((leftestPiece = players.getFirstPiece(enemyPieces))[0] != -1){
            getAllMoves(new Piece(enemyPieceType), leftestPiece[0], leftestPiece[1], false, false);
            //players.removeFirstPiece(enemyPieceType);
            enemyPieces = players.removeFirstPiece(enemyPieces);
        }

        while((leftestPiece = players.getFirstPiece(enemyKings))[0] != -1){
            getAllMoves(new King(enemyKingType), leftestPiece[0], leftestPiece[1], false, false);
            //players.removeFirstPiece(enemyPieceType);
            enemyKings = players.removeFirstPiece(enemyKings);
        }

        //players.setPieces(enemyPieceType, enemyPieces);

        return moves.isEmpty();
    }

    public Model clone(){
//        private BitBoard players;
//        private HashMap<int[], Integer> moves;
//        private int[] pieceToMove;
//        //private QuaternaryTree eatingPossibilities;
//        // private HashMap<String, QuaternaryTree> eatingPathPointer;
//        private HashMap<String, long[]> eatingPathPointer;
//        private HashMap<String, QuaternaryTree> lastPathPos;
//        private AI ai;
//        private boolean becomesKing;
//        //private boolean firstCheck;
//        public static HashMap<DirectionVector, int[]> intDirectionVector;
        Model newModel = new Model();
        newModel.players = this.players.clone();
        newModel.moves = new HashMap<>(moves);
        newModel.eatingPathPointer = new HashMap<>(eatingPathPointer);
        newModel.lastPathPos = new HashMap<>(lastPathPos);
        newModel.ai = ai;

        //newModel.players = players.clone();
        return newModel;
    }

//    public boolean canBeKing(PieceType piece, int row){
//        if(piece == PieceType.WHITEKING || piece == PieceType.REDKING)
//            return false;
//        if(piece == PieceType.WHITEPIECE)
//            return row == 0;
//
//        return row == 7;
//    }
//
//    public void makeKing(PieceType piece, int row, int col){
//        players.removePiece(piece, 7 -  pieceToMove[0], 7 - pieceToMove[1]);
//        players.makeKing(piece, 7 - row, 7 - col);
//    }
//
//    public PieceType checkPieceType(int row, int col){
//        int piece = players.hasPiece(7 - row, 7 - col);
//        switch (piece){
//            case 0:
//                return PieceType.WHITEPIECE;
//
//            case 1:
//                return PieceType.REDPIECE;
//
//            case 2:
//                return PieceType.SHADOW;
//
//            case 3:
//                return PieceType.WHITEKING;
//
//            case 4:
//                return PieceType.REDKING;
//
//        }
//
//        return PieceType.None;
//    }

    public HashMap<String, long[]> getEatingPathPointer(){
        return eatingPathPointer;
    }


    public BitBoard getBitBoard(){
        return this.players;
    }

    public Move getAIMove(Piece piece){
        return ai.chooseMove(piece, this, false);
    }

}
