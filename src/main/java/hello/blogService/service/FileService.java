package hello.blogService.service;

import hello.blogService.dto.FileInfo;
import hello.blogService.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    String path;
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // 파일 삭제
    // db의 정보는 게시물 삭제시 동시에 삭제되도록 처리하기때문에 서버측에서 이용하는 파일자체를 삭제하는 로직만으로 구성
    public void deleteFilesInServer(List<FileInfo> fileInfos) {
        for (FileInfo fileInfo : fileInfos) {
            String filePath = fileInfo.getFilePath();
            System.out.println("filePath = " + filePath);
            java.io.File oldFile = new java.io.File(path + "/boards/" + filePath);
            oldFile.delete();
        }
    }

    // 게시판번호로 파일들 찾기
    public List<FileInfo> findByBoardId(String id) {
        return fileRepository.findByBoardId(id);
    }

    // 파일 업로드 로직
    public List<FileInfo> boardFileUpload(List<MultipartFile> fileList, int boardId) throws IOException {
        List<FileInfo> filesList = new ArrayList<>();
        // 디렉토리 이름
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        String directory = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!fileList.get(0).isEmpty()) {
            for (MultipartFile file : fileList) {
                FileInfo fileInfo = new FileInfo();
                String originalName = date+"_"+ file.getOriginalFilename();
                String changedName = "/" + directory + "/" + originalName;
                // 시분초 포함한 날짜
                // "/board/" 부분은 차후 LocalDate 형식으로 폴더를 구성할 계획
                java.io.File f = new java.io.File(path + changedName);
                // 파일 폴더가 없을경우, 생성
                if (!f.exists()) {
                    f.mkdirs();
                }
                long size = file.getSize() / 1024; // kb
                file.transferTo(f);
                fileInfo.setBoardId(boardId);
                fileInfo.setFileName(originalName);
                fileInfo.setFilePath(changedName);
                fileInfo.setFileDate(now.toLocalDate());
                fileInfo.setFileSize(size);
                filesList.add(fileInfo);
            }
        }
            return filesList;
    }

    // 파일 업로드
    public void insertFiles(List<FileInfo> fileInfoList) {
        fileRepository.insertFiles(fileInfoList);
    }
}

