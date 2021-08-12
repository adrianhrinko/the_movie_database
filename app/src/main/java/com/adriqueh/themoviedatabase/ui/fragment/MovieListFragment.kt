package com.adriqueh.themoviedatabase.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.adriqueh.themoviedatabase.R
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.model.Movie
import com.adriqueh.themoviedatabase.databinding.FragmentMovieListBinding
import com.adriqueh.themoviedatabase.databinding.ItemMovieBinding
import com.adriqueh.themoviedatabase.ui.adapter.MovieAdapter
import com.adriqueh.themoviedatabase.ui.adapter.PagingStateAdapter
import com.adriqueh.themoviedatabase.ui.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment: BaseFragment<FragmentMovieListBinding, MoviesViewModel>(), MovieAdapter.MovieClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_movie_list

    @Inject
    lateinit var adapter: MovieAdapter

    private val viewModel: MoviesViewModel by viewModels()

    override fun getVM(): MoviesViewModel = viewModel

    override fun bindVM(binding: FragmentMovieListBinding, vm: MoviesViewModel) =
        with(binding) {
            with(adapter) {
                recyclerViewMovies.apply {
                    postponeEnterTransition()
                    viewTreeObserver.addOnPreDrawListener {
                        startPostponedEnterTransition()
                        true
                    }
                }

                recyclerViewMovies.adapter = withLoadStateHeaderAndFooter(
                    PagingStateAdapter(this),
                    PagingStateAdapter(this)
                )
                swipeRefresh.setOnRefreshListener { refresh() }
                movieClickListener = this@MovieListFragment

                with(vm) {
                    launchOnLifecycleScope {
                        moviesFlow.collectLatest { submitData(it) }
                    }
                    launchOnLifecycleScope {
                        loadStateFlow.collectLatest {
                            swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
                        }
                    }
                }

            }
        }

    override fun onMovieClicked(binding: ItemMovieBinding, movieId: ChangedMovieId) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movieId)
        )
    }
}