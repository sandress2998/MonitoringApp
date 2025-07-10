package ru.mephi.monitoringapp.database.entity

import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order: AbstractEntity() {
    @ManyToOne(optional = false)  // Эквивалент nullable = false в JPA
    @JoinColumn(name = "book_id")  // Название столбца в таблице orders
    lateinit var book: Book

    @Column
    var status: OrderStatus = OrderStatus.UNDONE

    // Метод для установки связи
    fun associateBook(book: Book): Order {
        this.book = book
        return this
    }
}

enum class OrderStatus {
    DONE, UNDONE
}