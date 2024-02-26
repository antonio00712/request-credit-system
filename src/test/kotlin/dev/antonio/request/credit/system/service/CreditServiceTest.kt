package dev.antonio.request.credit.system.service

import dev.antonio.request.credit.system.entity.Credit
import dev.antonio.request.credit.system.entity.Customer
import dev.antonio.request.credit.system.exception.BusinessException
import dev.antonio.request.credit.system.repository.CreditRepository
import dev.antonio.request.credit.system.service.impl.CreditService
import dev.antonio.request.credit.system.service.impl.CustomerService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        //creditService = CreditService(creditRepository, customerService)
    }
    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should create credit`() {
        // given
        val fakeCredit: Credit = buildCredit()
        val fakeCustomerId: Long = 1L
        every { customerService.findById(fakeCustomerId) } returns fakeCredit.customer!!
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        // when
        val actual: Credit = this.creditService.save(fakeCredit)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { customerService.findById(fakeCustomerId) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }

    }

    @Test
    fun `should not create credit when invalid dayFirstInstallment`() {
        // given
        val fakeCredit: Credit = buildCredit(dayFirstInstallment = LocalDate.now().plusMonths(5L))
        every { creditRepository.save(fakeCredit) } answers {fakeCredit}
        // when
        Assertions.assertThatThrownBy { creditService.save(fakeCredit) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Invalid day of first installment")
        // then
        verify(exactly = 0) { creditRepository.save(any()) }
    }

    @Test
    fun `should return a list of credits for a customer`() {
        // given
        val fakeCustomerId: Long = 1L
        val expectedCredits: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())
        every { creditRepository.findAllByCustomerId(fakeCustomerId) } returns expectedCredits
        // when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeCustomerId)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(expectedCredits)

        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeCustomerId) }
    }

    @Test
    fun `should return credit for a valid customer and credit code`() {
        //given
        val fakeCustomerId: Long = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = fakeCustomerId))

        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        val actual: Credit = creditService.findByCreditCode(fakeCustomerId, fakeCreditCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should throw BusinessException for invalid credit code`() {
        //given
        val fakeCustomerId: Long = 1L
        val invalidCreditCode: UUID = UUID.randomUUID()

        every { creditRepository.findByCreditCode(invalidCreditCode) } returns null
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(fakeCustomerId, invalidCreditCode) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("CreditCode $invalidCreditCode not found")
        //then
        verify(exactly = 1) { creditRepository.findByCreditCode(invalidCreditCode) }
    }

    @Test
    fun `should throw IllegalArgumentException for different customer ID`() {
        //given
        val fakeCustomerId: Long = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = 2L))

        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(fakeCustomerId, fakeCreditCode) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Contact admin")

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(100.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(2L),
        numberOfInstallments: Int = 15,
        customer: Customer = CustomerServiceTest.buildCustomer()
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )
}