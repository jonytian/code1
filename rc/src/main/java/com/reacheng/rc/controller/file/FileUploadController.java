package com.reacheng.rc.controller.file;

import org.apache.commons.io.FileUtils;

import com.reacheng.rc.core.StorageFileNotFoundException;
import com.reacheng.rc.service.StorageService;
import com.reacheng.rc.tools.GetServerRealPathUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//
//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }

//    @GetMapping("/download/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<File> serveFile(@PathVariable String filename) {
//
//        String filePath = GetServerRealPathUnit.getPath("upload");
//        File file = new File(filePath  + filename);
//        System.out.println(file.length());
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + filename+ "\"").contentLength(2074720).body(file);
//    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {

        String filePath = GetServerRealPathUnit.getPath("upload");
        File file = new File(filePath  + filename);
        HttpHeaders headers = new HttpHeaders();// 设置一个head
        headers.setContentDispositionFormData("attachment", filename);// 文件的属性，也就是文件叫什么吧
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);// 内容是字节流
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);// 开始下载
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




//    @GetMapping("/download/{filename:.+}")
//    @ResponseBody
//    public String download(@PathVariable String filename , HttpServletResponse response) {
//
//        String filePath = GetServerRealPathUnit.getPath("upload");
//        File file = new File(filePath  + filename);
//        if(file.exists()){ //判断文件父目录是否存在
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            // response.setContentType("application/force-download");
//            response.setHeader("Content-Disposition", "attachment;fileName=" +filename);
//
////            response.setContentType("application/force-download");// 设置强制下载不打开
////            response.addHeader("Content-Disposition",
////                    "attachment;fileName=" +  filename);// 设置文件名
//
//            byte[] buffer = new byte[1024];
//            FileInputStream fis = null; //文件输入流
//            BufferedInputStream bis = null;
//
//            OutputStream os = null; //输出流
//            try {
//                os = response.getOutputStream();
//                fis = new FileInputStream(file);
//                bis = new BufferedInputStream(fis);
//                int i = bis.read(buffer);
//                while(i != -1){
//                    os.write(buffer);
//                    i = bis.read(buffer);
//                }

//    int i;
//                while(i = bis.read(buffer) != -1){
//        os.write(buffer， 0 ，i);
//        os.flush()
//    }
//
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            System.out.println("----------file download---" + filename);
//            try {
//                bis.close();
//                fis.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
