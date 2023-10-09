package com.example.SpringKotlinBank.datasource

import com.example.SpringKotlinBank.model.Bank

interface BankDataSource { // Just an interface to ensure each function is implemented in its own way. WHen we create an instance of this we are passed onto any available implementations available

    fun retrieveBanks(): Collection<Bank>
    fun retrieveBank(accountNumber: String): Bank
    fun createBank(bank: Bank)
    fun updateBank(bank: Bank)
    fun deleteBank(accountNumber: String)
}