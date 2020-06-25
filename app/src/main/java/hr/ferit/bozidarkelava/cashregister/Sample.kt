package hr.ferit.bozidarkelava.cashregister

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Sample {
    private val et: EditText = EditText()
    private fun f() {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}