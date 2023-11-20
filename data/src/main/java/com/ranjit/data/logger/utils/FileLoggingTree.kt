package com.ranjit.data.logger.utils

import android.os.Environment
import android.util.Log
import androidx.annotation.Nullable
import com.ranjit.data.BuildConfig

import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FileLoggingTree : Timber.DebugTree() {
    protected override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val path = "Log"
            val fileNameTimeStamp: String = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).format(Date())
            val logTimeStamp: String = SimpleDateFormat(
                "E MMM dd yyyy 'at' HH:mm:ss:SSS aaa",
                Locale.getDefault()
            ).format(Date())
            val fileName = "$fileNameTimeStamp.html"

            // Create file
            val file: File? = generateFile(path, fileName)

            // If file created or exists save logs
            if (file != null) {
                val writer = FileWriter(file, true)
                writer.append(
                    "<p style=\"background:lightgray;\"><strong "
                            + "style=\"background:lightblue;\">&nbsp&nbsp"
                )
                    .append(logTimeStamp)
                    .append(" :&nbsp&nbsp</strong><strong>&nbsp&nbsp")
                    .append(tag)
                    .append("</strong> - ")
                    .append(message)
                    .append("</p>")
                writer.flush()
                writer.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error while logging into file : $e")
        }
    }

    protected override fun createStackElementTag(element: StackTraceElement): String? {
        // Add log statements line number to the log
        return super.createStackElementTag(element) + " - " + element.lineNumber
    }

    companion object {
        private val LOG_TAG = FileLoggingTree::class.java.simpleName

        /*  Helper method to create file*/
        @Nullable
        private fun generateFile(path: String, fileName: String): File? {
            var file: File? = null
            if (isExternalStorageAvailable) {
                val root = File(
                    Environment.getExternalStorageDirectory().absolutePath,
                    BuildConfig.LIBRARY_PACKAGE_NAME + File.separator.toString() + path
                )
                var dirExists = true
                if (!root.exists()) {
                    dirExists = root.mkdirs()
                }
                if (dirExists) {
                    file = File(root, fileName)
                }
            }
            return file
        }

        /* Helper method to determine if external storage is available*/
        private val isExternalStorageAvailable: Boolean
            private get() = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
    }
}