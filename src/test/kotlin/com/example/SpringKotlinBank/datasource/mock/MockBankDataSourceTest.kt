package com.example.SpringKotlinBank.datasource.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MockBankDataSourceTest {

    private val mockDataSource = MockBankDataSource() // An instance of our database that will go under test

    @Test
    fun shouldProvideACollectionOfBanks(){
        val banks = mockDataSource.retrieveBanks()
        assertThat(banks).isNotEmpty()
    }

    @Test
    fun `Should check requirements for banks accounts`(){
        val banks = mockDataSource.retrieveBanks()

        assertThat(banks).allMatch{it.accountNumber.isNotBlank()}
        assertThat(banks).allMatch{it.trust > 0.0}
        assertThat(banks).allMatch{it.transactionFee >= 0}
    }
}