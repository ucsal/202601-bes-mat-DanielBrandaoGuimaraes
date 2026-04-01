package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

import java.util.List;

public class ParticipanteService {

    private ParticipanteRepository repository;

    public ParticipanteService(ParticipanteRepository repository) {
        this.repository = repository;
    }

    public Participante cadastrar(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome inválido");
        }

        Participante p = new Participante();
        p.setNome(nome);
        p.setEmail(email);
        return repository.salvar(p);
    }

    public Participante buscarPorId(long id) {
        return repository.buscarPorId(id);
    }

    public List<Participante> listarTodos() {
        return repository.listarTodos();
    }
}
