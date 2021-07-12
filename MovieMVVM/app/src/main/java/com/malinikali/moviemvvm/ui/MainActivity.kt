package com.malinikali.moviemvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.map
import androidx.recyclerview.widget.GridLayoutManager
import com.malinikali.moviemvvm.R
import com.malinikali.moviemvvm.adapters.PopularMoviePageAdapter
import com.malinikali.moviemvvm.databinding.ActivityMainBinding
import com.malinikali.moviemvvm.interfaces.MovieDBClient
import com.malinikali.moviemvvm.interfaces.MovieDBInterface
import com.malinikali.moviemvvm.repositories.MoviePageListRepository
import com.malinikali.moviemvvm.repositories.NetworkState
import com.malinikali.moviemvvm.viewModels.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel : MainActivityViewModel

    lateinit var moviePageListRepository: MoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService:MovieDBInterface = MovieDBClient.getClient()
         moviePageListRepository = MoviePageListRepository(apiService)
        viewModel = getViewModel()

        val moviePageAdapter = PopularMoviePageAdapter(this)
        val gridLayourManager = GridLayoutManager(this,3)

        gridLayourManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = moviePageAdapter.getItemViewType(position)
                if (viewType == moviePageAdapter.MOVIE_VIEW_TYPE) return 1
                else
                    return 3
            }
        }

        binding.rvMovieList.layoutManager = gridLayourManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = moviePageAdapter

        viewModel.moviePagedList.observe(this, Observer{
           moviePageAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer{
            binding.pgPopular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else
                View.GONE
            binding.tvError.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else
                View.GONE

            if (!viewModel.listIsEmpty()){
                moviePageAdapter.setNetworkState(it)
            }
        })


    }


    private fun getViewModel(): MainActivityViewModel {

        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED CAST")
                return MainActivityViewModel(moviePageListRepository) as T
            }

        })[MainActivityViewModel::class.java]
    }
}