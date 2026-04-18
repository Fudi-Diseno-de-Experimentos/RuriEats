package com.api.rurieats.shared.infrastructure.documentation;

/**
 * GUÍA DE USO DEL SISTEMA DE MANEJO DE ERRORES
 * 
 * Esta guía muestra cómo usar las excepciones personalizadas para devolver
 * mensajes de error descriptivos a la aplicación móvil.
 * 
 * ============================================================================
 * EXCEPCIONES DISPONIBLES:
 * ============================================================================
 * 
 * 1. ResourceNotFoundException (404 Not Found)
 *    - Cuando un recurso solicitado no existe
 * 
 * 2. DuplicateResourceException (409 Conflict)
 *    - Cuando se intenta crear un recurso que ya existe
 * 
 * 3. ValidationException (400 Bad Request)
 *    - Cuando falla la validación de reglas de negocio
 * 
 * 4. UnauthorizedException (403 Forbidden)
 *    - Cuando el usuario no tiene permisos
 * 
 * ============================================================================
 * EJEMPLOS DE USO EN SERVICIOS:
 * ============================================================================
 * 
 * ANTES (usando IllegalArgumentException o Optional.empty()):
 * ----------------------------------------------------------------
 * 
 * public Optional<Account> handle(CreateAccountCommand command) {
 *     if (!profilesContextFacade.existsProfileById(command.profileId())) {
 *         throw new IllegalArgumentException("Profile does not exist");
 *     }
 *     
 *     if (accountRepository.existsByProfileIdAndName(profileId, command.name())) {
 *         throw new IllegalArgumentException("Account already exists");
 *     }
 *     
 *     var account = new Account(command);
 *     accountRepository.save(account);
 *     return Optional.of(account);
 * }
 * 
 * DESPUÉS (usando excepciones personalizadas):
 * ----------------------------------------------------------------
 * 
 * public Account handle(CreateAccountCommand command) {
 *     // Validar que el perfil existe
 *     if (!profilesContextFacade.existsProfileById(command.profileId())) {
 *         throw new ResourceNotFoundException("Perfil", command.profileId());
 *         // Mensaje: "Perfil con ID '123' no encontrado"
 *     }
 *     
 *     var profileId = new ProfileId(command.profileId());
 *     
 *     // Validar que no existe una cuenta con el mismo nombre
 *     if (accountRepository.existsByProfileIdAndName(profileId, command.name())) {
 *         throw new DuplicateResourceException(
 *             "una cuenta", 
 *             "el nombre", 
 *             command.name()
 *         );
 *         // Mensaje: "Ya existe una cuenta con el nombre 'Mi Cuenta Principal'"
 *     }
 *     
 *     // Validar cuenta primaria
 *     if (command.isPrimary() && accountRepository.existsByProfileIdAndIsPrimary(profileId, true)) {
 *         throw new ValidationException(
 *             "No puedes crear otra cuenta primaria. Ya existe una cuenta primaria para este perfil"
 *         );
 *     }
 *     
 *     var account = new Account(command);
 *     accountRepository.save(account);
 *     return account;
 * }
 * 
 * ============================================================================
 * EJEMPLOS ADICIONALES:
 * ============================================================================
 * 
 * // EJEMPLO 1: Transacción no encontrada
 * var transaction = transactionRepository.findById(transactionId)
 *     .orElseThrow(() -> new ResourceNotFoundException("Transacción", transactionId));
 * 
 * // EJEMPLO 2: Categoría ya existe
 * if (categoryRepository.existsByProfileIdAndName(profileId, categoryName)) {
 *     throw new DuplicateResourceException(
 *         "una categoría",
 *         "el nombre",
 *         categoryName
 *     );
 * }
 * 
 * // EJEMPLO 3: Validación de monto
 * if (amount.compareTo(BigInteger.ZERO) <= 0) {
 *     throw new ValidationException("El monto debe ser mayor a cero");
 * }
 * 
 * // EJEMPLO 4: Validación de fechas
 * if (endDate.before(startDate)) {
 *     throw new ValidationException(
 *         "La fecha de fin no puede ser anterior a la fecha de inicio"
 *     );
 * }
 * 
 * // EJEMPLO 5: Usuario sin permisos
 * if (!account.getProfileId().profileId().equals(currentUserId)) {
 *     throw new UnauthorizedException(
 *         "No tienes permisos para modificar esta cuenta"
 *     );
 * }
 * 
 * ============================================================================
 * RESPUESTA JSON QUE RECIBE LA APP:
 * ============================================================================
 * 
 * Cuando lanzas una excepción, la app móvil recibe un JSON como este:
 * 
 * {
 *   "status": 409,
 *   "message": "Ya existe una cuenta con el nombre 'Mi Cuenta Principal'",
 *   "details": "El recurso que intentas crear ya existe",
 *   "timestamp": "2026-02-16T22:28:25.141",
 *   "path": "/api/v1/accounts"
 * }
 * 
 * La app puede mostrar el campo "message" directamente al usuario.
 * 
 * ============================================================================
 * MODIFICACIÓN DE CONTROLADORES:
 * ============================================================================
 * 
 * ANTES:
 * ----------------------------------------------------------------
 * public ResponseEntity<AccountResource> createAccount(@Valid @RequestBody CreateAccountResource resource) {
 *     var command = CreateAccountCommandFromResourceAssembler.toCommandFromResource(resource);
 *     var account = accountCommandService.handle(command);
 *     
 *     if (account.isEmpty()) {
 *         return ResponseEntity.badRequest().build(); // ❌ Sin mensaje
 *     }
 *     
 *     var accountResource = AccountResourceFromEntityAssembler.toResourceFromEntity(account.get());
 *     return new ResponseEntity<>(accountResource, HttpStatus.CREATED);
 * }
 * 
 * DESPUÉS:
 * ----------------------------------------------------------------
 * public ResponseEntity<AccountResource> createAccount(@Valid @RequestBody CreateAccountResource resource) {
 *     var command = CreateAccountCommandFromResourceAssembler.toCommandFromResource(resource);
 *     var account = accountCommandService.handle(command); // ✅ Ya no devuelve Optional
 *     
 *     var accountResource = AccountResourceFromEntityAssembler.toResourceFromEntity(account);
 *     return new ResponseEntity<>(accountResource, HttpStatus.CREATED);
 * }
 * 
 * // El manejo de errores lo hace automáticamente GlobalExceptionHandler
 * // No necesitas try-catch ni validar Optional.isEmpty()
 * 
 * ============================================================================
 * CÓDIGOS DE ESTADO HTTP:
 * ============================================================================
 * 
 * - 200 OK: Operación exitosa
 * - 201 Created: Recurso creado exitosamente
 * - 400 Bad Request: ValidationException, datos inválidos
 * - 403 Forbidden: UnauthorizedException, sin permisos
 * - 404 Not Found: ResourceNotFoundException, recurso no existe
 * - 409 Conflict: DuplicateResourceException, recurso duplicado
 * - 500 Internal Server Error: Error inesperado del servidor
 * 
 * ============================================================================
 */
public final class ErrorHandlingGuide {
    private ErrorHandlingGuide() {}
}
