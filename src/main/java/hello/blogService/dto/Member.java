package hello.blogService.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
public class Member implements UserDetails {

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return memberPwd;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
