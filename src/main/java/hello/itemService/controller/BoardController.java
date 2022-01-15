package hello.itemService.controller;

import hello.itemService.domain.Board;
import hello.itemService.domain.Pagination;
import hello.itemService.domain.Reply;
import hello.itemService.service.BoardService;
import hello.itemService.service.ReplyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyServiceAjax;

    public BoardController(BoardService boardService, ReplyService replyServiceAjax) {
        this.boardService = boardService;
        this.replyServiceAjax = replyServiceAjax;
    }

    @GetMapping
    public String boards(Model model,
                         @RequestParam(required = false, defaultValue = "1")int page,
                         @RequestParam(required = false, defaultValue = "1")int range) {
        int listCnt = boardService.getBoardListCnt();
        Pagination pagination = new Pagination();
        pagination.pageInfo(page,range,listCnt);
        List<Board> boards = boardService.getBoards(pagination);
        model.addAttribute("boards", boards);
        model.addAttribute("pagination", pagination);
        return "/boards/boards";
    }

    @GetMapping("/{id}")
    public String board(@PathVariable String id, Model model) {
        Board board = boardService.visitBoard(id);
        model.addAttribute(board);
        return "/boards/board2";
    }

    @GetMapping("/add")
    public String addForm() {
        return "boards/addBoard";
    }

    @PostMapping("/add")
    public String addBoard(@ModelAttribute Board board,
                           @RequestParam("fileList") MultipartFile[] fileList,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) throws IOException {

        String login = (String) session.getAttribute("login");
        board.setWriter(login);
        int result = boardService.createBoard(board);
        boardService.fileUpload(fileList, result);
        if (result != 0) {
            redirectAttributes.addAttribute("id", result);
        } else {
            throw new IllegalStateException("게시글 생성에 실패하였습니다.");
        }
        return "redirect:/boards/{id}";
    }

    @GetMapping("/{id}/delete")
    public String deleteBoard(@PathVariable String id) {
        int result = boardService.deleteBoard(id);
        if (result == 0) {
            throw new IllegalStateException("게시판 삭제에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

    @GetMapping("/{id}/modify")
    public String modifyForm(@PathVariable String id, Model model) {
        Board board = boardService.searchBoard(id);
        model.addAttribute(board);
        return "/boards/modifyBoard";
    }

    @PostMapping("/{id}/modify")
    public String modifyBoard(@PathVariable String id,
                              @RequestParam("fileList") MultipartFile[] fileList,
                              HttpSession session,
                              @ModelAttribute Board board) throws IOException {
        board.setId(Integer.parseInt(id));
        String login = (String) session.getAttribute("login");
        board.setWriter(login);
        int result = boardService.modifyBoard(board);
        boardService.fileUpload(fileList,Integer.parseInt(id) );
        if (result == 0) {
            throw new IllegalStateException("게시판 수정에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

}
