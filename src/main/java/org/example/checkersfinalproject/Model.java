package org.example.checkersfinalproject;

import java.util.HashMap;

public class Model {
    // the bitboard
    private BitBoard bitBoard;

    // A list of moves that have information about the move.
    // Information like the number of jumps, if the piece became a king,
    // and if it was an eating move or not
    private HashMap<String, String> moves;

    // last piece that the model checked
    private int[] pieceToMove;

    // a list of moves that includes the ate pieces cordinations
    private HashMap<String, long[]> eatingPathPointer;

    // class that handles the AI
    private AI ai;


    // a direction vector array
    public static HashMap<DirectionVector, int[]> intDirectionVector;

    static {
        intDirectionVector = new HashMap<>();
        // north-west
        intDirectionVector.put(DirectionVector.northwest, new int[]{-1, -1});
        // north-east
        intDirectionVector.put(DirectionVector.northeast, new int[]{-1, 1});
        // south-west
        intDirectionVector.put(DirectionVector.southwest, new int[]{1, -1});
        // south-east
        intDirectionVector.put(DirectionVector.southeast, new int[]{1, 1});
    }
    public Model(Model model){
        this.bitBoard = model.bitBoard.clone();
        this.moves = new HashMap<>(model.moves);
        this.pieceToMove = model.pieceToMove.clone();
        this.eatingPathPointer = new HashMap<>(model.eatingPathPointer);
        this.ai = model.ai.clone();
    }
    public Model(){
        ai = new AI();
        bitBoard = new BitBoard();
        moves = new HashMap<>();
        pieceToMove = new int[2];
        eatingPathPointer = new HashMap<>();
    }

    public PieceType[][] initializeBoard(){
        ai.newGame();
        bitBoard.initialize();
        return bitBoard.convertToMatrix();
    }

    public PieceType[][] getBoard(){
        return bitBoard.convertToMatrix();
    }

    public PieceType hasPiece(int row, int col){
        return bitBoard.hasPiece(7 - row, 7 - col);
    }

    public PieceType canEat(Piece piece, int row, int col, DirectionVector direct){
        if(direct == DirectionVector.northwest && (col <= 1 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.northeast && (col >= 6 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.southwest && (col <= 1 || row >= 6)) return PieceType.None;
        else if(direct == DirectionVector.southeast && (col >= 6 || row >= 6)) return PieceType.None;

        return bitBoard.canEat(piece, 7 - row, 7 - col, direct);
    }

    public boolean canMove(int row, int col, DirectionVector direct){
        if(direct == DirectionVector.northwest && (col < 1 || row < 1)) return false;
        else if(direct == DirectionVector.northeast && (col > 6 || row < 1)) return false;
        else if(direct == DirectionVector.southwest && (col < 1 || row > 6)) return false;
        else if(direct == DirectionVector.southeast && (col > 6 || row > 6)) return false;

        return bitBoard.canMove(7 - row, 7 - col, direct);
    }

    public void addPiece(PieceType piece, int row, int col){
        bitBoard.addPiece(piece, 7 - row, 7 - col);
    }

    public void removeAllPieces(PieceType piece){
        bitBoard.removeAllPieces(piece);
    }

    public void makeMove(Piece piece, int row0, int col0, int row, int col, boolean becomesAKing){
        PieceType enemyPiece;

        enemyPiece = piece.getEnemyPieceType();

        if(becomesAKing){
            bitBoard.removePiece(piece.getPieceType(), 7 - row0, 7 - col0);
            piece = new King(piece.getDifferentType());
            bitBoard.addPiece(piece.getPieceType(), 7 - row0, 7 - col0);
        }

        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            bitBoard.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);

        bitBoard.movePieces(piece.getPieceType(), 7 - row0, 7 - col0, 7 - row, 7 - col);
    }

    public void makeMove(Piece piece, int row, int col, HashMap<String, String> move){
        PieceType enemyPiece;

        enemyPiece = piece.getEnemyPieceType();

        if(move.get(row + "," + col).split(",")[1].equals("true")){
            bitBoard.removePiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
            piece = new King(piece.getDifferentType());
            bitBoard.addPiece(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1]);
        }

        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            bitBoard.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);

