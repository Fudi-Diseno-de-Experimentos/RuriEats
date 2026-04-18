package com.api.rurieats.profiles.interfaces.rest;

import com.api.rurieats.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.api.rurieats.profiles.domain.model.queries.*;

import com.api.rurieats.profiles.domain.model.valueobjects.UserId;
import com.api.rurieats.profiles.domain.services.ProfileCommandService;
import com.api.rurieats.profiles.domain.services.ProfileQueryService;
import com.api.rurieats.profiles.interfaces.rest.resources.*;
import com.api.rurieats.profiles.interfaces.rest.transform.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * ProfilesController
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Available Profile Endpoints")
public class ProfilesController {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    /**
     * Constructor
     * @param profileCommandService The {@link ProfileCommandService} instance
     * @param profileQueryService The {@link ProfileQueryService} instance
     */
    public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    /**
     * Create a new profile
     * @param resource The {@link CreateProfileResource} instance
     * @return A {@link ProfileResource} resource for the created profile, or a bad request response if the profile could not be created.
     */
    @PostMapping
    @Operation(summary = "Create a new profile", description = "Create a new user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User already has a profile",
                    content = @Content)
    })
    public ResponseEntity<ProfileResource> createProfile(
            @Parameter(description = "Profile creation request", required = true)
            @Valid @RequestBody CreateProfileResource resource) {

        var CreateProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = profileCommandService.handle(CreateProfileCommand);

        if (profile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
    }

    /**
     * Get a profile by ID
     * @param profileId The profile ID
     * @return A {@link ProfileResource} resource for the profile, or a not found response if the profile could not be found.
     */
    @PreAuthorize("hasRole('ADMIN') or @profileSecurity.isOwner(authentication, #profileId)")
    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Retrieve a profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content)
    })
    public ResponseEntity<ProfileResource> getProfileById(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable UUID profileId) {

        var query = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(query);

        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get profile by user ID", description = "Retrieve a profile by its associated user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content)
    })
    public ResponseEntity<ProfileResource> getProfileByUserId(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId) {

        var query = new GetProfileByUserIdQuery(new UserId(userId));
        var profile = profileQueryService.handle(query);

        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }


    /**
     * Get all profiles
     * @return A list of {@link ProfileResource} resources for all profiles, or a not found response if no profiles are found.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all profiles", description = "Retrieve all profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class)))
    })
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {
        var profiles = profileQueryService.handle(new GetAllProfilesQuery());
        if (profiles.isEmpty()) return ResponseEntity.notFound().build();
        var profileResources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profileResources);
    }

    @PreAuthorize("hasRole('ADMIN') or @profileSecurity.isOwner(authentication, #profileId)")
    @PutMapping("/{profileId}")
    @Operation(summary = "Update profile", description = "Update an existing profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content)
    })
    public ResponseEntity<ProfileResource> updateProfile(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable UUID profileId,
            @Parameter(description = "Profile update request", required = true)
            @Valid @RequestBody UpdateProfileResource resource) {

        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
        var profile = profileCommandService.handle(command);

        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    /**
     * Get the profile of the currently authenticated user
     * @param authentication The authentication object containing user details
     * @return A {@link ProfileResource} resource for the authenticated user's profile
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Retrieve the profile of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated",
                    content = @Content)
    })
    public ResponseEntity<ProfileResource> getMyProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID userId;
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            userId = userDetails.getUserId();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var query = new GetProfileByUserIdQuery(new UserId(userId));
        var profile = profileQueryService.handle(query);

        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

}
