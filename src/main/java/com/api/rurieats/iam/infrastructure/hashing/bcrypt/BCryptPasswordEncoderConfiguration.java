package com.api.rurieats.iam.infrastructure.hashing.bcrypt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCrypt Password Encoder Configuration
 * <p>
 * This configuration class provides the PasswordEncoder bean separately
 * from WebSecurityConfiguration to avoid circular dependencies.
 * </p>
 */
@Configuration
public class BCryptPasswordEncoderConfiguration {

    private final BCryptHashingService hashingService;

    public BCryptPasswordEncoderConfiguration(BCryptHashingService hashingService) {
        this.hashingService = hashingService;
    }

    /**
     * This method creates the password encoder bean.
     * @return The {@link PasswordEncoder} instance with the BCrypt hashing service
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return hashingService;
    }
}
