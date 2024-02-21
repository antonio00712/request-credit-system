package dev.antonio.request.credit.system.service

import dev.antonio.request.credit.system.entity.Credit
import java.util.*

interface ICreditService {
    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(creditCode: UUID): Credit
}