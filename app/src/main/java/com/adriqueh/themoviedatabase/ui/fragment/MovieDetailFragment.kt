package com.adriqueh.themoviedatabase.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adriqueh.themoviedatabase.R
import com.adriqueh.themoviedatabase.data.model.Genre
import com.adriqueh.themoviedatabase.databinding.FragmentMovieDetailBinding
import com.adriqueh.themoviedatabase.misc.Constants
import com.adriqueh.themoviedatabase.misc.observe
import com.adriqueh.themoviedatabase.ui.viewmodel.MovieDetailViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment: BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_movie_detail

    private val characterDetailViewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun getVM(): MovieDetailViewModel = characterDetailViewModel

    override fun bindVM(binding: FragmentMovieDetailBinding, vm: MovieDetailViewModel) =
            with(vm) {
                enableBackButton()

                launchOnLifecycleScope {
                    getMovieInfo(args.movieId.id)
                    observe(isLoading) { loading ->
                        if (!loading) {
                            updateUi(binding, this)
                        }
                    }
                }
            }

    private fun updateUi(binding: FragmentMovieDetailBinding, vm: MovieDetailViewModel) = with(binding) {
        with(vm) {
            tvOverview.text = movieDetail.overview
            tvLanguage.text = movieDetail.original_language
            tvReleaseDate.text = movieDetail.release_date
            tvTitle.text = movieDetail.title
            tvGenre.text = getGenresString(movieDetail.genres)
            movieDetail.poster_path?.let {
                Glide.with(requireContext())
                        .load("${Constants.POSTERS_BASE_URL}$it")
                        .into(ivPoster)
            }
        }
    }

    private fun getGenresString(genres: List<Genre>): CharSequence? {
        val builder = StringBuilder()

        genres.forEach{
            builder.append(it.name)
            builder.append(" | ")
        }

        return builder.toString()
    }

}