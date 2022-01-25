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
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class BoardService {
    @Value("${spring.servlet.multipart.location}")
    String path;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final ReplyService replyService;

    public BoardService(BoardRepository boardRepository,FileService fileService, ReplyService replyService) {
        this.boardRepository = boardRepository;
        this.fileService = fileService;
        this.replyService = replyService;
    }
    public int getBoardListCnt(Pagination pagination){
        return boardRepository.getBoardListCnt(pagination);
    }

    public List<Board> getBoards(Pagination pagination) {
        return boardRepository.findByNum(pagination);
    }

    public Board searchBoard(String id) {
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<File> files = fileService.findByBoardId(id);
        Board board = boardWrap.get();
        board.setFiles(files);
        return board;
    }

    public Board visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<File> files = fileService.findByBoardId(id);
        List<Reply> replies = replyService.findByBoardId(id);
        Board board = boardWrap.get();
        board.setFiles(files);
        board.setReplies(replies);
        return board;
    }

    public int createBoard(Board board,List<MultipartFile> fileList) throws IOException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        boardRepository.insertBoard(board);
        if (!fileList.get(0).isEmpty()){
        List<File> files = fileService.boardFileUpload(fileList, board.getId());
        fileService.insertFiles(files);
        }
        return board.getId();
    }

    public int modifyBoard(Board board,List<MultipartFile> fileList) throws IOException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        fileService.boardFileUpload(fileList,board.getId());
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String id) {
        List<File> files = fileService.findByBoardId(id);
            fileService.deleteFilesInServer(files);
        return boardRepository.deleteBoard(id);
    }

}
