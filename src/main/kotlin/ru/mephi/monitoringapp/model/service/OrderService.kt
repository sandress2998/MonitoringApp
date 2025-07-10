package ru.mephi.monitoringapp.model.service

import ru.mephi.monitoringapp.database.entity.Order
import ru.mephi.monitoringapp.model.dto.CreateOrderRequest
import java.util.UUID

interface OrderService {
    fun create(createOrderRequest: CreateOrderRequest) : Order
    fun get(orderId: UUID): Order
    fun markAsFinished(orderId: UUID)
}