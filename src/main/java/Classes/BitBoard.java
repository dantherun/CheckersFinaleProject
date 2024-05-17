package Classes;

import Enums.DirectionVector;
import Enums.PieceType;

public class BitBoard {
    private long whitePieces;
    private long redPieces;
    private long whiteKings;
    private long redKings;

    // the shadow pieces that are used to show the possible moves of a piece
    private long shadows;
    public BitBoard(){
        initialize();
    }

    /**
     * Copy constructor
     * @param another - the object to be copied
     */
    public BitBoard(BitBoard another) {
        this.whitePieces = another.whitePieces;
        this.redPieces = another.redPieces;
        this.whiteKings = another.whiteKings;
        this.redKings = another.redKings;
        this.shadows = another.shadows;
    }

    /**
     * Initializes the board and setting the pieces to their initial positions
     */
    public void initialize(){
        whiteKings = 0;
        redKings = 0;
        shadows = 0;

        // setting the white and red pieces to their initial positions
        whitePieces = 11163050L;
        redPieces = 6172839697753047040L;
    }

    /**
     * Checks if the piece can eat another piece
     * @param piece - the piece that is going to eat
     * @param row - the row of the piece
     * @param col - the column of the piece
     * @param direct - the direction of the piece
     * @return the type of the piece that can be eaten
     */
    public PieceType canEat(Piece piece, int row, int col, DirectionVector direct){
        // setting the mask to the position of the piece
        long mask = (1L << 8 * row) << col;
        int direction = direct.getDirectionType();
        PieceType pieceOtherType;
        PieceType pieceAfter;
        pieceOtherType = piece.getDifferentType();

        // getting the piece after the piece that is going to eat
        pieceAfter = pieceAfter(row, col, direct);

        // if there is no piece after
        // or if the piece after is the same as the piece that is going to eat
        if(pieceAfter == PieceType.None || pieceAfter == piece.getPieceType() || pieceAfter == pieceOtherType)
            return PieceType.None;

        // setting the mask to the position of the position after the piece that is going to be eaten
        if(direction < 0)
            mask >>>= (-2 * direction);
        else
            mask <<= (2 * direction);

        // if there is no piece after the piece that is going to be eaten
        if(((whitePieces | whiteKings | redPieces | redKings) & mask) == 0)
            // return the type of the piece that can be eaten
            return pieceAfter;

        // return none
        return PieceType.None;
    }

    /**
     * checks if there is a piece after a location
     * @param row - the row of the location
     * @param col - the column of the location
     * @param direct - the direction of the piece to check
     * @return the type of the piece after the location
     */
    public PieceType pieceAfter(int row, int col, DirectionVector direct){
        // setting the mask to the position of the location
        long mask = (1L << 8 * row) << col;
        int direction = direct.getDirectionType();

        // setting the mask to the position of the piece after the location
        if(direction < 0)
            mask >>>= -direction;
        else
            mask <<= direction;

        // return the type of the piece after the location
        if((whitePieces & mask) != 0) return PieceType.WHITEPIECE;
        else if((whiteKings & mask) != 0) return PieceType.WHITEKING;
        else if((redPieces & mask) != 0) return PieceType.REDPIECE;
        else if((redKings & mask) != 0) return PieceType.REDKING;

        // return none if there is no piece after the location
        return PieceType.None;
    }

    /**
     * Checks if a piece can move to a location
     * @param row - the row of the location
     * @param col - the column of the location
     * @param direct - the direction of the piece
     * @return true if the piece can move to the location, false otherwise
     */
    public boolean canMove(int row, int col, DirectionVector direct){
        return pieceAfter(row, col, direct) == PieceType.None;
    }

    /**
     * moves a piece to a location
     * @param piece - the piece to be moved
     * @param row0 - the row of the piece
     * @param col0 - the column of the piece
     * @param row - the row of the location
     * @param col - the column of the location
     */
    public void movePieces(PieceType piece, int row0, int col0, int row, int col){
        // setting the mask to the position of the piece
        long mask = (1L << 8 * row0) << col0;

        // removing the piece from the original position and adding it to the new position
        switch (piece){
            case WHITEPIECE -> {
                whitePieces = whitePieces ^ mask;
                mask = (1L << 8 * row) << col;
                whitePieces = whitePieces | mask;
            }
            case REDPIECE -> {
                redPieces = redPieces ^ mask;
                mask = (1L << 8 * row) << col;
                redPieces = redPieces | mask;
            }
            case WHITEKING -> {
                whiteKings = whiteKings ^ mask;
                mask = (1L << 8 * row) << col;
                whiteKings = whiteKings | mask;
            }
            case REDKING -> {
                redKings = redKings ^ mask;
                mask = (1L << 8 * row) << col;
                redKings = redKings | mask;
            }
        }

    }

    /**
     * checks if there is a piece at a location
     * @param row - the row of the location
     * @param col - the column of the location
     * @return the type of the piece at the location
     */
    public PieceType hasPiece(int row, int col){
        long mask = 1L;

        // return the type of the piece at the location
        if((whitePieces & ((mask << 8 * row) << col)) != 0)
            return PieceType.WHITEPIECE;
        else if((redPieces & ((mask << 8 * row) << col)) != 0)
            return PieceType.REDPIECE;
        else if((shadows & ((mask << 8 * row) << col)) != 0)
            return PieceType.SHADOW;
        else if((whiteKings & ((mask << 8 * row) << col)) != 0)
            return PieceType.WHITEKING;
        else if((redKings & ((mask << 8 * row) << col)) != 0)
            return PieceType.REDKING;
        return PieceType.None;
    }

