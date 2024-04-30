package com.maggiver.wigilabspruebamaggiver.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.maggiver.wigilabspruebamaggiver.core.valueObject.BaseApplication
import com.maggiver.wigilabspruebamaggiver.core.valueObject.NetworkResult
import com.maggiver.wigilabspruebamaggiver.core.valueObject.ResourceState
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.model.MovieCustom
import com.maggiver.wigilabspruebamaggiver.domain.PopularMovieUserCaseContract
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

@HiltViewModel
class PopularMovieViewModel @Inject constructor(private val useCase: PopularMovieUserCaseContract) :
    ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    */

    private val _uiState: MutableStateFlow<ResourceState<List<MovieCustom>>> =
        MutableStateFlow(ResourceState.LoadingState())
    val uiState: StateFlow<ResourceState<List<MovieCustom>>> = _uiState

    @get:Inject
    val baseApplication: BaseApplication
        get() {
            return BaseApplication()
        }


    /*fun getAllPopularMovieViewModel(requireContext: Context) =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
            emit(ResourceState.LoadingState())
            try {
                emit(useCase.invoke(requireContext))
            } catch (e: Exception) {
                emit(ResourceState.FailureState(e))
            }
        }*/

    fun moviePopular1(context: Context) = flow<ResourceState<List<MovieCustom>>> {
        runCatching {
            useCase.invoke(context)
        }.onSuccess {
            it.map { resourceList ->
                emit(resourceList)
            }
        }.onFailure { throwable ->
            emit(ResourceState.FailureState(throwable))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResourceState.LoadingState()
    )

    fun moviePopular2(context: Context) = viewModelScope.launch {
        useCase.invoke(context)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }


    /*fun moviePopular3() = viewModelScope.launch {
        _uiState.value = ResourceState.LoadingState()
        useCase.invoke(baseApplication)
            .catch { e ->
                _uiState.value = ResourceState.FailureState(e)
            }.collect{
                _uiState.value = ResourceState.SuccesState(it)
            }
    }*/


    fun updateMovieFavoriteViewModel(favoriteState: Boolean, idMovie: Int) =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
            emit(ResourceState.LoadingState())
            try {
                emit(useCase.updateMovieFavoriteUseCase(favoriteState, idMovie))
            } catch (e: Exception) {
                emit(ResourceState.FailureState(e))
            }
        }

    fun getAllMovieFavorite() = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(ResourceState.LoadingState())
        try {
            emit(useCase.getAllMoviesFavoriteUseCase())
        } catch (e: Exception) {
            emit(ResourceState.FailureState(e))
        }
    }


}