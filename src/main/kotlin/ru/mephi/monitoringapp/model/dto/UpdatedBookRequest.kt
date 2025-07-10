package ru.mephi.monitoringapp.model.dto

import ru.mephi.monitoringapp.database.entity.Category

data class UpdatedBookRequest (
    val title: String,
    val author: String,
    val rating: Int,
    val category: Category
)