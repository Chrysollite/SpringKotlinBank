package com.example.SpringKotlinBank.service

import com.example.SpringKotlinBank.datasource.BankDataSource
import com.example.SpringKotlinBank.model.Bank
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

// Takes a repository (Datasource) as a parameter instead of how we did dependency injection last time - requests an implementation of the BankDataSource interface
// If you have multiple implementations then you need to specify by marking one as @Primary, asking them accept multiple beans or use @Qualifier to identify the correct bean
@Service
class BankService(@Qualifier("myChosenDatasourceImplementation") val dataSource: BankDataSource) {
    fun getBanks(): Collection<Bank>{
        return dataSource.retrieveBanks()
    }

    fun getBank(accountNumber: String): Bank{
        return dataSource.retrieveBank(accountNumber)
    }

    fun addBank(bank: Bank){
        dataSource.createBank(bank)
    }

    fun updateBank(bank: Bank){
        dataSource.updateBank(bank)
    }

    fun deleteBank(accountNumber: String){
        dataSource.deleteBank(accountNumber)
    }
}