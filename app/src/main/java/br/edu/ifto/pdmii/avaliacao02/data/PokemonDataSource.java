package br.edu.ifto.pdmii.avaliacao02.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.edu.ifto.pdmii.avaliacao02.http.HttpClient;
import br.edu.ifto.pdmii.avaliacao02.model.NamedAPIResourceList;
import br.edu.ifto.pdmii.avaliacao02.model.NamedApiResource;
import br.edu.ifto.pdmii.avaliacao02.model.Pokemon;
import br.edu.ifto.pdmii.avaliacao02.repository.RepositoryCallback;
import br.edu.ifto.pdmii.avaliacao02.repository.Result;
import br.edu.ifto.pdmii.avaliacao02.util.RandomInt;

public class PokemonDataSource {
    public void getPokemons(RepositoryCallback<List<Pokemon>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final List<Pokemon> pokemons = new ArrayList<>();

        executor.execute(() -> {
            try {
                NamedAPIResourceList result = makeSynchronousFetchNamedApiRequest();

                for (NamedApiResource namedApiResource : result.getResults()) {
                    Pokemon pokemon = makeSynchronousFetchPokemonRequest(
                            namedApiResource.getUrl());
                    pokemons.add(pokemon);
                }

                callback.onComplete(new Result.Success<>(pokemons));
            } catch (Exception e) {
                e.printStackTrace();
                callback.onComplete(new Result.Error<>(e));
            }
        });
    }

    private NamedAPIResourceList makeSynchronousFetchNamedApiRequest() throws IOException {
        int limit = RandomInt.between(10, 20);
        int offset = RandomInt.between(0, 1126 - limit);

        String templateUrl = "https://pokeapi.co/api/v2/pokemon?limit=%d&offset=%d";
        String url = String.format(Locale.getDefault(), templateUrl, limit, offset);

        Gson gson = new Gson();
        String data = new HttpClient().get(url);
        Type type = new TypeToken<NamedAPIResourceList>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    private Pokemon makeSynchronousFetchPokemonRequest(String url) throws IOException {
        Gson gson = new Gson();
        String data = new HttpClient().get(url);
        Type type = new TypeToken<Pokemon>() {
        }.getType();

        return gson.fromJson(data, type);
    }
}
