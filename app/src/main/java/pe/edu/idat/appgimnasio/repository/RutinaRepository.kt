package pe.edu.idat.appgimnasio.repository

import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.api.RutinaApi
import pe.edu.idat.appgimnasio.entity.EjercicioRutina
import pe.edu.idat.appgimnasio.entity.Rutina
import retrofit2.Call

class RutinaRepository {

    private val api = RetrofitClient.instance.create(RutinaApi::class.java)

    fun obtenerRutinasDeUsuario(idUsuario: Int): Call<List<Rutina>> {
        return api.obtenerRutinasDeUsuario(idUsuario)
    }

    fun obtenerEjerciciosDeRutina(idRutina: Int): Call<List<EjercicioRutina>> {
        return api.obtenerEjerciciosDeRutina(idRutina)
    }
}