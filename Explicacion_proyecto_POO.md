Escuela Politécnica Nacional 
-
Autor del proyecto:
- 
García Mireya 

El presente proyecto presenta una tienda de zapatos, en la cual empleados y clientes se registran, cada una de las entidades tienen sus ventanas, pues, realizan actividades diferentes. 
Actividades que pueden realizar los clientes: 
-	Agregar productos al carrito. 
-	Eliminar productos del carrito. 
-	Editar el producto que se encuentra en el carrito. 
-	Obtener una factura en pdf. 
 Actividades que pueden realizar los empleados: 
-	Publicar productos. 
-	Actualizar información del producto. 
-	Eliminar el producto.
Para la conexión adecuada y apesar de que la base de datos es en la nube, se colocó un driver para mongo. Se inserto un driver
en las librerias de java. Además, en mongo atlas se creo un cluster0 (se configuro para que funcione en cualquier equipo computacional)en el que se halla la base de datos y sus colecciones.
Linea de conexion:
"mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

Clase Clientes
-
Se almacena la información colocada por el cliente en la ventana "RegistroClientes".
![image](https://github.com/user-attachments/assets/6f3ee598-9552-4cd5-a5e7-966ba1ae4555)
![image](https://github.com/user-attachments/assets/71f07200-ad77-496b-b5f6-75cc2fee2700)

Clase Empleados 
-
Se almacena la información colocada por el cliente en la ventana "RegistroDuenio".
![image](https://github.com/user-attachments/assets/7dbd99c1-01da-4542-bddd-ddd1b601c135)
![image](https://github.com/user-attachments/assets/b3034c29-42dd-48af-bbca-844e6a0e5467)

 
Clase Productos 
-
Se almacena la información de los productos publicados.
![image](https://github.com/user-attachments/assets/209364d8-265f-4860-ba4c-8df0309c5259)
![image](https://github.com/user-attachments/assets/7164ca25-61c9-4fff-835f-5551531a9eb2)
Método para la visualización del catalogo con cada producto publicado.
![image](https://github.com/user-attachments/assets/2ca89455-0e5b-4997-af79-1f707b818513)
![image](https://github.com/user-attachments/assets/7b68b720-b627-4dba-b4e0-df9dba890d40)

Clase Compra
-
Se almacena datos de cada compra realizada por el cliente.
 ![image](https://github.com/user-attachments/assets/da512bf8-b91a-48a9-913f-3fc4bf9b0bb0)
 ![image](https://github.com/user-attachments/assets/856021b5-9e12-40e3-8fb9-4521774f976e)
 ![image](https://github.com/user-attachments/assets/9cc745a0-6986-4a4b-918f-569a472a19e9)

Ventanas
Registro de clientes 
-
Este código maneja la interfaz gráfica para registrar clientes. La clase incluye varios campos de texto (JTextField) y contraseña (JPasswordField) para capturar la información del cliente como cédula, nombre, apellido, correo y contraseña. Un botón (JButton) llamado registroCliente se utiliza para iniciar el proceso de registro.
Cuando se presiona en el botón registroCliente, se ejecuta un ActionListener que realiza las siguientes acciones:
-	Creación y Seteo de Objeto Cliente: 
  Crea un objeto Clientes y establece sus atributos con los datos ingresados.
-	Validación de campos: 
  Solo se deben aceptar caracteres numéricos.
 	Validación de Campos Vacíos.
-	Conexión a MongoDB:
  Establece una conexión con una base de datos MongoDB utilizando un cliente de MongoDB (MongoClient). Se conecta a la base de datos Proyectofinalpoo y a la colección clientes.
-	Inserción del Documento en MongoDB: 
  Crea un documento Document con los datos del cliente y lo inserta en la colección clientes. Si la inserción es exitosa, muestra un mensaje de confirmación en un JLabel; de lo contrario, muestra un mensaje de error.
![image](https://github.com/user-attachments/assets/cc8c17bd-5908-445a-bd88-579a8e5a4e6f)
![image](https://github.com/user-attachments/assets/b9432762-a312-479d-96d5-ad5359dee8d9)
![image](https://github.com/user-attachments/assets/72533cc5-a833-4e97-bbab-20bb80d791db)
![image](https://github.com/user-attachments/assets/edbe67f7-410d-4a8d-978b-b10797d50fe4)

Registro de empleados
-
Este codigo maneja la interfaz gráfica para registrar empleados. La clase incluye varios campos de texto y contraseña para capturar la información de los empleados como: codigo del empleado, nombre del empleado, apellido del empleado, correo del empleado y su contraseña
Cuando se presiona el botón registro empleado se comienza las siguientes validaciones:
-  Campos vacíos.
-  Que el código solo acepte caracteres numericos. 
Además de las siguientes acciones en la base de datos de MongoDB:
-  Se establece una conexión con una base de datos MongoDB utilizando un cliente de MongoDB (MongoClient). Se conecta a la base de datos Proyectofinalpoo y a la colección empleado.
-  Crea un documento Document con los datos del empleado y lo inserta en la colección empleado. Si la inserción es exitosa, muestra un mensaje de confirmación en un JLabel; de lo contrario, muestra un mensaje de error.
![image](https://github.com/user-attachments/assets/3cf364c8-3eec-4d7f-a919-123e75956b41)
![image](https://github.com/user-attachments/assets/39c42078-2c40-4870-ba95-8a050e523a91)
![image](https://github.com/user-attachments/assets/4b901f96-1e8f-4304-89d8-cde225664a90)

Login
-
En esta parte del sistema se validan correos y contraseñas que se encuentran realmente en la la base de datos, cuando se presiona el botón que permite entrar al sistema, para ello se necesita: 
-La contraseña y correo deben estar en la colección clientes o empleado.
La validación que se realiza es: no deben existir campos vacíos.  
Si se encuentran en alguna de las colecciones mencionadas se abrirán ventanas diferentes para cada una: 
- Para el cliente se presenta el catálogo de productos.
- Para el empleado se presenta una ventana para la gestión de productos.
Si no encuentra coincidencias, muestra un mensaje de error. Si ocurre un problema al conectar con la base de datos, muestra un mensaje de error relacionado.
Además, se encuentra una clase donde se guardan los datos del cliente para la posterior creación de la factura en pdf.
También, si el usuario no se encuentra registrado se encuentran dos botones que arrogan las ventanas de los registros para que pueda ingresar al sistema.
![image](https://github.com/user-attachments/assets/7f36e6ce-a362-4546-8504-7b8f015a7e55)
![image](https://github.com/user-attachments/assets/1a7ab050-4041-436a-9b2d-3e4b76e7079d)
![image](https://github.com/user-attachments/assets/939331c6-57d1-4427-997e-06404a0629cf)
![image](https://github.com/user-attachments/assets/bc11de94-fe67-4239-9638-46398ce958a4)
![image](https://github.com/user-attachments/assets/a8bf7a69-ff6b-43e0-b23f-09a5be332a76)
![image](https://github.com/user-attachments/assets/1fe8ca2a-3174-42f6-bfed-16556e1fb833)
![image](https://github.com/user-attachments/assets/56e4d0f8-e373-4433-84be-ea7be70170f6)
![image](https://github.com/user-attachments/assets/11c381b0-5ac5-4041-b3e6-2bf8ee508cba)

Subir Productos 
-
Aquí se realiza un código que da la oportunidad a los empleados de subir, eliminar y actualizar los productos: 
- Exportar imágenes
  Al hacer clic en el botón "explorarImagenes", se abre un diálogo para elegir una imagen. Una vez seleccionada, la ruta del archivo se muestra en un campo de texto y la imagen se muestra en un JLabel antes de que se suba a la base de datos y se presente en la JTable que será el catálogo que visualizará al cliente, con todos sus datos.
- Subir productos
  Se alamacena toda la información digitada por el empleado como código, nombre, stock, precio y la ruta de la imagen exportada por el usuario y después la inserta en la colección empleado.
  Se actualiza una tabla en la interfaz con los detalles del nuevo producto. También maneja errores si algunos campos están vacíos o si los datos no son válidos.
- Eliminar productos
  En la tabla se tiene que seleccionar una fila (producto que se desea eliminar) y presionar el botón para eliminarlo tanto de la tabla como de la base de datos.
  Si no se ha seleccionado ningún producto, muestra un mensaje de error.
- Actualizar productos y editar productos
  En la tabla se tiene que seleccionar una fila (producto que se desea actualizar) y presionar el botón para actualizarlo tanto en la tabla como en la base de datos.
  Antes de actualizarlo se tiene que seleccionar el botón "Editar producto" para que toda la información del producto que se encuentra en la tabla se presente en los campos donde el usuario ingresa los datos del producto y pueda actualizarlo.
- Vaciar los campos donde el usuario digita para colocar un nuevo producto, se limpian tanto textos, numeros como imagenes.
- Regresar hacia la ventana de login.
![image](https://github.com/user-attachments/assets/b7706a39-cc3c-4770-a936-62514eec7139)
![image](https://github.com/user-attachments/assets/9a7c01fa-d0c0-4dd7-a6b3-f5929b7db416)
![image](https://github.com/user-attachments/assets/b9eeaba8-1999-4f7f-8df0-ee22bf0e30f0)
![image](https://github.com/user-attachments/assets/2d718fa2-f9d4-4941-9dc1-9a430d05653a)
![image](https://github.com/user-attachments/assets/9d93d2d5-d320-4225-80c8-fcfef240073d)
![image](https://github.com/user-attachments/assets/7a5c3aa5-517c-43dd-8a97-f8c6c86b5cbc)
![image](https://github.com/user-attachments/assets/9ef28521-241a-4a72-877a-bb088004e681)
![image](https://github.com/user-attachments/assets/af5ca781-040b-4173-8f01-132af8c204aa)
![image](https://github.com/user-attachments/assets/501ce343-cdd8-4781-a855-72bf0ae7140c)
![image](https://github.com/user-attachments/assets/9e0de33b-b94e-43cc-9d79-38f503bc34c7)
![image](https://github.com/user-attachments/assets/3a1e266c-7459-47d6-a818-282a592f29c5)
![image](https://github.com/user-attachments/assets/59447ec7-fc8a-46e2-98d3-e40be9a55912)
![image](https://github.com/user-attachments/assets/ebde4d4a-4fe7-4ae1-9dfc-62920418039c)

Catalogo
- 
Se ha trabajado con dos tablas una para la presentración del catálogo y otra para visualizar que productos se tiene en el carrito, 
las funciones son agregar productos al carrito, editarlos, modificarlos, eliminarlos y generar una factura en formato pdf: 
- Añadir al carrito
  Si se selecciona en la tabla donde se encuentra el catálogo un producto, se coloca la cantidad deseada y se presiona el boton "agregar carrito", el producto seleccionado se viasualizará en el carrito (la imagen no lo hará, solo datos relevantes),
  abajo del carrito se encuentr un JLabel que presenta el total de los productos seleccionados.
- Editar al carrito
  La información del producto en especial la cantidad solicitada, pues, esta se modificará, se presenta en el campo donde el cliente puede digitar.
  Es importante mencionar que se debe seleccionar un producto en la tabla carrito y el mismo elegir en la tabla catálogo.
- Modificar el carrito 
  Cuando se edite la cantidad y se presione el botón actualizar, de inmediato se modifican los detalles de un producto en el carrito.
  Permite cambiar la cantidad del producto seleccionado en el carrito y recalcula el precio total. Además, se actualiza la cantidad disponible del producto en la base de datos y muestra mensajes de error si hay problemas durante el proceso.
- Eliminar al carrito 
  Se elimina la fila seleccionada (producto que se desea eliminar) y se presenta un error si no se elige una fila o se presenta algún problema en el proceso de eliminación.
- Generar PDF
  Se utiliza una librería denominada "itext", que sirve para crear y manipular archivos PDF, RTF, y HTML en Java.
  En esta ocasión se generó un documento en pdf, se le agrego una tabla donde se encuentran los productos seleccionados por el cliente, el total a pagar, sus datos más relevantes, el número de factura y el nombre de la empresa.
  Solo se deben añadir elementos al pdf, a estos elementos se les agrego la información de la factura.  
![image](https://github.com/user-attachments/assets/d4dea875-d3dc-442e-826f-1e470f635615)
![image](https://github.com/user-attachments/assets/83cc03ba-139d-4849-84c7-30da3b110441)
![image](https://github.com/user-attachments/assets/b7f55d29-6a19-4f7e-8836-69770e3d4370)
![image](https://github.com/user-attachments/assets/269cb509-722c-441c-a8ad-039f571beddc)
![image](https://github.com/user-attachments/assets/600d915f-ffed-4f28-ad2e-7b61368db6c3)
![image](https://github.com/user-attachments/assets/4656976e-2ad6-4906-aad7-3da2a63b0259)
![image](https://github.com/user-attachments/assets/e9fb79a1-2acb-4d71-baed-f65f831b2772)
![image](https://github.com/user-attachments/assets/07b94704-88b8-4ba8-9cd5-bd55f04af67e)
![image](https://github.com/user-attachments/assets/9ff307e4-377e-4621-9903-99252fb126dd)
![image](https://github.com/user-attachments/assets/36001187-39a1-4317-9068-27d873f103d5)
![image](https://github.com/user-attachments/assets/9f455f44-6b87-4d11-9deb-a1314955cbd3)
![image](https://github.com/user-attachments/assets/bd4b9a67-2aa0-4618-b471-e1abf4e6a11d)
![image](https://github.com/user-attachments/assets/70dc4620-b2e3-4cde-ab81-3c5745880576)
![image](https://github.com/user-attachments/assets/dde95110-da6d-4fd2-9e88-76ada18f8993)
![image](https://github.com/user-attachments/assets/69aa826e-45c6-4b3e-8b5d-ea0cac28b8c4)
![image](https://github.com/user-attachments/assets/501d82ea-a544-4fe9-8ade-506eed2cabe1)
![image](https://github.com/user-attachments/assets/6acbdaa8-cae8-4044-bb16-76fb4c741ac2)


  
  

