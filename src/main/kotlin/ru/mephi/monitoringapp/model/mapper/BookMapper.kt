package ru.mephi.monitoringapp.model.mapper

import org.springframework.stereotype.Component
import ru.mephi.monitoringapp.database.entity.Book
import ru.mephi.monitoringapp.model.dto.UpdatedBookRequest

@Component
class BookMapper {
    fun fromUpdatedDtoToEntity(updatedBook: UpdatedBookRequest, unchangedBook: Book): Book {
        unchangedBook.author = updatedBook.author
        unchangedBook.title = updatedBook.title
        unchangedBook.rating = updatedBook.rating
        unchangedBook.category = updatedBook.category
        return unchangedBook
    }
}