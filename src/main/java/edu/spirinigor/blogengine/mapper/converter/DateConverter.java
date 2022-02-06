package edu.spirinigor.blogengine.mapper.converter;

import jdk.jfr.Name;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DateConverter {
    @Named("convertDateToLong")
    public long convertDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toEpochSecond();
    }
}
