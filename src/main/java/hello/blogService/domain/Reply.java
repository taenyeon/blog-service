package hello.blogService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Reply {
    // 댓글 번호
    private int replyId;

    // 댓글 본문
    private String replyContent;

    // 댓글 작성자
    private String replyWriter;

    // 댓글 작성일
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime replyWriteDate;

    //댓글 수정일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime replyModifiedDate;
    // 댓글이 속한 게시글 번호
    private int boardId;
    // 부모 댓글 번호 -> 대댓글일 경우, 부모 댓글 삭제시 같이 삭제하기 위함
    private int replyParentId;
    // 댓글의 깊이 -> 부모 댓글의 대댓글일 경우, div의 길이 조절을 통해 대댓글임을 확인 -> 부모 댓글의 깊이 +1
    private int replyDepth;
    // 댓글의 순서 -> 게시물을 기준으로 댓글의 순서를 정의 -> 중간에 대댓글이 들어올경우, 그 뒤의 숫자 +1 처리
    private int replyOrder;

    private boolean replyIsDel;
}
