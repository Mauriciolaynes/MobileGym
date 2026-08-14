package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.adapter.MembresiaAdapter
import pe.edu.idat.appgimnasio.entity.Membresia
import pe.edu.idat.appgimnasio.repository.MembresiaRepository
import pe.edu.idat.appgimnasio.repository.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MiMembresiaActivity : AppCompatActivity() {
    private lateinit var rvHistorialMembresias: RecyclerView
    private lateinit var adapter: MembresiaAdapter
    private lateinit var tvTipoPlan: TextView
    private lateinit var tvEstadoPlan: TextView
    private lateinit var tvFechasPlan: TextView
    private lateinit var tvPrecioPlan: TextView

    private val membresiaRepository = MembresiaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_membresia)

        val toolbar = findViewById<Toolbar>(R.id.toolbarMembresia)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tvTipoPlan = findViewById(R.id.tvTipoPlan)
        tvEstadoPlan = findViewById(R.id.tvEstadoPlan)
        tvFechasPlan = findViewById(R.id.tvFechasPlan)
        tvPrecioPlan = findViewById(R.id.tvPrecioPlan)

        rvHistorialMembresias = findViewById(R.id.rvHistorialMembresias)
        rvHistorialMembresias.layoutManager = LinearLayoutManager(this)

        adapter = MembresiaAdapter(this, emptyList())
        rvHistorialMembresias.adapter = adapter

        val usuarioRepository = UsuarioRepository(this)
        val sesion = usuarioRepository.obtenerSesionActiva()
        val idUsuarioLogueado = sesion?.idUsuario ?: -1

        if (idUsuarioLogueado != -1) {
            cargarDatosMembresia(idUsuarioLogueado)
        } else {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarDatosMembresia(idUsuario: Int) {
        membresiaRepository.obtenerMembresias(idUsuario).enqueue(object : Callback<List<Membresia>> {
            override fun onResponse(call: Call<List<Membresia>>, response: Response<List<Membresia>>) {
                if (response.isSuccessful) {
                    val membresias = response.body() ?: emptyList()

                    val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val hoyString = formatoFecha.format(Date())
                    val fechaHoySinHora = formatoFecha.parse(hoyString)

                    val membresiasInteligentes = membresias.map { membresia ->
                        try {
                            val fechaFinObj = formatoFecha.parse(membresia.fechaFin)
                            if (fechaFinObj != null && fechaHoySinHora != null && fechaFinObj.before(fechaHoySinHora)) {
                                membresia.copy(estado = "VENCIDO")
                            } else {
                                membresia
                            }
                        } catch (e: Exception) {
                            membresia
                        }
                    }

                    val membresiaActiva = membresiasInteligentes.find {
                        it.estado.trim().lowercase().startsWith("activ")
                    }

                    if (membresiaActiva != null) {
                        val nombrePlan = if (membresiaActiva.tipoMembresia.isNullOrBlank()) "PLAN ACTIVO" else membresiaActiva.tipoMembresia.uppercase()

                        tvTipoPlan.text = nombrePlan
                        tvEstadoPlan.text = "Estado: ${membresiaActiva.estado.trim().uppercase()}"
                        tvFechasPlan.text = "Inicio: ${membresiaActiva.fechaInicio} - Fin: ${membresiaActiva.fechaFin}"
                        tvPrecioPlan.text = "Precio: S/ ${String.format("%.2f", membresiaActiva.precio)}"
                        tvEstadoPlan.setTextColor(getColor(android.R.color.holo_green_dark))
                    } else {
                        tvTipoPlan.text = "Sin Plan Activo"
                        tvEstadoPlan.text = "Estado: INACTIVO"
                        tvFechasPlan.text = "No tienes una membresía vigente"
                        tvPrecioPlan.text = "Precio: S/ 0.00"
                        tvEstadoPlan.setTextColor(getColor(android.R.color.darker_gray))
                    }

                    val historialVencidos = membresiasInteligentes.filter {
                        !it.estado.trim().lowercase().startsWith("activ")
                    }
                    adapter.actualizarDatos(historialVencidos)

                    if (membresiasInteligentes.isEmpty()) {
                        Toast.makeText(this@MiMembresiaActivity, "No tienes historial de membresías", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@MiMembresiaActivity, "Error al cargar membresías", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Membresia>>, t: Throwable) {
                Log.e("MiMembresiaActivity", "Error de red: ${t.message}")
                Toast.makeText(this@MiMembresiaActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}