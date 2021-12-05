package dev.leonardom.evaluacionupax.feature_movies.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.leonardom.evaluacionupax.core.domain.model.Movie
import dev.leonardom.evaluacionupax.core.util.Constants
import dev.leonardom.evaluacionupax.databinding.LayoutMovieItemBinding

class MovieListAdapter : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieDiffUtil) {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(
        itemBinding: LayoutMovieItemBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

        private val image = itemBinding.imageViewMoviePoster
        private val title = itemBinding.textViewMovieTitle
        private val vote = itemBinding.textViewMovieVote

        private var currentMovie: Movie? = null

        init {
            itemView.setOnClickListener {
                currentMovie?.let { movie ->
                    onItemClickListener?.let {
                        it(movie.id)
                    }
                }
            }
        }

        fun bind(movie: Movie) {

            currentMovie = movie

            image.load("${Constants.IMAGES_BASE_URL}${movie.poster_path}")
            title.text = movie.title
            vote.text = movie.vote_average.toString()
        }

    }

}

object MovieDiffUtil: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
