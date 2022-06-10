package br.edu.ifto.pdmii.avaliacao02.repository;

import java.util.List;

import br.edu.ifto.pdmii.avaliacao02.data.PokemonDataSource;
import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;

public class PokemonRepository {
    private static volatile PokemonRepository instance;
    private PokemonDataSource dataSource;

    private PokemonRepository(PokemonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static PokemonRepository getInstance(PokemonDataSource dataSource) {
        if (instance == null) {
            instance = new PokemonRepository(dataSource);
        }

        return instance;
    }

    public void getPokemons(final RepositoryCallback<List<Pokemon>> callback) {
        dataSource.getPokemons(callback);
    }
}