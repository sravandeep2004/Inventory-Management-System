package com.myapp.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class Hello {
    @RequestMapping("/")
    public String greet(){
        return "Hello World";
    }
}
