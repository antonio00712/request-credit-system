package dev.antonio.request.credit.system.dto

import dev.antonio.request.credit.system.entity.Address
import dev.antonio.request.credit.system.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Input cannot be empty") val firstName: String,
    @field:NotEmpty(message = "Input cannot be empty") val lastName: String,
    @field:NotEmpty(message = "Input cannot be empty")
    @field:CPF(message = "This cpf is invalid") val cpf: String,
    @field:NotNull(message = "Input cannot be empty") val income: BigDecimal,
    @field:Email(message = "Invalid email")
    @field:NotEmpty(message = "Input cannot be empty") val email: String,
    @field:NotEmpty(message = "Input cannot be empty") val password: String,
    @field:NotEmpty(message = "Input cannot be empty") val zipCode: String,
    @field:NotEmpty(message = "Input cannot be empty") val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(zipCode = this.zipCode, street = this.street)
    )
}

