package ir.aamnapm.resize_image.controller;

import ir.aamnapm.resize_image.dto.FileUploadDTO;
import ir.aamnapm.resize_image.iservice.IFileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@RestController
@RequestMapping("/upload")
@Validated
public class UploadRestController {

    private final IFileService iFileService;

    @PostMapping(value = "/multipartFile")
    public ResponseEntity<ByteArrayResource> uploadMultipartFile(
            @RequestPart("file") MultipartFile file,
            @Pattern(regexp = "image\\/resize((,m_(?<method>\\w+))*(,l_(?<longer>\\d+))*(,w_(?<width>\\d+))*(,h_(?<height>\\d+))*){4}"
                    , message = "structure not true") String process) {
        return iFileService.multipartFileResize(file, process);
    }

    @PostMapping(value = "/base64")
    public void uploadBase64File(@RequestBody FileUploadDTO fileUploadDTO) {
        iFileService.base64Resize(fileUploadDTO);
    }

}
