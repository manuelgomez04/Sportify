# Sportify 🏆
Sportify es una aplicación de diario deportivo que permite a los usuarios mantenerse al día con las últimas noticias, seguir sus equipos, ligas y deportes favoritos, y participar en la comunidad mediante comentarios y likes.

### El programa se ejecuta en el puerto Localhost:8080
### La documentación está realizada con swagger
### Realiza diferentes tipos de operaciones con Noticias, Deportes, Ligas, Usuarios y Equipos

## A la hora de ejecutar el programa habrá que escribir en la terminal el comando docker-compose up --build 
## Para poder arrancar el programa necesitas poner en el application-mail.properties tu correo y tu contraseña para que pueda compilar el programa

## Funcionalidades
> En función del tipo de Login que hagas pordrás hacer un tipo de peticiones u otras

### Usuarios
Registro: Los usuarios pueden registrarse como regulares, escritores o administradores.

Inicio de sesión: Los usuarios pueden iniciar sesión y obtener un token JWT.

Editar perfil: Los usuarios pueden actualizar su información personal.

Eliminar cuenta: Los usuarios pueden eliminar su cuenta.

### Noticias
Crear noticia: Los escritores pueden publicar nuevas noticias.

Obtener noticias: Los usuarios pueden ver una lista paginada de noticias.

Editar noticia: Los escritores pueden modificar sus noticias.

Eliminar noticia: Los escritores pueden eliminar sus noticias.

Filtrar noticias: Los usuarios pueden filtrar noticias por el titular, una fecha de creación entre dos rangos, por el deporte, la liga y .

### Deportes
Crear deporte: Los administradores pueden agregar nuevos deportes.

Eliminar deporte: Los administradores pueden eliminar deportes.

Seguir deporte: Los usuarios pueden seguir deportes de su interés.

Dejar de seguir deporte: Los usuarios pueden dejar de seguir deportes.

### Ligas
Crear liga: Los administradores pueden crear nuevas ligas asociadas a un deporte.

Seguir liga: Los usuarios pueden seguir ligas.

Dejar de seguir liga: Los usuarios pueden dejar de seguir ligas.

### Equipos
Crear equipo: Los administradores pueden agregar nuevos equipos.

Seguir equipo: Los usuarios pueden seguir equipos.

Dejar de seguir equipo: Los usuarios pueden dejar de seguir equipos.

### Comentarios
Agregar comentario: Los usuarios pueden comentar en las noticias.

Editar comentario: Los usuarios pueden editar sus comentarios.

Eliminar comentario: Los usuarios pueden eliminar sus comentarios, y los administradores pueden eliminar cualquier comentario.

Likes
Dar like: Los usuarios pueden dar like a las noticias.

Eliminar like: Los usuarios pueden quitar su like de una noticia.

# FIGMA
https://www.figma.com/design/HknyihjQ9WE5PwS4aCbiAc/SportifyWeb?node-id=38-1452&t=WgpIuGqkLTSESCfn-1
