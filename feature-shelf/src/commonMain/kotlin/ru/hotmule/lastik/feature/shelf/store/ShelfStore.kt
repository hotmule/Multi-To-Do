package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*

interface ShelfStore : Store<Intent, State, Nothing> {

    sealed class Intent {

    }

    sealed class Result {
        data class ItemsReceived(val items: List<ShelfItem>) : Result()
    }

    data class State(
        val items: List<ShelfItem> = listOf(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false
    )
}