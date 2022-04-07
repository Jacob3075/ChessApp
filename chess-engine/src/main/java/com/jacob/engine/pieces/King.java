package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class King extends Piece {
    private boolean tryingToCastle = false;
    private boolean moved = false;
    private boolean queenSideCastlingDone = false;
    private boolean kingSideCastlingDone = false;

    public King(boolean white) {
        super(white, "k", 0);
    }

    public boolean isTryingToCastle() {
        return tryingToCastle;
    }

    public void setTryingToCastle(boolean tryingToCastle) {
        this.tryingToCastle = tryingToCastle;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isQueenSideCastlingDone() {
        return queenSideCastlingDone;
    }

    public void setQueenSideCastlingDone(boolean queenSideCastlingDone) {
        this.queenSideCastlingDone = queenSideCastlingDone;
    }

    public boolean isKingSideCastlingDone() {
        return kingSideCastlingDone;
    }

    public void setKingSideCastlingDone(boolean kingSideCastlingDone) {
        this.kingSideCastlingDone = kingSideCastlingDone;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(start == null || end == null || start == end)
            return false;
        
        Piece capturedPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(capturedPiece != null && capturedPiece.isWhite() == this.isWhite())
            return false;

        int deltaI = Math.abs(start.getI() - end.getI());
        int deltaJ = Math.abs(start.getJ() - end.getJ());

        // the king can move by 1 spot if it's not attacked after the move
        if((deltaI | deltaJ) == 1)
            return !isKingAttackedAfterMove(board, start, end);

        // the king cannot move by more than 2 spots
        if(deltaI > 2 || deltaJ > 2 || (deltaI == 2 && deltaJ > 0) || (deltaJ == 2 && deltaI > 0))
            return false;

        // the king can only move by exactly 2 spots if it's a castling move
        if(this.isValidCastling(board, start, end)) {
            this.setTryingToCastle(true);
            return true;
        }
        else {
            this.setTryingToCastle(false);
            return false;
        }
    }

    private boolean isValidCastling(Board board, Spot start, Spot end) {
        int deltaI = start.getI() - end.getI();
        int deltaJ = start.getJ() - end.getJ();

        if(isKingSideCastlingDone() || isQueenSideCastlingDone())
            return false;

        // the king does not move only horizontally
        if(!(Math.abs(deltaJ) == 2 && deltaI == 0))
            return false;

        if(this.hasMoved())
            return false;

        Piece pieceToCastleWithKing;
        if(deltaJ < 0) {
            if(!isValidKingSideCastling(board, start))
                return false;
            pieceToCastleWithKing = board.getSpot(start.getI(), start.getJ()+3).getPiece();
        }
        else {
            if(!isValidQueenSideCastling(board, start))
                return false;
            pieceToCastleWithKing = board.getSpot(start.getI(), start.getJ()-4).getPiece();
        }

        // the other piece is not of the same colour
        if(pieceToCastleWithKing.isWhite() != this.isWhite())
            return false;

        // the pieceToCastleWith should be a rook that hasn't been moved previously
        if(!(pieceToCastleWithKing instanceof Rook && !((Rook) pieceToCastleWithKing).hasMoved()))
            return false;

        // the king is not attacked after making the move
        return !this.isKingAttackedAfterMove(board, start, end);
    }

    private boolean isValidKingSideCastling(Board board, Spot start) {
        for(int j = 1; j < 3; j++) {
            Spot currentSpot = board.getSpot(start.getI(), start.getJ()+j);
            // a piece is between the king and rook
            if(currentSpot.getPiece() != null)
                return false;

            // the currentSpot is under attack and the king has to go through it
            if(isKingAttackedAfterMove(board, start, currentSpot))
                return false;
        }
        return true;
    }

    private boolean isValidQueenSideCastling(Board board, Spot start) {
        for(int j = 1; j < 4; j++) {
            Spot currentSpot = board.getSpot(start.getI(), start.getJ()-j);
            // a piece is between the king and rook
            if(currentSpot.getPiece() != null)
                return false;
            // the currentSpot is under attack and the king has to go through it
            if(j < 3 && isKingAttackedAfterMove(board, start, currentSpot)) {
                return false;
            }
        }
        return true;
    }

}