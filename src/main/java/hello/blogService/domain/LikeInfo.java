package hello.blogService.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeInfo {

    private String likeId;

    private String boardId;

    private String memberId;
}
