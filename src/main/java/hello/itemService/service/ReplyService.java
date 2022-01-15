package hello.itemService.service;

import hello.itemService.domain.Reply;
import hello.itemService.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }
    public List<Reply> findById(String boardId){
        return replyRepository.findByBoardId(boardId);
    }

    public String addReply(Reply reply){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        reply.setReplyDate(now);
        int result = replyRepository.insertReply(reply);
        if (result != 0){
            return "o";
        } else {
            return "x";
        }
    }

    public String modifyReply(Reply reply){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        reply.setReplyDate(now);
        int result = replyRepository.updateReply(reply);
        if (result != 0){
            return "o";
        } else {
            return "x";
        }
    }

    public String deleteReply(String replyId){
        int result = replyRepository.deleteReply(replyId);
        if (result != 0){
            return "o";
        } else {
            return "x";
        }
    }
}
