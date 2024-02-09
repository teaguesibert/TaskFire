package com.jamesellerbee.taskfireandroid.bl.page

import com.jamesellerbee.taskfireandroid.dal.entities.Page
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PageProvider(initialPage: Page, val availablePages: List<Page>) {
    private val _selectedPage = MutableStateFlow(initialPage)
    val selectedPage = _selectedPage.asStateFlow()

    fun setSelectedPage(page: Page) {
        _selectedPage.value = page
    }
}