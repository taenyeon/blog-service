package hello.itemservice.controller;

import hello.itemservice.domain.Board;
import hello.itemservice.domain.Files;
import hello.itemservice.service.BoardService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Value("${spring.servlet.multipart.location}")
    String path;

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
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

        String originalName, changedName;
        String login = (String) session.getAttribute("login");
        board.setWriter(login);
        int result = boardService.createBoard(board);

        List<Files> filesList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            Files fileDomain = new Files();
            originalName = file.getOriginalFilename();
            LocalDateTime date = LocalDateTime.now();
            String getDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
            changedName = getDate + "_" + originalName;
            File f = new File(changedName);
            long size = f.length();
            file.transferTo(f);
            fileDomain.setBoardId(result);
            fileDomain.setFileName(originalName);
            fileDomain.setFilePath(changedName);
            fileDomain.setFileSize(size);
            filesList.add(fileDomain);
        }
        boardService.createFile(filesList);
        if (result != 0) {
            redirectAttributes.addAttribute("id", result);
        } else {
            throw new IllegalStateException("게시글 생성에 실패하였습니다.");
        }
        return "redirect:/boards/{id}";
    }

    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable String id) {
        int result = boardService.deleteBoard(id);
        if (result == 0) {
            throw new IllegalStateException("게시판 삭제에 실패하였습니다.");
        }
        return "redirect:/boards";
    }

    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable String id, Model model) {
        Board board = boardService.searchBoard(id);
        model.addAttribute(board);
        return "/boards/modifyBoard";
    }

    @PostMapping("/modify/{id}")
    public String modifyBoard(@PathVariable String id,
                              @ModelAttribute Board board) {
        board.setId(Integer.parseInt(id));
        int result = boardService.modifyBoard(board);
        if (result == 0) {
            throw new IllegalStateException("게시판 수정에 실패하였습니다.");
        }
        return "redirect:/boards";
    }


    @GetMapping("/file")
    public void fileDownload(@RequestParam String filePath,
                             @RequestParam String fileName,
                             HttpServletResponse response) {
        File file = new File(path +"/"+ filePath);

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
            try{
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
                if (bufferedInputStream != null){
                    bufferedInputStream.close();
                }
            } catch (Exception e){
                throw new IllegalStateException("서버 오류로 다운로드 로직을 종료하는데 실패하였습니다.");
            }

        }
    }

}
