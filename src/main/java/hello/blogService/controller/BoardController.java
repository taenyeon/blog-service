package hello.blogService.controller;

import hello.blogService.dto.Board;
import hello.blogService.dto.OAuthUser;
import hello.blogService.dto.Pagination;
import hello.blogService.service.BoardService;
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
                         @RequestParam(required = false, defaultValue = "1") int page) {
        Pagination pagination = new Pagination();
        int listCnt = boardService.getBoardListCnt();
        pagination.pageInfo(page,listCnt);
        List<Board> boards = boardService.getBoards(pagination);
        model.addAttribute("boards", boards);
        model.addAttribute("pagination", pagination);
        return "/boards/boards";
    }

    @GetMapping("/{boardId}")
    public String board(@PathVariable String boardId, Model model) {
        Board board = boardService.visitBoard(boardId);
        model.addAttribute(board);
        return "/boards/board";
    }

    @GetMapping("/add")
    public String addForm() {
        return "boards/addBoard";
    }

    @PostMapping("/add")
    public String addBoard(@ModelAttribute Board board,
                           @RequestParam("fileList") List<MultipartFile> fileList,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) throws IOException {

        OAuthUser user = (OAuthUser) session.getAttribute("user");
        board.setBoardWriter(user.getMemberEmail());
        String boardId = boardService.createBoard(board, fileList);
        if (boardId != null) {
            redirectAttributes.addAttribute("boardId", boardId);
        } else {
            throw new IllegalStateException("게시글 생성에 실패하였습니다.");
        }
        return "redirect:/boards/{boardId}";
    }

    @GetMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable String boardId) {
        int result = boardService.deleteBoard(boardId);
        if (result == 0) {
            throw new IllegalStateException("게시판 삭제에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

    @GetMapping("/{boardId}/modify")
    public String modifyForm(@PathVariable String boardId, Model model) {
        Board board = boardService.searchBoard(boardId);
        model.addAttribute(board);
        return "/boards/modifyBoard";
    }

    @PostMapping("/{boardId}/modify")
    public String modifyBoard(@PathVariable String boardId,
                              @RequestParam("fileList") List<MultipartFile> fileList,
                              HttpSession session,
                              @ModelAttribute Board board) throws IOException {
        board.setBoardId(boardId);
        String login = (String) session.getAttribute("login");
        board.setBoardWriter(login);
        int result = boardService.modifyBoard(board, fileList);
        if (result == 0) {
            throw new IllegalStateException("게시판 수정에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

}
