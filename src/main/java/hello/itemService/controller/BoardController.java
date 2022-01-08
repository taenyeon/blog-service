package hello.itemService.controller;

import hello.itemService.domain.Board;
import hello.itemService.domain.Reply;
import hello.itemService.service.BoardService;
import hello.itemService.service.ajax.ReplyServiceAjax;
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
    @Value("${spring.servlet.multipart.location}")
    String path;

    private final BoardService boardService;
    private final ReplyServiceAjax replyServiceAjax;

    public BoardController(BoardService boardService, ReplyServiceAjax replyServiceAjax) {
        this.boardService = boardService;
        this.replyServiceAjax = replyServiceAjax;
    }

    @GetMapping
    public String boards(Model model) {
        List<Board> boards = boardService.getBoards();
        model.addAttribute("boards", boards);
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


    @GetMapping("/file")
    public void fileDownload(@RequestParam String filePath,
                             @RequestParam String fileName,
                             HttpServletResponse response) {
        File file = new File(path + "/board/" + filePath);
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream = null;
        ServletOutputStream servletOutputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            servletOutputStream = response.getOutputStream();
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            response.setContentLength((int) file.length());
            int read = 0;
            while ((read = bufferedInputStream.read()) != -1) {
                servletOutputStream.write(read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                throw new IllegalStateException("서버 오류로 다운로드 로직을 종료하는데 실패하였습니다.");
            }
        }
    }

    @PostMapping("/reply/add")
    @ResponseBody
    public String addReply(@ModelAttribute Reply reply) {
        return replyServiceAjax.addReply(reply);
    }

    @PostMapping("/reply/modify")
    @ResponseBody
    public String modifyReply(@ModelAttribute Reply reply){
        return replyServiceAjax.modifyReply(reply);
    }

    @PostMapping("/reply/delete")
    @ResponseBody
    public String deleteReply(@RequestParam("replyId") String replyId){
        return replyServiceAjax.deleteReply(replyId);
    }
}
