package com.example.wordconnector

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private var buttonStateNext = false
    object PermissionConstants {
        const val READ_REQUEST_CODE = 101
        const val WRITE_REQUEST_CODE = 102
    }
    private val tag = "MainActivity"

    private fun fetchQuestionAnswer() {
        var questionTxt = findViewById<TextView>(R.id.txtQuestion)
        var answerTxt = findViewById<TextView>(R.id.txtAnswer)
        val (k, v) = Logic.getRandomQA()
        questionTxt.text = k
        answerTxt.text = v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupPermissions()

        var buttonChange = findViewById<Button>(R.id.btnNext)
        var answerTxt = findViewById<TextView>(R.id.txtAnswer)

        Logic.loadData(applicationContext)
        fetchQuestionAnswer()

        buttonChange.setOnClickListener {
            buttonStateNext = !buttonStateNext
            if (buttonStateNext) {
                answerTxt.visibility = View.VISIBLE
                buttonChange.setText(R.string.next)
            } else {
                answerTxt.visibility = View.INVISIBLE
                fetchQuestionAnswer()
                buttonChange.setText(R.string.show)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                return true
            }
            R.id.action_add -> {
                var intent = Intent(this@MainActivity, AddWordActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_export -> {
                Logic.exportData()
                Toast.makeText(applicationContext, "Exported", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_import -> {
                Logic.importData()
                Logic.saveData(applicationContext)
                Toast.makeText(applicationContext, "Imported", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            READ_REQUEST_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Log.i(TAG, READ_REQUEST_CODE.toString() + " Permission has been denied by user")
//                } else {
//                    Log.i(
//                        TAG,
//                        READ_REQUEST_CODE.toString() + " Permission has been granted by user"
//                    )
//                }
//            }
//            WRITE_REQUEST_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Log.i(
//                        TAG,
//                        WRITE_REQUEST_CODE.toString() + " Permission has been denied by user"
//                    )
//                } else {
//                    Log.i(
//                        TAG,
//                        WRITE_REQUEST_CODE.toString() + " Permission has been granted by user"
//                    )
//                }
//            }
//        }
//    }﻿

    private fun setupPermissions() {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(tag, "Permission to READ_EXTERNAL_STORAGE denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to read the storage is required for this app to import.")
                    .setTitle("Permission required")

                builder.setPositiveButton(
                    "OK"
                ) { _, _ ->
                    Log.i(tag, "Clicked")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PermissionConstants.READ_REQUEST_CODE
                    )
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PermissionConstants.READ_REQUEST_CODE
                )
            }
        }

        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(tag, "Permission to WRITE_EXTERNAL_STORAGE denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to write the storage is required for this app to export.")
                    .setTitle("Permission required")

                builder.setPositiveButton(
                    "OK"
                ) { _, _ ->
                    Log.i(tag, "Clicked")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PermissionConstants.WRITE_REQUEST_CODE
                    )
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PermissionConstants.WRITE_REQUEST_CODE
                )
            }
        }
    }
}
