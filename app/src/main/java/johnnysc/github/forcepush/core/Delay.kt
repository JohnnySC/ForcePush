package johnnysc.github.forcepush.core

import java.util.*

/**
 * @author Asatryan on 09.08.2021
 **/
class Delay<T>(private val delay: Long = 300, private val block: (T) -> Unit) {

    private var timer: Timer? = null
    private var isRunning = false
    private var cached: T? = null
    private var time: Long = 0

    fun add(item: T) {
        cached = item
        time = System.currentTimeMillis()
        if (!isRunning)
            start()
    }

    fun clear() {
        timer?.purge()
        timer?.cancel()
        timer = null
        isRunning = false
    }

    private fun start() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (System.currentTimeMillis() - time >= delay) {
                    cached?.let { block(it) }
                    clear()
                }
            }
        }, 0, delay)
        isRunning = true
    }

}