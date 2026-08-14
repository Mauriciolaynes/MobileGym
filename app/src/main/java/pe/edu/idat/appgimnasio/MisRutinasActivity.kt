package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.adapter.RutinaAdapter
import pe.edu.idat.appgimnasio.entity.Membresia
import pe.edu.idat.appgimnasio.entity.Rutina
import pe.edu.idat.appgimnasio.repository.MembresiaRepository
import pe.edu.idat.appgimnasio.repository.RutinaRepository
import pe.edu.idat.appgimnasio.repository.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MisRutinasActivity : AppCompatActivity() {

    private lateinit var rvMisRutinas: RecyclerView
    private lateinit var adapter: RutinaAdapter
    private val membresiaRepository = MembresiaRepository()
    private val rutinaRepository = RutinaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_rutinas)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMisRutinas)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        rvMisRutinas = findViewById(R.id.lvMisRutinas)
        rvMisRutinas.layoutManager = LinearLayoutManager(this)

        adapter = RutinaAdapter(this, emptyList()) { rutinaSeleccionada ->
            val intent = Intent(this, DetalleRutinaActivity::class.java)
            intent.putExtra("ID_RUTINA", rutinaSeleccionada.idRutina)
            intent.putExtra("NOMBRE_RUTINA", rutinaSeleccionada.nombreRutina)
            startActivity(intent)
        }
        rvMisRutinas.adapter = adapter

        val usuarioRepository = UsuarioRepository(this)
        val sesion = usuarioRepository.obtenerSesionActiva()
        val idUsuarioLogueado = sesion?.idUsuario ?: -1

        if (idUsuarioLogueado != -1) {
            verificarAccesoPorMembresia(idUsuarioLogueado)
        } else {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun verificarAccesoPorMembresia(idUsuario: Int) {
        membresiaRepository.obtenerMembresias(idUsuario).enqueue(object : Callback<List<Membresia>> {
            override fun onResponse(call: Call<List<Membresia>>, response: Response<List<Membresia>>) {
                if (response.isSuccessful) {
                    val membresias = response.body() ?: emptyList()
                    val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                    val hoyString = formatoFecha.format(Date())
                    val fechaHoySinHora = formatoFecha.parse(hoyString)

                    val tieneAcceso = membresias.any { membresia ->
                        try {
                            val fechaFinObj = formatoFecha.parse(membresia.fechaFin)
                            val estadoLimpio = membresia.estado.trim().lowercase()

                            val estaActivaEnTexto = estadoLimpio.startsWith("activ")
                            val noEstaVencidaEnFecha = fechaFinObj != null && fechaHoySinHora != null && !fechaFinObj.before(fechaHoySinHora)

                            estaActivaEnTexto && noEstaVencidaEnFecha
                        } catch (e: Exception) {
                            false
                        }
                    }

                    if (tieneAcceso) {
                        cargarMisRutinasDesdeServidor(idUsuario)
                    } else {
                        Toast.makeText(
                            this@MisRutinasActivity,
                            "Acceso Denegado: Necesitas una membresía activa",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }

                } else {
                    Toast.makeText(this@MisRutinasActivity, "Error al verificar tu membresía", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<List<Membresia>>, t: Throwable) {
                Log.e("MisRutinasActivity", "Error de red: ${t.message}")
                Toast.makeText(this@MisRutinasActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun cargarMisRutinasDesdeServidor(idUsuario: Int) {
        rutinaRepository.obtenerRutinasDeUsuario(idUsuario).enqueue(object : Callback<List<Rutina>> {
            override fun onResponse(call: Call<List<Rutina>>, response: Response<List<Rutina>>) {
                if (response.isSuccessful) {
                    val rutinas = response.body() ?: emptyList()
                    adapter.actualizarDatos(rutinas)

                    if (rutinas.isEmpty()) {
                        Toast.makeText(this@MisRutinasActivity, "No tienes rutinas asignadas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MisRutinasActivity, "Error al cargar rutinas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Rutina>>, t: Throwable) {
                Log.e("MisRutinasActivity", "Error de red: ${t.message}")
                Toast.makeText(this@MisRutinasActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}