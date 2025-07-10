package ru.mephi.monitoringapp.model.service.impl

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mephi.monitoringapp.annotations.TimeBusinessOperation
import ru.mephi.monitoringapp.database.dao.BookDAO
import ru.mephi.monitoringapp.database.dao.OrderDAO
import ru.mephi.monitoringapp.database.entity.Category
import ru.mephi.monitoringapp.database.entity.Order
import ru.mephi.monitoringapp.database.entity.OrderStatus
import ru.mephi.monitoringapp.model.dto.CreateOrderRequest
import ru.mephi.monitoringapp.model.exception.NotFoundException
import ru.mephi.monitoringapp.model.service.OrderService
import ru.mephi.monitoringapp.model.timer.TimerAspect
import java.util.*

@Service
class OrderServiceImpl (
    private val orderDAO: OrderDAO,
    private val bookDAO: BookDAO,
    private val registry: MeterRegistry,
    private val timerAspect: TimerAspect
): OrderService {
    init {
        Gauge.builder("orders.active") {
            getActiveOrdersQuantity()
        }
        .description("Current active orders quantity in database")
        .register(registry)

        for (method in OrderService::class.java.methods) {
            timerAspect.setBusinessTimer("order.service." + method.name)
        }
    }

    private val ordersCount =  mutableMapOf<Category, Counter>().apply {
        Category.entries.forEach { category ->
            this.put(category, Counter.builder("orders.created")
                .description("Total orders by category")
                .tag("type", "book")
                .tag("category", category.toString())
                .register(registry))
        }
    }

    private fun getActiveOrdersQuantity(): Long {
        return orderDAO.countByStatus()
    }

    @Transactional
    @TimeBusinessOperation("order.service.create")
    override fun create(createOrderRequest: CreateOrderRequest): Order {
        val relatedBook = bookDAO.findBookById(createOrderRequest.bookId)
        return if (relatedBook != null) {
            val order = orderDAO.save(Order().associateBook(relatedBook))
            ordersCount[relatedBook.category]?.increment()
            order
        } else {
            throw NotFoundException(NotFoundException.Cause.BOOK)
        }
    }

    @TimeBusinessOperation("order.service.get")
    override fun get(orderId: UUID): Order {
        return orderDAO.findOrderById(orderId) ?: throw NotFoundException(NotFoundException.Cause.ORDER)
    }

    @Transactional
    @TimeBusinessOperation("order.service.markAsFinished")
    override fun markAsFinished(orderId: UUID) {
        val order = orderDAO.findOrderById(orderId) ?: throw NotFoundException(NotFoundException.Cause.ORDER)
        order.status = OrderStatus.DONE
        orderDAO.save(order)
    }
}