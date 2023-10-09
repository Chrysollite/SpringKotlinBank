package com.example.SpringKotlinBank.datasource.mock

import com.example.SpringKotlinBank.datasource.BankDataSource
import com.example.SpringKotlinBank.model.Bank
import org.springframework.stereotype.Repository
import java.util.NoSuchElementException

@Repository("myChosenDatasourceImplementation")
class MockBankDataSource: BankDataSource { // Our temporary data source - our repository. Currently doesn't use a database, just has these values and one method

    val banks = mutableListOf(
        Bank("1234", 3.14, 1),
        Bank("4321", 5.0, 9))

    override fun retrieveBanks(): Collection<Bank> {
        return banks
    }

    // '?:' means 'if this is null'
    override fun retrieveBank(accountNumber: String): Bank {
        //return banks.first{ it.accountNumber == accountNumber }
        return banks.firstOrNull() {it.accountNumber == accountNumber}?: throw NoSuchElementException("THERE WAS NO BANK ACCOUNT WITH THE ACCOUNT NUMBER: $accountNumber")
    }

    override fun createBank(bank: Bank) {
        //banks.add(bank)
        if(banks.any { it.accountNumber == bank.accountNumber }){
            throw IllegalArgumentException("A bank with this number already exists")
        }
        banks.add(bank)
    }

    override fun updateBank(bank: Bank){
        val currentBank = banks.firstOrNull { it.accountNumber == bank.accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")

        banks.remove(currentBank)
        banks.add(bank)
    }

    override fun deleteBank(accountNumber: String) {
        val currentBank = banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
        banks.remove(currentBank)
    }
}