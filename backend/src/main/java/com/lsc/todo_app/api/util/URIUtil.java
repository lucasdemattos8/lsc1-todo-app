package com.lsc.todo_app.api.util;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class URIUtil {

    public static URI createNewURIById(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
