# Anotaciones de proyecto

## Problemas

## TODO
- Pruebas unitarias
- Coverage
- Documentación API
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