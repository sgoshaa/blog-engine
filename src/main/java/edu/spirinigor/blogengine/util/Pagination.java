package edu.spirinigor.blogengine.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class Pagination {
    public Pageable getPage(int offset, int limit) {
        int page = offset / limit;
        return PageRequest.of(page, limit);
    }

    public Pageable getPage(int offset, int limit, Sort sort) {
        int page = offset / limit;
        return PageRequest.of(page, limit, sort);
    }
}
