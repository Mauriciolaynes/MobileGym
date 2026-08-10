package pe.edu.idat.appgimnasio.api

import pe.edu.idat.appgimnasio.entity.Membresia
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MembresiaApi {

    @GET("membresias/usuario/{idUsuario}")
    fun obtenerMembresiasDeUsuario(@Path("idUsuario") idUsuario: Int): Call<List<Membresia>>
}