package ru.mephi.monitoringapp.controller

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ru.mephi.monitoringapp.annotations.TimeHttpRequest
import ru.mephi.monitoringapp.database.entity.Book
import ru.mephi.monitoringapp.model.dto.UpdatedBookRequest
import ru.mephi.monitoringapp.model.service.BookService
import java.util.*

@RestController
@Tag(name = "Book Controller", description = "Book Controller")
@RequestMapping("/api/books")
class BookController (
    private val bookService: BookService,
    private val registry: MeterRegistry
) {
    final val metric_name = "requests.total"

    val createBook = Counter.builder(metric_name)
        .tag("endpoint", "/api/books")
        .tag("method", "POST")
        .register(registry)

    val updateBook = Counter.builder(metric_name)
        .tag("endpoint", "/api/books/{bookId}")
        .tag("method", "PUT")
        .register(registry)

    val deleteBook = Counter.builder(metric_name)
        .tag("endpoint", "/api/books/{bookId}")
        .tag("method", "DELETE")
        .register(registry)

    val getBook = Counter.builder(metric_name)
        .tag("endpoint", "/api/books/{bookId}")
        .tag("method", "GET")
        .register(registry)

    val getAllBooks = Counter.builder(metric_name)
        .tag("endpoint", "/api/books")
        .tag("method", "GET")
        .register(registry)


    @GetMapping("/{bookId}")
    @TimeHttpRequest
    fun get(@PathVariable bookId: UUID) : Book {
        getBook.increment()
        return bookService.get(bookId)
    }

    @PostMapping
    @TimeHttpRequest
    fun create(@RequestBody newBook: Book) : Book {
        createBook.increment()
        return bookService.create(newBook)
    }

    @PutMapping("/{bookId}")
    @TimeHttpRequest
    fun update(@PathVariable bookId: UUID, @RequestBody updatedBook: UpdatedBookRequest): Book {
        updateBook.increment()
        return bookService.update(bookId, updatedBook)
    }

    @DeleteMapping("/{bookId}")
    @TimeHttpRequest
    fun delete(@PathVariable bookId: UUID) {
        deleteBook.increment()
        return bookService.delete(bookId)
    }

    @GetMapping
    @TimeHttpRequest
    fun getAll(): List<Book> {
        getAllBooks.increment()
        return bookService.getAll()
    }
}