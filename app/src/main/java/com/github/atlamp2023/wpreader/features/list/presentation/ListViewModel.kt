package com.github.atlamp2023.wpreader.features.list.presentation

import android.util.Log
import androidx.lifecycle.*
import com.github.atlamp2023.wpreader.core.util.*
import com.github.atlamp2023.wpreader.features.list.domain.ListInteractor
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

const val START_PAGE = 1
const val DEFAULT_MAX_PAGES = 1

typealias ResultList = List<ListItem>?
typealias Items = Result<ResultList>

class ListViewModel(private val interactor: ListInteractor,
                    stateValue: State
): ViewModel() {

    private val _list: MutableLiveData<Items> = MutableLiveData()
    val list: LiveData<Items> = _list

    private var maxPage: Int = DEFAULT_MAX_PAGES

    private var _state: MutableLiveData<State> = MutableLiveData(stateValue)
    val state: LiveData<State> = _state
    private var stateChanged: MutableLiveData<Boolean> = MutableLiveData(true)
    private val stateObserver: Observer<Boolean> = Observer {
        if(stateChanged.value == true){
            updateListDefault()
            stateChanged.value = false
        }
    }

    private var currentPage: Int = START_PAGE

    private var currentTag: Int? = null

    var currentPosition: Int = 0
        private set

    init {
        stateChanged.observeForever(stateObserver)
    }

    fun handleUserAction(act: UserActionOnList){
        when(act){
            is UserActionOnList.SwitchState -> {
                //setState(act.value)
            }
            is UserActionOnList.ScrollList -> {
                updateListByNewPage(act.value)
            }
            is UserActionOnList.ClickOnItem -> {
                currentPosition = act.value
            }
        }
    }

    fun isCurrentPageEqualStartPage() = (currentPage == START_PAGE)

    fun isCurrentPageEqualMaxPage() = (currentPage == maxPage)

    /*private fun setState(newState: State){
        _state.value = newState
        stateChanged.value = true
        currentPage = START_PAGE
        currentPosition = 0
    }*/

    private fun updateListByNewPage(scrollOffset: ScrollDirection) {
        var newValue = currentPage + scrollOffset.value
        newValue = min(newValue, maxPage)
        newValue = max(START_PAGE, newValue)
        updateList(pageNumber = newValue)
        currentPosition = 0
    }

    private fun updateListDefault() {
        detectMaxPages()
        //currentTag = null
        _list.value = interactor.showProgress()
        updateList(pageNumber = currentPage)
        //_list.value = interactor.getList(state = state.value, pageNumber = currentPage, tagId = currentTag)
    }

    private fun updateList(pageNumber: Int) = viewModelScope.launch {
        val tmpListResult = interactor.getList(state = state.value, pageNumber = pageNumber, tagId = currentTag)
        tmpListResult.let {
            if(it !is Result.Empty) {
                _list.value = it
                currentPage = pageNumber
            }
        }
    }

    private fun detectMaxPages() = viewModelScope.launch {
        maxPage = interactor.detectMaxPages()
    }

    override fun onCleared() {
        stateChanged.removeObserver(stateObserver)
        super.onCleared()
    }
}