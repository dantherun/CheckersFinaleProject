package org.example.checkersfinalproject;

import java.util.HashSet;
import java.util.Set;

public class BitBoard {
    private long whitePieces;
    private long redPieces;
    private long whiteKings;
    private long redKings;

    // the shadow pieces that are used to show the possible moves of a piece
    private long shadows;
  //  private long mask;

    public BitBoard(){
        initialize();
    }

    public BitBoard(BitBoard another) {
        this.whitePieces = another.whitePieces;
        this.redPieces = another.redPieces;
        this.whiteKings = another.whiteKings;
        this.redKings = another.redKings;
        this.shadows = another.shadows;
    }

    public void initialize(){
        long mask = 1;
        whitePieces = 0;
        redPieces = 0;
        whiteKings = 0;
        redKings = 0;
        shadows = 0;

        whitePieces = 11163050L;
        redPieces = 6172839697753047040L;
    }

    public PieceType canEat(Piece piece, int row, int col, DirectionVector direct){
        long mask = (1L << 8 * row) << col;
        int direction = direct.getDirectionType();
        PieceType pieceOtherType;
        PieceType pieceAfter;
        pieceOtherType = piece.getDifferentType();

        pieceAfter = pieceAfter(row, col, direct);
        if(pieceAfter == PieceType.None || pieceAfter == piece.getPieceType() || pieceAfter == pieceOtherType)
            return PieceType.None;

        if(direction < 0)
            mask >>>= (-2 * direction);

        else
            mask <<= (2 * direction);

        if(((whitePieces | whiteKings | redPieces | redKings) & mask) == 0)
            return pieceAfter;

        return PieceType.None;
    }

    public PieceType pieceAfter(int row, int col, DirectionVector direct){
        long mask = (1L << 8 * row) << col;
        int direction = direct.getDirectionType();

        if(direction < 0)
            mask >>>= -direction;

        else
            mask <<= direction;

        if((whitePieces & mask) != 0) return PieceType.WHITEPIECE;
        else if((whiteKings & mask) != 0) return PieceType.WHITEKING;
        else if((redPieces & mask) != 0) return PieceType.REDPIECE;
        else if((redKings & mask) != 0) return PieceType.REDKING;

        return PieceType.None;
    }
    public boolean canMove(int row, int col, DirectionVector direct){
        long mask = (1L << 8 * row) << col;
        int direction = direct.getDirectionType();

        if(direction < 0)
            mask >>>= -direction;

        else
            mask <<= direction;

        return ((whitePieces | whiteKings | redPieces | redKings) & mask) == 0;
    }

    public void movePieces(PieceType piece, int row0, int col0, int row, int col){
        long mask = (1L << 8 * row0) << col0;

        if(piece == PieceType.WHITEPIECE){
            whitePieces = whitePieces ^ mask;
            mask = (1L << 8 * row) << col;
            whitePieces = whitePieces | mask;
        }

        else if(piece == PieceType.REDPIECE){
            redPieces = redPieces ^ mask;
            mask = (1L << 8 * row) << col;
            redPieces = redPieces | mask;
        }

        else if(piece == PieceType.WHITEKING){
            whiteKings = whiteKings ^ mask;
            mask = (1L << 8 * row) << col;
            whiteKings = whiteKings | mask;
        }

        else {
            redKings = redKings ^ mask;
            mask = (1L << 8 * row) << col;
            redKings = redKings | mask;
        }

    }

    // returns the piece type
    public PieceType hasPiece(int row, int col){
        long mask = 1L;
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

    public void addPiece(PieceType piece, int row, int col){
        long mask = (1L << 8 * row) << col;

        switch (piece){
            case WHITEPIECE -> whitePieces = whitePieces | mask;
            case REDPIECE -> redPieces = redPieces | mask;
            case WHITEKING -> whiteKings = whiteKings | mask;
            case REDKING -> redKings = redKings | mask;
            default -> shadows = shadows | mask;
        }
    }

    public long addPiece(long player, int row, int col){
        long mask = (1L << 8 * row) << col;
        player = player | mask;
        return player;
    }

    public long removePiece(long player, int row, int col){
        long mask = (1L << 8 * row) << col;
        if((player & mask) != 0)
            player = player ^ mask;

        return player;
    }

    public void removePieces(PieceType pieceType, long pieces, long kings){
        if(pieceType == PieceType.WHITEPIECE){
            whitePieces = whitePieces ^ pieces;
            whiteKings = whiteKings ^ kings;
        }

        else{
            redPieces = redPieces ^ pieces;
            redKings = redKings ^ kings;
        }

    }

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

    public void removeAllPieces(PieceType piece){
        switch (piece){
            case WHITEPIECE -> whitePieces = 0;
            case REDPIECE -> redPieces = 0;
            case WHITEKING -> whiteKings = 0;
            case REDKING -> redKings = 0;
            default -> shadows = 0;
        }
    }

    public void removeAllPieces(){
        whitePieces = redPieces = whiteKings = redKings = shadows = 0;
    }

    public PieceType[][] convertToMatrix(){
        PieceType[][] mat = new PieceType[8][8];
        long mask = 1;
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

    public int[] getFirstPiece(PieceType piece){
        int[] pieceCordination = new int[2];
        int leftestBit = piece == PieceType.WHITEPIECE ? Long.numberOfLeadingZeros(whitePieces) : Long.numberOfLeadingZeros(redPieces);
        leftestBit = 63 - leftestBit;
        if (leftestBit != -1) {
            pieceCordination[0] = 7 - leftestBit / 8;
            pieceCordination[1] = 7 - leftestBit % 8;
            return pieceCordination;

        }

        else
            return new int[]{-1, -1};
    }

    public int[] getFirstPiece(long pieces){
        int[] pieceCordination = new int[]{-1, -1};
        int leftestBit = Long.numberOfLeadingZeros(pieces);
        leftestBit = 63 - leftestBit;
        if (leftestBit != -1) {
            pieceCordination[0] = 7 - leftestBit / 8;
            pieceCordination[1] = 7 - leftestBit % 8;
        }

        return pieceCordination;
    }

    public long removeFirstPiece(long pieces){
        long highestOneBit = Long.highestOneBit(pieces);
        pieces = pieces ^ highestOneBit;
        return pieces;
    }
    public long getPieces(PieceType piece){
        if(piece == PieceType.WHITEPIECE)
            return whitePieces;

        if(piece == PieceType.WHITEKING)
            return whiteKings;

        if(piece == PieceType.REDPIECE)
            return redPieces;

        return redKings;
    }

    public Set<int[]> getPiecesList(PieceType piece){
        Set<int[]> piecesSet = new HashSet<>();
        long piecesToReturn;
        if(piece == PieceType.WHITEPIECE)
            piecesToReturn = whitePieces;

        else if(piece == PieceType.WHITEKING)
            piecesToReturn = whiteKings;

        else if(piece == PieceType.REDPIECE)
            piecesToReturn = redPieces;

        else
            piecesToReturn = redKings;


        while(piecesToReturn != 0){
            piecesSet.add(getFirstPiece(piecesToReturn));
            piecesToReturn = removeFirstPiece(piecesToReturn);
        }

        return piecesSet;
    }

    public int[] convertToCordinates(long bits){
        int[] cords = new int[]{-1, -1};
        int leftestBit = Long.numberOfLeadingZeros(bits);
        leftestBit = 63 - leftestBit;
        if (leftestBit != -1) {
            cords[0] = 7 - leftestBit / 8;
            cords[1] = 7 - leftestBit % 8;
        }

        return cords;
    }

    public BitBoard clone(){
        return new BitBoard(this);
    }
}
