package hello.itemService.service;

import hello.itemService.domain.Board;
import hello.itemService.domain.File;
import hello.itemService.domain.Pagination;
import hello.itemService.domain.Reply;
import hello.itemService.repository.BoardRepository;
import hello.itemService.repository.FileRepository;
import hello.itemService.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
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
        List<File> files = fileRepository.findByBoardId(id);
        Board board = boardWrap.get();
        board.setFiles(files);
        return board;
    }

    public Board visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<File> files = fileRepository.findByBoardId(id);
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

    public int createFile(List<File> files) {
        return fileRepository.insertFiles(files);
    }

    public int modifyBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String id) {
        List<File> files = fileRepository.findByBoardId(id);
        for (File file : files) {
            String filePath = file.getFilePath();
            System.out.println("filePath = " + filePath);
            java.io.File oldFile = new java.io.File(path +"/boards/"+ filePath);
            oldFile.delete();
        }
        return boardRepository.deleteBoard(id);
    }

    public void fileUpload(MultipartFile[] fileList, int id) throws IOException {
        List<File> filesList = new ArrayList<>();
            if (!fileList[0].isEmpty()) {
        for (MultipartFile file : fileList) {
                String originalName;
                String changedName;
                File fileDomain = new File();
                originalName = file.getOriginalFilename();
                LocalDateTime date = LocalDateTime.now();
                String getDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
                changedName = getDate + "_" + originalName;
                java.io.File f = new java.io.File(path+"/board/"+changedName);
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
