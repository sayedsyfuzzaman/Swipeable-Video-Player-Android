package com.nexdecade.nd_shorts.common

import android.view.View

interface BaseListItemCallback<T: Any> {
    fun onItemClicked(item: T) {}
    fun onOpenMenu(view: View, item: T) {}
    fun onShareClicked(view: View, item: T) {}
    fun onItemClick(position: Int) {}
}