package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryQuestaoRepository implements QuestaoRepository {

    private final List<Questao> lista = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public Questao salvar(Questao questao) {
        if (questao.getId() == 0)
            questao.setId(proximoId++);
        lista.add(questao);
        return questao;
    }

    @Override
    public List<Questao> buscarPorProvaId(long provaId) {
        return lista.stream().filter(q -> q.getProvaId() == provaId).toList();
    }
}
