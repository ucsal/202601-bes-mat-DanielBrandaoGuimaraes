package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;
import br.com.ucsal.olimpiadas.service.ParticipanteService;
import br.com.ucsal.olimpiadas.service.ProvaService;
import br.com.ucsal.olimpiadas.service.QuestaoService;
import br.com.ucsal.olimpiadas.service.TentativaService;
import br.com.ucsal.olimpiadas.ui.ConsoleMenu;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        ParticipanteRepository participanteRepository = new ParticipanteRepository();
        ProvaRepository provaRepository = new ProvaRepository();
        QuestaoRepository questaoRepository = new QuestaoRepository();
        TentativaRepository tentativaRepository = new TentativaRepository();

        ParticipanteService participanteService = new ParticipanteService(participanteRepository);
        ProvaService provaService = new ProvaService(provaRepository);
        QuestaoService questaoService = new QuestaoService(questaoRepository, provaRepository);
        TentativaService tentativaService = new TentativaService(tentativaRepository);

        seed(provaService, questaoService);

        ConsoleMenu menu = new ConsoleMenu(
                participanteService,
                provaService,
                questaoService,
                tentativaService,
                new Scanner(System.in)
        );

        menu.iniciar();
    }

    private static void seed(ProvaService provaService, QuestaoService questaoService) {
        provaService.cadastrar("Olimpíada 2026 • Nível 1 • Prova A");

        long provaId = provaService.listarTodos().get(0).getId();

        String enunciado = "Questão 1 — Mate em 1.\n"
                + "É a vez das brancas.\n"
                + "Encontre o lance que dá mate imediatamente.";

        String[] alternativas = new String[] { "A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#" };

        questaoService.cadastrar(provaId, enunciado, alternativas, 'C', "6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1");
    }
}
