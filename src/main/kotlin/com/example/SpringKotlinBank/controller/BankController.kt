package com.example.SpringKotlinBank.controller

import com.example.SpringKotlinBank.model.Bank
import com.example.SpringKotlinBank.service.BankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/banks")
class BankController (private val service: BankService){ // Top layer - api/web layer. Takes the service from the layer under as a parameter

    @ExceptionHandler(NoSuchElementException::class) // Find out how this works
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String>{
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class) // Find out how this works
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String>{
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @GetMapping
    fun getBanks(): Collection<Bank> = service.getBanks()

    @GetMapping("/{accountNumber}")
    fun getBank(@PathVariable accountNumber: String): Bank{
        return service.getBank(accountNumber)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank){
        service.addBank(bank)
    }

    @PatchMapping
    fun updateBank(@RequestBody bank: Bank){
        service.updateBank(bank)
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable accountNumber: String){
        service.deleteBank(accountNumber)
    }
}