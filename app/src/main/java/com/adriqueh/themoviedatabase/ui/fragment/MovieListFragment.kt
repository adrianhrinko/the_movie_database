package com.adriqueh.themoviedatabase.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.adriqueh.themoviedatabase.R
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.databinding.FragmentMovieListBinding
import com.adriqueh.themoviedatabase.databinding.ItemMovieBinding
import com.adriqueh.themoviedatabase.misc.Constants
import com.adriqueh.themoviedatabase.ui.adapter.MovieAdapter
import com.adriqueh.themoviedatabase.ui.adapter.PagingStateAdapter
import com.adriqueh.themoviedatabase.ui.viewmodel.MoviesViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment: BaseFragment<FragmentMovieListBinding, MoviesViewModel>(), MovieAdapter.MovieClickListener, MovieAdapter.MovieInfoLoader, AdapterView.OnItemSelectedListener {

    override val layoutId: Int
        get() = R.layout.fragment_movie_list

    @Inject
    lateinit var adapter: MovieAdapter

    private val viewModel: MoviesViewModel by viewModels()

    override fun getVM(): MoviesViewModel = viewModel

    private val numberOfDays = IntRange(1, 14).toList()

    override fun bindVM(binding: FragmentMovieListBinding, vm: MoviesViewModel) =
        with(binding) {

            disableBackButton()

            ArrayAdapter<String>(this@MovieListFragment.requireContext(), android.R.layout.simple_spinner_item)
                    .let { adapter ->
                        adapter.addAll(numberOfDays.map { this@MovieListFragment.requireContext().resources.getQuantityString(R.plurals.number_of_days, it, it) })
                        spinnerIntervals.adapter = adapter
                        spinnerIntervals.onItemSelectedListener = this@MovieListFragment
                    }

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
                movieInfoLoader = this@MovieListFragment

                with(vm) {
                    launchOnLifecycleScope {
                        getAllMovies()
                        moviesFlow.collectLatest { submitData(it) }
                    }

                    launchOnLifecycleScope {
                        loadStateFlow.collectLatest {
                            swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
                        }
                    }
                }

                spinnerIntervals.setSelection(0)
            }
        }

    override fun onMovieClicked(binding: ItemMovieBinding, movieId: ChangedMovieId) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movieId)
        )
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val endDate = Date()

        val calendar = Calendar.getInstance()
        calendar.time = endDate
        calendar.add(Calendar.DAY_OF_WEEK, -numberOfDays[pos])

        val startDate = calendar.time

        loadMovies(startDate, endDate)
    }

    private fun loadMovies(startDate: Date?, endDate: Date?) {
        Timber.d("Picked interval: startDate($startDate), endDate($endDate)")
        with(adapter) {
            with(viewModel) {
                launchOnLifecycleScope {
                    getAllMovies(startDate, endDate)
                    submitData(PagingData.empty())
                    moviesFlow.collectLatest { submitData(it) }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun loadMovieInfo(binding: ItemMovieBinding, movieId: Long) {
        binding.progressBar.visibility = View.VISIBLE
        with(viewModel) {
            getMovieInfo(movieId) {
                with(binding) {
                    tvTitle.text = it.title
                    it.poster_path?.let { path ->
                        Glide.with(this@MovieListFragment)
                                .load("${Constants.POSTERS_BASE_URL}$path")
                                .into(ivPoster)
                    } ?: run{
                        Glide.with(this@MovieListFragment)
                                .load(R.drawable.ic_film_strip)
                                .into(ivPoster)
                    }

                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}