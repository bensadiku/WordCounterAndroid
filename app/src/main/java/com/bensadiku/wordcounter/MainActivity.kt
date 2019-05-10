package com.bensadiku.wordcounter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var clearButton: Button
    private lateinit var calculateButton: Button
    private lateinit var calculatedTextView: TextView
    private lateinit var pastedText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateButton = findViewById(R.id.activity_main_calculate_button)
        clearButton = findViewById(R.id.activity_main_clear_button)
        calculatedTextView = findViewById(R.id.activity_main_word_count_text_view)
        pastedText = findViewById(R.id.activity_main_word_count_edit_text)

        calculateButton.setOnClickListener {
            if (!pastedText.text.isNullOrEmpty()) {
                calculateWords(pastedText.text.toString())
            } else {
                calculatedTextView.text = getText(R.string.word_init)
            }
        }

        /**
         * Clear the edit text, reset the text view
         */
        clearButton.setOnClickListener {
            calculatedTextView.text = getText(R.string.word_init)
            pastedText.text.clear()
        }

        /**
         * Copy the results to keyboard when the textView is clicked
         */
        calculatedTextView.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", calculatedTextView.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this, "Copied results to clipboard!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Calculates words in the @param text
     * @param text returns all the text in the edit text when the button calculate is pressed
     *
     */
    private fun calculateWords(text: String) {
        val numOfWords = text.trim().split("\\s+".toRegex()).size
        val numOfLetters = text.length
        calculatedTextView.text = getString(R.string.total_words, numOfWords, numOfLetters).toSpanned()
    }

    /**
     * Extension function that formats the HTML from string resources.
     * Why HTML? Because the numbers need to be bold and to have placeholders for total words and total letters numbers
     */
    private fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }

//    fun phrase(phrase: String): Map<String, Int> {
//        return toWords(phrase).groupBy { it }.mapValues { it.value.size }
//    }
//
//    private fun toWords(phrase: String): List<String> {
//        return phrase.toLowerCase().replace(Regex("[^\\w']"), " ").trim().split(Regex("\\s+"))
//    }
}
