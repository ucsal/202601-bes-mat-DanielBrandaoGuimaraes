package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;

import java.util.List;
import java.util.Optional;

public class ProvaService {

    private final ProvaRepository repository;

    public ProvaService(ProvaRepository repository) {
        this.repository = repository;
    }

    public Prova cadastrar(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("Título não pode ser vazio.");

        var prova = new Prova();
        prova.setTitulo(titulo.trim());
        return repository.salvar(prova);
    }

    public List<Prova> listarTodos() {
        return repository.listarTodos();
    }

    public Optional<Prova> buscarPorId(long id) {
        return repository.buscarPorId(id);
    }
}
