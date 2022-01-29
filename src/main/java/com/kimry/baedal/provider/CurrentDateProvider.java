package com.kimry.baedal.provider;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CurrentDateProvider {

    public String getDate(){

        return OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZ"));

    }
}
