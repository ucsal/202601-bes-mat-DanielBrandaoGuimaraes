package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Questao;

import java.util.ArrayList;
import java.util.List;

public class QuestaoRepository {

    private List<Questao> lista = new ArrayList<>();
    private long proximoId = 1;

    public Questao salvar(Questao questao) {
        if (questao.getId() == 0) {
            questao.setId(proximoId++);
        }
        lista.add(questao);
        return questao;
    }

    public List<Questao> buscarPorProvaId(long provaId) {
        List<Questao> resultado = new ArrayList<>();
        for (Questao q : lista) {
            if (q.getProvaId() == provaId) {
                resultado.add(q);
            }
        }
        return resultado;
    }
}
