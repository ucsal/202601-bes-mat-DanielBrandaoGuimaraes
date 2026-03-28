package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.memory.*;
import br.com.ucsal.olimpiadas.service.*;
import br.com.ucsal.olimpiadas.ui.ChessBoardRenderer;
import br.com.ucsal.olimpiadas.ui.ConsoleMenu;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        var participanteRepo = new InMemoryParticipanteRepository();
        var provaRepo        = new InMemoryProvaRepository();
        var questaoRepo      = new InMemoryQuestaoRepository();
        var tentativaRepo    = new InMemoryTentativaRepository();

        var participanteService = new ParticipanteService(participanteRepo);
        var provaService        = new ProvaService(provaRepo);
        var questaoService      = new QuestaoService(questaoRepo, provaRepo);
        var tentativaService    = new TentativaService(tentativaRepo, questaoRepo);

        new DataSeeder(provaService, questaoService).executar();

        var menu = new ConsoleMenu(
                participanteService,
                provaService,
                questaoService,
                tentativaService,
                new ChessBoardRenderer(System.out),
                new Scanner(System.in)
        );

        menu.iniciar();
    }
}
