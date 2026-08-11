package pe.edu.idat.appgimnasio.api

import pe.edu.idat.appgimnasio.entity.dto.LoginRequestDTO
import pe.edu.idat.appgimnasio.entity.dto.LoginResponseDTO
import pe.edu.idat.appgimnasio.entity.dto.PerfilResponseDTO
import pe.edu.idat.appgimnasio.entity.dto.UsuarioRegistroDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioApi {
    @POST("usuarios/registro")
    fun registrarUsuario(@Body usuario: UsuarioRegistroDTO): Call<Void>

    @POST("usuarios/login")
    fun loginUsuario(@Body loginRequest: LoginRequestDTO): Call<LoginResponseDTO>

    @GET("usuarios/{id}/perfil")
    fun obtenerPerfil(@Path("id") idUsuario: Int): Call<PerfilResponseDTO>
}