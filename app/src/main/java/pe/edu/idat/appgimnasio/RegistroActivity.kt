package pe.edu.idat.appgimnasio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pe.edu.idat.appgimnasio.api.RetrofitClient
import pe.edu.idat.appgimnasio.api.UsuarioApi
import pe.edu.idat.appgimnasio.entity.dto.UsuarioRegistroDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    private lateinit var etRegDni: TextInputEditText
    private lateinit var tilDni: TextInputLayout
    private lateinit var etRegName: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var etRegLastName: TextInputEditText
    private lateinit var tilLastName: TextInputLayout
    private lateinit var etRegEmail: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etRegPhone: TextInputEditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var etRegPassword: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etRegConfirmPassword: TextInputEditText
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        initViews()
        setupListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        etRegDni = findViewById(R.id.etRegDni)
        tilDni = findViewById(R.id.tilDni)
        etRegName = findViewById(R.id.etRegName)
        tilName = findViewById(R.id.tilName)
        etRegLastName = findViewById(R.id.etRegLastName)
        tilLastName = findViewById(R.id.tilLastName)
        etRegEmail = findViewById(R.id.etRegEmail)
        tilEmail = findViewById(R.id.tilEmail)
        etRegPhone = findViewById(R.id.etRegPhone)
        tilPhone = findViewById(R.id.tilPhone)
        etRegPassword = findViewById(R.id.etRegPassword)
        tilPassword = findViewById(R.id.tilPassword)
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvBack = findViewById(R.id.tvBack)
    }

    private fun setupListeners() {
        tvBack.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            if (validateAll()) {
                registrarEnBackend()
            }
        }

        setupValidationWatcher(etRegDni, tilDni) { it.length == 8 }
        setupValidationWatcher(etRegEmail, tilEmail) { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
        setupValidationWatcher(etRegPhone, tilPhone) { it.length >= 9 }
        setupValidationWatcher(etRegPassword, tilPassword) { it.length >= 6 }

        etRegConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != etRegPassword.text.toString()) {
                    tilConfirmPassword.error = "Las contraseñas no coinciden"
                } else {
                    tilConfirmPassword.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun registrarEnBackend() {
        val dni = etRegDni.text.toString().trim()
        val nombres = etRegName.text.toString().trim()
        val apellidos = etRegLastName.text.toString().trim()
        val email = etRegEmail.text.toString().trim() // Se mandará como "correo"
        val telefono = etRegPhone.text.toString().trim()
        val password = etRegPassword.text.toString().trim()


        btnRegister.isEnabled = false
        btnRegister.text = "Registrando..."

        // Preparamos el objeto con rol por defecto "CLIENTE"
        val request = UsuarioRegistroDTO(dni, nombres, apellidos, email, telefono, password)

        val api = RetrofitClient.instance.create(UsuarioApi::class.java)

        api.registrarUsuario(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                btnRegister.isEnabled = true
                btnRegister.text = "Registrar"

                if (response.isSuccessful) {
                    Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegistroActivity, "Error al registrar. Verifica los datos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                btnRegister.isEnabled = true
                btnRegister.text = "Registrar"
                Log.e("Registro", "Error de red: ${t.message}")
                Toast.makeText(this@RegistroActivity, "Error de conexión al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupValidationWatcher(editText: TextInputEditText, inputLayout: TextInputLayout, validation: (String) -> Boolean) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!validation(s.toString())) {
                    inputLayout.error = "Campo inválido"
                } else {
                    inputLayout.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateAll(): Boolean {
        var isValid = true
        if (etRegDni.text?.length != 8) {
            tilDni.error = "DNI debe tener 8 dígitos"
            isValid = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etRegEmail.text.toString()).matches()) {
            tilEmail.error = "Email inválido"
            isValid = false
        }
        if (etRegPassword.text.toString().length < 6) {
            tilPassword.error = "Mínimo 6 caracteres"
            isValid = false
        }
        if (etRegPassword.text.toString() != etRegConfirmPassword.text.toString()) {
            tilConfirmPassword.error = "Las contraseñas no coinciden"
            isValid = false
        }
        return isValid
    }
}