package ru.mephi.monitoringapp.model.service

import ru.mephi.monitoringapp.database.entity.Book
import ru.mephi.monitoringapp.model.dto.UpdatedBookRequest
import java.util.UUID

interface BookService {
    fun create(newBook: Book): Book
    fun delete(bookId: UUID): Unit
    fun update(bookId: UUID, updatedBook: UpdatedBookRequest): Book
    fun get(bookId: UUID): Book
    fun getAll(): List<Book>
}