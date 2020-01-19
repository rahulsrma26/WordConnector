package com.example.wordconnector

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*


object Logic {

    private var dictionaryData: ArrayList<String> = arrayListOf()
    private val tag = "Logic"

    fun addPair(word1: String, word2: String) {
        val w1 = word1.trim()
        if (w1 == "") {
            Log.i(tag, "addPair: word1 null")
            return
        }
        val w2 = word2.trim()
        if (w2 == "") {
            Log.i(tag, "addPair: word2 null")
            return
        }
        dictionaryData.add(w1)
        dictionaryData.add(w2)
        Log.i(tag, "addPair $w1, $w2")
    }

    fun getRandomQA(): Pair<String, String> {
        if (dictionaryData.size > 1) {
            val idx = (Math.random() * dictionaryData.size).toInt()
            Log.i(tag, "getRandomQA idx = $idx")
            return when (idx % 2 == 0) {
                true -> Pair(dictionaryData[idx], dictionaryData[idx + 1])
                false -> Pair(dictionaryData[idx], dictionaryData[idx - 1])
            }
        }
        return Pair("No QA pair found", "Please add QA from menu")
    }

    private fun saveToFile(file: File) {
        Log.i(tag, "saveToFile $file.absolutePath")
        file.printWriter().use { out ->
            dictionaryData.forEach {
                out.println(it)
                Log.i(tag, "writing: '$it'")
            }
        }
        Log.i(tag, "saved dictionary size = ${dictionaryData.size}")
    }

    private fun loadFromFile(file: File) {
        Log.i(tag, "loadFromFile $file.absolutePath")
        if (file.exists()) {
            dictionaryData.clear()
            file.forEachLine {
                Log.i(tag, "reading: '$it'")
                if (it.trim() != "") {
                    dictionaryData.add(it)
                }
            }
            Log.i(tag, "loaded dictionary size = ${dictionaryData.size}")
        }
    }

    fun saveData(context: Context) {
        saveToFile(File(context.filesDir, "dictionary.txt"))
    }

    fun loadData(context: Context) {
        loadFromFile(File(context.filesDir, "dictionary.txt"))
    }

    fun exportData() {
        saveToFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "word-connector.txt"
            )
        )
    }

    fun importData() {
        loadFromFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "word-connector.txt"
            )
        )
    }
}