# Anotaciones de proyecto

## Problemas

- Preguntar por el coverage y que modulos deben de ser testeados
- Preguntar sobre como se debe de hacer la conexion con el otro microservicio para la consulta de email y documento de identidad

## TODO
- [X] Pruebas unitarias
- [X] Coverage
- [X] Documentación API
- Conexion con el otro micro para consulta de email y documento de identidad Con webclient

## Examples
- json postman
```json
{
  "documentoIdentidad": "{{$randomEmail}}",
  "tipoPrestamoId":"Préstamo Normal",
  "monto":50000,
  "plazoMeses": 5,
  "email":"{{$randomEmail}}"
}
```