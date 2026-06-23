package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.appgimnasio.adapter.MembresiaAdapter
import pe.edu.idat.appgimnasio.entity.Membresia

class MiMembresiaActivity : AppCompatActivity() {
    private lateinit var rvHistorialMembresias: RecyclerView
    private lateinit var adapter: MembresiaAdapter

    private lateinit var tvTipoPlan: TextView
    private lateinit var tvEstadoPlan: TextView
    private lateinit var tvFechasPlan: TextView
    private lateinit var tvPrecioPlan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_membresia)

        tvTipoPlan = findViewById(R.id.tvTipoPlan)
        tvEstadoPlan = findViewById(R.id.tvEstadoPlan)
        tvFechasPlan = findViewById(R.id.tvFechasPlan)
        tvPrecioPlan = findViewById(R.id.tvPrecioPlan)

        rvHistorialMembresias = findViewById(R.id.rvHistorialMembresias)

        rvHistorialMembresias.layoutManager = LinearLayoutManager(this)

        adapter = MembresiaAdapter(this, emptyList())
        rvHistorialMembresias.adapter = adapter


        cargarDatosMembresia()
    }

    private fun cargarDatosMembresia() {
        tvTipoPlan.text = "Plan VIP"
        tvEstadoPlan.text = "Estado: ACTIVO"
        tvFechasPlan.text = "Inicio: 01/06/2026 - Fin: 01/07/2026"
        tvPrecioPlan.text = "Precio: S/ 150.00"

        val lista = listOf(
            Membresia(1, "Plan Básico", "01/01/2026", "01/02/2026", "Vencido", 80.00),
            Membresia(2, "Plan Premium", "01/02/2026", "01/03/2026", "Vencido", 120.00),
            Membresia(3, "Plan VIP", "01/03/2026", "01/04/2026", "Vencido", 150.00)
        )
        adapter.actualizarDatos(lista)

    }

}