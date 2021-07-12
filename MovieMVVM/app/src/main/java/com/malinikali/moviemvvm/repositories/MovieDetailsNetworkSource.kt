package com.malinikali.moviemvvm.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.models.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkSource(private val apiService:MovieDBInterface,private val  composite: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()

    val networkState : LiveData<NetworkState>
    get() = _networkState

    private val  _downLoadedMovieResponse = MutableLiveData<MovieDetails>()

    val downLoadedMovieResponse:LiveData<MovieDetails>
    get() = _downLoadedMovieResponse

    fun fetchMovieDetails(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)


        try {
            composite.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downLoadedMovieResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message.toString())

                        }
                    )
            )

        }
        catch (e:Exception){
            Log.e("MovieDataSocException", e.message.toString())
        }
    }

}



