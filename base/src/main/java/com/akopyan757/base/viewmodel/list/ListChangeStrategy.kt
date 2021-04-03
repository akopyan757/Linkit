package com.akopyan757.base.viewmodel.list

sealed class ListChangeStrategy(var after: (() -> Unit)? = null) {
    class Cleared(after: (() -> Unit)? = null) : ListChangeStrategy(after)
    class Initialized(after: (() -> Unit)? = null) : ListChangeStrategy(after)
    class CustomChanged(after: (() -> Unit)? = null) : ListChangeStrategy(after)
    class RangeChanged(val range: IntRange, after: (() -> Unit)? = null) : ListChangeStrategy(after)
    class RangeInserted(val range: IntRange, after: (() -> Unit)? = null) : ListChangeStrategy(after)
}