package com.janwee.bookstore.common.domain.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class SimplePage<T> extends PageImpl<T> implements Page<T> {
    public SimplePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public SimplePage(Pageable pageable, long total) {
        super(new ArrayList<>(), pageable, total);
    }
}
