package hello.blogService.repository;

import hello.blogService.dto.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyRepository {
    List<Reply> findAll();
//    모든 댓글을 조회하여 댓글 정보 (Reply)를 List 에 담는다
    List<Reply> findByBoardId(@Param("boardId") String boardId,@Param("start") String start);
    // 해당 게시물의 댓글을 게시물 번호(boardId)로 찾아, 댓글의 순서에 맞게 댓글 정보(Reply)를 List 에 담는다.
    int insertReply(Reply reply);
    // 받은 댓글정보(Reply)에서 ReplyParentId 가 0 인지 아닌지로 부모 댓글인지, 부모 댓글의 하위 댓글인지를 구분하여 저장하도록 함
    // 성공 유무를 int 로 리턴.
    int deleteReply(@Param("replyId") String replyId);
    // replyId 가 댓글 번호인 댓글을 삭제하고, 만약 그 댓글의 자식 댓글이 있을경우, 같이 삭제될 수 있도록 하고
    // 성공 유무를 int 로 리턴.
    int updateReply(Reply reply);
    // replyId 가 댓글 번호인 댓글의 내용(content)를 수정하고 수정일(replyModifiedDate) 갱신 하고
    // 성공 유무를 int 로 리턴.
    Reply findByReplyId(@Param("replyId") String replyId);
    // replyId 가 댓글 번호인 댓글 정보(Reply)를 리턴.
    int findMaxReplyOrderByBoardId(@Param("boardId") String boardId);
    // 일반 댓글 성성시, boardId가 글 번호인 댓글들에서 마지막 댓글의 순서를 가져옴
    // 가져온 값을 int 로 리턴. 만약, 해당 글에 댓글이 없다면 NVL 을 통해 null 일 경우, 0을 넣을 수 있도록 함.
    int updateReplyOrder(@Param("replyOrder") String replyOrder);
    // 부모 댓글의 하위 댓글인 경우, 부모 댓글의 바로 밑에 위치해야하므로
    // 부모 댓글의 순서(replyOrder)보다 높은 순서의 댓글의 순서를 +1하여
    // 하위 댓글이 들어갈 자리를 만들고 성공 유무를 int로 리턴.
    int updateReplyOrderWhenAddDefaultReply();

}
