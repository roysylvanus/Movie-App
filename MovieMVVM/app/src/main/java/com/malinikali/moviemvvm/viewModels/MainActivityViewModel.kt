package com.malinikali.moviemvvm.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.malinikali.moviemvvm.models.Movie
import com.malinikali.moviemvvm.repositories.MoviePageListRepository
import com.malinikali.moviemvvm.repositories.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val moviePageListRepository: MoviePageListRepository):ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        moviePageListRepository.fetchMoviePageList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        moviePageListRepository.getNetworkState()
    }

     fun listIsEmpty():Boolean{
         return moviePagedList.value?.isEmpty() ?: true

     }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}