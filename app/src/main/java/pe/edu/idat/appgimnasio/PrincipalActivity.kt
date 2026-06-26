package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView

class PrincipalActivity : AppCompatActivity() {

    private lateinit var dlaymenu: DrawerLayout
    private lateinit var nvmenu: NavigationView
    private lateinit var ivmenu: ImageView
    private lateinit var cvMembership: MaterialCardView
    private lateinit var cvRutinas: MaterialCardView
    private lateinit var cvProgreso: MaterialCardView
    private lateinit var cvAsistencia: MaterialCardView
    private lateinit var cvPerfil: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        initViews()
        setupDrawer()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlaymenu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        dlaymenu = findViewById(R.id.dlaymenu)
        nvmenu = findViewById(R.id.nvmenu)
        ivmenu = findViewById(R.id.ivmenu)
        cvMembership = findViewById(R.id.cvMembership)
        cvRutinas = findViewById(R.id.cvRutinas)
        cvProgreso = findViewById(R.id.cvProgreso)
        cvAsistencia = findViewById(R.id.cvCerrarSesion)
        cvPerfil = findViewById(R.id.cvPerfil)
    }

    private fun setupDrawer() {
        ivmenu.setOnClickListener {
            dlaymenu.open()
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

        cvAsistencia.setOnClickListener {
            // startActivity(Intent(this, AsistenciaActivity::class.java))
        }

        cvPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        nvmenu.setNavigationItemSelectedListener { menuitem ->
            when (menuitem.itemId) {
                R.id.inicio -> {
                    // Ya estamos en el inicio
                }
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
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            dlaymenu.closeDrawers()
            true
        }
    }
}