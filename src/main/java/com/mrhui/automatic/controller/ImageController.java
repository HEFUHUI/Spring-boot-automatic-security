package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.ProjectConfig;
import com.mrhui.automatic.service.TImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/image")
@Slf4j
public class ImageController extends CrudControllerA<TImage> {

    @Autowired
    TImageService imageService;

    @Autowired
    ProjectConfig projectConfig;
    @Override
    public StandardResult<Page<TImage>> get(
            @RequestParam(required = false,value = "q") List<String> query,
            @RequestParam(value = "page",defaultValue = "1",required = false) int page,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit
    ) {
        super.get(query, page, limit);
        Page<TImage> imagePage = imageService.findByQuery(map,new Page<>(limit,page));
        return StandardResult.success("获取成功！",imagePage);
    }

    @Override
    public StandardResult<TImage> add(TImage data) {
        return null;
    }

    @Override
    public StandardResult<TImage> update(TImage data, String id) {
        return null;
    }

    @Override
    public StandardResult<TImage> delete(String id) {
        // 拿到图片信息
        TImage image = imageService.findById(id);
        File file = new File(projectConfig.getLocation() + "/" + image.getUrl());
        //如果图片存在
        if (file.exists()) {
            //删除图片
            boolean delete = file.delete();
            if(delete){
                log.info("图片：{}，被删除成功;",file.getName());
            }
        }
        imageService.remove(id);
        return StandardResult.success("删除成功！",HttpStatus.NO_CONTENT.value());
    }

    @PostMapping("upload")
    @ResponseBody
    public StandardResult<TImage> upload(@RequestParam("file") MultipartFile image) {
        Map<String,Object> map = new HashMap<>();
        map.put("alias",image.getOriginalFilename());
        Page<TImage> byQuery = imageService.findByQuery(map,new Page<>());
        if(byQuery.getItems().size() > 0){
            throw new ParamsException("图片已存在！");
        }
        //获取当天的日期字符串
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current_date = simpleDateFormat.format(new Date());
        //创建文件类
        File file = new File(projectConfig.getLocation()+'/'+ current_date);
        //判断文件夹是否存在
        if (!file.exists()) {
//            不存在则创建
            boolean isSuccess = file.mkdirs();
            if(isSuccess){
                log.info("图片 {} 上传成功！",file.getName());
            }
        }
        //创建文件类，最终要写入的图片文件
        File ImageFile = new File(file.getAbsoluteFile() + "/" +image.getOriginalFilename());
        try {
            //将接收到的文件写入到图片文件中
            image.transferTo(ImageFile);
            //创建出图片类，用户存数据库
            final TImage tImage = new TImage();
            //设置URL 格式为 '当前时间字符串/文件名称' 如：'2010-1-1/2.png'
            tImage.setUrl(current_date+'/'+image.getOriginalFilename());
//            将图片的别名设置为文件名称
            tImage.setAlias(image.getOriginalFilename());
            //保存到数据库中
            imageService.add(tImage);
            return StandardResult.success("上传成功!", HttpStatus.NO_CONTENT.value());
        } catch (IOException e) {
//            发生错误则返回
            return StandardResult.failed("上传失败！",HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @GetMapping("/get/{id}")
    public void img(@PathVariable String id, HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setDateHeader("Expires", 0);
        //查询数据库获取图片信息
        final TImage tImage = imageService.findById(id);
        if(tImage==null){
            return;
        }
        File file = new File(projectConfig.getLocation()+'/'+tImage.getUrl());
        if(!file.exists()){
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            final byte[] bytes = fileInputStream.readAllBytes();
            responseOutputStream.write(bytes);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException e) {
            log.error("读取文件{}失败了!",tImage.getAlias());
            e.printStackTrace();
        }
    }

    @GetMapping("index")
    public String index(Model model,
                        @RequestParam(value = "search",defaultValue = "%",required = false) String search,
                        @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                        @RequestParam(value = "limit",defaultValue = "10",required = false) int limit){
        System.out.println(projectConfig.getLocation());
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        model.addAttribute("title","图片");
        Map<String,Object> map = new HashMap<>();
        //输入别名为搜索条件
        map.put("alias",search);
        model.addAttribute("pages",imageService.findByQuery(map,new Page<>(limit,page)));
        return "pages/image";
    }
}
