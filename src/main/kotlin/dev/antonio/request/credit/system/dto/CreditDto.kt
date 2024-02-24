package dev.antonio.request.credit.system.dto

import dev.antonio.request.credit.system.entity.Credit
import dev.antonio.request.credit.system.entity.Customer
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto (
    @field:NotNull(message = "Input cannot be empty") val creditValue: BigDecimal,
    @field:FutureOrPresent(message = "Date cannot be in the past") val dayFirstInstallment: LocalDate,
    @field:NotNull(message = "Input cannot be empty")
    @field:Max(value = 48, message = "The max number of installments is 48")
    @field:Min(value = 1, message = "You need to have at least 1 installment") val numberOfInstallments: Int,
    @field:NotNull(message = "Input cannot be empty") val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
