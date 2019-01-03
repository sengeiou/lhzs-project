package cn.lhzs.web.controller;

import cn.lhzs.common.result.ResponseResult;
import cn.lhzs.common.util.DateUtil;
import cn.lhzs.common.util.StringUtil;
import cn.lhzs.common.uuid.UUIDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static cn.lhzs.common.result.ResponseResultGenerator.generatorSuccessResult;

/**
 * 附件上传处理
 */
@Controller
@RequestMapping("/notificationUpload")
public class NotificationUploadController {

    @RequestMapping("/deleteFile")
    @ResponseBody
    public ResponseResult deleteFile(String id) {
        return generatorSuccessResult();
    }

    @RequestMapping("/fileUpload")
    public void fileUpload(MultipartFile file, Long userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getParameter("method");
        String attach = UUIDUtil.generateUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uploadDir = createUploadDir(file, attach);
        if (file != null && StringUtil.equals("fileupload", method)) {
            String fileName = file.getOriginalFilename();
            double fileSize = file.getSize() / (1024 * 1024 * 1.0);
            response.getWriter().print(uploadDir + attach + "," + fileSize + "," + fileName);
        }
    }

    /**
     * 创建附件上传目录
     *
     * @return 文件上传目录绝对路径
     */
    public String createUploadDir(MultipartFile file, String attach) {
        try {
            String basePath = "D:" + File.separator + "attachment";
            String uploadDir = basePath + File.separator + DateUtil.formatDate(new Date(), "yyyyMM") + File.separator;// 按月分目录存储
            File savePath = new File(uploadDir, attach);
            if (!savePath.exists()) {
                savePath.mkdirs();
            }
            file.transferTo(savePath);
            return uploadDir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 附件下载
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/fileDownload")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        FileInputStream fis = null;
        try {
            String filename = "aa.txt";
            fis = new java.io.FileInputStream("D://aa.txt");
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + filename.toString() + "\"");

            byte[] b = new byte[1024];
            int length = 0;
            while ((length = fis.read(b)) != -1) {
                os.write(b, 0, length);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
    }
}
