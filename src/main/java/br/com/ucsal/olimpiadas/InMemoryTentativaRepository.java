package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTentativaRepository implements TentativaRepository {

    private final List<Tentativa> lista = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public Tentativa salvar(Tentativa tentativa) {
        if (tentativa.getId() == 0)
            tentativa.setId(proximoId++);
        lista.add(tentativa);
        return tentativa;
    }

    @Override
    public List<Tentativa> listarTodos() {
        return List.copyOf(lista);
    }
}