        bitBoard.movePieces(piece.getPieceType(), 7 - pieceToMove[0], 7 - pieceToMove[1], 7 - row, 7 - col);
    }


    public void getEatings(Piece piece, int row, int col, long piecesToEat, long kingsToEat, int totalJumpValue, boolean alreadyBecameKing){
        String move;
        PieceType pieceToEat;
        long lastPiecesToEat = piecesToEat;
        long lastKingsToEat = kingsToEat;
        int lastTotalJumpValue = totalJumpValue;
        int jumpValue;
        boolean becomesKing = false;

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
                    kingsToEat = bitBoard.addPiece(kingsToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));
                    jumpValue = 8;
                }

                else
                    piecesToEat = bitBoard.addPiece(piecesToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));

                totalJumpValue += jumpValue;
                move = (row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2);
                moves.put(move, totalJumpValue + "," + (becomesKing || alreadyBecameKing) + "," + true);


                eatingPathPointer.put((row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2), new long[]{piecesToEat, kingsToEat});

                getEatings(piece, row + tempMove[0] * 2, col + tempMove[1] * 2, piecesToEat, kingsToEat, totalJumpValue, (becomesKing || alreadyBecameKing));
            }

            piecesToEat = lastPiecesToEat;
            kingsToEat = lastKingsToEat;
            totalJumpValue = lastTotalJumpValue;
            becomesKing = false;
        }

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
    }

    public boolean canEat(Piece piece){
        eatingPathPointer.clear();
        moves.clear();
        PieceType kingType = piece.getDifferentType();
        long pieces = bitBoard.getPieces(piece.getPieceType());
        long kings = bitBoard.getPieces(kingType);
        int[] leftestPiece;

        while((leftestPiece = bitBoard.getFirstPiece(pieces))[0] != -1){
            getEatings(piece, leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
            pieces = bitBoard.removeFirstPiece(pieces);
        }

        while((leftestPiece = bitBoard.getFirstPiece(kings))[0] != -1){
            getEatings(new King(kingType), leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
            kings = bitBoard.removeFirstPiece(kings);
        }

        return !eatingPathPointer.isEmpty();
    }

    public HashMap<String, String> getAllMoves(Piece piece, int row, int col, boolean hasToEat, boolean clear){
        if(clear){
            moves.clear();
            eatingPathPointer.clear();
        }

        getEatings(piece, row, col, 0, 0, 0, false);
        if(!hasToEat)
            getMoves(piece, row, col);
        pieceToMove = new int[]{row, col};
        return moves;
    }

    public boolean isTie() {
        moves.clear();

        long whitePieces = bitBoard.getPieces(PieceType.WHITEPIECE);
        long blackPieces = bitBoard.getPieces(PieceType.REDPIECE);
        long whiteKings = bitBoard.getPieces(PieceType.WHITEKING);
        long blackKings = bitBoard.getPieces(PieceType.REDKING);
        int[] leftestWhitePiece;
        int[] leftestBlackPiece;
        int[] leftestWhiteKing;
        int[] leftestBlackKing;


        while ((leftestWhitePiece = bitBoard.getFirstPiece(whitePieces))[0] != -1) {
            getAllMoves(new Piece(PieceType.WHITEPIECE), leftestWhitePiece[0], leftestWhitePiece[1], false, false);
            whitePieces = bitBoard.removeFirstPiece(whitePieces);
        }

        while ((leftestWhiteKing = bitBoard.getFirstPiece(whiteKings))[0] != -1) {
            getAllMoves(new King(PieceType.WHITEKING), leftestWhiteKing[0], leftestWhiteKing[1], false, false);
            whiteKings = bitBoard.removeFirstPiece(whiteKings);
        }

        while ((leftestBlackPiece = bitBoard.getFirstPiece(blackPieces))[0] != -1) {
            getAllMoves(new Piece(PieceType.REDPIECE), leftestBlackPiece[0], leftestBlackPiece[1], false, false);
            blackPieces = bitBoard.removeFirstPiece(blackPieces);
        }

        while ((leftestBlackKing = bitBoard.getFirstPiece(blackKings))[0] != -1) {
            getAllMoves(new King(PieceType.REDKING), leftestBlackKing[0], leftestBlackKing[1], false, false);
            blackKings = bitBoard.removeFirstPiece(blackKings);
        }

        return moves.isEmpty();
    }

    public boolean hasWon(Piece piece){
        moves.clear();
        PieceType enemyPieceType;
        PieceType enemyKingType;

        enemyPieceType = piece.enemyPieces;
        enemyKingType = piece.enemyKings;


        //PieceType enemyPieceType = piece == PieceType.WHITEPIECE ? PieceType.REDPIECE : PieceType.WHITEPIECE;
        long enemyPieces = bitBoard.getPieces(enemyPieceType);
        long enemyKings = bitBoard.getPieces(enemyKingType);

        int[] leftestPiece;

        if(enemyPieces == 0 && enemyKings == 0)
            return true;


        while((leftestPiece = bitBoard.getFirstPiece(enemyPieces))[0] != -1){
            getAllMoves(new Piece(enemyPieceType), leftestPiece[0], leftestPiece[1], false, false);
            //players.removeFirstPiece(enemyPieceType);
            enemyPieces = bitBoard.removeFirstPiece(enemyPieces);
        }

        while((leftestPiece = bitBoard.getFirstPiece(enemyKings))[0] != -1){
            getAllMoves(new King(enemyKingType), leftestPiece[0], leftestPiece[1], false, false);
            //players.removeFirstPiece(enemyPieceType);
            enemyKings = bitBoard.removeFirstPiece(enemyKings);
        }

        //players.setPieces(enemyPieceType, enemyPieces);

        return moves.isEmpty();
    }

    public HashMap<String, long[]> getEatingPathPointer(){
        return eatingPathPointer;
    }

    public HashMap<String, String> getMoves(){
        return moves;
    }

    public BitBoard getBitBoard(){
        return this.bitBoard;
    }

    public Move getAIMove(Piece piece, AIDifficulty level){
        return ai.chooseMove(piece, this, false, level);
    }

    public Model clone(){
        return new Model(this);
    }
}
