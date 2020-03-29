package chess;

import java.util.HashMap;
import java.util.Map;

import chess.board.Location;
import chess.piece.Bishop;
import chess.piece.King;
import chess.piece.Knight;
import chess.piece.Pawn;
import chess.piece.Piece;
import chess.piece.Queen;
import chess.piece.Rook;
import chess.team.Team;

public class PieceFactory {
	public static Map<Location, Piece> createPieces() {
		Map<Location, Piece> pieces = new HashMap<>();

		putNoble(pieces, 1, Team.WHITE);
		putPawns(pieces, 2, Team.WHITE);

		putPawns(pieces, 7, Team.BLACK);
		putNoble(pieces, 8, Team.BLACK);

		return pieces;
	}

	private static void putNoble(Map<Location, Piece> pieces, int row, Team team) {
		pieces.put(Location.of(row, 'a'), new Rook(team));
		pieces.put(Location.of(row, 'b'), new Knight(team));
		pieces.put(Location.of(row, 'c'), new Bishop(team));
		pieces.put(Location.of(row, 'd'), new Queen(team));
		pieces.put(Location.of(row, 'e'), new King(team));
		pieces.put(Location.of(row, 'f'), new Bishop(team));
		pieces.put(Location.of(row, 'g'), new Knight(team));
		pieces.put(Location.of(row, 'h'), new Rook(team));
	}

	private static void putPawns(Map<Location, Piece> pieces, int row, Team team) {
		for (int i = 0; i < 8; i++) {
			pieces.put(Location.of(row, (char)(i + 'a')), new Pawn(team));
		}
	}
}
