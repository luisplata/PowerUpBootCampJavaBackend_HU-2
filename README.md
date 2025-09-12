# Anotaciones de proyecto

## Preguntas

- Como es la comunicacion entre los microservicios. Como se identifica que viene de otro micro o de un cliente externo.

## TODO

- [X] Pruebas unitarias
- [X] Coverage
- [X] Documentación API
- [X] Conexion con el otro micro para consulta de email y documento de identidad Con webclient
- [ ] Capturar las excepciones y retornar respuestas personalizadas.
- [ ] Paginacion de las solicitudes

## Examples

- json postman

```json
{
  "documentoIdentidad": "1143346134",
  "tipoPrestamoId": "Préstamo Normal",
  "monto": 50000,
  "plazoMeses": 5,
  "email": "www.luisplata@gmail.com"
}
```

### Documentación API
- http://localhost:8082/webjars/swagger-ui/index.html