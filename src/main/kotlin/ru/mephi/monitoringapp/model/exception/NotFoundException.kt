package ru.mephi.monitoringapp.model.exception

class NotFoundException(private val customCause: Cause): RuntimeException() {
    override val message: String
        get() = when (customCause) {
            Cause.BOOK -> "Book not found"
            Cause.ORDER -> "Order not found"
        }

    enum class Cause {
        BOOK, ORDER
    }
}

