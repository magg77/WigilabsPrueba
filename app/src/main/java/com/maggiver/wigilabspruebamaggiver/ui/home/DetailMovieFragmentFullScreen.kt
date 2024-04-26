package com.maggiver.wigilabspruebamaggiver.ui.home

import android.app.Dialog
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentDialog
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.maggiver.wigilabspruebamaggiver.R
import com.maggiver.wigilabspruebamaggiver.core.utils.Constants
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailMovieFragmentFullScreen : DialogFragment() {

    private lateinit var alert: AlertDialog.Builder
    private lateinit var alertLoader: AlertDialog
    private val args by navArgs<DetailMovieFragmentFullScreenArgs>()
    private var dataResultMovie: Result? = Result()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_fullscreen_detail_movie, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        (dialog as ComponentDialog)
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                //findNavController().navigateUp()
                dismiss()
            }

        val toolbar: MaterialToolbar = view.findViewById(R.id.materialToolbar_detail_movie) as MaterialToolbar
        val context = (context as ContextWrapper).baseContext
        (context as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(false)
        toolbar.setNavigationOnClickListener { dismiss() }

        arguments?.let {
            //dataResultMovie = it.getBundle("result")
        }

        val titleMovieDetail: MaterialTextView = view.findViewById(R.id.tv_title_movie_detail) as MaterialTextView
        val imageMovieDetail: ImageView = view.findViewById(R.id.imageMovieDetail) as ImageView
        val tvVote: TextView = view.findViewById(R.id.tv_vote) as TextView
        val tvDate: TextView = view.findViewById(R.id.tv_dateRelease) as TextView
        val tvPopularity: TextView = view.findViewById(R.id.tv_popularity) as TextView
        val tvDescriptionDetailMovie: TextView = view.findViewById(R.id.tv_descripcion_detalle)

        titleMovieDetail.text = args.movieDetail.title
        Glide.with(context).load("${Constants.IMG_MOVIE_DB_COVER}${args.movieDetail.posterPath}")
            .optionalCenterCrop()
            .into(imageMovieDetail)

        tvVote.text = args.movieDetail.voteCount.toString()
        tvDate.text = args.movieDetail.releaseDate
        tvPopularity.text = args.movieDetail.popularity.toString()
        tvDescriptionDetailMovie.text = args.movieDetail.overview



    }

}