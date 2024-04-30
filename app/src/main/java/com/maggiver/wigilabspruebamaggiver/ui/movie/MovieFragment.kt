package com.maggiver.wigilabspruebamaggiver.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.databinding.FragmentMovieBinding
import com.maggiver.wigilabspruebamaggiver.presentation.PopularMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModelPopularMovie by viewModels<PopularMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayouts()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //custom methods *******************************************************************************
    private fun setupLayouts() {
        binding.rvHomeFragment.layoutManager = GridLayoutManager(
            requireContext(), 2, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun setupObservers() {

        /*iewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelPopularMovie.moviePopular1(requireContext()).collect()*/

        viewModelPopularMovie.moviePopular2(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelPopularMovie.uiState.collect() {
                    when (it) {
                        is ResourceState.LoadingState -> {
                            binding.psHome.visibility = View.VISIBLE
                        }

                        is ResourceState.SuccesState -> {
                            binding.psHome.visibility = View.GONE
                            binding.rvHomeFragment.adapter =
                                AdapterMovies(context = requireContext(),
                                    moviesList = it.data,
                                    onItemClickListener = { dataResult ->
                                        Log.i("data", "$dataResult")
                                        val bundle: Bundle = Bundle()
                                        bundle.putParcelable("movieDetail", dataResult)

                                        val action =
                                            MovieFragmentDirections.actionNavigationHomeToDetailMovieFragmentFullScreen(
                                                dataResult
                                            )
                                        findNavController().navigate(action)
                                    })
                        }

                        is ResourceState.FailureState -> {
                            binding.psHome.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Ocurrio un error al mostrar los datos: ${it.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }


}