package edu.spirinigor.blogengine.mapper.converter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
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

    public LocalDateTime convertTimestamp(Long timestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp),ZoneId.systemDefault());
    }
}
