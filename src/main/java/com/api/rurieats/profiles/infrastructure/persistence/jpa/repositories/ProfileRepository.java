package com.api.rurieats.profiles.infrastructure.persistence.jpa.repositories;

import com.api.rurieats.profiles.domain.model.aggregates.Profile;
import com.api.rurieats.profiles.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Profile Repository
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    /**
     * Find profile by user ID
     * @param userId the user ID
     * @return optional profile
     */
    Optional<Profile> findByUserId(UserId userId);
    /**
     * Check if profile exists by user ID
     * @param userId the user ID
     * @return true if exists
     */
    boolean existsByUserId(UserId userId);
}
