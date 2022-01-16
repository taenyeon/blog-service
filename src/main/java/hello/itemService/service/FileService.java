package hello.itemService.service;

import hello.itemService.domain.File;
import hello.itemService.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {this.fileRepository = fileRepository;}

    public List<File> getFiles(String id){
        return fileRepository.findByBoardId(id);
    }
    public int createFiles(List<File> files){
        return fileRepository.insertFiles(files);
    }
    public int deleteFiles(String id){
        return fileRepository.deleteFiles(id);
    }
}
