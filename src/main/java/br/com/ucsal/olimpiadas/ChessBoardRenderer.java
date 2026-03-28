package br.com.ucsal.olimpiadas.ui;

import java.io.PrintStream;

public class ChessBoardRenderer {

    private final PrintStream out;

    public ChessBoardRenderer(PrintStream out) {
        this.out = out;
    }

    public void renderFen(String fen) {
        if (fen == null || fen.isBlank()) return;

        String[] ranks = fen.split(" ")[0].split("/");

        out.println();
        out.println("    a b c d e f g h");
        out.println("   -----------------");

        for (int r = 0; r < 8; r++) {
            out.print((8 - r) + " | ");
            for (char c : ranks[r].toCharArray()) {
                if (Character.isDigit(c)) {
                    int vazios = c - '0';
                    for (int i = 0; i < vazios; i++) out.print(". ");
                } else {
                    out.print(c + " ");
                }
            }
            out.println("| " + (8 - r));
        }

        out.println("   -----------------");
        out.println("    a b c d e f g h");
        out.println();
    }
}
