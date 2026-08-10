package pe.edu.idat.appgimnasio

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pe.edu.idat.appgimnasio.adapter.ProgresoAdapter
import pe.edu.idat.appgimnasio.entity.Progreso
import pe.edu.idat.appgimnasio.repository.ProgresoRepository
import pe.edu.idat.appgimnasio.repository.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProgresoActivity : AppCompatActivity() {

    private lateinit var etNuevoPeso: TextInputEditText
    private lateinit var etBuscarProgreso: TextInputEditText
    private lateinit var btnRegistrarMedida: MaterialButton
    private lateinit var rvHistorialPeso: RecyclerView
    private lateinit var ivBackProgreso: ImageView

    private lateinit var progresoRepository: ProgresoRepository
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var adapter: ProgresoAdapter
    private var idUsuario: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progreso)

        progresoRepository = ProgresoRepository()
        usuarioRepository = UsuarioRepository(this)
        
        val sesion = usuarioRepository.obtenerSesionActiva()
        if (sesion != null) {
            idUsuario = sesion.idUsuario
        } else {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        setupRecyclerView()
        loadData()

        btnRegistrarMedida.setOnClickListener {
            registrarPeso()
        }

        etBuscarProgreso.isFocusable = false
        etBuscarProgreso.isClickable = true
        etBuscarProgreso.setOnClickListener {
            mostrarDatePicker()
        }

        findViewById<TextInputLayout>(R.id.tilBuscarProgreso).setEndIconOnClickListener {
            etBuscarProgreso.text?.clear()
            filtrarProgreso("")
        }
    }

    private fun mostrarDatePicker() {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val fechaMostrada = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                etBuscarProgreso.setText(fechaMostrada)
                
                val fechaFiltro = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                filtrarProgreso(fechaFiltro)
            },
            year, month, day
        )
        
        datePickerDialog.show()
    }

    private fun initViews() {
        etNuevoPeso = findViewById(R.id.etNuevoPeso)
        etBuscarProgreso = findViewById(R.id.etBuscarProgreso)
        btnRegistrarMedida = findViewById(R.id.btnRegistrarMedida)
        rvHistorialPeso = findViewById(R.id.rvHistorialPeso)
        ivBackProgreso = findViewById(R.id.ivBackProgreso)

        ivBackProgreso.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
        progresoRepository.listarProgresosPorUsuario(idUsuario).enqueue(object : Callback<List<Progreso>> {
            override fun onResponse(call: Call<List<Progreso>>, response: Response<List<Progreso>>) {
                if (response.isSuccessful) {
                    listaCompleta = response.body() ?: listOf()
                    adapter.actualizarLista(listaCompleta)
                } else {
                    Toast.makeText(this@ProgresoActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Progreso>>, t: Throwable) {
                Log.e("ProgresoActivity", "Error de red: ${t.message}")
                Toast.makeText(this@ProgresoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registrarPeso() {
        val pesoStr = etNuevoPeso.text.toString()
        if (pesoStr.isNotEmpty()) {
            val peso = pesoStr.toDouble()
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            
            progresoRepository.registrarProgreso(idUsuario, fecha, peso).enqueue(object : Callback<Progreso> {
                override fun onResponse(call: Call<Progreso>, response: Response<Progreso>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProgresoActivity, "Peso registrado correctamente", Toast.LENGTH_SHORT).show()
                        etNuevoPeso.text?.clear()
                        loadData()
                    } else {
                        Toast.makeText(this@ProgresoActivity, "Error al registrar el peso", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Progreso>, t: Throwable) {
                    Toast.makeText(this@ProgresoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            etNuevoPeso.error = "Ingrese un peso válido"
        }
    }

    private var listaCompleta: List<Progreso> = listOf()

    private fun filtrarProgreso(query: String) {
        val listaFiltrada = if (query.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter { it.fechaRegistro.contains(query, ignoreCase = true) }
        }
        adapter.actualizarLista(listaFiltrada)
    }

    private fun eliminarProgreso(progreso: Progreso) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Registro")
            .setMessage("¿Estás seguro de que deseas eliminar este registro de peso?")
            .setPositiveButton("Eliminar") { _, _ ->
                progresoRepository.eliminarProgreso(progreso.idProgreso).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ProgresoActivity, "Registro eliminado", Toast.LENGTH_SHORT).show()
                            loadData()
                        } else {
                            Toast.makeText(this@ProgresoActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ProgresoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }
                })
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
                    progresoRepository.actualizarProgreso(progreso.idProgreso, idUsuario, progreso.fechaRegistro, nuevoPeso)
                        .enqueue(object : Callback<Progreso> {
                            override fun onResponse(call: Call<Progreso>, response: Response<Progreso>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@ProgresoActivity, "Registro actualizado", Toast.LENGTH_SHORT).show()
                                    loadData()
                                } else {
                                    Toast.makeText(this@ProgresoActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Progreso>, t: Throwable) {
                                Toast.makeText(this@ProgresoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    Toast.makeText(this, "Peso inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
