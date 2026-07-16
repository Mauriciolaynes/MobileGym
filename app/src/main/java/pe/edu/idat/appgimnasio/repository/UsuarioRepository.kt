package pe.edu.idat.appgimnasio.repository

import android.content.ContentValues
import android.content.Context
import pe.edu.idat.appgimnasio.data.AppDatabaseHelper
import pe.edu.idat.appgimnasio.entity.Usuario

class UsuarioRepository(context: Context) {
    private val dbHelper = AppDatabaseHelper(context)

    fun registrarUsuario(usuario: Usuario): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("dni", usuario.dni)
            put("nombres", usuario.nombres)
            put("apellidos", usuario.apellidos)
            put("email", usuario.email)
            put("telefono", usuario.telefono)
            put("password", usuario.password)
        }
        return db.insert("usuario", null, values)
    }

    fun validarUsuario(email: String, password: String): Usuario? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM usuario WHERE LOWER(email) = LOWER(?) AND password = ?"
        db.rawQuery(query, arrayOf(email.trim(), password.trim())).use { cursor ->
            if (cursor.moveToFirst()) {
                return Usuario(
                    dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                    nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres")),
                    apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                )
            }
        }
        return null
    }
}