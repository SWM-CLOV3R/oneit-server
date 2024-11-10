package clov3r.api.common.service;

import static clov3r.domain.error.errorcode.CustomErrorCode.S3_DELETE_OBJECT_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_FILE_EXTENSION_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_PUT_OBJECT_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_REQUEST_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_UPLOAD_ERROR;
import static clov3r.domain.error.errorcode.CustomErrorCode.S3_URL_DECODING_ERROR;

import clov3r.domain.error.exception.BaseExceptionV2;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cdn.domain}")
    private String cdnDomain;

    public String upload(MultipartFile image, String dirName) {
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new BaseExceptionV2(S3_REQUEST_ERROR);
        }
        return this.uploadImage(image, dirName);
    }

    private String uploadImage(MultipartFile image, String dirName) {
        this.validateImageFileExtention(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            return this.uploadImageToS3(image, dirName);
        } catch (IOException e) {
            throw new BaseExceptionV2(S3_UPLOAD_ERROR);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new BaseExceptionV2(S3_ERROR);
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new BaseExceptionV2(S3_FILE_EXTENSION_ERROR);
        }
    }

    private String uploadImageToS3(MultipartFile image, String dirName) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        String savePath = bucketName + "/" + dirName;
        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(savePath, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        } catch (Exception e) {
            throw new BaseExceptionV2(S3_PUT_OBJECT_ERROR);
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        String s3Url = amazonS3.getUrl(savePath, s3FileName).toString();
        return s3Url.replace("https://s3.ap-northeast-2.amazonaws.com/"+bucketName, cdnDomain);
    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new BaseExceptionV2(S3_DELETE_OBJECT_ERROR);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e){
            throw new BaseExceptionV2(S3_URL_DECODING_ERROR);
        }
    }
}
