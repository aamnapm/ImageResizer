package ir.aamnapm.resize_image.service;

import ir.aamnapm.resize_image.dto.FileUploadDTO;
import ir.aamnapm.resize_image.exception.InternalErrorException;
import ir.aamnapm.resize_image.iservice.IFileService;
import ir.aamnapm.resize_image.utils.Image;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;

@AllArgsConstructor
@Service
public class FileService implements IFileService {

    private final ServletContext servletContext;

    @Override
    public ResponseEntity<ByteArrayResource> multipartFileResize(MultipartFile file, String process) {
        try {
            return new Image(servletContext).resize(file.getInputStream(), file.getOriginalFilename(), process, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalErrorException(e.getMessage());
        }
    }

    @Override
    public void base64Resize(FileUploadDTO fileUploadDTO) {

    }
}
