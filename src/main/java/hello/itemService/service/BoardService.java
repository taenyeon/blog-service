package hello.itemService.service;

import hello.itemService.domain.Board;
import hello.itemService.domain.Files;
import hello.itemService.domain.Pagination;
import hello.itemService.domain.Reply;
import hello.itemService.repository.BoardRepository;
import hello.itemService.repository.FileRepository;
import hello.itemService.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BoardService {
    @Value("${spring.servlet.multipart.location}")
    String path;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository, FileRepository fileRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.fileRepository = fileRepository;
        this.replyRepository = replyRepository;
    }
    public int getBoardListCnt(){
        return boardRepository.getBoardListCnt();
    }

    public List<Board> getBoards(Pagination pagination) {
        return boardRepository.findByNum(pagination);
    }

    public Board searchBoard(String id) {
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<Files> files = fileRepository.findByBoardId(id);
        Board board = boardWrap.get();
        board.setFiles(files);
        return board;
    }

    public Board visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<Files> files = fileRepository.findByBoardId(id);
        List<Reply> replies = replyRepository.findByBoardId(id);
        Board board = boardWrap.get();
        board.setFiles(files);
        board.setReplies(replies);
        return board;
    }

    public int createBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        boardRepository.insertBoard(board);
        return board.getId();
    }

    public int createFile(List<Files> files) {
        return fileRepository.insertFiles(files);
    }

    public int modifyBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String id) {
        System.out.println(id);
        List<Files> files = fileRepository.findByBoardId(id);
        fileRepository.deleteFiles(id);
        replyRepository.deleteReply(id);
        for (Files file : files) {
            String filePath = file.getFilePath();
            System.out.println("filePath = " + filePath);
            File oldFile = new File(path +"/boards/"+ filePath);
            oldFile.delete();
        }
        return boardRepository.deleteBoard(id);
    }

    public void fileUpload(MultipartFile[] fileList, int id) throws IOException {
        List<Files> filesList = new ArrayList<>();
            if (!fileList[0].isEmpty()) {
        for (MultipartFile file : fileList) {
                String originalName;
                String changedName;
                Files fileDomain = new Files();
                originalName = file.getOriginalFilename();
                LocalDateTime date = LocalDateTime.now();
                String getDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
                changedName = getDate + "_" + originalName;
                File f = new File(path+"/board/"+changedName);
                long size = file.getSize() / 1024; // kb
                file.transferTo(f);
                fileDomain.setBoardId(id);
                fileDomain.setFileName(originalName);
                fileDomain.setFilePath(changedName);
                System.out.println("changedName = " + changedName);
                fileDomain.setFileSize(size);
                filesList.add(fileDomain);
            }
            createFile(filesList);
        }
    }
}
