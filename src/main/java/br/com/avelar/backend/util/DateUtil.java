package br.com.avelar.backend.util;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;

@Stateless
public class DateUtil {
    
    public Date sumSecondsToDate(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }
    
}
