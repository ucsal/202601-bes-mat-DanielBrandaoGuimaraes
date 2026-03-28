package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.repository.memory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TentativaServiceTest {

    private ProvaService provaService;
    private QuestaoService questaoService;
    private TentativaService tentativaService;

    private static final String[] ALTS = { "A) op1", "B) op2", "C) op3", "D) op4", "E) op5" };

    @BeforeEach
    void setUp() {
        var provaRepo = new InMemoryProvaRepository();
        var questaoRepo = new InMemoryQuestaoRepository();

        provaService = new ProvaService(provaRepo);
        questaoService = new QuestaoService(questaoRepo, provaRepo);
        tentativaService = new TentativaService(new InMemoryTentativaRepository(), questaoRepo);
    }

    @Test
    void deveRegistrarTentativaECalcularNota() {
        var prova = provaService.cadastrar("Prova");
        var q1 = questaoService.cadastrar(prova.getId(), "P1?", ALTS, 'A', null);
        var q2 = questaoService.cadastrar(prova.getId(), "P2?", ALTS, 'B', null);

        var tentativa = tentativaService.realizar(1L, prova.getId(), Map.of(q1.getId(), 'A', q2.getId(), 'C'));

        assertEquals(2, tentativa.getRespostas().size());
        assertEquals(1, tentativaService.calcularNota(tentativa));
    }

    @Test
    void deveRetornarZeroQuandoTodasErradas() {
        var prova = provaService.cadastrar("Prova");
        var q1 = questaoService.cadastrar(prova.getId(), "P1?", ALTS, 'A', null);
        var q2 = questaoService.cadastrar(prova.getId(), "P2?", ALTS, 'A', null);

        var tentativa = tentativaService.realizar(1L, prova.getId(), Map.of(q1.getId(), 'B', q2.getId(), 'C'));

        assertEquals(0, tentativaService.calcularNota(tentativa));
    }

    @Test
    void deveRetornarNotaMaximaQuandoTodasCorretas() {
        var prova = provaService.cadastrar("Prova");
        var q1 = questaoService.cadastrar(prova.getId(), "P1?", ALTS, 'A', null);
        var q2 = questaoService.cadastrar(prova.getId(), "P2?", ALTS, 'B', null);
        var q3 = questaoService.cadastrar(prova.getId(), "P3?", ALTS, 'C', null);

        var tentativa = tentativaService.realizar(1L, prova.getId(),
                Map.of(q1.getId(), 'A', q2.getId(), 'B', q3.getId(), 'C'));

        assertEquals(3, tentativaService.calcularNota(tentativa));
    }

    @Test
    void deveLancarExcecaoParaProvaSemQuestoes() {
        var prova = provaService.cadastrar("Prova Vazia");

        assertThrows(IllegalStateException.class,
                () -> tentativaService.realizar(1L, prova.getId(), Map.of()));
    }

    @Test
    void deveListarTodasAsTentativas() {
        var prova = provaService.cadastrar("Prova");
        var q = questaoService.cadastrar(prova.getId(), "P?", ALTS, 'A', null);
        var r = Map.of(q.getId(), 'A');

        tentativaService.realizar(1L, prova.getId(), r);
        tentativaService.realizar(2L, prova.getId(), r);

        assertEquals(2, tentativaService.listarTodos().size());
    }

    @Test
    void deveContarRespostaXComoErrada() {
        var prova = provaService.cadastrar("Prova");
        var q = questaoService.cadastrar(prova.getId(), "P?", ALTS, 'A', null);

        var tentativa = tentativaService.realizar(1L, prova.getId(), Map.of(q.getId(), 'X'));

        assertEquals(0, tentativaService.calcularNota(tentativa));
    }
}
