package ir.aamnapm.resize_image.utils;

import ir.aamnapm.resize_image.enums.EResizeMethod;
import ir.aamnapm.resize_image.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class Image {

    private final ServletContext servletContext;

    public ResponseEntity<ByteArrayResource> resize(InputStream inputStream, String name, String process, byte[] byteArray) {
        if (byteArray.length > 0 && inputStream != null) {
            try {

                final MediaType mediaType = getMediaTypeForFileName(name);

                byte[] resource = null;

                if (!Strings.isEmpty(process)) {
                    BufferedImage bufferedImage = ImageIO.read(inputStream);

                    Matcher matcher = createMatcher(process);

                    EResizeMethod resizeMethod = null;
//                    String resizeMethod = null;
                    int longer = 0;
                    int width = 0;
                    int height = 0;

                    if (matcher.matches()) {
//                        resizeMethod = matcher.group("method");
                        width = Integer.parseInt(matcher.group("width") != null ? matcher.group("width") : "0");
                        height = Integer.parseInt(matcher.group("height") != null ? matcher.group("height") : "0");

                        String method = matcher.group("method");

                        if (!Strings.isEmpty(method)) {
                            resizeMethod = EResizeMethod.valueOfMethodName(method);
                            switch (resizeMethod) {
                                case L_FIT:
                                    if (width == 0 || height == 0)
                                        break;
                                    if ((float) bufferedImage.getWidth() / width > (float) bufferedImage.getHeight() / height)
                                        height = height(width, bufferedImage);
                                    else
                                        width = weight(height, bufferedImage);
                                    break;
                                case M_FIT:
                                    if (width == 0 || height == 0)
                                        break;
                                    if ((float) bufferedImage.getWidth() / width < (float) bufferedImage.getHeight() / height)
                                        height = height(width, bufferedImage);
                                    else
                                        width = weight(height, bufferedImage);
                                    break;
                            }
                        } else {
                            longer = Integer.parseInt(matcher.group("longer") != null ? matcher.group("longer") : "0");
                        }
                    }

                    if (longer != 0) {
                        if (bufferedImage.getWidth() > bufferedImage.getHeight()) {
                            width = longer;
                            height = height(width, bufferedImage);
                        } else {
                            height = longer;
                            width = weight(height, bufferedImage);
                        }
                    }

                    if (width != 0 && height == 0) {
                        height = height(width, bufferedImage);
                    }

                    if (width == 0 && height != 0) {
                        width = weight(height, bufferedImage);
                    }

                    if (width != 0 && height != 0) {
                        bufferedImage = new Resize(width, height, bufferedImage).resize();

                        final int[] rgb = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
                        bufferedImage.setRGB(0, 0, width, height, rgb, 0, width);
                    }

                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, mediaType.getSubtype(), byteArrayOutputStream);

                    resource = byteArrayOutputStream.toByteArray();
                } else {
                    resource = byteArray;
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mediaType.toString()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                        .body(new ByteArrayResource(resource));

            } catch (IOException e) {
                throw new InternalErrorException(e.getMessage());
            }
        } else throw new InternalErrorException("Image is null.");
    }

    private Matcher createMatcher(String process) {
        final String patternString = "image\\/resize((,m_(?<method>\\w+))*(,l_(?<longer>\\d+))*(,w_(?<width>\\d+))*(,h_(?<height>\\d+))*){4}";
        final Pattern pattern = Pattern.compile(patternString);

        return pattern.matcher(process);
    }

    private MediaType getMediaTypeForFileName(String fileName) {
        String mimeType = servletContext.getMimeType(fileName);

        try {
            return MediaType.parseMediaType(mimeType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


    private int height(int width, BufferedImage bufferedImage) {
        return Math.round((float) width / bufferedImage.getWidth() * bufferedImage.getHeight());
    }

    private int weight(int height, BufferedImage bufferedImage) {
        return Math.round((float) height / bufferedImage.getHeight() * bufferedImage.getWidth());
    }

}

