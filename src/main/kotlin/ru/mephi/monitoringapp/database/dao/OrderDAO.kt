package ru.mephi.monitoringapp.database.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.monitoringapp.annotations.TimeDatabaseQuery
import ru.mephi.monitoringapp.database.entity.Order
import ru.mephi.monitoringapp.database.entity.OrderStatus
import java.util.*

@Repository
interface OrderDAO: CrudRepository<Order, UUID> {
    @TimeDatabaseQuery
    fun findOrderById(id: UUID): Order?
    @TimeDatabaseQuery
    fun countByStatus(status: OrderStatus = OrderStatus.UNDONE): Long
}