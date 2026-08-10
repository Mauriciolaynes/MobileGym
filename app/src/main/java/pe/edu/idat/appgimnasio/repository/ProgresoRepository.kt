package pe.edu.idat.appgimnasio.repository

import pe.edu.idat.appgimnasio.api.ProgresoApi
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.entity.Progreso
import pe.edu.idat.appgimnasio.entity.dto.ProgresoRegistroDTO
import retrofit2.Call

class ProgresoRepository {
    private val api = RetrofitClient.instance.create(ProgresoApi::class.java)

    fun listarProgresosPorUsuario(idUsuario: Int): Call<List<Progreso>> {
        return api.obtenerProgresosPorUsuario(idUsuario)
    }

    fun registrarProgreso(idUsuario: Int, fecha: String, peso: Double): Call<Progreso> {
        val dto = ProgresoRegistroDTO(idUsuario, fecha, peso)
        return api.registrarProgreso(dto)
    }

    fun actualizarProgreso(idProgreso: Int, idUsuario: Int, fecha: String, peso: Double): Call<Progreso> {
        val dto = ProgresoRegistroDTO(idUsuario, fecha, peso)
        return api.actualizarProgreso(idProgreso, dto)
    }

    fun eliminarProgreso(idProgreso: Int): Call<Void> {
        return api.eliminarProgreso(idProgreso)
    }
}
