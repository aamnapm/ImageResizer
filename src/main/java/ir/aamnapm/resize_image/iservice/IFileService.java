package ir.aamnapm.resize_image.iservice;

import ir.aamnapm.resize_image.dto.FileUploadDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    ResponseEntity<ByteArrayResource> multipartFileResize(MultipartFile file, String process);

    void base64Resize(FileUploadDTO fileUploadDTO);

}
