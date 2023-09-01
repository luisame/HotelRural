# HotelRural Programa de gestion Hotel Ejemplo
 
Estructura del directorio src en el Proyecto HotelRural:

hotelrural: Es el paquete principal del proyecto. En JavaFX, la estructura de paquetes sigue una convención en la que se agrupan las clases y recursos relacionados.

a. interfaces: Este paquete podría contener las interfaces gráficas de usuario (GUI) del proyecto. Algunas posibles clases o archivos que se encuentren aquí podrían ser:
- VentanaPrincipal.java: La ventana o pantalla principal de la aplicación.
- ReservaHabitacion.java: Una ventana o formulario dedicado a la reserva de habitaciones.
- RegistroCliente.java: Un formulario o ventana para el registro de nuevos clientes.

b. modelo: Este paquete contiene clases que representan las entidades o modelos de datos del proyecto.
- Cliente.java: Una clase que representa a un cliente con sus atributos como nombre, ID, dirección, etc.
- Habitacion.java: Representa una habitación del hotel con atributos como número, tipo, capacidad, etc.
- Reserva.java: Representa una reserva realizada por un cliente, con detalles como fecha, habitación reservada, etc.

c. controladores: Estas clases manejan la lógica de negocio y actúan como intermediarios entre las interfaces y el modelo.
- ControladorReservas.java: Gestiona las operaciones relacionadas con las reservas.
- ControladorClientes.java: Maneja operaciones relacionadas con los clientes.
- ControladorHabitaciones.java: Administra la información de las habitaciones.

d. basededatos: Este paquete contiene las clases y utilidades relacionadas con la conexión y operaciones con la base de datos MariaDB.
- ConexionBD.java: Una clase que establece y gestiona la conexión con la base de datos.
- OperacionesBD.java: Contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos.

e. recursos: Puede contener imágenes, iconos, archivos de configuración y otros recursos utilizados en la aplicación.

f. utilidades: Aquí se pueden encontrar clases que proporcionan funcionalidades adicionales o utilidades, como la validación de entradas, formateo de fechas, entre otras.
