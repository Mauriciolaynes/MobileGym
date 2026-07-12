package pe.edu.idat.appgimnasio.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "gym.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE progreso (
                idProgreso INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fechaRegistro TEXT,
                peso REAL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS progreso")
        onCreate(db)
    }
}
