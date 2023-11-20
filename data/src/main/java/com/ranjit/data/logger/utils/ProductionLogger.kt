package com.ranjit.data.logger.utils

import timber.log.Timber


/**
 * Logs error messages. Ignores rest of the messages.
 */
open class ProductionLogger : NoOpLogger() {
    override fun verbose(tag: String?, message: String?, exception: Throwable?) {}
    override fun debug(tag: String?, message: String?, exception: Throwable?) {}
    override fun info(tag: String?, message: String?, exception: Throwable?) {}
    override fun warn(tag: String?, message: String?, exception: Throwable?) {}
    override fun error(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").e(exception, message)
    }
}