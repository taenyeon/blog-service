package hello.blogService.controller;

import hello.blogService.domain.Reply;
import hello.blogService.repository.ReplyRepository;
import hello.blogService.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyRepository replyRepository, ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("/{boardId}")
    public Map<String, List<Reply>> getReply(@PathVariable String boardId) {
        Map<String, List<Reply>> map = new HashMap<>();
        map.put("reply", replyService.findById(boardId));
        return map;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addReply(@ModelAttribute Reply reply, HttpServletRequest request) {
        String login = (String) request.getSession().getAttribute("login");
        if (login != null) {
            reply.setReplyWriter(login);
            int result = replyService.addReply(reply);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(300).build();
            }
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<Object> modifyReply(@ModelAttribute Reply reply) {
        int result = replyService.modifyReply(reply);
        if (result > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(300).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteReply(@RequestParam("replyId") String replyId) {
        int result = replyService.deleteReply(replyId);
        if (result > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(300).build();
        }
    }
}
