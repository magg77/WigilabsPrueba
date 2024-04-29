package com.maggiver.wigilabspruebamaggiver.data.repository

import android.content.Context
import com.maggiver.wigilabspruebamaggiver.core.utils.ConnectionManager
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.data.provider.local.entity.toMovieCustom
import com.maggiver.wigilabspruebamaggiver.data.provider.local.serviceLocal.DataSourceLocalContract
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.MovieCustom
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.PopularMovieResponse
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.toMovieEntity
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.server.DataSourceRemoteContract
import javax.inject.Inject


/**
 * Created by
 * @AUTHOR: Daniel Maggiver Acevedo
 * @NICK_NAME: mackgaru
 * @DATE: 25,abril,2024
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

class RepositoryImpl @Inject constructor(
    private val dataSourceRemote: DataSourceRemoteContract,
    private val dataSourceLocal: DataSourceLocalContract
) :
    RepositoryContract {

    override suspend fun repoGetAllMoviePopular(requireContext: Context): ResourceState<List<MovieCustom>> {

        val dataMovieResponse: ResourceState.SuccesState<PopularMovieResponse>
        return if (ConnectionManager.isNetworkAvailable(requireContext)) {
            dataMovieResponse = dataSourceRemote.getMoviePopularRemote()

            //saved local movies
            insertAllMovieRemote(dataMovieResponse.data)

            getAllMovie()
        } else {
            getAllMovie()
        }
    }

    override suspend fun insertAllMovieRemote(data: PopularMovieResponse) {
        data.results.forEachIndexed { index, result ->
            dataSourceLocal.insertMovie(
                result.toMovieEntity()
            )
        }
    }

    override suspend fun getAllMovie(): ResourceState<List<MovieCustom>> {
        val movieLocal = dataSourceLocal.getAllMovie()
        val listMovieCustom: MutableList<MovieCustom> = mutableListOf()
        return try {
            when (movieLocal) {
                is ResourceState.SuccesState -> {
                    movieLocal.data.forEachIndexed { index, movieEntity ->
                        listMovieCustom.add(movieEntity.toMovieCustom())
                    }
                    ResourceState.SuccesState(listMovieCustom)
                }

                else -> {
                    ResourceState.SuccesState(listMovieCustom)
                }
            }
        } catch (e: Exception) {
            ResourceState.FailureState(e)
        }
    }

    override suspend fun getAllMovieFavorite(): ResourceState<List<MovieCustom>> {
        val favoriteLocal = dataSourceLocal.getAllMovieFavorite(true)
        val movieCustomFavorite: MutableList<MovieCustom> = mutableListOf()
        return try {
            when (favoriteLocal) {
                is ResourceState.SuccesState -> {
                    favoriteLocal.data.forEachIndexed { index, movie ->
                        movieCustomFavorite.add(movie.toMovieCustom())
                    }
                    ResourceState.SuccesState(movieCustomFavorite)
                }

                else -> {
                    ResourceState.SuccesState(movieCustomFavorite)
                }
            }
        } catch (e: Exception) {
            ResourceState.FailureState(e)
        }
    }


    override suspend fun updateMovieFavorite(
        favoriteState: Boolean,
        idMovie: Int
    ): ResourceState.SuccesState<Boolean> {
        dataSourceLocal.updateMovieFavorite(favoriteState, idMovie)
        return ResourceState.SuccesState(!favoriteState)
    }

}