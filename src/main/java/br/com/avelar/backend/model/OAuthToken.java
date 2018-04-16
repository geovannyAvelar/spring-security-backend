package br.com.avelar.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OAUTH_TOKENS")
public class OAuthToken {

    @Id
    @Column(name = "TOKEN")
    @NotNull
    private String token;

    @Column(name = "EXPIRES")
    @NotNull
    private Date expires;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "username")
    @NotNull
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean valid() {
        return user.isEnabled() && expires.after(new Date());
    }
    
    public Long getTimeToExpire() {
       Long expiresTimestamp = (expires.getTime() - new Date().getTime()) / 1000;
       
       if(expiresTimestamp < 0) {
           expiresTimestamp = 0l;
       }
       
       return expiresTimestamp;
       
    }
    
}
