package hello.blogService.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Blog {
    private String blogAdress;

    private String blogWriter;

    private String blogName;

    private String blogExplanation;

    private boolean blogIsTeam;

    private boolean blogIsDel;

    private boolean blogIsPrivate;

    private String blogBackgroundImg;

    private String blogMainImg;

    public void setBlogIsPrivate(String blogIsPrivate) {
        this.blogIsPrivate = (Integer.parseInt(blogIsPrivate) != 0);
    }

    public void setBlogIsTeam(String blogIsTeam) {
        this.blogIsTeam = (Integer.parseInt(blogIsTeam) != 0);
    }
}
