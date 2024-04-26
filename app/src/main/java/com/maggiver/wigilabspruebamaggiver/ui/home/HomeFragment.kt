package com.maggiver.wigilabspruebamaggiver.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.databinding.FragmentHomeBinding
import com.maggiver.wigilabspruebamaggiver.presentation.PopularMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModelPopularMovie by viewModels<PopularMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
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
        viewModelPopularMovie.getAllCategoriesViewModel(requireContext()).observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResourceState.LoadingState -> {
                    binding.psHome.visibility = View.VISIBLE
                }

                is ResourceState.SuccesState -> {
                    binding.psHome.visibility = View.GONE
                    binding.rvHomeFragment.adapter = AdapterMovies(
                        context = requireContext(),
                        moviesList = it.data.results,
                        onItemClickListener = { dataResult ->
                            Log.i("data", "$dataResult")
                            val bundle: Bundle = Bundle()
                            bundle.putParcelable("movieDetail", dataResult)

                            val action = HomeFragmentDirections.actionNavigationHomeToDetailMovieFragmentFullScreen(dataResult)
                            findNavController().navigate(action)
                        }
                    )
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
        })
    }


}