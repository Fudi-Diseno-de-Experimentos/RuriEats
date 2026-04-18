package com.api.rurieats.restaurants.interfaces.rest;

import com.api.rurieats.restaurants.domain.model.commands.CreateRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.DeleteRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.commands.UpdateRestaurantCommand;
import com.api.rurieats.restaurants.domain.model.queries.GetRestaurantByIdQuery;
import com.api.rurieats.restaurants.domain.services.RestaurantCommandService;
import com.api.rurieats.restaurants.domain.services.RestaurantQueryService;
import com.api.rurieats.restaurants.interfaces.rest.resources.CreateRestaurantResource;
import com.api.rurieats.restaurants.interfaces.rest.resources.RestaurantResource;
import com.api.rurieats.restaurants.interfaces.rest.resources.UpdateRestaurantResource;
import com.api.rurieats.restaurants.interfaces.rest.transform.RestaurantResourceFromEntityAssembler;
import com.api.rurieats.shared.domain.model.valueobjects.ProfileId;

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

import java.util.UUID;

/**
 * RestaurantsController
 */
@RestController
@RequestMapping(value = "/api/v1/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurants", description = "Available Restaurant Endpoints")
public class RestaurantsController {

    private final RestaurantCommandService restaurantCommandService;
    private final RestaurantQueryService restaurantQueryService;

    public RestaurantsController(RestaurantCommandService restaurantCommandService, RestaurantQueryService restaurantQueryService) {
        this.restaurantCommandService = restaurantCommandService;
        this.restaurantQueryService = restaurantQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Create a new restaurant", description = "Create a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    public ResponseEntity<RestaurantResource> createRestaurant(
            @Parameter(description = "Restaurant creation request", required = true)
            @Valid @RequestBody CreateRestaurantResource resource) {
        var command = new CreateRestaurantCommand(new ProfileId(resource.ownerId()), resource.name(), resource.address());
        var restaurant = restaurantCommandService.handle(command);
        if (restaurant.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(RestaurantResourceFromEntityAssembler.toResourceFromEntity(restaurant.get()), HttpStatus.CREATED);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get restaurant by ID", description = "Retrieve a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResource.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    public ResponseEntity<RestaurantResource> getRestaurantById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId) {
        var query = new GetRestaurantByIdQuery(restaurantId);
        var restaurant = restaurantQueryService.handle(query);
        if (restaurant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(RestaurantResourceFromEntityAssembler.toResourceFromEntity(restaurant.get()));
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN') or @restaurantSecurity.isOwner(authentication, #restaurantId)")
    @Operation(summary = "Update restaurant", description = "Update an existing restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    public ResponseEntity<RestaurantResource> updateRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @Parameter(description = "Restaurant update request", required = true)
            @Valid @RequestBody UpdateRestaurantResource resource) {
        var command = new UpdateRestaurantCommand(restaurantId, resource.name(), resource.address());
        var restaurant = restaurantCommandService.handle(command);
        if (restaurant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(RestaurantResourceFromEntityAssembler.toResourceFromEntity(restaurant.get()));
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN') or @restaurantSecurity.isOwner(authentication, #restaurantId)")
    @Operation(summary = "Delete restaurant", description = "Delete an existing restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId) {
        var command = new DeleteRestaurantCommand(restaurantId);
        restaurantCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
