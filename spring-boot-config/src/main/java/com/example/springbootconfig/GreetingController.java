package com.example.springbootconfig;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class GreetingController {

    // Value annotation tutorial

    // Accessing values from the application.properties files
    @Value("${we.desc}")
    private String greetingMsg;

    // If the property does not exist spring will through an error, to handle that
    // we have default value which is separated by ':'
    @Value("${my.gret: World is great baby}")
    private String msg;

    // For a list of values
    @Value("${my.list.values}")
    private List<String> listOfValues;

    // Key value pairs
    @Value("#{${dbvalues}}")
    private Map<String, String> hmap;

    // Configurationproperties
    @Autowired
    private DBSettings dbSettings;

    @GetMapping(value = "/greeting")
    public String greeting() {
        System.err.println(msg);

        listOfValues.forEach(System.out::println);

        System.out.println(dbSettings);

        return greetingMsg + hmap + dbSettings.getConnection() + dbSettings.getHost();
    }

}
