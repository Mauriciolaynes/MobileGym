package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        val tvPerfilNombre = findViewById<TextView>(R.id.tvPerfilNombre)
        val tvPerfilApellido = findViewById<TextView>(R.id.tvPerfilApellido)
        val tvPerfilEmail = findViewById<TextView>(R.id.tvPerfilEmail)
        val tvPerfilTelefono = findViewById<TextView>(R.id.tvPerfilTelefono)
        
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        tvPerfilNombre.text = sharedPreferences.getString("userName", "N/A")
        tvPerfilApellido.text = sharedPreferences.getString("userLastName", "N/A")
        tvPerfilEmail.text = sharedPreferences.getString("userEmail", "N/A")
        tvPerfilTelefono.text = sharedPreferences.getString("userPhone", "N/A")
        
        val btnCerrar = findViewById<Button>(R.id.btnCerrar)
        btnCerrar.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}