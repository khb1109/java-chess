package chess.piece;

import java.util.Map;

import chess.board.Location;
import chess.team.Team;

public abstract class Piece {
	protected final char name;

	Piece(char name) {
		this.name = name;
	}

	public abstract boolean canMove(Location now, Location after);

	public abstract double getScore(boolean hasVerticalEnemy);

	protected boolean isBlack() {
		return Character.isUpperCase(name);
	}

	public boolean isSameTeam(boolean black) {
		return isBlack() == black;
	}

	public boolean isSameTeam(Team blackTeam) {
		return blackTeam == Team.of(isBlack());
	}

	public boolean isSameTeam(Piece piece) {
		return isBlack() == piece.isBlack();
	}

	public boolean hasObstacle(Map<Location, Piece> board, Location now, Location destination) {
		for (int weight = 1; ; weight++) {
			Location nowLocation = now.calculateNextLocation(destination, weight);
			if (nowLocation.equals(destination)) {
				break;
			}
			System.out.println(nowLocation);
			if (board.containsKey(nowLocation)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.valueOf(name);

	}
}
