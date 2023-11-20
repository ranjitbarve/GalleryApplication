package com.ranjit.data.logger.utils

import timber.log.Timber

class DebugLogger : NoOpLogger() {
    override fun verbose(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").v(exception, message)
    }

    override fun debug(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").d(exception, message)
    }

    override fun info(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").i(exception, message)
    }

    override fun warn(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").w(exception, message)
    }

    override fun error(tag: String?, message: String?, exception: Throwable?) {
        Timber.tag("${LoggerConstants.LOG_PREFIX}$tag").e(exception, message)
    }
}
