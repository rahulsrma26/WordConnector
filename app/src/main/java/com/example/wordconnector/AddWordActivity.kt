package com.example.wordconnector

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_add_word.*

class AddWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var buttonAdd = findViewById<Button>(R.id.btnAdd)
        var answerTxt = findViewById<EditText>(R.id.tiAnswer)
        var questionTxt = findViewById<EditText>(R.id.tiQuestion)

        buttonAdd.setOnClickListener {
            Logic.addPair(questionTxt.text.toString(), answerTxt.text.toString())
            Logic.saveData(applicationContext)
            finish()
        }
    }

}
