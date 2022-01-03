package hello.itemservice.controller;

import hello.itemservice.domain.Member;
import hello.itemservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/join")
    public String memberJoinForm(HttpServletRequest request) {
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String memberJoin(@ModelAttribute Member member, HttpServletRequest request) {
        memberService.join(member);
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "redirect:" + referer;
    }

    @GetMapping("/login")
    public String memberLoginForm(HttpServletRequest request) {
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/loginMemberForm";
    }


    @PostMapping("/login")
    public String memberLogin(@ModelAttribute Member member, HttpServletRequest request) {
        String loginId = memberService.loginMember(member).get();
        request.getSession().setAttribute("login", loginId);
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "redirect:" + referer;
    }

    @GetMapping("/logout")
    public String memberLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("login");
        String referer = request.getHeader("REFERER");
        return "redirect:" + referer;
    }

}
