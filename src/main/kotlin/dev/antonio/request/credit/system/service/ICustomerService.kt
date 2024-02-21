package dev.antonio.request.credit.system.service

import dev.antonio.request.credit.system.entity.Customer

interface ICustomerService{
    fun save(customer: Customer): Customer

    fun findById(id: Long): Customer

    fun delete(id: Long): Customer
}