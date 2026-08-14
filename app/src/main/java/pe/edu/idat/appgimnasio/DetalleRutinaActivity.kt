package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.idat.appgimnasio.adapter.EjercicioAdapter
import pe.edu.idat.appgimnasio.entity.EjercicioRutina
import pe.edu.idat.appgimnasio.repository.RutinaRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleRutinaActivity : AppCompatActivity() {

    private lateinit var tvTituloDetalle: TextView
    private lateinit var rvEjercicios: RecyclerView
    private lateinit var adapter: EjercicioAdapter
    private val rutinaRepository = RutinaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_rutina)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDetalle)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tvTituloDetalle = findViewById(R.id.tvNombreRutinaElegida)
        rvEjercicios = findViewById(R.id.rvEjerciciosRutina)

        rvEjercicios.layoutManager = LinearLayoutManager(this)
        adapter = EjercicioAdapter(this, emptyList())
        rvEjercicios.adapter = adapter

        val idRutina = intent.getIntExtra("ID_RUTINA", -1)
        val nombreRutina = intent.getStringExtra("NOMBRE_RUTINA") ?: "Rutina"

        tvTituloDetalle.text = nombreRutina

        val btnFinalizar = findViewById<MaterialButton>(R.id.btnFinalizarEntrenamiento)
        btnFinalizar.setOnClickListener {
            Toast.makeText(this, "¡Entrenamiento finalizado con éxito!", Toast.LENGTH_LONG).show()
            finish()
        }

        if (idRutina != -1) {
            cargarEjerciciosDesdeServidor(idRutina)
        } else {
            Toast.makeText(this, "Error: Rutina no válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarEjerciciosDesdeServidor(idRutina: Int) {
        rutinaRepository.obtenerEjerciciosDeRutina(idRutina).enqueue(object : Callback<List<EjercicioRutina>> {
            override fun onResponse(call: Call<List<EjercicioRutina>>, response: Response<List<EjercicioRutina>>) {
                if (response.isSuccessful) {
                    val ejercicios = response.body() ?: emptyList()
                    adapter.actualizarDatos(ejercicios)

                    if (ejercicios.isEmpty()) {
                        Toast.makeText(this@DetalleRutinaActivity, "Esta rutina no tiene ejercicios", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetalleRutinaActivity, "Error al cargar ejercicios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EjercicioRutina>>, t: Throwable) {
                Log.e("DetalleRutina", "Error de red: ${t.message}")
                Toast.makeText(this@DetalleRutinaActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}