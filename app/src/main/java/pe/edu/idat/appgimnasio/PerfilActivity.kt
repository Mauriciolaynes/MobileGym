package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import pe.edu.idat.appgimnasio.repository.UsuarioRepository

class PerfilActivity : AppCompatActivity() {

    private lateinit var ivBackPerfil: ImageView
    private lateinit var tvPerfilNombreCompleto: TextView
    private lateinit var tvPerfilRol: TextView
    private lateinit var tvPerfilEmail: TextView
    private lateinit var tvPerfilTelefono: TextView
    private lateinit var btnCerrarSesionPerfil: MaterialButton

    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        usuarioRepository = UsuarioRepository(this)
        initViews()
        loadUserProfile()
    }

    private fun initViews() {
        ivBackPerfil = findViewById(R.id.ivBackPerfil)
        tvPerfilNombreCompleto = findViewById(R.id.tvPerfilNombreCompleto)
        tvPerfilRol = findViewById(R.id.tvPerfilRol)
        tvPerfilEmail = findViewById(R.id.tvPerfilEmail)
        tvPerfilTelefono = findViewById(R.id.tvPerfilTelefono)
        btnCerrarSesionPerfil = findViewById(R.id.btnCerrarSesionPerfil)

        ivBackPerfil.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnCerrarSesionPerfil.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun loadUserProfile() {
        val sesion = usuarioRepository.obtenerSesionActiva()
        if (sesion != null) {
            mostrarDatosPerfil(sesion.nombres, sesion.apellidos, sesion.correo, sesion.telefono, sesion.rol)


            usuarioRepository.obtenerPerfilApi(sesion.idUsuario) { perfil ->
                if (perfil != null) {
                    mostrarDatosPerfil(perfil.nombres, perfil.apellidos, perfil.correo, perfil.telefono, sesion.rol)
                }
            }
        } else {
            Toast.makeText(this, "No se pudo cargar la información del perfil", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun mostrarDatosPerfil(nombres: String, apellidos: String, correo: String, telefono: String, rol: String) {
        val nombreCompleto = "$nombres $apellidos"
        tvPerfilNombreCompleto.text = nombreCompleto
        val rolInfo = "Miembro King Sport (${rol.uppercase()})"
        tvPerfilRol.text = rolInfo
        tvPerfilEmail.text = correo
        tvPerfilTelefono.text = if (telefono.isBlank()) "No registrado" else telefono
    }

    private fun cerrarSesion() {
        usuarioRepository.cerrarSesion()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
