package com.portfolio.fsm.auth.configs;

import com.portfolio.fsm.auth.models.Permission;
import com.portfolio.fsm.auth.models.Role;
import com.portfolio.fsm.auth.models.User;
import com.portfolio.fsm.auth.repositories.PermissionRepository;
import com.portfolio.fsm.auth.repositories.RoleRepository;
import com.portfolio.fsm.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("Initializing default database data...");

            // Create Default Permissions
            Permission allAccess = new Permission();
            allAccess.setName("ALL_ACCESS");
            allAccess.setDescription("Full system access");
            permissionRepository.save(allAccess);

            // Create Default Role
            Role adminRole = new Role();
            adminRole.setUuidRole(UUID.randomUUID());
            adminRole.setName("ADMIN");
            adminRole.setActive(true);
            adminRole.setPermissions(List.of(allAccess));
            roleRepository.save(adminRole);

            // Create Default Admin User
            User adminUser = new User();
            adminUser.setUuidUser(UUID.randomUUID());
            adminUser.setName("System Administrator");
            adminUser.setUsername("admin");
            // The password must be hashed so Spring Security can validate it on login!
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("admin@fsm.com");
            adminUser.setCreatedAt(new Date());
            adminUser.setActive(true);
            adminUser.setRole(adminRole);
            
            userRepository.save(adminUser);

            System.out.println("✅ Default Admin User created successfully: admin / admin123");
        } else {
            System.out.println("✅ Database already initialized. Skipping default data creation.");
        }
    }
}
