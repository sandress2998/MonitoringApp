package ru.mephi.monitoringapp.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Book (
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var rating: Int,

    @Column(nullable = false)
    var category: Category
): AbstractEntity()

enum class Category {
    FANTASY, NOVEL, DETECTIVE, UNKNOWN
}