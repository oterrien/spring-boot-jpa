package com.ote.test.service.generator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IDGeneratorServiceProvider {

    @Getter
    private static IIDGeneratorService idGeneratorService;

    public IDGeneratorServiceProvider(@Autowired IIDGeneratorService idGeneratorService){
        this.idGeneratorService = idGeneratorService;
    }
}
