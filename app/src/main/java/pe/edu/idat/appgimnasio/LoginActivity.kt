package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.royrodriguez.transitionbutton.TransitionButton
import pe.edu.idat.appgimnasio.repository.UsuarioRepository

class LoginActivity : AppCompatActivity() {
    
    private lateinit var usuarioRepository: UsuarioRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioRepository = UsuarioRepository(this)
        
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<TransitionButton>(R.id.btnLogin)
        val tvCreateAccount = findViewById<TextView>(R.id.tvCreateAccount)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.startAnimation()

            Handler(Looper.getMainLooper()).postDelayed({
                
                val usuario = usuarioRepository.validarUsuario(email, password)

                if (usuario != null) {
                    // Guardar los datos del usuario en SharedPreferences
                    val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userName", usuario.nombres)
                    editor.putString("userLastName", usuario.apellidos)
                    editor.putString("userEmail", usuario.email)
                    editor.putString("userPhone", usuario.telefono)
                    editor.apply()

                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
                
            }, 2000)
        }

        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
