package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Prova;

import java.util.ArrayList;
import java.util.List;

public class ProvaRepository {

    private List<Prova> lista = new ArrayList<>();
    private long proximoId = 1;

    public Prova salvar(Prova prova) {
        if (prova.getId() == 0) {
            prova.setId(proximoId++);
        }
        lista.add(prova);
        return prova;
    }

    public Prova buscarPorId(long id) {
        for (Prova p : lista) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Prova> listarTodos() {
        return lista;
    }
}
