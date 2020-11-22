package ir.aamnapm.resize_image.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class FileUploadDTO {

    @Pattern(regexp = "image\\/resize((,m_(?<method>\\w+))*(,l_(?<longer>\\d+))*(,w_(?<width>\\d+))*(,h_(?<height>\\d+))*){4}"
            , message = "structure not true")
    private String process;

    private String fileName;
    private String base64File;

}
