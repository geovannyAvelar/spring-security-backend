package br.com.avelar.backend.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

public class QueryBuilder<T> {

    private TypedQuery<T> query;

    public QueryBuilder(TypedQuery<T> query) {
        this.query = query;
    }

    public QueryBuilder<T> param(String nome, Object valor) {
        query.setParameter(nome, valor);
        return this;
    }

    public QueryBuilder<T> cacheable() {
        return cacheable(true);
    }

    public QueryBuilder<T> cacheable(boolean cacheable) {
        query.setHint("org.hibernate.cacheable", cacheable);
        return this;
    }

    public QueryBuilder<T> skip(int startPosition) {
        query.setFirstResult(startPosition);
        return this;
    }

    public QueryBuilder<T> take(int maxResult) {
        query.setMaxResults(maxResult);
        return this;
    }

    public List<T> find() {
        return query.getResultList();
    }

    public Optional<T> findUnique() {
        query.setMaxResults(2);

        List<T> results = find();
        if (results.size() > 1) {
            throw new NonUniqueResultException();
        }

        return find().stream().findFirst();
    }

    public int executeUpdate() {
        return query.executeUpdate();
    }

}
