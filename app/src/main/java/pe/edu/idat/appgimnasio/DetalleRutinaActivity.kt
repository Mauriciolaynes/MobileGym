package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.adapter.EjercicioAdapter
import pe.edu.idat.appgimnasio.entity.EjercicioRutina

class DetalleRutinaActivity : AppCompatActivity() {

    private lateinit var tvTituloDetalle: TextView
    private lateinit var rvEjercicios: RecyclerView
    private lateinit var adapter: EjercicioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_rutina)

        tvTituloDetalle = findViewById(R.id.tvNombreRutinaElegida)
        rvEjercicios = findViewById(R.id.rvEjerciciosRutina)

        rvEjercicios.layoutManager = LinearLayoutManager(this)
        adapter = EjercicioAdapter(this, emptyList())
        rvEjercicios.adapter = adapter


        val idRutina = intent.getIntExtra("ID_RUTINA", -1)
        val nombreRutina = intent.getStringExtra("NOMBRE_RUTINA") ?: "Rutina"

        tvTituloDetalle.text = nombreRutina

        cargarEjerciciosDeRutina(idRutina)
    }

    private fun cargarEjerciciosDeRutina(idRutina: Int) {
        val listaEjercicios = when (idRutina) {
            1 -> listOf(
                EjercicioRutina(101, "Press de Banca Plano", 4, 12, R.drawable.remada),
                EjercicioRutina(102, "Aperturas con Mancuernas", 4, 15,R.drawable.remada),
                EjercicioRutina(103, "Fondos en Paralelas", 3, 10,R.drawable.remada),
                EjercicioRutina(104, "Extensión de Tríceps en Polea", 4, 12,R.drawable.remada)
            )
            2 -> listOf(
                EjercicioRutina(201, "Dominadas", 4, 8,R.drawable.remada),
                EjercicioRutina(202, "Remo con Barra", 4, 12,R.drawable.remada),
                EjercicioRutina(203, "Jalón al Pecho", 3, 15,R.drawable.remada),
                EjercicioRutina(204, "Curl de Bíceps con Barra", 4, 12,R.drawable.remada)
            )
            3 -> listOf(
                EjercicioRutina(301, "Sentadillas Libres", 4, 10,R.drawable.remada),
                EjercicioRutina(302, "Prensa Inclinada", 4, 12,R.drawable.remada),
                EjercicioRutina(303, "Press Militar con Mancuernas", 4, 10,R.drawable.remada),
                EjercicioRutina(304, "Elevaciones Laterales", 4, 15,R.drawable.remada)
            )
            else -> listOf(
                EjercicioRutina(401, "Burpees", 4, 15,R.drawable.remada),
                EjercicioRutina(402, "Plancha Abdominal", 3, 60,R.drawable.remada),
                EjercicioRutina(403, "Zancadas Caminando", 3, 20,R.drawable.remada)
            )
        }


        adapter.actualizarDatos(listaEjercicios)
    }
}