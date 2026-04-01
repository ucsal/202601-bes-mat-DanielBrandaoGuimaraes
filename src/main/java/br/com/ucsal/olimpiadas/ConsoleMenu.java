package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.service.ParticipanteService;
import br.com.ucsal.olimpiadas.service.ProvaService;
import br.com.ucsal.olimpiadas.service.QuestaoService;
import br.com.ucsal.olimpiadas.service.TentativaService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private ParticipanteService participanteService;
    private ProvaService provaService;
    private QuestaoService questaoService;
    private TentativaService tentativaService;
    private Scanner in;

    public ConsoleMenu(ParticipanteService participanteService,
                       ProvaService provaService,
                       QuestaoService questaoService,
                       TentativaService tentativaService,
                       Scanner in) {
        this.participanteService = participanteService;
        this.provaService = provaService;
        this.questaoService = questaoService;
        this.tentativaService = tentativaService;
        this.in = in;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n=== OLIMPÍADA DE QUESTÕES (V1) ===");
            System.out.println("1) Cadastrar participante");
            System.out.println("2) Cadastrar prova");
            System.out.println("3) Cadastrar questão (A–E) em uma prova");
            System.out.println("4) Aplicar prova (selecionar participante + prova)");
            System.out.println("5) Listar tentativas (resumo)");
            System.out.println("0) Sair");
            System.out.print("> ");

            String opcao = in.nextLine();

            if (opcao.equals("1")) {
                cadastrarParticipante();
            } else if (opcao.equals("2")) {
                cadastrarProva();
            } else if (opcao.equals("3")) {
                cadastrarQuestao();
            } else if (opcao.equals("4")) {
                aplicarProva();
            } else if (opcao.equals("5")) {
                listarTentativas();
            } else if (opcao.equals("0")) {
                System.out.println("tchau");
                return;
            } else {
                System.out.println("opção inválida");
            }
        }
    }

    private void cadastrarParticipante() {
        System.out.print("Nome: ");
        String nome = in.nextLine();

        System.out.print("Email (opcional): ");
        String email = in.nextLine();

        try {
            participanteService.cadastrar(nome, email);
            System.out.println("Participante cadastrado: " + participanteService.listarTodos().get(participanteService.listarTodos().size() - 1).getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarProva() {
        System.out.print("Título da prova: ");
        String titulo = in.nextLine();

        try {
            provaService.cadastrar(titulo);
            System.out.println("Prova criada: " + provaService.listarTodos().get(provaService.listarTodos().size() - 1).getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarQuestao() {
        if (provaService.listarTodos().isEmpty()) {
            System.out.println("não há provas cadastradas");
            return;
        }

        Long provaId = escolherProva();
        if (provaId == null) {
            return;
        }

        System.out.println("Enunciado:");
        String enunciado = in.nextLine();

        String[] alternativas = new String[5];
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

        try {
            Questao q = questaoService.cadastrar(provaId, enunciado, alternativas, correta, null);
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

        Long participanteId = escolherParticipante();
        if (participanteId == null) {
            return;
        }

        Long provaId = escolherProva();
        if (provaId == null) {
            return;
        }

        List<Questao> questoes = questaoService.buscarPorProvaId(provaId);
        if (questoes.isEmpty()) {
            System.out.println("esta prova não possui questões cadastradas");
            return;
        }

        Tentativa tentativa = new Tentativa();
        tentativa.setParticipanteId(participanteId);
        tentativa.setProvaId(provaId);

        System.out.println("\n--- Início da Prova ---");

        for (Questao q : questoes) {
            System.out.println("\nQuestão #" + q.getId());
            System.out.println(q.getEnunciado());

            if (q.getFenInicial() != null && !q.getFenInicial().isBlank()) {
                System.out.println("Posição inicial:");
                imprimirTabuleiro(q.getFenInicial());
            }

            for (String alt : q.getAlternativas()) {
                System.out.println(alt);
            }

            System.out.print("Sua resposta (A–E): ");
            char marcada;
            try {
                marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
            } catch (Exception e) {
                System.out.println("resposta inválida (marcando como errada)");
                marcada = 'X';
            }

            Resposta r = new Resposta();
            r.setQuestaoId(q.getId());
            r.setAlternativaMarcada(marcada);
            r.setCorreta(q.isRespostaCorreta(marcada));

            tentativa.getRespostas().add(r);
        }

        tentativaService.salvar(tentativa);

        int nota = tentativaService.calcularNota(tentativa);
        System.out.println("\n--- Fim da Prova ---");
        System.out.println("Nota (acertos): " + nota + " / " + tentativa.getRespostas().size());
    }

    private void listarTentativas() {
        System.out.println("\n--- Tentativas ---");
        for (Tentativa t : tentativaService.listarTodos()) {
            System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n",
                    t.getId(), t.getParticipanteId(), t.getProvaId(),
                    tentativaService.calcularNota(t), t.getRespostas().size());
        }
    }

    private Long escolherParticipante() {
        System.out.println("\nParticipantes:");
        for (br.com.ucsal.olimpiadas.model.Participante p : participanteService.listarTodos()) {
            System.out.printf("  %d) %s%n", p.getId(), p.getNome());
        }
        System.out.print("Escolha o id do participante: ");
        try {
            long id = Long.parseLong(in.nextLine());
            if (participanteService.buscarPorId(id) == null) {
                System.out.println("id inválido");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("entrada inválida");
            return null;
        }
    }

    private Long escolherProva() {
        System.out.println("\nProvas:");
        for (br.com.ucsal.olimpiadas.model.Prova p : provaService.listarTodos()) {
            System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
        }
        System.out.print("Escolha o id da prova: ");
        try {
            long id = Long.parseLong(in.nextLine());
            if (provaService.buscarPorId(id) == null) {
                System.out.println("id inválido");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("entrada inválida");
            return null;
        }
    }

    private void imprimirTabuleiro(String fen) {
        String parteTabuleiro = fen.split(" ")[0];
        String[] ranks = parteTabuleiro.split("/");

        System.out.println();
        System.out.println("    a b c d e f g h");
        System.out.println("   -----------------");

        for (int r = 0; r < 8; r++) {
            System.out.print((8 - r) + " | ");
            for (char c : ranks[r].toCharArray()) {
                if (Character.isDigit(c)) {
                    int vazios = c - '0';
                    for (int i = 0; i < vazios; i++) {
                        System.out.print(". ");
                    }
                } else {
                    System.out.print(c + " ");
                }
            }
            System.out.println("| " + (8 - r));
        }

        System.out.println("   -----------------");
        System.out.println("    a b c d e f g h");
        System.out.println();
    }
}
