package hello.blogService.controller;

import hello.blogService.dto.OAuthUser;
import hello.blogService.dto.Reply;
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

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("/get/{boardId}")
    public Map<String, List<Reply>> getReply(@PathVariable String boardId) {
        Map<String, List<Reply>> map = new HashMap<>();
        map.put("reply", replyService.findById(boardId));
        ResponseEntity.ok(map);
        return map;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addReply(@ModelAttribute Reply reply, HttpServletRequest request) {
        OAuthUser user = (OAuthUser) request.getSession().getAttribute("user");
            reply.setReplyWriter(user.getMemberEmail());
            int result = replyService.addReply(reply);
        System.out.println(result);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(300).build();
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
