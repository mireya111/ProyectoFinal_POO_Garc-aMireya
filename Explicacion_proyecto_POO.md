Escuela Politécnica Nacional 
POO
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
![image](https://github.com/user-attachments/assets/aacd6189-feb2-48b0-9ca0-e4cc198d39f9)
![image](https://github.com/user-attachments/assets/48c281ba-4e10-4a78-9fc7-a56efef64996)
![image](https://github.com/user-attachments/assets/be6d280d-00e1-4310-bc4e-d8b31c40aea7)
![image](https://github.com/user-attachments/assets/bf530f05-ecaa-4cea-8e02-83e0d0b7c69e)
![image](https://github.com/user-attachments/assets/f26c9352-608b-4d21-99df-5604aab56e5e)
![image](https://github.com/user-attachments/assets/fa727998-0fa6-4e4d-97c1-f25faf16fbf9)
![image](https://github.com/user-attachments/assets/7a28c87c-3392-412b-9f32-26f510175bf3)
![image](https://github.com/user-attachments/assets/d5475702-b22b-45ce-aeb7-b703d2ed9ef0)
![image](https://github.com/user-attachments/assets/590f1c92-5f50-4b1e-bc65-eb40217cc202)
![image](https://github.com/user-attachments/assets/6fe32287-c893-47cb-a015-36973f9ff95b)
![image](https://github.com/user-attachments/assets/3d197701-d1e2-4b19-9b17-43d08c031edc)
![image](https://github.com/user-attachments/assets/982bea02-233c-4d32-8414-b3648aa865b0)
![image](https://github.com/user-attachments/assets/ea99db35-8a46-422b-8df2-911ddf9ce63b)
![image](https://github.com/user-attachments/assets/99989d6d-fc31-4e37-b589-f3c06e35b9a1)
![image](https://github.com/user-attachments/assets/e9e1ad7a-e42b-4e22-a8e2-04ef8e364e35)
![image](https://github.com/user-attachments/assets/5612f463-45a7-43c5-81bb-510570cce9bb)

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
![image](https://github.com/user-attachments/assets/2c2a81b3-8aa8-4ed0-a5d8-699136c8ac3d)
![image](https://github.com/user-attachments/assets/a52e43c8-f1f3-4fc8-a764-0850e0d2e4ff)
![image](https://github.com/user-attachments/assets/7ad9028c-5f62-4fe0-abe7-3df806f7ca31)
![image](https://github.com/user-attachments/assets/7b466410-9c91-4659-bbc5-c9c793df8b4d)
![image](https://github.com/user-attachments/assets/8f95acdf-daa2-42cf-bc3c-781d53f5e2dc)
![image](https://github.com/user-attachments/assets/66382db8-5dc8-4bf6-9511-416f7cba155e)
![image](https://github.com/user-attachments/assets/ce783a35-1c40-494b-b9ce-4ef30ac4c4bd)
![image](https://github.com/user-attachments/assets/1c754b84-fa2b-4c5f-be80-3339c15de2ab)
![image](https://github.com/user-attachments/assets/8ed45a89-7ff7-4311-baa9-2abbefbf0ff6)
![image](https://github.com/user-attachments/assets/d2c0ee59-41c3-4273-811d-79e441ed1986)
![image](https://github.com/user-attachments/assets/cdd035c1-6dde-42ba-9ede-9b1e8dc934fe)
![image](https://github.com/user-attachments/assets/e09eb304-c0ac-45d3-9d3f-c32469b5d9d2)
![image](https://github.com/user-attachments/assets/588d107a-19bc-4fcd-b2cd-2c4e00ff4d04)
![image](https://github.com/user-attachments/assets/b9716492-9bba-493e-8390-110b33e69b8a)
![image](https://github.com/user-attachments/assets/c6b322cc-08a0-4639-ad17-29da236cde1a)
![image](https://github.com/user-attachments/assets/76dbd9b2-b0a1-4f90-b57a-ac80e4c3cc63)

  
  

