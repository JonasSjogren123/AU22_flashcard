package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import androidx.room.Room
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class QuizActivity : AppCompatActivity() {

    lateinit var wordView : TextView
    var currentWord : Word? = null
    val wordList = WordList()
    lateinit var db : AppDatabase
    lateinit var startWordActivityButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        db = AppDatabase.getInstance(this)

        wordView = findViewById(R.id.wordTextView)
        startWordActivityButton = findViewById(R.id.startWordActivityButton)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }

        startWordActivityButton.setOnClickListener {
            startNewWordActivity()
        }

        val list = loadAllWords()


        launch {
            val wordList = list.await()

            // vilken kod vi vill och här har vi vår lista som vi är vana vid
            //start
            for(word in wordList) {
                Log.d("!!!!!!!!","word:$word")
            }
        }

    }

    fun revealTranslation() {
        wordView.text = currentWord?.english
    }


    fun showNewWord() {

        currentWord = wordList.getNewWord()
        wordView.text = currentWord?.swedish
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }

    fun startNewWordActivity(){
        val intent = Intent(this, NewWordActivity::class.java)
        // start your next activity
        startActivity(intent)
    }

    fun loadAllWords() : Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().getAllWords()
        }

}

//Vad skall göras:

//1. Skapa en ny aktivitet där ett nytt ord får skrivas in.
//2. spara det nya ordet i databasen.

//3. I main activity, läs in alla ord från databasen.

// (Använd corouttiner när ni läser och skriver till databasen, se tidigare exempel.)
