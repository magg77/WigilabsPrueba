package com.maggiver.wigilabspruebamaggiver.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maggiver.wigilabspruebamaggiver.core.utils.Constants
import com.maggiver.wigilabspruebamaggiver.core.valueObject.BaseViewHolder
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.MovieCustom
import com.maggiver.wigilabspruebamaggiver.databinding.FavoriteCardviewBinding


/**
 * Created by
 * @AUTHOR: Daniel Maggiver Acevedo
 * @NICK_NAME: mackgaru
 * @DATE: 26,abril,2024
 * @COMPAN: Juice
 * @EMAIL: dmacevedo00@misena.edu.co
 *
 * Todos los derechos de @AUTHOR y de Propiedad Intelectual, son reservados y protegidos por su creador y se phohibe su reprodución, edición, copias, conservación, divulgación y comercialización sin consentimiento escrito.
 * En caso que un tercero haga uso indebidode esta propiedad intelectual, su @AUTHOR, puede ejercer una acción legal indemnizatoria por el uso indebido de sus obras legitimas.
 * solicitando al juez civil que condene al infractor al pago de perjuicios o, de igual forma, interponga una denuncia por ser víctima del delito de violación a los derechos morales, patrimoniales de autor y demas derechos vulnerados
 *
 *
 * Su @AUTHOR GOZA DE LOS DERECHOS DE:
 * @Derechos_de_AUTHOR: El software por ser una obra literaria goza de protección legal desde el momento de su creación.
 * @Derechos_Morales: prerrogativas irrenunciables e inalienables de las que goza el autor legítimo del software que le permite conservar la obra de forma inédita, divulgarla, oponerse a las posibles modificaciones que tenga o retirarla del mercado cuando así lo considere.
 * @Derechos_Patrimoniales: Prerrogativas de carácter económico que le permiten al autor del software obtener provecho económico de todas las utilidades que genere la reproducción o distribución del software.
 *                         pueden ser transferibles a terceros con la autorización del titular del software en virtud de la autonomía de su voluntad, en cuyo caso, el autor o titular de la obra denominado cedente transmite total o parcialmente sus derechos a un tercero a través de un contrato de cesión de derechos.
 * @Derecho_de_transformacion_distribucion_y_reproduccion_de_la_obra: facultad que tiene el titular o autor de un software de realizar cambios totales o parciales al código de su obra; ponerla a disposición del público o autorizar su difusión.
 */

class AdapterFavorite(
    private val context: Context,
    private val favoriteMovieList: List<MovieCustom>,
    private val onItemClickListener: (MovieCustom) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = FavoriteCardviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return favoriteMovieList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(favoriteMovieList[position], position)
        }
    }


    inner class MainViewHolder(val binding: FavoriteCardviewBinding) :
        BaseViewHolder<MovieCustom>(binding.root) {

        override fun bind(item: MovieCustom, position: Int) = with(binding) {

            Glide.with(context).load("${Constants.IMG_MOVIE_DB_COVER}${item.posterPath}")
                .into(imvMovieFavorite)

            tvTitleMovieFavorite.text = item.title
            tvDescripShortFavorite.text = item.overview
            binding.contentCardView.setOnClickListener {
                onItemClickListener(favoriteMovieList[position])
            }
        }
    }
}