    /**
     * adds a piece to the board
     * @param piece - the type of the piece
     * @param row - the row of the piece
     * @param col - the column of the piece
     */
    public void addPiece(PieceType piece, int row, int col){
        // setting the mask to the position of the piece
        long mask = (1L << 8 * row) << col;

        // adding the piece to the board
        switch (piece){
            case WHITEPIECE -> whitePieces = whitePieces | mask;
            case REDPIECE -> redPieces = redPieces | mask;
            case WHITEKING -> whiteKings = whiteKings | mask;
            case REDKING -> redKings = redKings | mask;
            default -> shadows = shadows | mask;
        }
    }

    /**
     * adds a piece to the board
     * @param player - the player to add the piece to
     * @param row - the row of the piece
     * @param col - the column of the piece
     * @return the player's updated pieces after adding the piece
     */
    public long addPiece(long player, int row, int col){
        // setting the mask to the position of the piece
        long mask = (1L << 8 * row) << col;
        // adding the piece to the player
        player = player | mask;
        // return the player's updated pieces
        return player;
    }

    /**
     * removes pieces from the board
     * @param pieceType - the type of the pieces to be removed
     * @param pieces - the pieces to be removed in a long type
     * @param kings - the kings to be removed in a long type
     */
    public void removePieces(PieceType pieceType, long pieces, long kings){
        // removing the pieces from the board
        if(pieceType == PieceType.WHITEPIECE){
            whitePieces = whitePieces ^ pieces;
            whiteKings = whiteKings ^ kings;
        }
        else{
            redPieces = redPieces ^ pieces;
            redKings = redKings ^ kings;
        }
    }

    /**
     * removes a piece from the board
     * @param piece - the type of the piece to be removed
     * @param row - the row of the piece in a coordination type
     * @param col - the column of the piece in a coordination type
     */
    public void removePiece(PieceType piece, int row, int col){
        long mask = (1L << 8 * row) << col;
        if (piece == PieceType.WHITEPIECE && ((whitePieces & mask) != 0))
            whitePieces = whitePieces ^ mask;

        else if (piece == PieceType.REDPIECE && ((redPieces & mask) != 0))
            redPieces = redPieces ^ mask;

        else if(piece == PieceType.WHITEKING && ((whiteKings & mask) != 0))
            whiteKings = whiteKings ^ mask;

        else if(piece == PieceType.REDKING && ((redKings & mask) != 0))
            redKings = redKings ^ mask;

        else
            shadows = shadows ^ mask;
    }

    /**
     * removes all the pieces of a certain type from the board
     * @param piece - the type of the pieces to be removed
     */
    public void removeAllPieces(PieceType piece){
        switch (piece){
            case WHITEPIECE -> whitePieces = 0;
            case REDPIECE -> redPieces = 0;
            case WHITEKING -> whiteKings = 0;
            case REDKING -> redKings = 0;
            default -> shadows = 0;
        }
    }

    /**
     * removes all the pieces from the board
     */
    public void removeAllPieces(){
        whitePieces = redPieces = whiteKings = redKings = shadows = 0;
    }

    /**
     * converts the board to a matrix
     * @return the board as a matrix
     */
    public PieceType[][] convertToMatrix(){
        PieceType[][] mat = new PieceType[8][8];
        long mask = 1;
        // iterating over the board and setting the matrix
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if((whitePieces & ((mask << 8 * i) << j)) != 0)
                    mat[7 - i][7 - j] = PieceType.WHITEPIECE;
                else if((redPieces & ((mask << 8 * i) << j)) != 0)
                    mat[7 - i][7 - j] = PieceType.REDPIECE;
                else if((whiteKings & ((mask << 8 * i) << j)) != 0)
                    mat[7 - i][7 - j] = PieceType.WHITEKING;
                else if((redKings & ((mask << 8 * i) << j)) != 0)
                    mat[7 - i][7 - j] = PieceType.REDKING;
                else if((shadows & ((mask << 8 * i) << j)) != 0)
                    mat[7 - i][7 - j] = PieceType.SHADOW;
                else
                    mat[7 - i][7 - j] = PieceType.None;
            }
        }

        return mat;
    }

    /**
     * gets the leftest piece in the board of a certain type
     * @param pieces - the pieces to get the leftest piece from
     * @return the coordinates of the leftest piece
     */
    public int[] getFirstPiece(long pieces){
        int[] pieceCordination = new int[]{-1, -1};
        // getting the leftest bit in the board
        int leftestBit = Long.numberOfLeadingZeros(pieces);
        leftestBit = 63 - leftestBit;
        // setting the coordinates of the leftest piece
        if (leftestBit != -1) {
            pieceCordination[0] = 7 - leftestBit / 8;
            pieceCordination[1] = 7 - leftestBit % 8;
        }

        // return the coordinates of the leftest piece
        return pieceCordination;
    }

    /**
     * removes the leftest piece from the board
     * @param pieces - the pieces to remove the leftest piece from
     * @return the updated pieces after removing the leftest piece
     */
    public long removeFirstPiece(long pieces){
        long highestOneBit = Long.highestOneBit(pieces);
        pieces = pieces ^ highestOneBit;
        return pieces;
    }

    /**
     * gets the pieces of a certain type in a long type
     * @param piece - the type of the pieces to get
     * @return the pieces of the type in a long type
     */
    public long getPieces(PieceType piece){
        switch (piece){
            case WHITEPIECE -> {
                return whitePieces;
            }
            case WHITEKING -> {
                return whiteKings;
            }
            case REDPIECE -> {
                return redPieces;
            }
            case REDKING -> {
                return redKings;
            }
            default -> {
                return shadows;
            }
        }
    }

    /**
     * clones the class
     * @return the cloned class
     */
    public BitBoard clone(){
        return new BitBoard(this);
    }
}
