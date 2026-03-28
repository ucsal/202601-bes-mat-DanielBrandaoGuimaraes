package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.service.ParticipanteService;
import br.com.ucsal.olimpiadas.service.ProvaService;
import br.com.ucsal.olimpiadas.service.QuestaoService;
import br.com.ucsal.olimpiadas.service.TentativaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class ConsoleMenu {

    private final ParticipanteService participanteService;
    private final ProvaService provaService;
    private final QuestaoService questaoService;
    private final TentativaService tentativaService;
    private final ChessBoardRenderer boardRenderer;
    private final Scanner in;

    public ConsoleMenu(ParticipanteService participanteService,
                       ProvaService provaService,
                       QuestaoService questaoService,
                       TentativaService tentativaService,
                       ChessBoardRenderer boardRenderer,
                       Scanner in) {
        this.participanteService = participanteService;
        this.provaService = provaService;
        this.questaoService = questaoService;
        this.tentativaService = tentativaService;
        this.boardRenderer = boardRenderer;
        this.in = in;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n=== OLIMPÍADA DE QUESTÕES ===");
            System.out.println("1) Cadastrar participante");
            System.out.println("2) Cadastrar prova");
            System.out.println("3) Cadastrar questão em uma prova");
            System.out.println("4) Aplicar prova");
            System.out.println("5) Listar tentativas");
            System.out.println("0) Sair");
            System.out.print("> ");

            switch (in.nextLine().trim()) {
                case "1" -> cadastrarParticipante();
                case "2" -> cadastrarProva();
                case "3" -> cadastrarQuestao();
                case "4" -> aplicarProva();
                case "5" -> listarTentativas();
                case "0" -> { System.out.println("tchau"); return; }
                default -> System.out.println("opção inválida");
            }
        }
    }

    private void cadastrarParticipante() {
        System.out.print("Nome: ");
        var nome = in.nextLine();

        System.out.print("Email (opcional): ");
        var email = in.nextLine();

        try {
            var p = participanteService.cadastrar(nome, email);
            System.out.println("Participante cadastrado: " + p.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarProva() {
        System.out.print("Título da prova: ");
        var titulo = in.nextLine();

        try {
            var prova = provaService.cadastrar(titulo);
            System.out.println("Prova criada: " + prova.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarQuestao() {
        if (provaService.listarTodos().isEmpty()) {
            System.out.println("não há provas cadastradas");
            return;
        }

        var provaId = escolherProva();
        if (provaId == null) return;

        System.out.println("Enunciado:");
        var enunciado = in.nextLine();

        var alternativas = new String[5];
        for (int i = 0; i < 5; i++) {
            char letra = (char) ('A' + i);
            System.out.print("Alternativa " + letra + ": ");
            alternativas[i] = letra + ") " + in.nextLine();
        }

        System.out.print("Alternativa correta (A–E): ");
        char correta;
        try {
            correta = Questao.normalizar(in.nextLine().trim().charAt(0));
        } catch (Exception e) {
            System.out.println("alternativa inválida");
            return;
        }

        System.out.print("FEN inicial (deixe em branco para omitir): ");
        var fen = in.nextLine().trim();

        try {
            var q = questaoService.cadastrar(provaId, enunciado, alternativas, correta,
                    fen.isEmpty() ? null : fen);
            System.out.println("Questão cadastrada: " + q.getId() + " (na prova " + provaId + ")");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void aplicarProva() {
        if (participanteService.listarTodos().isEmpty()) {
            System.out.println("cadastre participantes primeiro");
            return;
        }
        if (provaService.listarTodos().isEmpty()) {
            System.out.println("cadastre provas primeiro");
            return;
        }

        var participanteId = escolherParticipante();
        if (participanteId == null) return;

        var provaId = escolherProva();
        if (provaId == null) return;

        var questoes = questaoService.buscarPorProvaId(provaId);
        if (questoes.isEmpty()) {
            System.out.println("esta prova não possui questões cadastradas");
            return;
        }

        System.out.println("\n--- Início da Prova ---");
        Map<Long, Character> respostas = new HashMap<>();

        for (var q : questoes) {
            System.out.println("\nQuestão #" + q.getId());
            System.out.println(q.getEnunciado());

            if (q.getFenInicial() != null && !q.getFenInicial().isBlank()) {
                System.out.println("Posição inicial:");
                boardRenderer.renderFen(q.getFenInicial());
            }

            for (var alt : q.getAlternativas()) System.out.println(alt);

            System.out.print("Sua resposta (A–E): ");
            char marcada;
            try {
                marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
            } catch (Exception e) {
                System.out.println("resposta inválida (marcando como errada)");
                marcada = 'X';
            }
            respostas.put(q.getId(), marcada);
        }

        var tentativa = tentativaService.realizar(participanteId, provaId, respostas);
        int nota = tentativaService.calcularNota(tentativa);

        System.out.println("\n--- Fim da Prova ---");
        System.out.println("Nota (acertos): " + nota + " / " + tentativa.getRespostas().size());
    }

    private void listarTentativas() {
        System.out.println("\n--- Tentativas ---");
        for (var t : tentativaService.listarTodos()) {
            System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n",
                    t.getId(), t.getParticipanteId(), t.getProvaId(),
                    tentativaService.calcularNota(t), t.getRespostas().size());
        }
    }

    private Long escolherParticipante() {
        System.out.println("\nParticipantes:");
        participanteService.listarTodos()
                .forEach(p -> System.out.printf("  %d) %s%n", p.getId(), p.getNome()));
        System.out.print("Escolha o id do participante: ");
        return lerIdValido(id -> participanteService.buscarPorId(id).isPresent());
    }

    private Long escolherProva() {
        System.out.println("\nProvas:");
        provaService.listarTodos()
                .forEach(p -> System.out.printf("  %d) %s%n", p.getId(), p.getTitulo()));
        System.out.print("Escolha o id da prova: ");
        return lerIdValido(id -> provaService.buscarPorId(id).isPresent());
    }

    private Long lerIdValido(Predicate<Long> existe) {
        try {
            long id = Long.parseLong(in.nextLine().trim());
            if (!existe.test(id)) {
                System.out.println("id inválido");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("entrada inválida");
            return null;
        }
    }
}
