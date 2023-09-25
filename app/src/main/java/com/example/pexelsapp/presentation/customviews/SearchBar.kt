package com.example.pexelsapp.presentation.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.SearchBarLayoutBinding

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SearchBarLayoutBinding = SearchBarLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var searchQuery = AppConfig.getEmptyString()
    private val searchEditText = binding.searchEditText
    private val clearIcon = binding.clearIcon

    init {
        initViews()
    }

    private fun initViews() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                val isEmpty = searchQuery.isBlank()
                clearIcon.isVisible = !isEmpty
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e("text", "${searchEditText.text}")
                true
            } else {
                false
            }
        }

        clearIcon.setOnClickListener {
            clearTextField()
        }
    }

    private fun clearTextField() {
        searchEditText.text.clear()
    }

}
