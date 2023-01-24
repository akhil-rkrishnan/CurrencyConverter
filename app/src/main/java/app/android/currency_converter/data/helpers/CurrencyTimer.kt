package app.android.currency_converter.data.helpers

import app.android.currency_converter.data.constants.TIMER_DELAY
import java.util.*

/**
* Timer task class for schedule api call.
*/
private class ApiTimerTask(private val onTimeHit: () -> Unit) : TimerTask() {
    override fun run() {
        onTimeHit()
    }
}

/**
 * Timer class for schedule api call.
 */
class CurrencyTimer(
    private val delay: Long = TIMER_DELAY,
    private val durationInMillis: Long,
    tasks: () -> Unit
) : Timer() {

    private val apiTimerTask = ApiTimerTask(tasks)

    private var isAlreadyScheduled = false

    /**
    * Method to schedule and start the process.
    */
    fun scheduleAndStart() {
        if (isAlreadyScheduled) {
            return
        }
        schedule(apiTimerTask, delay, durationInMillis)
        isAlreadyScheduled = true
    }

    /**
    * Method to stop the timer.
    */
    fun stopTimer() {
        isAlreadyScheduled = false
        cancel()
    }
}