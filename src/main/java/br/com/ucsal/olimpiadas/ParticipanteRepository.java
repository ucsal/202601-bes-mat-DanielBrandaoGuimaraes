package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Participante;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteRepository {

    private List<Participante> lista = new ArrayList<>();
    private long proximoId = 1;

    public Participante salvar(Participante participante) {
        if (participante.getId() == 0) {
            participante.setId(proximoId++);
        }
        lista.add(participante);
        return participante;
    }

    public Participante buscarPorId(long id) {
        for (Participante p : lista) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Participante> listarTodos() {
        return lista;
    }
}
