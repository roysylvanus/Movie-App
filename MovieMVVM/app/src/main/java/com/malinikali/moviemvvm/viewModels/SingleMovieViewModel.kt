package com.malinikali.moviemvvm.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.malinikali.moviemvvm.repositories.MovieDetailsRepository
import com.malinikali.moviemvvm.repositories.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieDetailsRepository: MovieDetailsRepository, movieId:Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieDetailsRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}