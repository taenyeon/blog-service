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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String memberList(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }

    @GetMapping("/join")
    public String memberJoinForm(HttpServletRequest request){
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String memberJoin(@ModelAttribute Member member,HttpServletRequest request){
        memberService.join(member);
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "redirect:"+referer;
    }

    @GetMapping("/login")
    public String memberLoginForm(HttpServletRequest request){
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/loginMemberForm";
    }


    @PostMapping("/login")
    public String memberLogin(@ModelAttribute Member member, HttpServletRequest request){
        String loginId = memberService.loginMember(member).get();
        request.getSession().setAttribute("login",loginId);
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "redirect:"+referer;
    }

}
