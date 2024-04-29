package com.maggiver.wigilabspruebamaggiver.ui.favorite

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.databinding.FragmentFavoriteBinding
import com.maggiver.wigilabspruebamaggiver.presentation.PopularMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModelPopularMovie by viewModels<PopularMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayouts()
        setupObserversFavorite()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //custom methods favorite*******************************************************************************
    private fun setupLayouts() {
        binding.rvFavoriteFragment.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    private fun setupObserversFavorite() {
        viewModelPopularMovie.getAllMovieFavorite()
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResourceState.LoadingState -> {
                        binding.psFavorite.visibility = View.VISIBLE
                    }

                    is ResourceState.SuccesState -> {
                        binding.psFavorite.visibility = View.GONE
                        binding.rvFavoriteFragment.adapter = AdapterFavorite(
                            context = requireContext(),
                            favoriteMovieList = it.data,
                            onItemClickListener = { dataResult ->
                                Log.i("favorite", "$dataResult")

                                val action = FavoriteFragmentDirections.actionNavigationFavoriteToDetailMovieFragmentFullScreen(dataResult)
                                findNavController().navigate(action)
                            })
                    }

                    is ResourceState.FailureState -> {
                        binding.psFavorite.visibility = View.GONE
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