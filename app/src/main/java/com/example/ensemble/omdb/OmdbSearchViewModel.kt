package com.example.ensemble.omdb

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.ensemble.service.ApiService
import com.example.ensemble.service.OmdbData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OmdbSearchViewModel : ViewModel() {
    private val TAG = OmdbSearchViewModel::class.java.name
    private val isSearching: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val mOmdbData: MutableLiveData<List<OmdbData>> by lazy {
        MutableLiveData<List<OmdbData>>()
    }

    fun observeOmdbData(owner: LifecycleOwner, observer: Observer<List<OmdbData>>) {
        mOmdbData.observe(owner, observer)
    }

    fun observeSearching(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isSearching.observe(owner, observer)
    }

    @SuppressLint("CheckResult")
    fun getDataFromApi(title: String) {
        mOmdbData.value = mutableListOf()
        val canSearch = title.length > 3
        if (canSearch) {
            isSearching.value = true
            val observable = ApiService.omdbApiCall().getOmdbMoviesByTitle(title)
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ omdbRootData ->
                    mOmdbData.value = omdbRootData?.result ?: mutableListOf()
                    isSearching.value = false
                }, {
                    isSearching.value = false
                    Log.e(TAG, it.message ?: "Something went wrong")
                }
                )
        } else {
            isSearching.value = false
        }
    }
}