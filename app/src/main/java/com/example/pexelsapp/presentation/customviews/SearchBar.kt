package com.example.pexelsapp.presentation.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.SearchBarLayoutBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val searchSubject: PublishSubject<String> = PublishSubject.create()
    private var disposable: Disposable? = null
    private val binding: SearchBarLayoutBinding = SearchBarLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var onSearchListener: ((String) -> Unit)? = null
    var onClearListener: (() -> Unit)? = null

    private var searchQuery = AppConfig.getEmptyString()
    private val searchEditText = binding.searchEditText
    private val clearIcon = binding.clearIcon

    init {
        initViews()
        initSearch()
    }

    private fun initViews() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                searchSubject.onNext(searchQuery)
                val isEmpty = searchQuery.isBlank()
                clearIcon.isVisible = !isEmpty
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                onSearchListener?.invoke(searchQuery)
                true
            } else {
                false
            }
        }

        clearIcon.setOnClickListener {
            clearTextField()
            onClearListener?.invoke()
        }
    }

    private fun initSearch() {
        disposable = searchSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { query ->
                onSearchListener?.invoke(query)
            }
    }

    fun setRequestText(title: String) {
        binding.searchEditText.apply {
            text.clear()
            setText(title)
        }
    }

    fun clearTextField() {
        searchEditText.text.clear()
    }


    fun onDestroy() {
        disposable?.dispose()
    }

}
