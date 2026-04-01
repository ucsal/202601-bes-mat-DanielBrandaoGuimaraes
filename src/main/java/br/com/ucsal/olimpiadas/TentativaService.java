package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.List;

public class TentativaService {

    private TentativaRepository tentativaRepository;

    public TentativaService(TentativaRepository tentativaRepository) {
        this.tentativaRepository = tentativaRepository;
    }

    public Tentativa salvar(Tentativa tentativa) {
        return tentativaRepository.salvar(tentativa);
    }

    public int calcularNota(Tentativa tentativa) {
        int acertos = 0;
        for (Resposta r : tentativa.getRespostas()) {
            if (r.isCorreta()) {
                acertos++;
            }
        }
        return acertos;
    }

    public List<Tentativa> listarTodos() {
        return tentativaRepository.listarTodos();
    }
}
