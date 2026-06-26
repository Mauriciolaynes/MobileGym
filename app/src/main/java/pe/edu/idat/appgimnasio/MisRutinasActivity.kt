package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.adapter.RutinaAdapter
import pe.edu.idat.appgimnasio.entity.Rutina

class MisRutinasActivity : AppCompatActivity() {

    private lateinit var rvMisRutinas: RecyclerView
    private lateinit var adapter: RutinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mis_rutinas)


        rvMisRutinas = findViewById(R.id.lvMisRutinas)
        rvMisRutinas.layoutManager = LinearLayoutManager(this)


        adapter = RutinaAdapter(this, emptyList()) { rutinaSeleccionada ->

            val intent = Intent(this, DetalleRutinaActivity::class.java)

            intent.putExtra("ID_RUTINA", rutinaSeleccionada.idRutina)
            intent.putExtra("NOMBRE_RUTINA", rutinaSeleccionada.idNombreRutina)

            startActivity(intent)
        }
        rvMisRutinas.adapter = adapter

        cargarMisRutinas()
    }
    private fun cargarMisRutinas() {
        val listaPrueba = listOf(
            Rutina(1, "Día 1: Pecho y Tríceps"),
            Rutina(2, "Día 2: Espalda y Bíceps"),
            Rutina(3, "Día 3: Piernas y Hombros"),
            Rutina(4, "Día 4: Full Body")
        )
        adapter.actualizarDatos(listaPrueba)
    }
}