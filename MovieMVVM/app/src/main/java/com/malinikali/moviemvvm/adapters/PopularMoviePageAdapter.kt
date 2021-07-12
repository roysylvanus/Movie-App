package com.malinikali.moviemvvm.adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malinikali.moviemvvm.R
import com.malinikali.moviemvvm.databinding.MovieItemBinding
import com.malinikali.moviemvvm.interfaces.POSTER_BASE_URL
import com.malinikali.moviemvvm.models.Movie
import com.malinikali.moviemvvm.repositories.NetworkState
import com.malinikali.moviemvvm.ui.SingleMovie

class PopularMoviePageAdapter(  val context: Context):PagedListAdapter<Movie,RecyclerView.ViewHolder>(MovieDiffCallback()) {
        val MOVIE_VIEW_TYPE = 1
        val NETWORK_VIEW_TYPE = 2

    private var  networkState:NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        } else
        {
            networkState?.let { (holder as NetworkStateItemViewHolder).bind(it) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val view:View
        if (viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_item,parent,false)
            return MovieItemViewHolder(view)
        }else
        {
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return MovieItemViewHolder(view)
        }


    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        } else
        {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback:DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return  oldItem.id == newItem.id
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){


        fun bind(movie : Movie?, context: Context){
            itemView.tvMoviePosterPop.text = movie?.title
            itemView.tvMovieReleaseDate.text = movie?.releaseDate.toString()

            val moviePosterURl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURl)
                .into(itemView.ivMoviePosterPop)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id",movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(networkState:NetworkState){

            if (networkState!=null && networkState == NetworkState.LOADING){
                itemView.pg.visibility = View.VISIBLE

            }else{
                itemView.pg.visibility = View.GONE

            }


            if (networkState!=null && networkState == NetworkState.ERROR){
                itemView.tvError.visibility = View.VISIBLE
                itemView.tvError.text = networkState.msg

            }else if (networkState!=null && networkState == NetworkState.ENDOFLIST){
                itemView.tvError.visibility = View.VISIBLE
                itemView.tvError.text = networkState.msg
            }
            else{
                itemView.tvError.visibility = View.GONE
            }

        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow !=  hasExtraRow){
            if (hadExtraRow)
            {
                notifyItemRemoved(super.getItemCount())
            }
        else
            {
                notifyItemInserted(super.getItemCount())
            }
        } else if(hasExtraRow && previousState != newNetworkState) {
                notifyItemChanged(itemCount-1)
        }
    }
}