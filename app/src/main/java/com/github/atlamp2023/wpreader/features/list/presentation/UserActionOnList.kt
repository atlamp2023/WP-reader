package com.github.atlamp2023.wpreader.features.list.presentation

import com.github.atlamp2023.wpreader.core.util.State

sealed class UserActionOnList {
    class SwitchState(val value: State): UserActionOnList()
    class ScrollList(val value: ScrollDirection): UserActionOnList()
    class ClickOnItem(val value: Int): UserActionOnList()
}
