package hello.blogService.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUser {
    private String memberName;

    private String memberEmail;

    private String memberImg;

    private String memberRole;

    @Builder
    public OAuthUser(String memberName, String memberEmail, String memberImg, String memberRole) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberImg = memberImg;
        this.memberRole = memberRole;
    }

    public OAuthUser update(String name, String picture){
        this.memberName = name;
        this.memberImg = picture;
        return this;
    }
}
