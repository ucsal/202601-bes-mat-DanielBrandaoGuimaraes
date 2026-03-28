package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.repository.memory.InMemoryProvaRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryQuestaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestaoServiceTest {

    private ProvaService provaService;
    private QuestaoService questaoService;

    private static final String[] ALTS = { "A) op1", "B) op2", "C) op3", "D) op4", "E) op5" };

    @BeforeEach
    void setUp() {
        var provaRepo = new InMemoryProvaRepository();
        provaService = new ProvaService(provaRepo);
        questaoService = new QuestaoService(new InMemoryQuestaoRepository(), provaRepo);
    }

    @Test
    void deveCadastrarQuestaoEmProvaExistente() {
        var prova = provaService.cadastrar("Prova Teste");

        var q = questaoService.cadastrar(prova.getId(), "Enunciado?", ALTS, 'B', null);

        assertNotNull(q);
        assertTrue(q.getId() > 0);
        assertEquals(prova.getId(), q.getProvaId());
        assertEquals('B', q.getAlternativaCorreta());
    }

    @Test
    void deveLancarExcecaoParaProvaInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> questaoService.cadastrar(999L, "Enunciado?", ALTS, 'A', null));
    }

    @Test
    void deveLancarExcecaoParaEnunciadoVazio() {
        var prova = provaService.cadastrar("Prova Teste");

        assertThrows(IllegalArgumentException.class,
                () -> questaoService.cadastrar(prova.getId(), "", ALTS, 'A', null));
    }

    @Test
    void deveLancarExcecaoParaAlternativaInvalida() {
        var prova = provaService.cadastrar("Prova Teste");

        assertThrows(IllegalArgumentException.class,
                () -> questaoService.cadastrar(prova.getId(), "Enunciado?", ALTS, 'F', null));
    }

    @Test
    void deveLancarExcecaoParaMenosDeCincoAlternativas() {
        var prova = provaService.cadastrar("Prova Teste");

        assertThrows(IllegalArgumentException.class,
                () -> questaoService.cadastrar(prova.getId(), "Enunciado?", new String[]{"A) só uma"}, 'A', null));
    }

    @Test
    void deveBuscarQuestoesPorProva() {
        var p1 = provaService.cadastrar("Prova 1");
        var p2 = provaService.cadastrar("Prova 2");

        questaoService.cadastrar(p1.getId(), "Q1", ALTS, 'A', null);
        questaoService.cadastrar(p1.getId(), "Q2", ALTS, 'B', null);
        questaoService.cadastrar(p2.getId(), "Q3", ALTS, 'C', null);

        assertEquals(2, questaoService.buscarPorProvaId(p1.getId()).size());
        assertEquals(1, questaoService.buscarPorProvaId(p2.getId()).size());
    }

    @Test
    void deveAceitarLetraMinuscula() {
        var prova = provaService.cadastrar("Prova Teste");

        var q = questaoService.cadastrar(prova.getId(), "Enunciado?", ALTS, 'c', null);

        assertEquals('C', q.getAlternativaCorreta());
    }
}
