package dev.antonio.request.credit.system.dto

import dev.antonio.request.credit.system.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto (
    @field:NotEmpty(message = "Input cannot be empty") val firstName: String,
    @field:NotEmpty(message = "Input cannot be empty") val lastName: String,
    @field:NotNull(message = "Input cannot be empty") val income: BigDecimal,
    @field:NotEmpty(message = "Input cannot be empty") val zipCode: String,
    @field:NotEmpty(message = "Input cannot be empty") val street: String
) {
  fun toEntity(customer: Customer): Customer {
      customer.firstName = this.firstName
      customer.lastName = this.lastName
      customer.income = this.income
      customer.address.zipCode = this.zipCode
      customer.address.street = this.street
      return customer
  }
}
