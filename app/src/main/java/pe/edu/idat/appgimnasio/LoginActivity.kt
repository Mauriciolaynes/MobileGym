package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.royrodriguez.transitionbutton.TransitionButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<TransitionButton>(R.id.btnLogin)
        val tvCreateAccount = findViewById<TextView>(R.id.tvCreateAccount)

        btnLogin.setOnClickListener {
            // Iniciar la animación de carga
            btnLogin.startAnimation()

            // Simular un proceso de login (2 segundos)
            Handler(Looper.getMainLooper()).postDelayed({
                
                // Aquí iría tu lógica de validación
                val loginExitoso = true 

                if (loginExitoso) {
                    // Animación de expansión exitosa y navegación
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) {
                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Animación de error (sacudida)
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }
                
            }, 2000)
        }

        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
