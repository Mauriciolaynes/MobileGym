package pe.edu.idat.appgimnasio.api

import pe.edu.idat.appgimnasio.entity.EjercicioRutina
import pe.edu.idat.appgimnasio.entity.Rutina
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RutinaApi {

    @GET("rutinas/usuario/{idUsuario}")
    fun obtenerRutinasDeUsuario(@Path("idUsuario") idUsuario: Int): Call<List<Rutina>>

    @GET("rutinas/{idRutina}/ejercicios")
    fun obtenerEjerciciosDeRutina(@Path("idRutina") idRutina: Int): Call<List<EjercicioRutina>>
}