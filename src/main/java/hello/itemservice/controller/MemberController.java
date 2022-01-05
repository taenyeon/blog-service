package hello.itemservice.controller;

import hello.itemservice.domain.Member;
import hello.itemservice.service.EmailService;
import hello.itemservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailService) {
        this.memberService = memberService;
        this.emailService = emailService;
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
        return "home";
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
    @GetMapping("/{id}")
    public String memberInfo(@PathVariable String id,Model model,HttpServletRequest request){
        if (request.getSession().getAttribute("login").equals(id)){
        Optional<Member> member = memberService.findMember(id);
            model.addAttribute("member",member.get());
        return "members/memberInfo";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }
    @PostMapping("/{id}")
    public String memberInfoUpdate(@PathVariable String id,@ModelAttribute Member member,HttpServletRequest request){
        if (request.getSession().getAttribute("login").equals(id)){
            memberService.updateMember(member);
            return "redirect:/members/{id}";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }

    @GetMapping("/sendEmail")
    @ResponseBody
    public String joinSendMail(@RequestParam("email") String email) throws Exception {
        Optional<Member> member = memberService.findMemberByEmail(email);
        if (member.isPresent()){
         return "x";
        } else {
        return emailService.sendSimpleMessage(email);
        }
    }
    @GetMapping("/checkId")
    @ResponseBody
    public String joinCheckId(@RequestParam("id") String id){
        Optional<Member> member = memberService.findMember(id);
        if(member.isPresent()){
            return "x";
        } else {
            return "o";
        }
    }

}
