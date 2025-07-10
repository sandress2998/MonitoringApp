package ru.mephi.monitoringapp.model.exception

class NotAcceptableException: RuntimeException() {
    override val message: String
        get() = "Request isn't allowed"
}