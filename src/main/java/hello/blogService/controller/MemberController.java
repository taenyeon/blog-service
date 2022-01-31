package hello.blogService.controller;

import hello.blogService.domain.Member;
import hello.blogService.service.EmailService;
import hello.blogService.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    public String memberJoin(Member member, HttpServletRequest request) {
        memberService.join(member);
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "index";
    }

    @GetMapping("/login")
    public String memberLoginForm(HttpServletRequest request) {
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/loginMemberForm";
    }


    @PostMapping("/login")
    public String memberLogin(Member member, HttpServletRequest request) {
        System.out.println("memberId : "+member.getMemberId());
        Member loginMember = memberService.loginMember(member).get();
        request.getSession().setAttribute("login", loginMember.getMemberId());
        request.getSession().setAttribute("img", loginMember.getMemberImg());
        String referer = (String) request.getSession().getAttribute("redirectURI");
        request.getSession().removeAttribute("redirectURI");
        return "redirect:" + referer;
    }

    @GetMapping("/logout")
    public String memberLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("login");
        request.getSession().removeAttribute("img");
        String referer = request.getHeader("REFERER");
        return "redirect:" + referer;
    }

    @GetMapping("/{memberId}")
    public String memberInfo(@PathVariable String memberId, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("login").equals(memberId)) {
            Optional<Member> member = memberService.findMember(memberId);
            model.addAttribute("member", member.get());
            return "members/memberInfo";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }

    @PostMapping("/{memberId}")
    public String memberInfoUpdate(@PathVariable String memberId,
                                   @ModelAttribute Member member,
                                   @RequestParam("file") List<MultipartFile> files,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("login").equals(memberId)) {
            String imgSet = memberService.updateMember(member, files);
            redirectAttributes.addAttribute("id", memberId);
            request.getSession().setAttribute("img", imgSet);
            return "redirect:/members/{memberId}";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }

    // 회원가입 관련 ajax
    @GetMapping("/sendEmail")
    @ResponseBody
    public ResponseEntity<Object> joinSendMail(@RequestParam String memberEmail) {
        return memberService.findMemberByEmail(memberEmail)
                .map(isMember -> ResponseEntity.status(300).build())
                .orElseGet(() -> {
                    try {
                        return ResponseEntity.ok(emailService.sendSimpleMessage(memberEmail));
                    } catch (Exception e) {
                        return null;
                    }
                });
    }

    @GetMapping("/checkId")
    @ResponseBody
    public ResponseEntity<Object> joinCheckId(@RequestParam String memberId) {
        return memberService.findMember(memberId)
                .map(isMember -> ResponseEntity.status(300).build())
                .orElse(ResponseEntity.ok().build());
    }
}

