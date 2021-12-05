package dev.leonardom.evaluacionupax.core.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.databinding.DialogErrorBinding
import dev.leonardom.evaluacionupax.databinding.FragmentMoviesBinding
import dev.leonardom.evaluacionupax.feature_movies.presentation.MovieListAdapter
import dev.leonardom.evaluacionupax.feature_movies.presentation.MoviesViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ErrorDialog : DialogFragment() {

    private var _binding: DialogErrorBinding? = null
    private val binding get() = _binding!!

    private val args: ErrorDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewDialogTitle.text = args.title
        binding.textViewDialogDescription.text = args.description

        binding.buttonDialogOk.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}