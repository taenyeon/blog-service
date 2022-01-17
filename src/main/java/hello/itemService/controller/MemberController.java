package hello.itemService.controller;

import hello.itemService.domain.Member;
import hello.itemService.service.EmailService;
import hello.itemService.service.MemberService;
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
    private final EmailService emailServiceAjax;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailServiceAjax) {
        this.memberService = memberService;
        this.emailServiceAjax = emailServiceAjax;
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
        return "index";
    }

    @GetMapping("/login")
    public String memberLoginForm(HttpServletRequest request) {
        String referer = request.getHeader("REFERER");
        request.getSession().setAttribute("redirectURI", referer);
        return "members/loginMemberForm";
    }


    @PostMapping("/login")
    public String memberLogin(@ModelAttribute Member member, HttpServletRequest request) {
        Member loginMember = memberService.loginMember(member).get();
        request.getSession().setAttribute("login", loginMember.getId());
        request.getSession().setAttribute("img", loginMember.getImg());
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

    @GetMapping("/{id}")
    public String memberInfo(@PathVariable String id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("login").equals(id)) {
            Optional<Member> member = memberService.findMember(id);
            model.addAttribute("member", member.get());
            return "members/memberInfo";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }

    @PostMapping("/{id}")
    public String memberInfoUpdate(@PathVariable String id,
                                   @ModelAttribute Member member,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("login").equals(id)) {
            String imgSet = memberService.updateMember(member, file);
            redirectAttributes.addAttribute("id", id);
            request.getSession().setAttribute("img", imgSet);
            return "redirect:/members/{id}";
        } else {
            throw new IllegalStateException("현재 세션에서 제한된 접근입니다.");
        }
    }

    // 회원가입 관련 ajax
    @GetMapping("/sendEmail")
    @ResponseBody
    public ResponseEntity<Object> joinSendMail(@RequestParam("email") String email){
        return memberService.findMemberByEmail(email)
                .map(isMember -> ResponseEntity.status(300).build())
                .orElseGet(() -> {
                    try {
                        return ResponseEntity.ok(emailServiceAjax.sendSimpleMessage(email));
                    } catch (Exception e) {
                        return null;
                    }
                });
    }

    @GetMapping("/checkId")
    @ResponseBody
    public ResponseEntity<Object> joinCheckId(@RequestParam("id") String id) {
        return memberService.findMember(id)
                .map(isMember -> ResponseEntity.status(300).build())
                .orElse(ResponseEntity.ok().build());
    }
}

