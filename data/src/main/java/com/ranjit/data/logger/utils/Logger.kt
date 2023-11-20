package com.ranjit.data.logger.utils

interface Logger {
    fun verbose(tag: String?, message: String?)
    fun verbose(tag: String?, message: String?, exception: Throwable?)
    fun debug(tag: String?, message: String?)
    fun debug(tag: String?, message: String?, exception: Throwable?)
    fun info(tag: String?, message: String?)
    fun info(tag: String?, message: String?, exception: Throwable?)
    fun warn(tag: String?, message: String?)
    fun warn(tag: String?, message: String?, exception: Throwable?)
    fun error(tag: String?, message: String?)
    fun error(tag: String?, message: String?, exception: Throwable?)
}
