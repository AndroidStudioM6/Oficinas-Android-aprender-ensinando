package com.example.oficina_android

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object DateInputFormatter {
    fun attach(input: EditText) {
        input.addTextChangedListener(object : TextWatcher {
            private var formatting = false
            private var digitCountBeforeCursor = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (formatting) return
                val text = s?.toString().orEmpty()
                val cursor = input.selectionStart.coerceIn(0, text.length)
                digitCountBeforeCursor = text.take(cursor).count(Char::isDigit)
            }

            override fun afterTextChanged(editable: Editable?) {
                if (formatting || editable == null) return
                val original = editable.toString()
                val digits = original.filter(Char::isDigit).take(8)
                val boundedDigits = digits.toCharArray()
                if (digits.length >= 2) {
                    val day = digits.substring(0, 2).toIntOrNull()?.coerceIn(1, 31) ?: 1
                    day.toString().padStart(2, '0').forEachIndexed { index, digit ->
                        boundedDigits[index] = digit
                    }
                }
                if (digits.length >= 4) {
                    val month = digits.substring(2, 4).toIntOrNull()?.coerceIn(1, 12) ?: 1
                    month.toString().padStart(2, '0').forEachIndexed { index, digit ->
                        boundedDigits[index + 2] = digit
                    }
                }
                val normalizedDigits = String(boundedDigits)
                val formatted = when {
                    normalizedDigits.length > 4 -> "${normalizedDigits.substring(0, 2)}/${normalizedDigits.substring(2, 4)}/${normalizedDigits.substring(4)}"
                    normalizedDigits.length > 2 -> "${normalizedDigits.substring(0, 2)}/${normalizedDigits.substring(2)}"
                    else -> normalizedDigits
                }
                if (formatted == original) return

                val cursor = when {
                    digitCountBeforeCursor > 4 -> digitCountBeforeCursor + 2
                    digitCountBeforeCursor > 2 -> digitCountBeforeCursor + 1
                    else -> digitCountBeforeCursor
                }.coerceIn(0, formatted.length)

                // Defer replacing the text until the IME finishes its current edit transaction.
                input.post {
                    if (input.text?.toString() != original) return@post
                    formatting = true
                    try {
                        input.setText(formatted)
                        input.setSelection(cursor.coerceAtMost(input.text?.length ?: 0))
                    } finally {
                        formatting = false
                    }
                }
            }
        })
    }
}
