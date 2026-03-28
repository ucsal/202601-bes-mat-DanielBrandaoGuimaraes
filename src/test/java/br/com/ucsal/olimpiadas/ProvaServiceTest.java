package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.repository.memory.InMemoryProvaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProvaServiceTest {

    private ProvaService service;

    @BeforeEach
    void setUp() {
        service = new ProvaService(new InMemoryProvaRepository());
    }

    @Test
    void deveCadastrarProvaComSucesso() {
        var prova = service.cadastrar("Olimpíada 2026");

        assertNotNull(prova);
        assertTrue(prova.getId() > 0);
        assertEquals("Olimpíada 2026", prova.getTitulo());
    }

    @Test
    void deveLancarExcecaoParaTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(""));
    }

    @Test
    void deveLancarExcecaoParaTituloNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
    }

    @Test
    void deveListarTodasAsProvas() {
        service.cadastrar("Prova A");
        service.cadastrar("Prova B");

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveBuscarProvaPorId() {
        var prova = service.cadastrar("Prova X");

        var resultado = service.buscarPorId(prova.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Prova X", resultado.get().getTitulo());
    }

    @Test
    void deveBuscarIdInexistenteRetornarVazio() {
        assertTrue(service.buscarPorId(42L).isEmpty());
    }
}
