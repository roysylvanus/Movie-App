package com.malinikali.moviemvvm.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.LivePagedListBuilder

import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.interfaces.POSTS_PER_PAGE
import com.malinikali.moviemvvm.models.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService: MovieDBInterface) {

    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory


    fun fetchMoviePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>>{
        movieDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POSTS_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviePageList
    }

    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource,NetworkState>(
            movieDataSourceFactory.mutableLiveDataSource,MovieDataSource::networkState
        )

    }
}