package br.com.avelar.backend.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import br.com.avelar.backend.util.ProducerOnly;

@Dependent
public class GenericDaoJpaProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    @SuppressWarnings("unchecked")
    public <T> GenericDaoJpa<T> produce(InjectionPoint ip, BeanManager bm) {
        Bean<? extends Object> bean = bm.resolve(bm.getBeans(GenericDaoJpa.class,
                GenericDaoJpa.class.getAnnotationsByType(ProducerOnly.class)));

        CreationalContext<? extends Object> ctx = bm.createCreationalContext(bean);
        GenericDaoJpa<T> dao = (GenericDaoJpa<T>) bm.getReference(bean, bean.getBeanClass(), ctx);

        Class<T> entityClass = (Class<T>) ((ParameterizedType) ip.getType())
                .getActualTypeArguments()[0];
        dao.setEntityClass(entityClass);

        return dao;
    }
}
