package com.api.rurieats.restaurants.interfaces.rest;

import com.api.rurieats.restaurants.domain.model.commands.CreateDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteDishCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateDishCommand;
import com.api.rurieats.restaurants.domain.model.queries.GetDishByIdQuery;
import com.api.rurieats.restaurants.domain.model.queries.GetDishesByRestaurantIdQuery;
import com.api.rurieats.restaurants.domain.model.valueobjects.Money;
import com.api.rurieats.restaurants.domain.services.DishCommandService;
import com.api.rurieats.restaurants.domain.services.DishQueryService;
import com.api.rurieats.restaurants.interfaces.rest.resources.CreateDishResource;
import com.api.rurieats.restaurants.interfaces.rest.resources.DishResource;
import com.api.rurieats.restaurants.interfaces.rest.resources.UpdateDishResource;
import com.api.rurieats.restaurants.interfaces.rest.transform.DishResourceFromEntityAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * RestaurantDishesController
 */
@RestController
@RequestMapping(value = "/api/v1/restaurants/{restaurantId}/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant Dishes", description = "Available Restaurant Dish Endpoints")
public class RestaurantDishesController {

    private final DishCommandService dishCommandService;
    private final DishQueryService dishQueryService;

    public RestaurantDishesController(DishCommandService dishCommandService, DishQueryService dishQueryService) {
        this.dishCommandService = dishCommandService;
        this.dishQueryService = dishQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Create a new dish", description = "Create a new dish for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    public ResponseEntity<DishResource> createDish(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @Parameter(description = "Dish creation request", required = true)
            @Valid @RequestBody CreateDishResource resource) {
        var price = new Money(resource.priceAmount(), resource.priceCurrency());
        var command = new CreateDishCommand(restaurantId, resource.name(), resource.description(), price, resource.ingredients(), resource.imageUrl());
        var dish = dishCommandService.handle(command);
        if (dish.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(DishResourceFromEntityAssembler.toResourceFromEntity(dish.get()), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get dishes by restaurant ID", description = "Retrieve all dishes for a specific restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResource.class)))
    })
    public ResponseEntity<List<DishResource>> getDishesByRestaurantId(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId) {
        var query = new GetDishesByRestaurantIdQuery(restaurantId);
        var dishes = dishQueryService.handle(query);
        var resources = dishes.stream()
                .map(DishResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{dishId}")
    @Operation(summary = "Get dish by ID", description = "Retrieve a dish by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResource.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content)
    })
    public ResponseEntity<DishResource> getDishById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @Parameter(description = "Dish ID", required = true)
            @PathVariable UUID dishId) {
        var query = new GetDishByIdQuery(dishId);
        var dish = dishQueryService.handle(query);
        if (dish.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DishResourceFromEntityAssembler.toResourceFromEntity(dish.get()));
    }

    @PutMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN') or @restaurantSecurity.isOwner(authentication, #restaurantId)")
    @Operation(summary = "Update dish", description = "Update an existing dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content)
    })
    public ResponseEntity<DishResource> updateDish(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @Parameter(description = "Dish ID", required = true)
            @PathVariable UUID dishId,
            @Parameter(description = "Dish update request", required = true)
            @Valid @RequestBody UpdateDishResource resource) {
        var price = new Money(resource.priceAmount(), resource.priceCurrency());
        var command = new UpdateDishCommand(dishId, resource.name(), resource.description(), price, resource.ingredients(), resource.imageUrl());
        var dish = dishCommandService.handle(command);
        if (dish.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DishResourceFromEntityAssembler.toResourceFromEntity(dish.get()));
    }

    @DeleteMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN') or @restaurantSecurity.isOwner(authentication, #restaurantId)")
    @Operation(summary = "Delete dish", description = "Delete an existing dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dish deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteDish(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @Parameter(description = "Dish ID", required = true)
            @PathVariable UUID dishId) {
        var command = new DeleteDishCommand(dishId);
        dishCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
