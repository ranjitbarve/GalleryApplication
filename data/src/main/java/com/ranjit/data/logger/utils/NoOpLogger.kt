package com.ranjit.data.logger.utils


abstract class NoOpLogger : Logger {
    override fun verbose(tag: String?, message: String?) {
        verbose(tag, message, null)
    }

    override fun verbose(tag: String?, message: String?, exception: Throwable?) {}
    override fun debug(tag: String?, message: String?) {
        debug(tag, message, null)
    }

    override fun debug(tag: String?, message: String?, exception: Throwable?) {}
    override fun info(tag: String?, message: String?) {
        info(tag, message, null)
    }

    override fun info(tag: String?, message: String?, exception: Throwable?) {}
    override fun warn(tag: String?, message: String?) {
        warn(tag, message, null)
    }

    override fun warn(tag: String?, message: String?, exception: Throwable?) {}
    override fun error(tag: String?, message: String?) {
        error(tag, message, null)
    }

    override fun error(tag: String?, message: String?, exception: Throwable?) {}
}
