package com.example.SpringKotlinBank.model

// Data classes implement equals, hash and toString methods as well as sort getters and setters implicitly
// Data layer

data class Bank(val accountNumber: String, val trust: Double, val transactionFee: Int) {

}