package hello.blogService.service;

import hello.blogService.dto.Board;
import hello.blogService.dto.FileInfo;
import hello.blogService.dto.Pagination;
import hello.blogService.repository.BoardRepository;
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

    public BoardService(BoardRepository boardRepository,FileService fileService) {
        this.boardRepository = boardRepository;
        this.fileService = fileService;
    }
    public int getBoardListCnt(){
        return boardRepository.getBoardListCnt();
    }

    public List<Board> getBoards(Pagination pagination) {
        return boardRepository.findByNum(pagination);
    }

    public Board searchBoard(String boardId) {
        Optional<Board> boardWrap = boardRepository.findById(boardId);
        List<FileInfo> fileInfos = fileService.findByBoardId(boardId);
        if (boardWrap.isPresent()){
        Board board = boardWrap.get();
        board.setFileInfos(fileInfos);
        return board;
        } else {
            throw new IllegalStateException("해당 게시물을 찾을 수 없습니다.");
        }
    }

    public Board visitBoard(String boarId) {
        boardRepository.hitUp(boarId);
       return searchBoard(boarId);
    }

    public String createBoard(Board board,List<MultipartFile> fileList) throws IOException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setBoardWriteDate(now);
        boardRepository.insertBoard(board);
        if (!fileList.get(0).isEmpty()){
        List<FileInfo> fileInfos = fileService.boardFileUpload(fileList, board.getBoardId());
        fileService.insertFiles(fileInfos);
        }
        return board.getBoardId();
    }

    public int modifyBoard(Board board,List<MultipartFile> fileList) throws IOException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        board.setBoardWriteDate(now);
        fileService.boardFileUpload(fileList,board.getBoardId());
        return boardRepository.updateBoard(board);
    }

    public int deleteBoard(String boarId) {
        List<FileInfo> fileInfos = fileService.findByBoardId(boarId);
            fileService.deleteFilesInServer(fileInfos);
        return boardRepository.deleteBoard(boarId);
    }

}
