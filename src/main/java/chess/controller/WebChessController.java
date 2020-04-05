package chess.controller;

import chess.domain.board.Board;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebChessController {
    private Board board;

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public void run() {
        port(8080);
        staticFiles.location("/templates");

        get("/start", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("cells", null);
            return render(model, "index.html");
        });

        get("/chessGame", (req, res) -> {
            this.board = new Board();

            Map<String, Object> model = new HashMap<>();
            model.put("cells", this.board.getCells());
            model.put("currentTeam", this.board.getTeam().getName());
            model.put("error", null);
            return render(model, "index.html");
        });

        get("/initialization", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("cells", this.board.getCells());
            model.put("currentTeam", this.board.getTeam().getName());
            model.put("error", null);
            return render(model, "index.html");
        });
    }
}
