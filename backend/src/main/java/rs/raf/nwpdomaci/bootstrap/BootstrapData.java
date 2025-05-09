package rs.raf.nwpdomaci.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.nwpdomaci.model.Permission;
import rs.raf.nwpdomaci.model.User;
import rs.raf.nwpdomaci.model.Vacuum;
import rs.raf.nwpdomaci.model.VacuumStatus;
import rs.raf.nwpdomaci.repositories.PermissionRepository;
import rs.raf.nwpdomaci.repositories.UserRepository;

import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Permission createUserPermission = new Permission();
        createUserPermission.setName("can_create_users");
        Permission readUserPermission = new Permission();
        readUserPermission.setName("can_read_users");
        Permission updateUserPermission = new Permission();
        updateUserPermission.setName("can_update_users");
        Permission deleteUserPermission = new Permission();
        deleteUserPermission.setName("can_delete_users");
        permissionRepository.save(createUserPermission);
        permissionRepository.save(readUserPermission);
        permissionRepository.save(updateUserPermission);
        permissionRepository.save(deleteUserPermission);

        Permission addVacuumPermission = new Permission();
        addVacuumPermission.setName("can_add_vacuum");
        Permission searchVacuumPermission = new Permission();
        searchVacuumPermission.setName("can_search_vacuum");
        Permission removeVacuumPermission = new Permission();
        removeVacuumPermission.setName("can_remove_vacuum");
        Permission startVacuumPermission = new Permission();
        startVacuumPermission.setName("can_start_vacuum");
        Permission stopVacuumPermission = new Permission();
        stopVacuumPermission.setName("can_stop_vacuum");
        Permission dischargeVacuumPermission = new Permission();
        dischargeVacuumPermission.setName("can_discharge_vacuum");
        permissionRepository.save(addVacuumPermission);
        permissionRepository.save(searchVacuumPermission);
        permissionRepository.save(removeVacuumPermission);
        permissionRepository.save(startVacuumPermission);
        permissionRepository.save(stopVacuumPermission);
        permissionRepository.save(dischargeVacuumPermission);

        Set<Permission> permissions = new HashSet<>();
        permissions.add(createUserPermission);
        permissions.add(readUserPermission);
        permissions.add(updateUserPermission);
        permissions.add(deleteUserPermission);
        permissions.add(addVacuumPermission);
        permissions.add(searchVacuumPermission);
        permissions.add(removeVacuumPermission);
        permissions.add(startVacuumPermission);
        permissions.add(stopVacuumPermission);
        permissions.add(dischargeVacuumPermission);

        User admin = new User();
        admin.setId(1);
        admin.setEmail("admin@ms.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setPermissions(permissions);
        admin.setVacuums(new ArrayList<>());

        User user1 = new User();
        user1.setId(2);
        user1.setEmail("mradivojevic13220rn@raf.rs");
        user1.setPassword(passwordEncoder.encode("marko123"));
        user1.setName("Marko");
        user1.setSurname("Radivojević");
        user1.setPermissions(permissions);

        User user2 = new User();
        user2.setId(3);
        user2.setEmail("svetaradivojevic@gmail.com");
        user2.setPassword(passwordEncoder.encode("sveta123"));
        user2.setName("Svetozar");
        user2.setSurname("Radivojević");
        user2.setPermissions(permissions);

        Vacuum vacuum1 = new Vacuum();
        vacuum1.setName("Roomba X");
        vacuum1.setUser(admin);
        vacuum1.setDateCreated(System.currentTimeMillis());
        vacuum1.setStatus(VacuumStatus.STOPPED);
        vacuum1.setChangingStatus(false);
        vacuum1.setActive(true);

        Vacuum vacuum2 = new Vacuum();
        vacuum2.setName("Zoomba X");
        vacuum2.setUser(admin);
        vacuum2.setDateCreated(new Date().getTime() - 86400000); // yesterday
        vacuum2.setStatus(VacuumStatus.STOPPED);
        vacuum2.setChangingStatus(false);
        vacuum2.setActive(true);

        Vacuum vacuum3 = new Vacuum();
        vacuum3.setName("Doomba X");
        vacuum3.setUser(admin);
        vacuum3.setDateCreated(System.currentTimeMillis());
        vacuum3.setStatus(VacuumStatus.STOPPED);
        vacuum3.setChangingStatus(false);
        vacuum3.setActive(true);

        admin.getVacuums().add(vacuum1);
        admin.getVacuums().add(vacuum2);
        admin.getVacuums().add(vacuum3);

        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
