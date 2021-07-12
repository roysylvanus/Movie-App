package com.malinikali.moviemvvm.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.malinikali.moviemvvm.interfaces.FIRST_PAGE
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.models.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int,Movie>() {

    private val page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages > params.key){
                            callback.onResult(it.result,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else
                        {
                            networkState.postValue(NetworkState.ENDOFLIST)

                        }
                    },{
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource","ERROR")
                    }
                )
        )    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.result,null,page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },{
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource","ERROR")
                    }
                )
        )
    }
}