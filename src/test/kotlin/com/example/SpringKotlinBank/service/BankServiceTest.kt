package com.example.SpringKotlinBank.service

import com.example.SpringKotlinBank.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class BankServiceTest {

    // BankDataSource is used instead of MockBankDataSource this is because it contains all the methods we will use and we are redefining them here for the purpose of testing i.e: every {dataSource.retrieveBanks()} returns emptyList()
    private val dataSource: BankDataSource = mockk() // A mock is used to simulate our datasource without interfering with it
    private val bankService = BankService(dataSource) // Instance of our service that will go under test

    @Test
    fun `Should call its data source to retrieve banks`(){

        every {dataSource.retrieveBanks()} returns emptyList() // Makes it return an empty list for the sake of testing

        val banks = bankService.getBanks()

        verify { dataSource.retrieveBanks() } // Verifies the method call has been done
        verify(exactly = 1) { dataSource.retrieveBanks() } // Verifies the method call has been done ONCE
    }
}