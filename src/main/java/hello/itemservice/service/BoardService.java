package hello.itemservice.service;

import hello.itemservice.domain.Board;
import hello.itemservice.domain.Files;
import hello.itemservice.repository.BoardRepository;
import hello.itemservice.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    public List<Object> searchBoard(String id) {
        Optional<Board> board = boardRepository.findById(id);
        List<Files> files = fileRepository.findById(id);
        List<Object> items = new ArrayList<>();
        items.add(board);
        items.add(files);
        return items;
    }
    public List<Object> visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> board = boardRepository.findById(id);
        List<Files> files = fileRepository.findById(id);
        List<Object> items = new ArrayList<>();
        items.add(board);
        items.add(files);
        return items;
    }

    public int createBoard(Board board, List<Files> files) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        boardRepository.insertBoard(board);
        fileRepository.insertFiles(files);
        return board.getId();
    }

    public int modifyBoard(Board board) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String id) {
        fileRepository.deleteFiles(id);
        return boardRepository.deleteBoard(id);
    }
}
