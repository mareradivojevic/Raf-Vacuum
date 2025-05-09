package rs.raf.nwpdomaci.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.nwpdomaci.dto.UpdateDto;
import rs.raf.nwpdomaci.dto.UserDto;
import rs.raf.nwpdomaci.model.Permission;
import rs.raf.nwpdomaci.model.User;
import rs.raf.nwpdomaci.repositories.PermissionRepository;
import rs.raf.nwpdomaci.repositories.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository allUsers;
    private final PermissionRepository allPermissions;

    @Autowired
    public UserService(UserRepository allUsers, PermissionRepository allPermissions) {
        this.allUsers = allUsers;
        this.allPermissions = allPermissions;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = findByEmail(username);
        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist.");
        }

        User user = optionalUser.get();

        Set<Permission> permissions = user.getPermissions();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(Permission permission: permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public Optional<User> findById(Integer id) {
        return allUsers.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return allUsers.findByEmail(email);
    }

    public List<User> findAll() {
        return allUsers.findAll();
    }

    public User save(User user) {
        return this.allUsers.save(user);
    }

    public User create(UserDto userDto) {
        User user = userDtoToUser(userDto);
        return this.allUsers.save(user);
    }

    public User update(UpdateDto updateDto) {
        User user = updateDtoToUser(updateDto);
        return this.allUsers.save(user);
    }

    public void delete(Integer id) {
        this.allUsers.deleteById(id);
    }

    private User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());

        //permissions
        List<String> permissionNames = userDto.getPermissions();
        if(permissionNames == null)
            return user;

        Set<Permission> permissions = new HashSet<>();
        for(String permissionName: permissionNames) {
//            System.err.println(permissionName);
            Permission permission = this.allPermissions.findByName(permissionName);
            permissions.add(permission);
        }

        user.getPermissions().clear();
        user.getPermissions().addAll(permissions);

        return user;
    }

    private User updateDtoToUser(UpdateDto updateDto) {
        User user = this.allUsers.findById(updateDto.getId()).orElseThrow();
        user.setEmail(updateDto.getEmail());
        user.setPassword(updateDto.getPassword());
        user.setName(updateDto.getName());
        user.setSurname(updateDto.getSurname());

        //permissions
        List<String> permissionNames = updateDto.getPermissions();
        if(permissionNames == null)
            return user;

        Set<Permission> permissions = new HashSet<>();
        for(String permissionName: permissionNames) {
//            System.err.println(permissionName);
            Permission permission = this.allPermissions.findByName(permissionName);
            permissions.add(permission);
        }

        user.getPermissions().clear();
        user.getPermissions().addAll(permissions);

        return user;
    }


}
