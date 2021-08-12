package com.adriqueh.themoviedatabase.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.model.Movie
import com.adriqueh.themoviedatabase.databinding.ItemMovieBinding
import com.adriqueh.themoviedatabase.misc.Constants
import com.bumptech.glide.Glide
import javax.inject.Inject


class MovieAdapter @Inject constructor() :
    PagingDataAdapter<ChangedMovieId, MovieAdapter.MovieViewHolder>(MovieComparator) {
    var movieClickListener: MovieClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                movieClickListener?.onMovieClicked(
                    binding,
                    getItem(absoluteAdapterPosition) as ChangedMovieId
                )
            }
        }

        fun bind(item: ChangedMovieId) = with(binding) {
            tvTitle.text = "Movie id: ${item.id}"
            /*
            Glide.with(context)
                .load("${Constants.POSTERS_BASE_URL}${item.poster_path}")
                .into(imageViewCover)

             */
        }
    }

    object MovieComparator : DiffUtil.ItemCallback<ChangedMovieId>() {
        override fun areItemsTheSame(oldItem: ChangedMovieId, newItem: ChangedMovieId) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChangedMovieId, newItem: ChangedMovieId) =
            oldItem == newItem
    }

    interface MovieClickListener {
        fun onMovieClicked(binding: ItemMovieBinding, movieId: ChangedMovieId)
    }


}