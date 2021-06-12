package com.mrhui.automatic.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class ProjectConfig {
    @Value("${upload.location}")
    private String location;

    @Value("${opencv.dll_path}")
    private String dllPath;
}
