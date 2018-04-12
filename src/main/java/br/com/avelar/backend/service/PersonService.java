package br.com.avelar.backend.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.avelar.backend.model.Person;
import br.com.avelar.backend.persistence.GenericDaoJpa;

@Stateless
public class PersonService {

    @Inject
    private GenericDaoJpa<Person> personDao;

    public void savePerson(Person person) {
        personDao.persist(person);
    }

    public List<Person> findAll() {
        return personDao.all().find();
    }

    public void deletePerson(Person person) {
        Person p = personDao.find(person.getId());

        if (p != null) {
            personDao.delete(p);
        }
    }

}
