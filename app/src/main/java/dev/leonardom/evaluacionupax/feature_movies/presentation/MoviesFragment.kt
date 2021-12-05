package dev.leonardom.evaluacionupax.feature_movies.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.leonardom.evaluacionupax.NavGraphDirections
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.databinding.FragmentMoviesBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment: Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var movieListAdapter: MovieListAdapter

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewMovies.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = movieListAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.movieList.collect { movies ->
                movieListAdapter.submitList(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.displayProgressBar.collect { state ->
                binding.progressBar.isVisible = state == ProgressBarState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.errorMessageQueue.collect { queue ->
                if(!queue.isEmpty()){
                    queue.peek()?.let { uiComponent ->
                        if(uiComponent is UIComponent.Dialog){
                            val action = NavGraphDirections.actionGlobalErrorDialog(
                                title = uiComponent.title,
                                description = uiComponent.description
                            )
                            findNavController().navigate(action)
                            viewModel.removeHeadMessage()
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}