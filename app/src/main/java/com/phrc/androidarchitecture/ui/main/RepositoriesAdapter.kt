package com.phrc.androidarchitecture.ui.main

import android.util.Log
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.phrc.androidarchitecture.R
import com.phrc.androidarchitecture.databinding.ListItemRepositoriesBinding
import com.phrc.androidarchitecture.ui.base.AbstractRecycleViewAdapter

class RepositoriesAdapter :  AbstractRecycleViewAdapter<RepositoriesViewData, ListItemRepositoriesBinding>() {

    override fun layoutResourceForItem(position: Int): Int {
        return R.layout.list_item_repositories
    }

    override fun fillHolder(abstractHolder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun inflateView(binding: ListItemRepositoriesBinding, viewType: Int): RecyclerView.ViewHolder {
        return RepositoriesViewHolder(binding)
    }
}