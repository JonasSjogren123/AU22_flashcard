package com.example.au22_flashcard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Index
import androidx.room.Insert
import androidx.room.Query
import java.util.Locale.Category


@Dao
interface WordDao {

    @Insert
    fun insertWord(word:Word)

    @Delete
    fun deleteWord(word: Word)

    @Query("SELECT * FROM word_table")
    fun getAllWords() : List<Word>

    /*@Query("SELECT * FROM word_table WHERE category LIKE :categoryName")
    fun findWordsByCategory(categoryName: String) : List<Word>*/
}