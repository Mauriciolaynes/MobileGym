package pe.edu.idat.appgimnasio.entity.dto

data class LoginResponseDTO(
    val idUsuario: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val rol: String
)