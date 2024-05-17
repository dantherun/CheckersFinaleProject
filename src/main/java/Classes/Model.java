package Classes;

import Enums.AIDifficulty;
import Enums.DirectionVector;
import Enums.PieceType;

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

    /**
     * Initializes the board
     * @return the board
     */
    public PieceType[][] initializeBoard(){
        ai.newGame();
        bitBoard.initialize();
        return bitBoard.convertToMatrix();
    }

    /**
     * Gets the board
     * @return the board
     */
    public PieceType[][] getBoard(){
        return bitBoard.convertToMatrix();
    }

    /**
     * Checks if there is a piece at the given row and column
     * @param row the row of the piece
     * @param col the column of the piece
     * @return the piece at the given row and column
     */
    public PieceType hasPiece(int row, int col){
        return bitBoard.hasPiece(7 - row, 7 - col);
    }

    /**
     * Checks if the piece can eat
     * @param piece the piece to check
     * @return if the piece can eat
     */
    public PieceType canEat(Piece piece, int row, int col, DirectionVector direct){
        // checks if the coordinates are out of bounds
        if(direct == DirectionVector.northwest && (col <= 1 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.northeast && (col >= 6 || row <= 1)) return PieceType.None;
        else if(direct == DirectionVector.southwest && (col <= 1 || row >= 6)) return PieceType.None;
        else if(direct == DirectionVector.southeast && (col >= 6 || row >= 6)) return PieceType.None;

        // checks if the piece can eat if the piece is in the bounds
        return bitBoard.canEat(piece, 7 - row, 7 - col, direct);
    }

    /**
     * Checks if the piece can move
     * @param row the row of the piece
     * @param col the column of the piece
     * @param direct the direction to move
     * @return if the piece can move
     */
    public boolean canMove(int row, int col, DirectionVector direct){
        // checks if the coordinates are out of bounds
        if(direct == DirectionVector.northwest && (col < 1 || row < 1)) return false;
        else if(direct == DirectionVector.northeast && (col > 6 || row < 1)) return false;
        else if(direct == DirectionVector.southwest && (col < 1 || row > 6)) return false;
        else if(direct == DirectionVector.southeast && (col > 6 || row > 6)) return false;

        // checks if the piece can move if the piece is in the bounds
        return bitBoard.canMove(7 - row, 7 - col, direct);
    }

    /**
     * Adds a piece to the board
     * @param piece the piece to add
     * @param row the row of the piece
     * @param col the column of the piece
     */
    public void addPiece(PieceType piece, int row, int col){
        // adds the piece to the board
        bitBoard.addPiece(piece, 7 - row, 7 - col);
    }

    /**
     * Removes all pieces of a certain type
     * @param piece - the type of piece to remove
     */
    public void removeAllPieces(PieceType piece){
        // removes all pieces of a certain type
        bitBoard.removeAllPieces(piece);
    }

    /**
     * makes a move
     * @param piece the piece to move
     * @param row0 - the row of the piece
     * @param col0 - the column of the piece
     * @param row - the row to move to
     * @param col - the column to move to
     * @param becomesAKing - if the piece becomes a king
     */
    public void makeMove(Piece piece, int row0, int col0, int row, int col, boolean becomesAKing){
        PieceType enemyPiece;

        enemyPiece = piece.getEnemyPieceType();

        // if the piece becomes a king, remove the piece and add a king
        if(becomesAKing){
            bitBoard.removePiece(piece.getPieceType(), 7 - row0, 7 - col0);
            piece = new King(piece.getDifferentType());
            bitBoard.addPiece(piece.getPieceType(), 7 - row0, 7 - col0);
        }

        // remove the pieces that were eaten
        long[] piecesToRemove = eatingPathPointer.get(row + "," + col);
        if(piecesToRemove != null)
            bitBoard.removePieces(enemyPiece, piecesToRemove[0], piecesToRemove[1]);

        // move the piece
        bitBoard.movePieces(piece.getPieceType(), 7 - row0, 7 - col0, 7 - row, 7 - col);
    }

    /**
     * Makes a move
     * @param piece the piece to move
     * @param row the row to move to
     * @param col the column to move to
     * @param move the move to make
     */
    public void makeMove(Piece piece, int row, int col, HashMap<String, String> move){
        makeMove(piece, pieceToMove[0], pieceToMove[1], row, col, move.get(row + "," + col).split(",")[1].equals("true"));
    }

    /**
     * Gets all the possible eatings for a piece
     * @param piece - the piece to get the eatings for
     * @param row - the row of the piece
     * @param col - the column of the piece
     * @param piecesToEat - the pieces that can be eaten
     * @param kingsToEat - the kings that can be eaten
     * @param totalJumpValue - the total jump value
     * @param alreadyBecameKing - if the piece already became a king
     */
    public void getEatings(Piece piece, int row, int col, long piecesToEat, long kingsToEat, int totalJumpValue, boolean alreadyBecameKing){
        String move;
        PieceType pieceToEat;
        long lastPiecesToEat = piecesToEat;
        long lastKingsToEat = kingsToEat;
        int lastTotalJumpValue = totalJumpValue;
        int jumpValue;
        boolean becomesKing = false;

        // iterates over all the directions that the piece can eat
        for(DirectionVector direction : piece.getEatingDirections()) {
            // sets the jump value to 4
            jumpValue = 4;
            int[] tempMove = intDirectionVector.get(direction);
            // checks if the piece can eat and if it was not checked before
            if ((pieceToEat = canEat(piece, row, col, direction)) != PieceType.None &&
                    !eatingPathPointer.containsKey((row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2))) {

                // if the piece is not a king and it reaches the last row, it becomes a king
                if(!(piece instanceof King) && row + tempMove[0] * 2 == piece.rowToBeKing()){
                    becomesKing = true;
                    piece = new King(piece.getDifferentType());
                }

                // if the piece to eat is a king, add it to the kings to eat and set the jump value to 8
                if (pieceToEat == PieceType.WHITEKING || pieceToEat == PieceType.REDKING){
                    kingsToEat = bitBoard.addPiece(kingsToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));
                    jumpValue = 8;
                }

                // if the piece to eat is a piece, add it to the pieces to eat
                else
                    piecesToEat = bitBoard.addPiece(piecesToEat, 7 - (row + tempMove[0]), 7 - (col + tempMove[1]));

                // add the jump value to the total jump value
                totalJumpValue += jumpValue;
                // add the move to the moves
                move = (row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2);
                moves.put(move, totalJumpValue + "," + (becomesKing || alreadyBecameKing) + "," + true);

                // add the move to the eating path pointer
                eatingPathPointer.put((row + tempMove[0] * 2) + "," + (col + tempMove[1] * 2), new long[]{piecesToEat, kingsToEat});

                // get the next eatings after the current eating
                getEatings(piece, row + tempMove[0] * 2, col + tempMove[1] * 2, piecesToEat, kingsToEat, totalJumpValue, (becomesKing || alreadyBecameKing));
            }

            // reset the values to the last values before the current eating for the next loop
            piecesToEat = lastPiecesToEat;
            kingsToEat = lastKingsToEat;
            totalJumpValue = lastTotalJumpValue;
            becomesKing = false;
        }

    }

    /**
     * Gets all the possible moves for a piece
     * @param piece - the piece to get the moves for
     * @param row - the row of the piece
     * @param col - the column of the piece
     */
    public void getMoves(Piece piece, int row, int col){
        boolean becomesKing = false;
        String move;

        // iterates over all the directions that the piece can move
        for(DirectionVector direction : piece.getEatingDirections()) {
            int[] tempMove = intDirectionVector.get(direction);
            // checks if the piece can move
            if(canMove(row, col, direction)){
                // if the piece is not a king and it reaches the last row, it becomes a king
                if(!(piece instanceof King) && row + tempMove[0] == piece.rowToBeKing())
                    becomesKing = true;


                // add the move to the moves
                move = (row + tempMove[0]) + "," + (col + tempMove[1]);
                moves.put(move, 0 + "," + becomesKing + "," + false);
            }
        }
    }

    /**
     * Checks if a piece has to eat
     * @param piece - the piece to check
     * @return if the piece has to eat
     */
    public boolean needToEat(Piece piece){
        eatingPathPointer.clear();
        moves.clear();
        PieceType kingType = piece.getDifferentType();
        long pieces = bitBoard.getPieces(piece.getPieceType());
        long kings = bitBoard.getPieces(kingType);
        int[] leftestPiece;

        // iterates over all the pieces to get the eatings
        while((leftestPiece = bitBoard.getFirstPiece(pieces))[0] != -1){
            getEatings(piece, leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
            pieces = bitBoard.removeFirstPiece(pieces);
        }

        // iterates over all the kings to get the eatings
        while((leftestPiece = bitBoard.getFirstPiece(kings))[0] != -1){
            getEatings(new King(kingType), leftestPiece[0], leftestPiece[1], 0, 0, 0, false);
            kings = bitBoard.removeFirstPiece(kings);
        }


        return !eatingPathPointer.isEmpty();
    }

    /**
     * Gets all the possible moves for a piece
     * @param piece - the piece to get the moves for
     * @param row - the row of the piece
     * @param col - the column of the piece
     * @param hasToEat - if the piece has to eat
     * @param clear - if the moves should be cleared first
     * @return the moves
     */
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

    /**
     * Checks if a player has won
     * @param piece - the piece to check
     * @return if the player has won
     */
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
            enemyPieces = bitBoard.removeFirstPiece(enemyPieces);
            if(!moves.isEmpty())
                return false;
        }

        while((leftestPiece = bitBoard.getFirstPiece(enemyKings))[0] != -1){
            getAllMoves(new King(enemyKingType), leftestPiece[0], leftestPiece[1], false, false);
            enemyKings = bitBoard.removeFirstPiece(enemyKings);
            if(!moves.isEmpty())
                return false;
        }

        //players.setPieces(enemyPieceType, enemyPieces);

        return true;
    }

    /**
     * returns the eating path pointer
     * @return the eating path pointer
     */
    public HashMap<String, long[]> getEatingPathPointer(){
        return eatingPathPointer;
    }

    /**
     * returns the bitboard
     * @return the bitboard
     */
    public BitBoard getBitBoard(){
        return this.bitBoard;
    }

    /**
     * returns the best move that the AI found
     * @param piece - the piece to move
     * @param level - the level of the AI
     * @return the best move
     */
    public Move getAIMove(Piece piece, AIDifficulty level){
        return ai.chooseMove(piece, this, level);
    }

    /**
     * Clones the model
     * @return the cloned model
     */
    public Model clone(){
        return new Model(this);
    }
}
