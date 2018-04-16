package br.com.avelar.backend.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.avelar.backend.model.OAuthToken;
import br.com.avelar.backend.model.User;
import br.com.avelar.backend.persistence.GenericDaoJpa;
import br.com.avelar.backend.util.DateUtil;

@Stateless
public class OAuthTokenService {

    @Inject
    private GenericDaoJpa<OAuthToken> tokenDao;

    @Inject
    private DateUtil dateUtil;

    public void registerToken(String token, User user) {
        OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setToken(token);
        oAuthToken.setUser(user);
        oAuthToken.setExpires(dateUtil.sumSecondsToDate(new Date(), 1200));
        tokenDao.persist(oAuthToken);
    }

    public OAuthToken getTokenInfo(String token) {
        return tokenDao.find(token);
    }
    
    public void revokeToken(String token) {
        OAuthToken t = getTokenInfo(token);
        tokenDao.delete(t);
    }

    public OAuthToken getTokenByUser(User user) {
        List tokens = tokenDao.createQuery("select t from OAuthToken t where t.user = :user")
                                       .setParameter("user", user)
                                   .getResultList();

        if(!tokens.isEmpty()) {
            return (OAuthToken) tokens.get(0);
        }

        return null;

    }

}
