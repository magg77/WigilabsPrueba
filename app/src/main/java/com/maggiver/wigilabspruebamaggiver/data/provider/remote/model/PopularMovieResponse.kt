package com.maggiver.wigilabspruebamaggiver.data.provider.remote.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.maggiver.wigilabspruebamaggiver.data.provider.local.entity.MovieEntity
import kotlinx.parcelize.Parcelize


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

@Parcelize
data class PopularMovieResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") var results: List<Result> = listOf(),
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0
) : Parcelable

@Parcelize
data class Result(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("adult") val adult: Boolean = true,
    @SerializedName("backdrop_path") val backdropPath: String = "",
    @SerializedName("genre_ids") val genreIds: List<Int> = listOf(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0
) : Parcelable

@Parcelize
data class ListMovieCustom(
    val listMovie: List<MovieCustom> = listOf()
) : Parcelable

@Parcelize
data class MovieCustom(
    @SerializedName("id") val id: Int,
    @SerializedName("posterPath") val posterPath: String,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("voteCount") val voteCount: Int,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("favoriteState") var favoriteState: Boolean = false
) : Parcelable

fun PopularMovieResponse.toListMovieCustom(): List<MovieCustom> {
    val resultList = mutableListOf<MovieCustom>()
    this.results.forEachIndexed{index, value ->
        resultList.add(value.toMovieCustom())
    }
    return resultList
}
fun Result.toMovieCustom(): MovieCustom = MovieCustom(
    id = this.id,
    posterPath = this.posterPath,
    title = this.title,
    overview = this.overview,
    voteCount = this.voteCount,
    releaseDate = this.releaseDate,
    popularity = this.popularity,
    favoriteState = false
)

fun Result.toMovieEntity(): MovieEntity = MovieEntity(
    this.id,
    this.posterPath,
    this.title,
    this.overview,
    this.voteCount,
    this.releaseDate,
    this.popularity,
    favoriteState = false
)