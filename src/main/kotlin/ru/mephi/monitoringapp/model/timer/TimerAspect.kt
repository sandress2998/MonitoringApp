package ru.mephi.monitoringapp.model.timer

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import ru.mephi.monitoringapp.annotations.TimeBusinessOperation
import java.util.concurrent.ConcurrentHashMap

// 2. Аспект
@Aspect
@Component
class TimerAspect(private val meterRegistry: MeterRegistry) {
    // Инициализируем таймеры один раз при создании аспекта
    private val httpRequestTimer = getTimer(
        "http.request.time",
        "Time taken to process HTTP requests",
        "layer", "controller"
    )

    private val dbQueryTimer = getTimer(
        "db.query.time",
        "Time taken to execute database queries",
        "type", "sql"
    )

    // Для бизнес-операций используем мапу, так как имена динамические
    private val businessTimers = ConcurrentHashMap<String, Timer>()

    fun setBusinessTimer(functionName: String) {
        businessTimers[functionName] = getTimer(
            "business.operation.time",
            "Time taken to execute business operations",
            "operation", functionName
        )
    }

    private fun getTimer(name: String, description: String, vararg tags: String): Timer =
        Timer.builder(name)
            .description(description)
            .tags(*tags)
            .register(meterRegistry)

    @Around("@annotation(ru.mephi.monitoringapp.annotations.TimeHttpRequest)")
    fun measureHttpRequest(joinPoint: ProceedingJoinPoint): Any? {
        return httpRequestTimer.recordCallable { joinPoint.proceed() }
    }

    @Around("@annotation(ru.mephi.monitoringapp.annotations.TimeDatabaseQuery)")
    fun measureDatabaseQuery(joinPoint: ProceedingJoinPoint): Any? {
        return dbQueryTimer.recordCallable { joinPoint.proceed() }
    }

    @Around("@annotation(timeBusinessOperation)")
    fun measureBusinessOperation(
        joinPoint: ProceedingJoinPoint,
        timeBusinessOperation: TimeBusinessOperation
    ): Any? {
        return businessTimers[timeBusinessOperation.value]?.recordCallable { joinPoint.proceed() }
    }
}