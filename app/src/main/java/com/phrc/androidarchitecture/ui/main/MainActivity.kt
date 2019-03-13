package com.phrc.androidarchitecture.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.phrc.androidarchitecture.BR
import com.phrc.androidarchitecture.R
import com.phrc.androidarchitecture.dagger.Injectable
import com.phrc.androidarchitecture.databinding.ActivityMainBinding
import com.phrc.androidarchitecture.repository.DataRequestModel
import com.phrc.androidarchitecture.ui.base.DataRequestUIHandler
import com.phrc.androidarchitecture.ui.base.DataRequestUIHandler.SuccessCallback
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private lateinit var bind : ActivityMainBinding
    private lateinit var repositoriesRequest : DataRequestUIHandler<DataRequestModel<RepositoriesListViewData>, RepositoriesListViewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind =  DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        bind.setVariable(BR.viewData, viewModel.viewData)

        val adapter = RepositoriesAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        bind.lsvRepositories.layoutManager = linearLayoutManager
        bind.lsvRepositories.adapter = adapter


        repositoriesRequest = DataRequestUIHandler(
                this,
                bind.root,
                object : SuccessCallback <RepositoriesListViewData> {
                    override fun onSuccess(obj: RepositoriesListViewData) {
                        adapter.updateList(obj.items)
                    }
                }
        )

        viewModel.respositories.observe(this, Observer { repositoriesRequest.handleResult(it)})

        viewModel.params.value = "PedroAttitudeTech"

        bind.search.setOnClickListener { viewModel.params.value = viewModel.viewData.search }

    }
}
