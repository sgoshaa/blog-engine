package edu.spirinigor.blogengine.api.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class CalendarResponse {
    private List<Integer> years;
    private Map<LocalDate, Integer> posts;
}
