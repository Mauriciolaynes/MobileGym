package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import pe.edu.idat.appgimnasio.api.MembresiaApi
import pe.edu.idat.appgimnasio.api.ProgresoApi
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.entity.Membresia
import pe.edu.idat.appgimnasio.entity.Progreso
import pe.edu.idat.appgimnasio.repository.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrincipalActivity : AppCompatActivity() {

    private lateinit var dlaymenu: DrawerLayout
    private lateinit var nvmenu: NavigationView
    private lateinit var ivmenu: ImageView
    private lateinit var ivLogoutTop: ImageView
    private lateinit var tvGreeting: TextView
    private lateinit var cvMembership: MaterialCardView
    private lateinit var cvRutinas: MaterialCardView
    private lateinit var cvProgreso: MaterialCardView
    private lateinit var cvCerrarSesion: MaterialCardView
    private lateinit var cvPerfil: MaterialCardView

    private lateinit var tvMenuTipoPlan: TextView
    private lateinit var tvMenuFechaPlan: TextView
    private lateinit var tvLastWeight: TextView
    private lateinit var tvLastWeightDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        initViews()
        setupActions()
        setupDrawer()
        mostrarNombreUsuario()
    }

    override fun onResume() {
        super.onResume()
        cargarMembresiaActiva()
        cargarUltimoProgreso()
    }

    private fun initViews() {
        dlaymenu = findViewById(R.id.dlaymenu)
        nvmenu = findViewById(R.id.nvmenu)
        ivmenu = findViewById(R.id.ivmenu)
        ivLogoutTop = findViewById(R.id.ivLogoutTop)
        tvGreeting = findViewById(R.id.tvGreeting)
        cvMembership = findViewById(R.id.cvMembership)
        cvRutinas = findViewById(R.id.cvRutinas)
        cvProgreso = findViewById(R.id.cvProgreso)
        cvCerrarSesion = findViewById(R.id.cvCerrarSesion) // Este ahora dice "Mi Plan" en el XML
        cvPerfil = findViewById(R.id.cvPerfil)

        tvMenuTipoPlan = findViewById(R.id.tvMenuTipoPlan)
        tvMenuFechaPlan = findViewById(R.id.tvMenuFechaPlan)
        tvLastWeight = findViewById(R.id.tvLastWeight)
        tvLastWeightDate = findViewById(R.id.tvLastWeightDate)
    }

    private fun mostrarNombreUsuario() {
        val usuarioRepository = UsuarioRepository(this)
        val sesion = usuarioRepository.obtenerSesionActiva()

        val nombreCompleto = sesion?.nombres ?: "Cliente"
        val primerNombre = nombreCompleto.split(" ").firstOrNull() ?: "Cliente"

        tvGreeting.text = "Hola, $primerNombre \uD83D\uDC4B"
    }

    private fun cargarMembresiaActiva() {
        val usuarioRepository = UsuarioRepository(this)
        val sesion = usuarioRepository.obtenerSesionActiva()
        val idUsuarioLogueado = sesion?.idUsuario ?: -1

        if (idUsuarioLogueado != -1) {
            val apiMembresia = RetrofitClient.instance.create(MembresiaApi::class.java)

            apiMembresia.obtenerMembresiasDeUsuario(idUsuarioLogueado).enqueue(object : Callback<List<Membresia>> {
                override fun onResponse(call: Call<List<Membresia>>, response: Response<List<Membresia>>) {
                    if (response.isSuccessful) {
                        val membresias = response.body() ?: emptyList()
                        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        val hoyString = formatoFecha.format(Date())
                        val fechaHoySinHora = formatoFecha.parse(hoyString)

                        val membresiaActiva = membresias.find { membresia ->
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

                        if (membresiaActiva != null) {
                            val nombrePlan = if (membresiaActiva.tipoMembresia.isNullOrBlank()) "PLAN ACTIVO" else membresiaActiva.tipoMembresia.uppercase()

                            tvMenuTipoPlan.text = nombrePlan
                            tvMenuFechaPlan.text = "Vence: ${membresiaActiva.fechaFin}"
                        } else {
                            tvMenuTipoPlan.text = "Sin Plan Activo"
                            tvMenuFechaPlan.text = "Toca aquí para ver tu historial"
                        }
                    }
                }

                override fun onFailure(call: Call<List<Membresia>>, t: Throwable) {
                    Log.e("PrincipalActivity", "Error de red: ${t.message}")
                    tvMenuTipoPlan.text = "Modo sin conexión"
                    tvMenuFechaPlan.text = "Revisa tu internet"
                }
            })
        } else {
            tvMenuTipoPlan.text = "Error de sesión"
            tvMenuFechaPlan.text = "Inicia sesión nuevamente"
        }
    }

    private fun cargarUltimoProgreso() {
        val usuarioRepository = UsuarioRepository(this)
        val sesion = usuarioRepository.obtenerSesionActiva()
        val idUsuario = sesion?.idUsuario ?: -1

        if (idUsuario != -1) {
            val apiProgreso = RetrofitClient.instance.create(ProgresoApi::class.java)
            apiProgreso.obtenerProgresosPorUsuario(idUsuario).enqueue(object : Callback<List<Progreso>> {
                override fun onResponse(call: Call<List<Progreso>>, response: Response<List<Progreso>>) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        if (lista.isNotEmpty()) {
                            // El backend devuelve la lista, tomamos el último registro (asumiendo orden cronológico)
                            // o el que tenga mayor ID si el backend no ordena.
                            val ultimo = lista.maxByOrNull { it.idProgreso }
                            if (ultimo != null) {
                                tvLastWeight.text = "${ultimo.peso} Kg"
                                tvLastWeightDate.text = ultimo.fechaRegistro
                            }
                        } else {
                            tvLastWeight.text = "Sin datos"
                            tvLastWeightDate.text = "Regístrate ahora"
                        }
                    }
                }

                override fun onFailure(call: Call<List<Progreso>>, t: Throwable) {
                    tvLastWeight.text = "--"
                    tvLastWeightDate.text = "Error de carga"
                }
            })
        }
    }

    private fun setupActions() {
        ivLogoutTop.setOnClickListener {
            cerrarSesion()
        }

        cvMembership.setOnClickListener {
            startActivity(Intent(this, MiMembresiaActivity::class.java))
        }

        cvRutinas.setOnClickListener {
            startActivity(Intent(this, MisRutinasActivity::class.java))
        }

        cvProgreso.setOnClickListener {
            startActivity(Intent(this, ProgresoActivity::class.java))
        }

        cvCerrarSesion.setOnClickListener {
            // Este tile ahora lo usamos para ir a Membresía también
            startActivity(Intent(this, MiMembresiaActivity::class.java))
        }

        cvPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    private fun setupDrawer() {
        ivmenu.setOnClickListener {
            dlaymenu.open()
        }

        nvmenu.setNavigationItemSelectedListener { menuitem ->
            when (menuitem.itemId) {
                R.id.inicio -> { }
                R.id.itlista -> {
                    startActivity(Intent(this, MisRutinasActivity::class.java))
                }
                R.id.ithistorial -> {
                    startActivity(Intent(this, ProgresoActivity::class.java))
                }
                R.id.itperfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
                R.id.itcerrar -> {
                    cerrarSesion()
                }
            }
            dlaymenu.closeDrawers()
            true
        }
    }

    private fun cerrarSesion() {
        val usuarioRepository = UsuarioRepository(this)
        usuarioRepository.cerrarSesion()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
