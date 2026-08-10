package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.royrodriguez.transitionbutton.TransitionButton
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.api.UsuarioApi
import pe.edu.idat.appgimnasio.entity.dto.LoginRequestDTO
import pe.edu.idat.appgimnasio.entity.dto.LoginResponseDTO
import pe.edu.idat.appgimnasio.repository.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

            val request = LoginRequestDTO(email, password)
            val api = RetrofitClient.instance.create(UsuarioApi::class.java)

            api.loginUsuario(request).enqueue(object : Callback<LoginResponseDTO> {
                override fun onResponse(call: Call<LoginResponseDTO>, response: Response<LoginResponseDTO>) {
                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!

                        val usuarioRepository = UsuarioRepository(this@LoginActivity)
                        usuarioRepository.guardarSesionLocal(usuario)

                        Handler(Looper.getMainLooper()).postDelayed({
                            btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                                val intent = Intent(this@LoginActivity, PrincipalActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }, 2000)

                    } else {
                        btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponseDTO>, t: Throwable) {
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                    Log.e("LoginActivity", "Error de red: ${t.message}")
                    Toast.makeText(this@LoginActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }

        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}