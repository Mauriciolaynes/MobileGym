package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.edu.idat.appgimnasio.adapter.ProgresoAdapter
import pe.edu.idat.appgimnasio.entity.Progreso
import pe.edu.idat.appgimnasio.repository.ProgresoRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProgresoActivity : AppCompatActivity() {

    private lateinit var etNuevoPeso: TextInputEditText
    private lateinit var etBuscarProgreso: TextInputEditText
    private lateinit var btnRegistrarMedida: MaterialButton
    private lateinit var rvHistorialPeso: RecyclerView

    private lateinit var btnEditarProgreso: MaterialButton
    private lateinit var btnEliminarProgreso: MaterialButton
    private lateinit var progresoRepository: ProgresoRepository
    private lateinit var adapter: ProgresoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progreso)

        initViews()
        progresoRepository = ProgresoRepository(this)
        setupRecyclerView()
        loadData()

        btnRegistrarMedida.setOnClickListener {
            registrarPeso()
        }
        btnEditarProgreso.setOnClickListener{
            editarProgreso()
        }
        btnEliminarProgreso.setOnClickListener {
            eliminarProgreso()
        }

        etBuscarProgreso.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buscarProgreso(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initViews() {
        etNuevoPeso = findViewById(R.id.etNuevoPeso)
        etBuscarProgreso = findViewById(R.id.etBuscarProgreso)
        btnRegistrarMedida = findViewById(R.id.btnRegistrarMedida)
        rvHistorialPeso = findViewById(R.id.rvHistorialPeso)
        btnEditarProgreso = findViewById(R.id.btnEditarProgreso)
        btnEliminarProgreso = findViewById(R.id.btnEliminarProgreso)
    }

    private fun setupRecyclerView() {
        adapter = ProgresoAdapter(listOf())
        rvHistorialPeso.layoutManager = LinearLayoutManager(this)
        rvHistorialPeso.adapter = adapter
    }

    private fun loadData() {
        val lista = progresoRepository.listarProgreso()
        adapter.actualizarLista(lista)
    }

    private fun registrarPeso() {
        val pesoStr = etNuevoPeso.text.toString()
        if (pesoStr.isNotEmpty()) {
            val peso = pesoStr.toDouble()
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val nuevoProgreso = Progreso(0, fecha, peso)
            
            val result = progresoRepository.registrarProgreso(nuevoProgreso)
            if (result > -1) {
                Toast.makeText(this, "Peso registrado correctamente", Toast.LENGTH_SHORT).show()
                etNuevoPeso.text?.clear()
                loadData()
            } else {
                Toast.makeText(this, "Error al registrar el peso", Toast.LENGTH_SHORT).show()
            }
        } else {
            etNuevoPeso.error = "Ingrese un peso válido"
        }
    }

    private fun buscarProgreso(query: String) {
        val listaFiltrada = if (query.isEmpty()) {
            progresoRepository.listarProgreso()
        } else {
            progresoRepository.buscarProgresoPorFecha(query)
        }
        adapter.actualizarLista(listaFiltrada)
    }

    private fun eliminarProgreso() {
        AlertDialog.Builder(this)
            .setTitle("Borrar Historial")
            .setMessage("¿Estás seguro de que deseas eliminar TODOS los registros de peso?")
            .setPositiveButton("Sí, borrar todo") { _, _ ->
                // Traemos la lista actual y la recorremos para borrar cada elemento
                val listaActual = progresoRepository.listarProgreso()
                for (progreso in listaActual) {
                    progresoRepository.eliminarProgreso(progreso.idProgreso)
                }

                loadData() // Recargamos la lista (ahora estará vacía)
                Toast.makeText(this, "Todo el historial ha sido borrado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun editarProgreso() {
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val inputId = EditText(this).apply { hint = "Ingrese ID a editar" }
        val inputPeso = EditText(this).apply { hint = "Ingrese nuevo peso" }
        layout.addView(inputId)
        layout.addView(inputPeso)

        AlertDialog.Builder(this)
            .setTitle("Editar")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val id = inputId.text.toString().toIntOrNull()
                val peso = inputPeso.text.toString().toDoubleOrNull()

                if (id != null && peso != null) {
                    val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    progresoRepository.actualizarProgreso(Progreso(id, fecha, peso))
                    loadData()
                    Toast.makeText(this, "Editado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

}
