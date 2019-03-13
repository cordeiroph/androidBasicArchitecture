package com.phrc.androidarchitecture.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.phrc.androidarchitecture.ui.main.RepositoriesViewHolder

abstract class AbstractRecycleViewAdapter<T : AdapterItem<*>, B : ViewDataBinding> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listObjects: List<T>? = null
        protected set

    protected abstract fun layoutResourceForItem(position: Int): Int

    protected abstract fun fillHolder(abstractHolder: RecyclerView.ViewHolder, position: Int)

    protected abstract fun inflateView(binding: B, viewType: Int): RecyclerView.ViewHolder

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<B>(LayoutInflater.from(viewGroup.context), layoutResourceForItem(viewType), viewGroup, false)
        return inflateView(binding, viewType)
    }

    override fun onBindViewHolder(abstractHolder: RecyclerView.ViewHolder, position: Int) {
        (abstractHolder as AbstractRecyclerViewHolder<B>).binding.setVariable(BR.data, listObjects!![position])
        fillHolder(abstractHolder, position)
    }

    override fun getItemCount(): Int {
        return if (listObjects == null) {
            0
        } else listObjects!!.size
    }

    protected fun getItem(position: Int): T {
        return listObjects!![position]
    }

    protected fun getItemPosition(item: T): Int {
        for (i in listObjects!!.indices) {
            if (listObjects!![i].adapterId == item.adapterId) {
                return i
            }
        }
        return -1
    }

    fun updateItem(item: T) {
        val position = getItemPosition(item)
        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    fun updateAll() {
        notifyDataSetChanged()
    }

    fun updateList(list: List<T>) {
        if (listObjects == null) {
            listObjects = list
            notifyItemRangeInserted(0, listObjects!!.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listObjects!!.size
                }

                override fun getNewListSize(): Int {
                    return list.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return listObjects!![oldItemPosition]
                            .adapterId == list[newItemPosition]
                            .adapterId
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newItem = list[newItemPosition]
                    val oldItem = listObjects!![oldItemPosition]
                    return newItem == oldItem
                }
            })
            listObjects = list
            result.dispatchUpdatesTo(this)
        }
    }
}
