package hello.itemService.service;

import hello.itemService.domain.Board;
import hello.itemService.domain.FileInfo;
import hello.itemService.domain.Pagination;
import hello.itemService.domain.Reply;
import hello.itemService.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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
        List<FileInfo> fileInfos = fileService.findByBoardId(id);
        if (boardWrap.isPresent()){
        Board board = boardWrap.get();
        board.setFileInfos(fileInfos);
        return board;
        } else {
            throw new IllegalStateException("해당 게시물을 찾을 수 없습니다.");
        }
    }

    public Board visitBoard(String id) {
        boardRepository.hitUp(id);
        Optional<Board> boardWrap = boardRepository.findById(id);
        List<FileInfo> fileInfos = fileService.findByBoardId(id);
        List<Reply> replies = replyService.findByBoardId(id);
        if (boardWrap.isPresent()){
        Board board = boardWrap.get();
        board.setFileInfos(fileInfos);
        board.setReplies(replies);
        return board;
        } else {
            throw new IllegalStateException("해당 게시물을 찾을 수 없습니다.");
        }
    }

    public int createBoard(Board board,List<MultipartFile> fileList) throws IOException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setDate(now);
        boardRepository.insertBoard(board);
        if (!fileList.get(0).isEmpty()){
        List<FileInfo> fileInfos = fileService.boardFileUpload(fileList, board.getId());
        fileService.insertFiles(fileInfos);
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
        List<FileInfo> fileInfos = fileService.findByBoardId(id);
            fileService.deleteFilesInServer(fileInfos);
        return boardRepository.deleteBoard(id);
    }

}
