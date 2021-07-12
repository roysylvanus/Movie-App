package com.malinikali.moviemvvm.repositories

import androidx.lifecycle.LiveData
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieDBInterface) {

    lateinit var movieDetailNetworkDataSource:  MovieDetailsNetworkSource


    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int) : LiveData<MovieDetails>{

        movieDetailNetworkDataSource = MovieDetailsNetworkSource(apiService,compositeDisposable)
        movieDetailNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailNetworkDataSource.downLoadedMovieResponse
    }

    fun getMovieDetailNetworkState():LiveData<NetworkState>{

        return movieDetailNetworkDataSource.networkState

    }
}