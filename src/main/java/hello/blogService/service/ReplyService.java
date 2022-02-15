package hello.blogService.service;

import hello.blogService.config.DateSet;
import hello.blogService.dto.OAuthUser;
import hello.blogService.dto.Reply;
import hello.blogService.repository.MemberRepository;
import hello.blogService.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;

    public ReplyService(ReplyRepository replyRepository, MemberRepository memberRepository) {
        this.replyRepository = replyRepository;
        this.memberRepository = memberRepository;
    }
    public List<Reply> findById(String boardId,String start){
        List<Reply> replies = replyRepository.findByBoardId(boardId,start);
        for (Reply reply : replies){
            Optional<OAuthUser> userOptional = memberRepository.findByEmail(reply.getReplyWriter());
            OAuthUser user = userOptional.orElseThrow(() -> new IllegalStateException("Not Found User"));
            reply.setReplyWriterName(user.getMemberName());
            reply.setReplyWriterImg(user.getMemberImg());
        }
        return replies;

    }


    public int addReply(Reply reply){
        reply.setReplyWriteDate(DateSet.getNow());
        if (reply.getReplyParentId() == 0){
            replyRepository.updateReplyOrderWhenAddDefaultReply();
        }
        else {
            System.out.println(reply.getReplyParentId());
            // 부모 댓글 꺼내옴
            Reply parentReply = replyRepository.findByReplyId(String.valueOf(reply.getReplyParentId()));
            // 부모 댓글의 부모 번호 저장 (삭제시 동시 삭제 가능)
            reply.setReplyParentId(parentReply.getReplyId());
            // 부모 댓글의 깊이 +1 저장 (부모 댓글의 아래에 존재하는 댓글임을 의미)
            reply.setReplyDepth(parentReply.getReplyDepth()+1);
            // 부모 댓글의 순서 +1 저장 (자식 댓글이 부모 댓글의 바로 아래에 올 수 있도록 함.)
            reply.setReplyOrder(parentReply.getReplyOrder()+1);
            replyRepository.updateReplyOrder(String.valueOf(parentReply.getReplyOrder()));
        }
        return replyRepository.insertReply(reply);
    }

    public int modifyReply(Reply reply){
        reply.setReplyModifyDate(DateSet.getNow());
        return replyRepository.updateReply(reply);

    }

    public int deleteReply(String replyId){
        return replyRepository.deleteReply(replyId);
    }
}
