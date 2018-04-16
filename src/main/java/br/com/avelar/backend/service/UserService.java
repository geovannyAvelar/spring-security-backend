package br.com.avelar.backend.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.avelar.backend.model.User;
import br.com.avelar.backend.persistence.GenericDaoJpa;

@Stateless
public class UserService {

    @Inject
    private GenericDaoJpa<User> userDao;
    
    public User findUserByUsername(String username) {
        return userDao.find(username);
    }
    
}
