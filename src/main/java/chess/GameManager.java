package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import chess.board.ChessBoard;
import chess.board.Location;
import chess.gamestate.GameState;
import chess.piece.Piece;
import chess.result.GameResult;
import chess.result.GameStatistic;
import chess.result.Score;
import chess.team.Team;

public class GameManager {
	private final ChessBoard chessBoard;
	private GameState gameState;

	public GameManager(Map<Location, Piece> pieces) {
		Objects.requireNonNull(pieces, "pieces의 정보가 없습니다.");
		this.chessBoard = new ChessBoard(pieces);
		this.gameState = GameState.RUNNING_BLACK_TURN;
	}

	public boolean isRunning() {
		return gameState.isGameRunning();
	}

	public void movePiece(Location now, Location destination) {
		checkTurn(now);
		chessBoard.move(now, destination);
		gameState = gameState.of(chessBoard.hasTwoKings());
	}

	private void checkTurn(Location now) {
		if (chessBoard.isTurn(now, gameState)) {
			throw new IllegalArgumentException("유저의 턴이 아닙니다.");
		}
	}

	public List<GameStatistic> createStatistics() {
		Score blackTeamScore = chessBoard.calculateScore(Team.WHITE);
		Score whiteTeamScore = chessBoard.calculateScore(Team.BLACK);

		List<GameStatistic> gameStatistics = new ArrayList<>();
		gameStatistics.add(
			new GameStatistic(Team.WHITE, whiteTeamScore, GameResult.findResult(whiteTeamScore, blackTeamScore)));
		gameStatistics.add(
			new GameStatistic(Team.BLACK, blackTeamScore, GameResult.findResult(blackTeamScore, whiteTeamScore)));
		return gameStatistics;
	}

	public Map<Location, Piece> getBoard() {
		return chessBoard.getBoard();
	}
}
