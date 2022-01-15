package hello.itemService.controller;

import hello.itemService.domain.Reply;
import hello.itemService.repository.ReplyRepository;
import hello.itemService.service.ReplyService;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public String addReply(@ModelAttribute Reply reply) {
        return replyService.addReply(reply);
    }

    @PostMapping("/modify")
    @ResponseBody
    public String modifyReply(@ModelAttribute Reply reply) {
        return replyService.modifyReply(reply);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteReply(@RequestParam("replyId") String replyId) {
        return replyService.deleteReply(replyId);
    }
}
