package rs.raf.nwpdomaci.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.nwpdomaci.model.Vacuum;

import java.util.List;

@Repository
public interface VacuumRepository extends CrudRepository<Vacuum, Integer> {

    List<Vacuum> findByUserId(Integer userId);
}
