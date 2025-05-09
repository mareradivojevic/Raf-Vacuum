package rs.raf.nwpdomaci.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.nwpdomaci.model.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    Permission findByName(String name);
}
