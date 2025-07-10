package ru.mephi.monitoringapp.database.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.monitoringapp.annotations.TimeDatabaseQuery
import ru.mephi.monitoringapp.database.entity.Book
import java.util.UUID

@Repository
interface BookDAO: CrudRepository<Book, UUID> {
    @TimeDatabaseQuery()
    override fun deleteById(id: UUID): Unit
    @TimeDatabaseQuery
    fun findBookById(id: UUID): Book?
    @TimeDatabaseQuery
    override fun findAll(): List<Book>
}