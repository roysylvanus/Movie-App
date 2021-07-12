package com.malinikali.moviemvvm.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.models.Movie
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(private val apiService: MovieDBInterface,private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int,Movie>() {

    val mutableLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {

        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        mutableLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}