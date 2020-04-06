package chess.controller;

import chess.dao.ChessBoardDAO;
import chess.domain.GameResult;
import chess.domain.board.Board;
import chess.domain.command.MoveCommand;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebChessController {
    private ChessBoardDAO chessBoardDAO;
    private Board board;
    private GameResult gameResult;

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public void run() {
        port(8080);
        staticFiles.location("/static");

        this.chessBoardDAO = ChessBoardDAO.getInstance();
        if (chessBoardDAO.getBoard() == null) {
            this.board = new Board();
        } else {
            this.board = chessBoardDAO.getBoard();
        }
        this.gameResult = this.board.createGameResult();

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        get("/chessGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("cells", this.board.getCells());
            model.put("currentTeam", this.board.getTeam().getName());
            model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
            model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
            return render(model, "index.html");
        });

        get("/newChessGame", (req, res) -> {
            this.board = new Board();
            this.gameResult = this.board.createGameResult();

            Map<String, Object> model = new HashMap<>();
            model.put("cells", this.board.getCells());
            model.put("currentTeam", this.board.getTeam().getName());
            model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
            model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
            return render(model, "index.html");
        });

        post("/move", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            try {
                String source = req.queryParams("source");
                String target = req.queryParams("target");

                this.board.move(new MoveCommand(String.format("move %s %s", source, target)));

                if (this.board.isGameOver()) {
                    GameResult gameResult = this.board.createGameResult();
                    model.put("winner", gameResult.getWinner());
                    model.put("loser", gameResult.getLoser());
                    model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
                    model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

                    chessBoardDAO.deletePreviousBoard();
                    chessBoardDAO.closeConnection();
                    this.board = new Board();

                    return render(model, "winner.html");
                }
            } catch (Exception e) {
                model.put("error", e.getMessage());
            }
            this.gameResult = this.board.createGameResult();

            model.put("cells", this.board.getCells());
            model.put("currentTeam", this.board.getTeam().getName());
            model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
            model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

            chessBoardDAO.deletePreviousBoard();
            chessBoardDAO.saveBoard(board.createBoardDTO());
            return render(model, "index.html");
        });

    }
}
