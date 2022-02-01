package hello.blogService.repository;

import hello.blogService.domain.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyRepository {
    List<Reply> findAll();
    List<Reply> findByBoardId(@Param("boardId") String boardId);
    int insertReply(Reply reply);
    int deleteReply(@Param("replyId") String replyId);
    int updateReply(Reply reply);
    Reply findByReplyId(@Param("replyId") String replyId);
    Integer findMaxReplyOrderByBoardId(@Param("boardId") String boardId);
    int updateReplyOrder(@Param("replyOrder") String replyOrder);
    Integer findMaxReplyOrderByReplyId(@Param("replyId") String replyId);
}