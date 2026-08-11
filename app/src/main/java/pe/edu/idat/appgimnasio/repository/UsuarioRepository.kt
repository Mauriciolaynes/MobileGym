package pe.edu.idat.appgimnasio.repository

import android.content.ContentValues
import android.content.Context
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.api.UsuarioApi
import pe.edu.idat.appgimnasio.data.AppDatabaseHelper
import pe.edu.idat.appgimnasio.entity.dto.LoginResponseDTO
import pe.edu.idat.appgimnasio.entity.dto.PerfilResponseDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioRepository(context: Context) {
    private val dbHelper = AppDatabaseHelper(context)
    private val usuarioApi = RetrofitClient.instance.create(UsuarioApi::class.java)

    fun obtenerPerfilApi(idUsuario: Int, callback: (PerfilResponseDTO?) -> Unit) {
        usuarioApi.obtenerPerfil(idUsuario).enqueue(object : Callback<PerfilResponseDTO> {
            override fun onResponse(call: Call<PerfilResponseDTO>, response: Response<PerfilResponseDTO>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<PerfilResponseDTO>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun guardarSesionLocal(usuario: LoginResponseDTO) {
        val db = dbHelper.writableDatabase

        db.delete("usuario", null, null)

        val values = ContentValues().apply {
            put("idUsuario", usuario.idUsuario)
            put("nombres", usuario.nombres)
            put("apellidos", usuario.apellidos)
            put("correo", usuario.correo)
            put("telefono", usuario.telefono)
            put("rol", usuario.rol)
        }
        db.insert("usuario", null, values)
    }

    fun obtenerSesionActiva(): LoginResponseDTO? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM usuario LIMIT 1"

        db.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                return LoginResponseDTO(
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")),
                    nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres")) ?: "",
                    apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos")) ?: "",
                    correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")) ?: "",
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")) ?: "",
                    rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")) ?: ""
                )
            }
        }
        return null
    }

    fun cerrarSesion() {
        val db = dbHelper.writableDatabase
        db.delete("usuario", null, null)
    }
}