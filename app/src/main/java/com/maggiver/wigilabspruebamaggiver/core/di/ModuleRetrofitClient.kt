package com.maggiver.wigilabspruebamaggiver.core.di

import com.google.gson.GsonBuilder
import com.maggiver.wigilabspruebamaggiver.BuildConfig
import com.maggiver.wigilabspruebamaggiver.data.provider.remote.server.WebServiceContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


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

@Module
@InstallIn(SingletonComponent::class)
object ModuleRetrofitClient {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        val timeOut = 30
        return OkHttpClient.Builder()
            //interceptor_token_header
            .addInterceptor{chain: Interceptor.Chain ->
                chain.proceed(chain.request().newBuilder().also {  }.build())
            }
            //interceptor_token_log_http
            .also { okHttpClient: OkHttpClient.Builder ->
                if (BuildConfig.DEBUG) {
                    val httpLogginInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    okHttpClient.addInterceptor(httpLogginInterceptor)
                }
            }.build()
    }

    @Singleton
    @Provides
    fun provideNetworkRetrofitModule(okHttpClient: OkHttpClient): Retrofit {
        val BASE_URL = "https://api.themoviedb.org/3/"

        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideWebServiceInterface(retrofit: Retrofit): WebServiceContract {
        return retrofit.create(WebServiceContract::class.java)
    }

}