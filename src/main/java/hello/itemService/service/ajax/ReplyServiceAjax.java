package hello.itemService.service.ajax;

import hello.itemService.domain.Reply;
import hello.itemService.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReplyServiceAjax {
    private final ReplyRepository replyRepository;

    public ReplyServiceAjax(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public String addReply(Reply reply){
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        reply.setReplyDate(now);
        int result = replyRepository.insertReply(reply);
        if (result != 0){
            return "o";
        } else {
            return "x";
        }
    }
}
