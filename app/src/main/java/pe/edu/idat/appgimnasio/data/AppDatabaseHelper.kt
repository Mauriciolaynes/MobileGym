package pe.edu.idat.appgimnasio.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "gym.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE progreso (
                idProgreso INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fechaRegistro TEXT,
                peso REAL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE usuario (
                dni TEXT PRIMARY KEY NOT NULL,
                nombres TEXT,
                apellidos TEXT,
                email TEXT,
                telefono TEXT,
                password TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS progreso")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        onCreate(db)
    }
}
