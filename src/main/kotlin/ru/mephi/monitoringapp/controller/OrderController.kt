package ru.mephi.monitoringapp.controller

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.web.bind.annotation.*
import ru.mephi.monitoringapp.annotations.TimeHttpRequest
import ru.mephi.monitoringapp.database.entity.Order
import ru.mephi.monitoringapp.database.entity.OrderStatus
import ru.mephi.monitoringapp.model.dto.CreateOrderRequest
import ru.mephi.monitoringapp.model.exception.NotAcceptableException
import ru.mephi.monitoringapp.model.service.OrderService
import java.util.*


@RestController
@RequestMapping("/api/orders")
class OrderController (
    private val orderService: OrderService,
    private val registry: MeterRegistry
) {
    final val metric_name = "requests.total"

    val createOrder = Counter.builder(metric_name)
        .tag("endpoint", "/api/orders")
        .tag("method", "POST")
        .register(registry)

    val getOrder = Counter.builder(metric_name)
        .tag("endpoint", "/api/orders/{orderId}")
        .tag("method", "GET")
        .register(registry)

    val finishOrder = Counter.builder(metric_name)
        .tag("endpoint", "/api/orders/{orderId}")
        .tag("method", "PATCH")
        .register(registry)

    @PostMapping
    @TimeHttpRequest
    fun create(@RequestBody newOrderRequest: CreateOrderRequest): Order {
        createOrder.increment()
        return orderService.create(newOrderRequest)
    }

    @GetMapping("/{id}")
    @TimeHttpRequest
    fun get(@PathVariable id: UUID): Order {
        getOrder.increment()
        return orderService.get(id)
    }

    @PatchMapping("/{id}")
    @TimeHttpRequest
    fun markAsFinish(@PathVariable id: UUID, @RequestParam status: OrderStatus) {
        finishOrder.increment()
        return when (status) {
            OrderStatus.DONE -> orderService.markAsFinished(id)
            OrderStatus.UNDONE -> throw NotAcceptableException()
        }
    }
}