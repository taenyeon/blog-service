package hello.itemService.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
public class Member {

    private String id;

    private String pwd;

    private String name;

    private String level;

    private String adress;

    private String tel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String email;

    private String img;
}
