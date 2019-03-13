package com.phrc.androidarchitecture.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class AbstractRecyclerViewHolder<T : ViewDataBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)
