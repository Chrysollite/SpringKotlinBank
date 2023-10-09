package com.example.SpringKotlinBank

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/hello")
class HelloWorldController {

    @GetMapping("Bob")
    fun helloWorld(): String{
        return "Hello Worlddddddddddddddd"
    }
}