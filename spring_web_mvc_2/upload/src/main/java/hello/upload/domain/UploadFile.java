package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName; // 고객이 업로드한 파일명(한 고객과 다른 고객이 똑같은 파일명을 사용할 수도 있음)
    // 그래서 사용자가 사용하는 파일명과 서버 내부에서 관리하는 별도의 파일명이 필요함.
    // 사용자가 사용하는 파일명은 중복이 있어도 되지만 서버 내부에서 관리하는 파일명은 중복이 있으면 안됨.
    private String storeFileName; // 서버 내부에서 관리하는 파일명(절대 중복이 있으면 안됨)

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
