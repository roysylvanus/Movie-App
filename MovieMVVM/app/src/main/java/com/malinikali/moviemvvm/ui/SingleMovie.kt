package com.malinikali.moviemvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.malinikali.moviemvvm.databinding.ActivitySingleMovieBinding
import com.malinikali.moviemvvm.interfaces.MovieDBClient
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.interfaces.POSTER_BASE_URL
import com.malinikali.moviemvvm.models.MovieDetails
import com.malinikali.moviemvvm.repositories.MovieDetailsRepository
import com.malinikali.moviemvvm.repositories.NetworkState
import com.malinikali.moviemvvm.viewModels.SingleMovieViewModel

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var binding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId: Int = intent.getIntExtra("id",1)

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.pg.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: MovieDetails?) {

        if (it != null) {
            binding.tvMovieTitle.text = it.title
            binding.tvMovieTagLine.text = it.tagline
            binding.tvBudget.text = it.budget.toString()
            binding.tvOverview.text = it.overview
            binding.tvRating.text = it.voteAverage.toString()
            binding.tvReleaseDate.text = it.releaseDate
            binding.tvRuntime.text = it.runtime.toString()

            val moviePosterUrl = POSTER_BASE_URL + it.posterPath
            Glide.with(this)
                .load(moviePosterUrl)
                .into(binding.ivMoviePoster)

        }

    }

    private fun getViewModel(movieId:Int): SingleMovieViewModel{
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED CAST")
                return SingleMovieViewModel(movieDetailsRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]

    }
}