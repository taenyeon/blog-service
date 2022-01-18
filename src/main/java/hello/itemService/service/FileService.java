package hello.itemService.service;

import hello.itemService.domain.File;
import hello.itemService.repository.FileRepository;
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
    public void deleteFilesInServer(List<File> files) {
        for (File file : files) {
            String filePath = file.getFilePath();
            System.out.println("filePath = " + filePath);
            java.io.File oldFile = new java.io.File(path + "/boards/" + filePath);
            oldFile.delete();
        }
    }

    // 게시판번호로 파일들 찾기
    public List<File> findByBoardId(String id) {
        return fileRepository.findByBoardId(id);
    }

    // 파일 업로드 로직
    public List<File> boardFileUpload(MultipartFile[] fileList, int boardId) throws IOException {
        List<File> filesList = new ArrayList<>();
        // 디렉토리 이름
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String directory = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!fileList[0].isEmpty()) {
            for (MultipartFile file : fileList) {
                File fileInfo = new File();
                String originalName = file.getOriginalFilename();
                // 시분초 포함한 날짜
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
                String changedName = "/" + directory + "/" + date + "_" + originalName;
                // "/board/" 부분은 차후 LocalDate 형식으로 폴더를 구성할 계획
                java.io.File f = new java.io.File(path + changedName);
                if (!f.exists()) {
                    f.mkdirs();
                }
                long size = file.getSize() / 1024; // kb
                file.transferTo(f);
                fileInfo.setBoardId(boardId);
                fileInfo.setFileName(originalName);
                fileInfo.setFilePath(changedName);
                fileInfo.setFileDate(now);
                fileInfo.setFileSize(size);
                filesList.add(fileInfo);
            }
        }
            return filesList;
    }

    public void insertFiles(List<File> fileList) {
        fileRepository.insertFiles(fileList);
    }
}
