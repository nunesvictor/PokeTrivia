package br.edu.ifto.pdmii.avaliacao02.repository;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}