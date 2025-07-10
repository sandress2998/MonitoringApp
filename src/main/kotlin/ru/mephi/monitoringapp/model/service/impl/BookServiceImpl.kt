package ru.mephi.monitoringapp.model.service.impl

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mephi.monitoringapp.annotations.TimeBusinessOperation
import ru.mephi.monitoringapp.database.dao.BookDAO
import ru.mephi.monitoringapp.database.entity.Book
import ru.mephi.monitoringapp.model.dto.UpdatedBookRequest
import ru.mephi.monitoringapp.model.exception.NotFoundException
import ru.mephi.monitoringapp.model.mapper.BookMapper
import ru.mephi.monitoringapp.model.service.BookService
import ru.mephi.monitoringapp.model.service.OrderService
import ru.mephi.monitoringapp.model.timer.TimerAspect
import java.util.*

@Service
class BookServiceImpl (
    private val bookDAO: BookDAO,
    private val bookMapper: BookMapper,
    private val registry: MeterRegistry,
    private val timerAspect: TimerAspect
): BookService {
    init {
        Gauge.builder("books.present") {
            bookDAO.count()
        }
        .description("Shows quantity of books that exist in db")
        .register(registry)

        for (method in BookService::class.java.methods) {
            timerAspect.setBusinessTimer("book.service." + method.name)
        }
    }

    @Transactional
    @TimeBusinessOperation("book.service.create")
    override fun create(newBook: Book): Book {
        return bookDAO.save(newBook)
    }

    @Transactional
    @TimeBusinessOperation("book.service.delete")
    override fun delete(bookId: UUID): Unit {
        if (bookDAO.existsById(bookId)) {
            bookDAO.deleteById(bookId)
        } else {
            throw NotFoundException(NotFoundException.Cause.BOOK)
        }
    }

    @Transactional
    @TimeBusinessOperation("book.service.update")
    override fun update(bookId: UUID, updatedBook: UpdatedBookRequest): Book {
        val unchangedBook = bookDAO.findBookById(bookId) ?: throw NotFoundException(NotFoundException.Cause.BOOK)
        return bookDAO.save(bookMapper.fromUpdatedDtoToEntity(updatedBook, unchangedBook))
    }

    @TimeBusinessOperation("book.service.get")
    override fun get(bookId: UUID): Book {
        return bookDAO.findBookById(bookId) ?: throw NotFoundException(NotFoundException.Cause.BOOK)
    }

    @TimeBusinessOperation("book.service.getAll")
    override fun getAll(): List<Book> {
        return bookDAO.findAll()
    }
}