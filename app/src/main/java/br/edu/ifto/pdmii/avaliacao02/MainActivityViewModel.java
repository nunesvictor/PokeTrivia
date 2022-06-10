package br.edu.ifto.pdmii.avaliacao02;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifto.pdmii.avaliacao02.data.PokemonDataSource;
import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;
import br.edu.ifto.pdmii.avaliacao02.repository.PokemonRepository;
import br.edu.ifto.pdmii.avaliacao02.repository.Result;

public class MainActivityViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Pokemon>> pokemonsLiveData = new MutableLiveData<>(new ArrayList<>());

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        PokemonDataSource dataSource = new PokemonDataSource();
        PokemonRepository pokemonRepository = PokemonRepository.getInstance(dataSource);

        pokemonRepository.getPokemons(result -> {
            if (result instanceof Result.Success) {
                pokemonsLiveData.postValue(((Result.Success<List<Pokemon>>) result).data);
            }
        });
    }

    public MutableLiveData<List<Pokemon>> getPokemonsLiveData() {
        return pokemonsLiveData;
    }
}
