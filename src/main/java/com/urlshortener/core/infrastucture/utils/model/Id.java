package com.urlshortener.core.infrastucture.utils.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Id {
    private final String id;
    public Id(){
        this.id = UUID.randomUUID().toString();
    }
    public Id(String id){
        this.id = id;
    }
    private String generateId(){
        return UUID.randomUUID().toString();
    }
}
