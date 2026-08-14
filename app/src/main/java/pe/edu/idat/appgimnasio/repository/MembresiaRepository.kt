package pe.edu.idat.appgimnasio.repository

import pe.edu.idat.appgimnasio.api.MembresiaApi
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.entity.Membresia
import retrofit2.Call

class MembresiaRepository {

    private val api = RetrofitClient.instance.create(MembresiaApi::class.java)

    fun obtenerMembresias(idUsuario: Int): Call<List<Membresia>> {
        return api.obtenerMembresiasDeUsuario(idUsuario)
    }
}