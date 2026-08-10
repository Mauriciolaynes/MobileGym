package pe.edu.idat.appgimnasio.entity.dto

data class UsuarioRegistroDTO(
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val password: String,
    val rol: String = "CLIENTE"
)