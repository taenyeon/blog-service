package hello.blogService.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUser {
    private String name;

    private String email;

    private String picture;

    private String role;

    @Builder
    public OAuthUser(String name, String email, String picture, String role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public OAuthUser update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
}
