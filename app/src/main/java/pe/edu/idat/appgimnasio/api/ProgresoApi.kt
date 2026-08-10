package pe.edu.idat.appgimnasio.api

import pe.edu.idat.appgimnasio.entity.Progreso
import pe.edu.idat.appgimnasio.entity.dto.ProgresoRegistroDTO
import retrofit2.Call
import retrofit2.http.*

interface ProgresoApi {
    @GET("progresos/usuario/{idUsuario}")
    fun obtenerProgresosPorUsuario(@Path("idUsuario") idUsuario: Int): Call<List<Progreso>>

    @POST("progresos")
    fun registrarProgreso(@Body registroDTO: ProgresoRegistroDTO): Call<Progreso>

    @PUT("progresos/{idProgreso}")
    fun actualizarProgreso(@Path("idProgreso") idProgreso: Int, @Body registroDTO: ProgresoRegistroDTO): Call<Progreso>

    @DELETE("progresos/{idProgreso}")
    fun eliminarProgreso(@Path("idProgreso") idProgreso: Int): Call<Void>
}
