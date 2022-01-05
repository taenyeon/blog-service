package hello.itemservice.service;

import hello.itemservice.domain.Board;
import hello.itemservice.domain.Files;
import hello.itemservice.repository.BoardRepository;
import hello.itemservice.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    public BoardService(BoardRepository boardRepository, FileRepository fileRepository) {
        this.boardRepository = boardRepository;
        this.fileRepository = fileRepository;
    }

    public List<Board> getBoards() {
        return boardRepository.findAll();
    }

    public Board searchBoard(String id) {
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<Files> files = fileRepository.findById(id);
        for (Files file : files){
            System.out.println(file.getBoardId());
            System.out.println(file.getFileName());
            System.out.println(file.getFilePath());
        }
        Board board = boardWrap.get();
        board.setFiles(files);
        return board;
    }
    public Board visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<Files> files = fileRepository.findById(id);
        for (Files file : files){
            System.out.println(file.getBoardId());
            System.out.println(file.getFileName());
            System.out.println(file.getFilePath());
        }
        Board board = boardWrap.get();
        board.setFiles(files);
        return board;
    }

    public int createBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        boardRepository.insertBoard(board);
        return board.getId();
    }
    public int createFile(List<Files> files){
        return fileRepository.insertFiles(files);
    }

    public int modifyBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String id) {
        System.out.println(id);
        List<Files> files = fileRepository.findById(id);
        fileRepository.deleteFiles(id);
        for (Files file : files) {
            String filePath = file.getFilePath();
            System.out.println("filePath = " + filePath);
            File oldFile = new File(filePath);
            oldFile.delete();
        }
        return boardRepository.deleteBoard(id);
    }
}
