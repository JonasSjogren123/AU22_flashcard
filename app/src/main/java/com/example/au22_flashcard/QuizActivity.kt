package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Deferred
import androidx.room.Room
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class QuizActivity : AppCompatActivity(), CoroutineScope  {

    lateinit var wordView : TextView
    var currentWord : Word? = null
    private val wordList = mutableListOf<Word>()
    private val usedWords = mutableListOf<Word>()
    private lateinit var db : AppDatabase
    lateinit var startWordActivityButton: Button

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        wordList.clear()
        job = Job()

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

        launch {
            val newWordList = loadAllWords()
            val wordList = newWordList.await()
            addNewWord(wordList)

            // vilken kod vi vill och här har vi vår lista som vi är vana vid
            //start
            //wordList.....????
            for(word in wordList) {

                Log.d("!!!!!!!1","word:$word")
            }
        }
    }

    fun revealTranslation() {
        wordView.text = currentWord?.english
    }


    fun showNewWord() {
        currentWord = getNewWord()
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

    fun addNewWord(list:List<Word>){
        for (word in list) {
            wordList.add(word)
        }
    }

    fun getNewWord() : Word {
        if (wordList.size == usedWords.size) {
            usedWords.clear()
        }

        var word : Word? = null

        do {
            val rnd = (0 until wordList.size).random()
            word = wordList[rnd]
        } while(usedWords.contains(word))



        usedWords.add(word!!)

        return word
    }

}

//Vad skall göras:

//1. Skapa en ny aktivitet där ett nytt ord får skrivas in.
//2. spara det nya ordet i databasen.

//3. I main activity, läs in alla ord från databasen.

// (Använd corouttiner när ni läser och skriver till databasen, se tidigare exempel.)
