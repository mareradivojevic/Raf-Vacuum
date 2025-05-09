package rs.raf.nwpdomaci.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String email;
    private String password;
    private String name;
    private String surname;

    private List<String> permissions;
}
