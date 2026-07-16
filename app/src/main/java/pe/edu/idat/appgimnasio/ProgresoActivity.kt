package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
    }

    private fun setupRecyclerView() {
        adapter = ProgresoAdapter(listOf()) { progreso ->
            mostrarOpciones(progreso)
        }
        rvHistorialPeso.layoutManager = LinearLayoutManager(this)
        rvHistorialPeso.adapter = adapter
    }

    private fun mostrarOpciones(progreso: Progreso) {
        val opciones = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(this)
            .setTitle("Seleccione una opción")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> editarProgreso(progreso)
                    1 -> eliminarProgreso(progreso)
                }
            }
            .show()
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

    private fun eliminarProgreso(progreso: Progreso) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Registro")
            .setMessage("¿Estás seguro de que deseas eliminar este registro de peso?")
            .setPositiveButton("Eliminar") { _, _ ->
                progresoRepository.eliminarProgreso(progreso.idProgreso)
                loadData()
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun editarProgreso(progreso: Progreso) {
        val inputPeso = EditText(this).apply { 
            hint = "Ingrese nuevo peso"
            setText(progreso.peso.toString())
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Peso")
            .setView(inputPeso)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoPeso = inputPeso.text.toString().toDoubleOrNull()

                if (nuevoPeso != null) {
                    val progresoActualizado = progreso.copy(peso = nuevoPeso)
                    progresoRepository.actualizarProgreso(progresoActualizado)
                    loadData()
                    Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Peso inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


}
