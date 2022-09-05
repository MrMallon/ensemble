package com.example.ensemble.omdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ensemble.databinding.FragmentOmdbSearchBinding
import com.example.ensemble.utils.Extension.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OmdbSearchFragment : Fragment() {

    private lateinit var viewModel: OmdbSearchViewModel
    private lateinit var mBinding: FragmentOmdbSearchBinding
    private lateinit var mSearchAdapter: SearchAdapter
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OmdbSearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOmdbSearchBinding.inflate(inflater)
        return mBinding.root
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchAdapter = SearchAdapter(mutableListOf())

        mBinding.recycler.layoutManager = LinearLayoutManager(this.context)
        mBinding.recycler.adapter = mSearchAdapter

        viewModel.observeSearching(this) { searching ->
            mBinding.searchProgress.visibility = if (searching) View.VISIBLE else View.GONE
        }
        viewModel.observeOmdbData(this) {
            mSearchAdapter.updateList(it)
        }
        mBinding.titleEt.textChanges().debounce(500).onEach {
            viewModel.getDataFromApi(it.toString())
        }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}