package Classes;

import Enums.DirectionVector;
import Enums.PieceType;

public class King extends Piece{
    public King(PieceType pieceType){
        super();
        super.pieceType = pieceType;

        super.differentPieceType = pieceType == PieceType.WHITEKING ? PieceType.WHITEPIECE : PieceType.REDPIECE;

        super.directionsToMove.add(DirectionVector.northwest);
        super.directionsToMove.add(DirectionVector.northeast);
        super.directionsToMove.add(DirectionVector.southwest);
        super.directionsToMove.add(DirectionVector.southeast);

        super.enemyPieces = super.pieceType == PieceType.WHITEKING ? PieceType.REDPIECE : PieceType.WHITEPIECE;
        super.enemyKings = super.pieceType == PieceType.WHITEKING ? PieceType.REDKING : PieceType.WHITEKING;
    }

    @Override
    public PieceType getPieceColor() {
        return super.pieceType == PieceType.WHITEKING ? PieceType.WHITEPIECE : PieceType.REDPIECE;
    }

}
