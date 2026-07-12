package pe.edu.idat.appgimnasio.repository

import android.content.ContentValues
import android.content.Context
import pe.edu.idat.appgimnasio.data.AppDatabaseHelper
import pe.edu.idat.appgimnasio.entity.Progreso

class ProgresoRepository(context: Context) {
    private val dbHelper = AppDatabaseHelper(context)

    fun registrarProgreso(progreso: Progreso): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("fechaRegistro", progreso.fechaRegistro)
            put("peso", progreso.peso)
        }
        return db.insert("progreso", null, values)
    }

    fun listarProgreso(): List<Progreso> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Progreso>()
        db.rawQuery("SELECT * FROM progreso ORDER BY idProgreso DESC", null).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(
                    Progreso(
                        idProgreso = cursor.getInt(cursor.getColumnIndexOrThrow("idProgreso")),
                        fechaRegistro = cursor.getString(cursor.getColumnIndexOrThrow("fechaRegistro")),
                        peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
                    )
                )
            }
        }
        return list
    }

    fun buscarProgresoPorFecha(fecha: String): List<Progreso> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Progreso>()
        val query = "SELECT * FROM progreso WHERE fechaRegistro LIKE ? ORDER BY idProgreso DESC"
        val args = arrayOf("%$fecha%")
        db.rawQuery(query, args).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(
                    Progreso(
                        idProgreso = cursor.getInt(cursor.getColumnIndexOrThrow("idProgreso")),
                        fechaRegistro = cursor.getString(cursor.getColumnIndexOrThrow("fechaRegistro")),
                        peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
                    )
                )
            }
        }
        return list
    }
}
