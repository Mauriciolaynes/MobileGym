package pe.edu.idat.appgimnasio.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "gym.db", null, 4) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuario (
                idUsuario INTEGER PRIMARY KEY NOT NULL,
                nombres TEXT,
                apellidos TEXT,
                correo TEXT,
                telefono TEXT,
                rol TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuario")
        onCreate(db)
    }
}