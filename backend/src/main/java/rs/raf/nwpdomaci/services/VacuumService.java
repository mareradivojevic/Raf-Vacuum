package rs.raf.nwpdomaci.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.raf.nwpdomaci.model.Vacuum;
import rs.raf.nwpdomaci.model.VacuumStatus;
import rs.raf.nwpdomaci.repositories.VacuumRepository;

import java.util.*;

@Service
public class VacuumService {

    private final VacuumRepository allVacuums;

    @Autowired
    public VacuumService(VacuumRepository allVacuums) {
        this.allVacuums = allVacuums;
    }

    public List<Vacuum> getActiveVacuums(List<Vacuum> vacuums) {
        List<Vacuum> activeVacuums = new ArrayList<>();
        for(Vacuum vacuum: vacuums) {
            if (vacuum.getActive())
                activeVacuums.add(vacuum);
        }

        return activeVacuums;
    }

    public Vacuum createVacuum(String name) {
        Vacuum vacuum = new Vacuum();
        vacuum.setName(name);
        vacuum.setDateCreated(System.currentTimeMillis());
        vacuum.setStatus(VacuumStatus.STOPPED);
        vacuum.setChangingStatus(false);
        vacuum.setActive(true);
        return vacuum;
    }

    @Async
    public void startVacuum(Vacuum vacuum) {

        vacuum.setChangingStatus(true);
        vacuum = this.allVacuums.save(vacuum);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vacuum.setStatus(VacuumStatus.STARTED);
        vacuum.setChangingStatus(false);
        this.allVacuums.save(vacuum);
    }

    public Optional<Vacuum> findById(Integer id) {
        return this.allVacuums.findById(id);
    }

    public List<Vacuum> searchVacuums(int userId, String name, List<String> statuses, Date dateFrom, Date dateTo) {
        List<Vacuum> vacuums = this.allVacuums.findByUserId(userId);
        vacuums = this.getActiveVacuums(vacuums);

        vacuums = filterByName(vacuums, name);
        vacuums = filterByStatus(vacuums, statuses);
        vacuums = filterByDate(vacuums, dateFrom, dateTo);
        return vacuums;
    }

    private List<Vacuum> filterByName(List<Vacuum> vacuums, String name) {
        if(name == null)
            return  vacuums;

        List<Vacuum> filteredList = new ArrayList<>();
        String upperCaseName = name.toUpperCase();

        for(Vacuum vacuum: vacuums) {
            if(vacuum.getName().toUpperCase().contains(upperCaseName))
                filteredList.add(vacuum);
        }

        return filteredList;
    }

    private List<Vacuum> filterByStatus(List<Vacuum> vacuums, List<String> statuses) {
        if(statuses == null)
            return vacuums;

        List<Vacuum> filteredList = new ArrayList<>();
        for(Vacuum vacuum: vacuums) {
            String statusString = vacuum.getStatus().toString();
            if(statuses.contains(statusString))
                filteredList.add(vacuum);
        }

        return filteredList;
    }

    private List<Vacuum> filterByDate(List<Vacuum> vacuums, Date dateFrom, Date dateTo) {
        if(dateFrom == null || dateTo == null)
            return vacuums;

        List<Vacuum> filteredList = new ArrayList<>();
        for(Vacuum vacuum: vacuums) {
            Date dateCreated = new Date(vacuum.getDateCreated());
            if(dateFrom.before(dateCreated) && dateTo.after(dateCreated))
                filteredList.add(vacuum);
        }

        return filteredList;
    }
}
