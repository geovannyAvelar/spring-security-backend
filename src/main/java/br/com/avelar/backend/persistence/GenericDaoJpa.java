package br.com.avelar.backend.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.avelar.backend.util.ProducerOnly;

@Dependent
@ProducerOnly
public class GenericDaoJpa<T> {

    static final Logger log = LoggerFactory.getLogger(GenericDaoJpa.class);

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    private Class<T> entityClass;

    public GenericDaoJpa() {
        
    }

    public GenericDaoJpa(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public Session getHibernateSession() {
        return em.unwrap(org.hibernate.Session.class);
    }

    @SuppressWarnings("unchecked")
    public List<T> appendQueriesMultipleResults(String namedQuery, String appendPart,
            Map<String, ? extends Object> nomeValor) {
        String queryToString = em.createNamedQuery(namedQuery).unwrap(org.hibernate.Query.class)
                .getQueryString();
        StringBuilder hql = new StringBuilder(queryToString);
        hql.append(appendPart);
        String queryToStringFinal = hql.toString();
        try {
            javax.persistence.Query queryFinal = em.createQuery(queryToStringFinal);
            if (nomeValor != null && !nomeValor.isEmpty()) {
                for (Entry<String, ? extends Object> chave : nomeValor.entrySet()) {
                    queryFinal.setParameter(chave.getKey(), chave.getValue());
                }
            }
            return queryFinal.getResultList();
        } catch (Exception e) {
            log.error("Erro ao montar query: ", e);
        }

        return new ArrayList<T>();
    }

    public T find(Serializable pk) {
        return getEntityManager().find(entityClass, pk);
    }

    public T getReference(Long id) {
        return em.getReference(entityClass, id);
    }

    public void persist(T entity) {
        getEntityManager().persist(entity);
    }

    public T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    public void delete(T entity) {
        getEntityManager().remove(entity);
    }

    public void flush() {
        getEntityManager().flush();
    }

    public QueryBuilder<T> namedQuery(String name) {
        return new QueryBuilder<T>(createNamedQuery(name));
    }

    public QueryBuilder<T> all() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        return new QueryBuilder<T>(em.createQuery(cq.select(cq.from(entityClass))));
    }

    protected TypedQuery<T> createNamedQuery(String namedQuery) {
        return getEntityManager().createNamedQuery(namedQuery, entityClass);
    }

    public Query createSimpleNamedQuery(String namedQuery) {
        return getEntityManager().createNamedQuery(namedQuery);
    }

    public void remove(Object object) {
        em.remove(object);
    }

    public void evict() {
        getEntityManager().getEntityManagerFactory().getCache().evict(entityClass);
    }

    public void evictAllDAOs() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    protected void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Query createQuery(String qlString) {
        return em.createQuery(qlString);
    }

    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> findAll(Class<T> classe) {
        StringBuilder ql = new StringBuilder();
        ql.append("select o from ").append(classe.getSimpleName()).append(" o");
        Query query = getEntityManager().createQuery(ql.toString());
        return query.getResultList();
    }

    public String queryToString(String namedQuery) {
        return em.createNamedQuery(namedQuery).unwrap(org.hibernate.Query.class).getQueryString();
    }

}
