package hello.itemService.controller;

import hello.itemService.domain.Board;
import hello.itemService.domain.Pagination;
import hello.itemService.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping()
    public String boards(Model model,
                         @RequestParam(required = false, defaultValue = "1") int page,
                         @RequestParam(required = false, defaultValue = "1") int range,
                         @RequestParam(required = false, defaultValue = "title") String searchType,
                         @RequestParam(required = false, defaultValue = "") String keyword) {
        Pagination pagination = new Pagination();
        pagination.setSearchType(searchType);
        pagination.setKeyword(keyword);
        int listCnt = boardService.getBoardListCnt(pagination);
        pagination.pageInfo(page, range, listCnt);
        List<Board> boards = boardService.getBoards(pagination);
        model.addAttribute("boards", boards);
        model.addAttribute("pagination", pagination);
        return "/boards/boards";
    }

    @GetMapping("/{id}")
    public String board(@PathVariable String id, Model model) {
        Board board = boardService.visitBoard(id);
        model.addAttribute(board);
        return "/boards/board";
    }

    @GetMapping("/add")
    public String addForm() {
        return "/boards/addBoard2";
    }

    @PostMapping("/add")
    public String addBoard(@ModelAttribute Board board,
                           @RequestParam("fileList") List<MultipartFile> fileList,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) throws IOException {

        String login = (String) session.getAttribute("login");
        board.setWriter(login);
        int id = boardService.createBoard(board, fileList);
        if (id != 0) {
            redirectAttributes.addAttribute("id", id);
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
                              @RequestParam("fileList") List<MultipartFile> fileList,
                              HttpSession session,
                              @ModelAttribute Board board) throws IOException {
        board.setId(Integer.parseInt(id));
        String login = (String) session.getAttribute("login");
        board.setWriter(login);
        int result = boardService.modifyBoard(board, fileList);
        if (result == 0) {
            throw new IllegalStateException("게시판 수정에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

}
