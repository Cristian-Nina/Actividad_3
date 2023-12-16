<em> CRUD GIMNASIO </em> <br>
La aplicación del gimnasio es un sistema básico de gestión de clientes diseñado para un gimnasio para proporcionar a los administradores del gimnasio una interfaz para gestionar la información de los clientes, facilitando la tarea de llevar un registro actualizado de los pagos de cuotas y la información personal de los miembros del gimnasio.

Agregar Cliente:
Permite registrar un nuevo cliente con su nombre, apellido, estado de cuota (pagada o no pagada) y fecha de vencimiento de la cuota.
La fecha de vencimiento indica hasta cuándo el cliente ha pagado su cuota.

Actualizar Cliente:
Permite modificar la información de un cliente existente, incluyendo su nombre, apellido, estado de cuota y fecha de vencimiento.
Los cambios se reflejan en la base de datos.

Eliminar Cliente:
Permite eliminar un cliente existente de la base de datos.

Mostrar Clientes:
Muestra en una tabla la lista de todos los clientes registrados en el gimnasio.
Resalta las filas de los clientes cuya cuota está vencida.

Mostrar Detalles del Cliente:
Permite ver detalles específicos de un cliente, incluyendo su ID, nombre, apellido, estado de cuota y fecha de vencimiento.

Validación de Datos:
Antes de realizar operaciones de agregar o actualizar, la aplicación valida que los campos obligatorios (nombre, apellido, fecha de vencimiento) no estén vacíos y que la fecha de vencimiento tenga un formato correcto.

Verificación de Cuotas Vencidas:
La aplicación verifica si la fecha de vencimiento de la cuota de un cliente está en el pasado, indicando que la cuota está vencida. Esto se refleja visualmente resaltando la fila correspondiente en la tabla.



Lógica de Validación de Datos:

_La lógica de validación de datos se implementa en el método 'validarDatosCliente' dentro de la clase 'Form'. Este método se encarga de verificar que los campos obligatorios (nombre, apellido y fecha de vencimiento) no estén vacíos y que la fecha de vencimiento tenga el formato correcto (yyyy-MM-dd). Si alguna de estas condiciones no se cumple, se muestra un mensaje de error y la operación correspondiente (agregar, actualizar) no se realiza.

Funcionalidades:

_Agregar Cliente:
La funcionalidad de agregar un cliente se implementa en el método addCliente.
Se obtienen los datos del cliente desde los campos de la interfaz gráfica.
Se valida la entrada de datos utilizando el método validarDatosCliente.
Se prepara y ejecuta una sentencia SQL para insertar un nuevo cliente en la base de datos.

_Actualizar Cliente:
La funcionalidad de actualizar un cliente se implementa en el método updateCliente.
Se obtienen los datos del cliente desde los campos de la interfaz gráfica.
Se valida la entrada de datos utilizando el método validarDatosCliente.
Se convierte la fecha de vencimiento a un objeto Date.
Se obtiene el ID del cliente seleccionado.
Se prepara y ejecuta una sentencia SQL para actualizar la información del cliente en la base de datos.

_Eliminar Cliente:
La funcionalidad de eliminar un cliente se implementa en el método deleteCliente.
Se obtiene el ID del cliente seleccionado.
Se prepara y ejecuta una sentencia SQL para eliminar al cliente de la base de datos.

_Mostrar Clientes:
La funcionalidad de mostrar todos los clientes se implementa en el método showClientes.
Se ejecuta una consulta SQL para obtener todos los clientes de la base de datos.
Se crea un modelo de tabla y se agregan las filas con la información de cada cliente.
Se verifica si la cuota está vencida y se resalta la fila correspondiente en la tabla.

_Mostrar Detalles del Cliente:
La funcionalidad de mostrar los detalles de un cliente se implementa en el método mostrarDetallesCliente.
Se obtiene el ID del cliente seleccionado.
Se ejecuta una consulta SQL para obtener los detalles del cliente.
Se muestra un cuadro de diálogo con la información detallada del cliente.

_Obtener ID del Cliente Seleccionado:
La funcionalidad de obtener el ID del cliente seleccionado se implementa en el método obtenerIdClienteSeleccionado.
Se obtiene el índice de la fila seleccionada en la tabla.
Se obtiene el valor de la columna "ID" de la fila seleccionada.

_Verificar Cuota Vencida:
La funcionalidad de verificar si la cuota está vencida se implementa en el método esCuotaVencida.
Se compara la fecha de vencimiento con la fecha actual para determinar si la cuota está vencida.




Informe del Problema y Solución en el Código:

Problema:
Anteriormente, el código presentaba un problema potencial relacionado con la actualización visual de la tabla de clientes después de realizar ciertas operaciones como agregar, actualizar o eliminar un cliente. La actualización visual de la tabla podría no reflejar correctamente los cambios realizados en la base de datos.
El problema radicaba en la forma en que se manejaba la actualización de la tabla después de realizar operaciones de CRUD. Después de agregar, actualizar o eliminar un cliente, la tabla no siempre se actualizaba adecuadamente para reflejar los cambios en tiempo real.

Solución Implementada:
Se realizó una revisión y mejora del método showClientes() en la clase Form.java. Se ajustó la lógica utilizada para mostrar los clientes en la tabla después de realizar operaciones CRUD. Ahora, después de cada operación, se vuelve a cargar la información de la base de datos y se actualiza el modelo de la tabla de manera más eficiente.

