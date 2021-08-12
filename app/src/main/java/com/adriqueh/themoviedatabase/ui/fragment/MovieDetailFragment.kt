package com.adriqueh.themoviedatabase.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adriqueh.themoviedatabase.R
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
        with(binding) {
            with(vm) {
                launchOnLifecycleScope {
                    getMovieInfo(args.movieId.id)
                    observe(movieDetail) { movie ->
                        tvOverview.text = movie.overview
                        tvTitle.text = movie.title
                        Glide.with(requireContext())
                            .load("${Constants.POSTERS_BASE_URL}${movie.poster_path}")
                            .into(ivPoster)
                    }
                }
            }
    }

}