package com.malinikali.moviemvvm.interfaces

import com.malinikali.moviemvvm.models.MovieDetails
import com.malinikali.moviemvvm.models.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int):Single<MovieResponse>


}