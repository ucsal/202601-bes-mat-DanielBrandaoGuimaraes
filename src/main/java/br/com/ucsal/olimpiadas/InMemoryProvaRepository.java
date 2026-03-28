package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProvaRepository implements ProvaRepository {

    private final List<Prova> lista = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public Prova salvar(Prova prova) {
        if (prova.getId() == 0)
            prova.setId(proximoId++);
        lista.add(prova);
        return prova;
    }

    @Override
    public Optional<Prova> buscarPorId(long id) {
        return lista.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Prova> listarTodos() {
        return List.copyOf(lista);
    }
}
