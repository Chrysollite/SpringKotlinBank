package com.example.SpringKotlinBank.controller

import com.example.SpringKotlinBank.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

    // GIVEN WHEN THEN structure for tests, examples below used a combined when-then block

    // Both these autowired variables can be added to a constructor instead - this is useful if you have many and don't want to keep writting out the annotation
    @Autowired
    lateinit var mockMvc: MockMvc // Allows for requests to rest API without issuing any http requests which is faster

    @Autowired
    lateinit var objectMapper: ObjectMapper // Used to format the banks we post as valid json

    @Test
    fun `should return all banks`(){
        // Print gives info on the request and response in the console - great for debugging
        // Status is like 200 or 404 etc - 200 is okay
        // with jsonPath i'm picking the account_number of the first item being returned
        // mockMvc.get means that i'm essentially simulating a get request
        // Skipped Given - this test uses When + Then
        mockMvc.get("/api/banks").andDo { print() }.andExpect{ status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) // Expect JSON content
            jsonPath("\$[0].accountNumber").value("1234") }}
    }

    @Test
    fun `Should return the bank with the given account number`(){
        // Given
        val accountNumber = 1234

        // When + Then
        mockMvc.get("/api/banks/$accountNumber")
            .andDo { print() }
            .andExpect { status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON)}
//                jsonPath("$[0].accountNumber").value((accountNumber))}// Expect JSON content
                jsonPath("$.trust"){value("3.14")}// Expect JSON content
                jsonPath("$.transactionFee"){value("1")}}// Expect JSON content
    }

    @Test
    fun `Should return NOT FOUND if the account does not exist`(){
        // Given
        val accountNumber = "does_not_exist"

        // When + Then
        mockMvc.get("/api/banks/$accountNumber")
            .andDo { print() }
            .andExpect { status { isNotFound() }}
    }

    @Nested // Nested classes help format the testing - keeps multiple tests regarding a single function all under one sorta thread
    @DisplayName("POST add bank") // The name that appears for the branch/ thread of tests
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank{
        @Test
        fun `Should add the new bank`(){

            // Given
            val testBank = Bank("acc123", 31.415, 2)

            // When
            val performPost = mockMvc.post("/api/banks"){
                contentType = MediaType.APPLICATION_JSON // Setting the content type of the request to JSON, indicating that the request body will be in JSON format.
                content = objectMapper.writeValueAsString(testBank) // Converting the testBank object to a JSON string using an objectMapper and setting it as the content of the request
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect { status { isCreated() } } // Check if a request has resulted in the creation of a resource
        }

        @Test
        fun `Should return BAD REQUEST if bank with given account number already exists`(){

            // Given
            val invalidTestBank = Bank("1234", 31.415, 2)

            // When
            val performPost = mockMvc.post("/api/banks"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidTestBank)
            }

            // Then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        fun `should update an existing bank`() {
            // Given
            val updatedBank = Bank("1234", 1.0, 1)

            // When
            val performPatchRequest = mockMvc.patch("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }

            // Then
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() } }

            mockMvc.get("/api/banks/${updatedBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedBank)) } }
        }

        @Test
        fun `should return BAD REQUEST if no bank with given account number exists`() {
            // Given
            val invalidBank = Bank("does_not_exist", 1.0, 1)

            // When
            val performPatchRequest = mockMvc.patch("/api/banks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // Then
            performPatchRequest
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("DELETE remove bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank{
        @Test
        @DirtiesContext // Indicates the associated test or class modifies the ApplicationContext. It tells the testing framework to close and recreate the context for later tests.
        fun `Should delete an existing bank by account number`(){

            // Given
            val accountNumber = 1234

            // When
            val performDelete = mockMvc.delete("/api/banks/$accountNumber")

            // Then
            performDelete
                .andDo { print() }
                .andExpect { status { isNoContent() } } // Indicates that the request was successfully processed, but there is no content to return in the response body - Code 204

            mockMvc.get("/api/banks/$accountNumber")
                .andExpect { status { isNotFound() } } // When the requested resource is not found - code 404
        }

        @Test
        fun `Should throw NOT FOUND when trying to delete a non existing bank`(){

            // Given
            val accountNumber = "DoesNotExist"

            // When
            val performDelete = mockMvc.delete("/api/banks/$accountNumber")

            // Then
            performDelete
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}