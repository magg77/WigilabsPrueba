package com.maggiver.wigilabspruebamaggiver.data.repository

import android.content.Context
import com.maggiver.wigilabspruebamaggiver.core.utils.ConnectionManager
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.data.provider.local.entity.MovieEntity
import com.maggiver.wigilabspruebamaggiver.data.provider.local.entity.toMovieCustom
import com.maggiver.wigilabspruebamaggiver.data.provider.local.serviceLocal.DataSourceLocalContract
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.MovieCustom
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.PopularMovieResponse
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.toListMovieCustom
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.toMovieEntity
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.server.DataSourceRemoteContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
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

    override suspend fun repoGetAllMoviePopular(requireContext: Context): Flow<ResourceState<List<MovieCustom>>> =
        flow {
            //emit(ResourceState.LoadingState())
            try {
                if (ConnectionManager.isNetworkAvailable(requireContext)) {


                    val moviePopularRemote = dataSourceRemote.getMoviePopularRemote()
                    val dataResponse = moviePopularRemote

                    if (dataResponse.isSuccessful) {
                        val data = dataResponse.body()
                        if (data != null) {

                            var dataMovieCustom = data.toListMovieCustom()
                            emit(ResourceState.SuccesState(dataMovieCustom))

                        } else {
                            val error = dataResponse.errorBody()
                            if (error != null) {
                                emit(ResourceState.FailureState(Throwable(error.toString())))
                            } else {
                                emit(ResourceState.FailureState(Throwable("Algo no salio bien, estamos trabajando para solucionarlo.")))
                            }
                        }
                    } else {
                        emit(
                            ResourceState.FailureState(
                                Throwable(
                                    dataResponse.errorBody().toString()
                                )
                            )
                        )
                    }

                } else {
                    //emit(ResourceState.FailureState(throw NullPointerException("No Connecction to internet")))
                    emit(ResourceState.FailureState(Throwable("No Connecction to internet.")))
                }

            } catch (e: Throwable) {
                emit(ResourceState.FailureState(e))
            }
        }

    override suspend fun repoGetAllMoviePopularOneTrue(requireContext: Context): Flow<ResourceState<List<MovieCustom>>> =
        channelFlow {

            send(ResourceState.LoadingState())
            try {
                if (ConnectionManager.isNetworkAvailable(requireContext)) {

                    val dataResponse = dataSourceRemote.getMoviePopularRemote()
                    if (dataResponse.isSuccessful) {
                        val data = dataResponse.body()
                        if (data != null) {

                            var dataMovieCustom = data.toListMovieCustom()
                            send(ResourceState.SuccesState(dataMovieCustom))

                        } else {
                            val error = dataResponse.errorBody()
                            if (error != null) {
                                send(ResourceState.FailureState(Throwable(error.toString())))
                            } else {
                                send(ResourceState.FailureState(Throwable("Algo no salio bien, estamos trabajando para solucionarlo.")))
                            }
                        }
                    } else {
                        send(
                            ResourceState.FailureState(
                                Throwable(
                                    dataResponse.errorBody().toString()
                                )
                            )
                        )
                    }

                } else {
                    //emit(ResourceState.FailureState(throw NullPointerException("No Connecction to internet")))
                    send(ResourceState.FailureState(Throwable("No Connecction to internet.")))
                }

            } catch (e: Throwable) {
                send(ResourceState.FailureState(e))
            }

            val movieCustom: MutableList<MovieCustom> = mutableListOf()
            getAllMovie()
                .flowOn(Dispatchers.IO)
                .collect() { movieEntity ->
                    movieEntity.map { movie ->
                        movieCustom.add(movie.toMovieCustom())
                    }
                    val list: List<MovieCustom> = movieCustom
                    send(ResourceState.SuccesState(list))
                }
        }

    override suspend fun insertAllMovieRemote(data: PopularMovieResponse) {
        data.results.forEachIndexed { index, result ->
            dataSourceLocal.insertMovie(
                result.toMovieEntity()
            )
        }
    }

    override fun getAllMovie(): Flow<List<MovieEntity>> {
        val data = dataSourceLocal.getAllMovie()
        return data
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