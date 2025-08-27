# Anotaciones de proyecto

## Problemas

## TODO
- [X] Pruebas unitarias
- [X] Coverage
- [X] Documentación API
- Conexion con el otro micro para consulta de email y documento de identidad

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