package hello.itemService.service;

import hello.itemService.domain.Files;
import hello.itemService.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {this.fileRepository = fileRepository;}

    public List<Files> getFiles(String id){
        return fileRepository.findByBoardId(id);
    }
    public int createFiles(List<Files> files){
        return fileRepository.insertFiles(files);
    }
    public int deleteFiles(String id){
        return fileRepository.deleteFiles(id);
    }
}
