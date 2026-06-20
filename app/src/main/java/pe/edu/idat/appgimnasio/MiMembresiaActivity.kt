package pe.edu.idat.appgimnasio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MiMembresiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_membresia)
        // Eliminado ViewCompat ya que causaba error al no encontrar el ID "main"
    }
}