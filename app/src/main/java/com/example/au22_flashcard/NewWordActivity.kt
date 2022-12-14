package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NewWordActivity : AppCompatActivity(), CoroutineScope {

    lateinit var newWordSwedishView : EditText
    lateinit var newWordEnglishView : EditText
    var currentWord : Word? = null
   // val wordList = WordList()
    lateinit var word : Word
    private lateinit var job : Job
    lateinit var db : AppDatabase
    lateinit var saveNewWordButton : Button
    lateinit var startQuizActivityButton : Button

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        job = Job()

        db = AppDatabase.getInstance(this)
        newWordSwedishView = findViewById(R.id.newWordSwedishEditTextView)
        newWordEnglishView = findViewById(R.id.newWordEnglishEditTextView)
        saveNewWordButton = findViewById(R.id.saveNewWordButton)
        startQuizActivityButton = findViewById(R.id.startQuizActivity)

        //showNewWord()

        saveNewWordButton.setOnClickListener {
            val swedishWord = newWordSwedishView.text.toString()
            val englishWord = newWordEnglishView.text.toString()


           val word =  Word(0, englishWord, swedishWord)
            saveWord(word)
        }

        startQuizActivityButton.setOnClickListener{
            startQuizActivity()
        }
    }

    fun delete(word: Word) =
        launch (Dispatchers.IO) {
            db.wordDao().deleteWord(word)
        }

    /*fun loadByCategory(category: String) : Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().findWordsByCategory(category)
        }

     */

    fun saveWord(word: Word) {
        launch(Dispatchers.IO) {
            db.wordDao().insertWord(word)
        }
    }

    fun startQuizActivity(){
        val intent = Intent(this, QuizActivity::class.java)
        // start your next activity
        finish()
    }

}

//Vad skall g??ras:

//1. Skapa en ny aktivitet d??r ett nytt ord f??r skrivas in.
//2. spara det nya ordet i databasen.

//3. I main activity, l??s in alla ord fr??n databasen.

// (Anv??nd corouttiner n??r ni l??ser och skriver till databasen, se tidigare exempel.)