package hello.itemservice.controller;

import hello.itemservice.domain.member.Member;
import hello.itemservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/join")
    public String memberJoinForm(){
        return "members/joinMemberForm";
    }

    @PostMapping("/members/join")
    public String memberJoin(@ModelAttribute Member member, HttpServletRequest request){
        memberService.join(member);
        String referer = request.getHeader("REFERER");

        return "redirect:"+referer;
    }

    @GetMapping("/members")
    public String memberList(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
