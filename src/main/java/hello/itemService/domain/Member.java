package hello.itemService.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class Member {

    private String memberId;

    private String memberPwd;

    private String memberName;

    private String memberTel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberCreated;

    private String memberEmail;

    private String memberImg;

    private String memberStatusMessage;

    private boolean memberIsDel;


}
