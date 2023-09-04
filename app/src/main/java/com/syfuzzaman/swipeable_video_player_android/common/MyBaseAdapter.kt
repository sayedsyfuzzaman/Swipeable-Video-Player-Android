package com.syfuzzaman.swipeable_video_player_android.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class MyBaseAdapter<T: Any>(val callback: BaseListItemCallback<T>? = null) :RecyclerView.Adapter<MyViewHolder>() {

    protected val values: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType, parent, false
        )
        adjustLayout(binding)
        return MyViewHolder(binding)
    }

    open fun adjustLayout(binding: ViewDataBinding){}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(obj, callback, position)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    private fun getObjForPosition(position: Int): T = values[position]

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    override fun getItemCount():Int {
        return values.size
    }

    fun add(item: T) = values.add(item)

    fun add(position: Int, item: T) = values.add(position, item)

    fun addAll(items: List<T>) {
        val positionStart = values.size
        values.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }

    fun remove(item: T){
        val index = values.indexOf(item)
        values.removeAt(index)
        notifyItemRemoved(index)
    }

    fun remove(position: Int) {
        values.removeAt(position)
        notifyItemRemoved(position)
    }

    fun reloadItem(item: T) {
        val idx = values.indexOf(item)
        notifyItemChanged(idx)
    }

    fun reloadItem(pos: Int) {
        notifyItemChanged(pos)
    }

    fun getItem(position: Int): T {
        return values[position]
    }

    fun getItems(): List<T> = values

    fun removeAll()  {
        values.clear()
        notifyDataSetChanged()
    }
}