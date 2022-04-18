package top.cafebabe.undo.file.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.cafebabe.fileManager.manger.TempFileManager;
import top.cafebabe.fileManager.manger.integrator.Integrator;
import top.cafebabe.undo.common.bean.LoginUser;
import top.cafebabe.undo.common.bean.ResponseMessage;
import top.cafebabe.undo.common.util.MessageUtil;
import top.cafebabe.undo.file.bean.AppConfig;
import top.cafebabe.undo.file.bean.UserFile;
import top.cafebabe.undo.file.service.UserFileSer;
import top.cafebabe.undo.file.util.DownIdUtil;
import top.cafebabe.undo.file.util.FileIdUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author cafababe
 */
@RestController
public class GetCtrl {

    final TempFileManager tempFileManager;
    final UserFileSer userFileSer;

    public GetCtrl(TempFileManager tempFileManager, UserFileSer userFileSer) {
        this.tempFileManager = tempFileManager;
        this.userFileSer = userFileSer;
    }

    @GetMapping("/get.cors/{id}")
    public ResponseMessage get(@PathVariable String id, HttpServletResponse response, HttpSession session) {
        try {
            return this.doGet(id, response, session, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageUtil.error("获取文件失败");
    }

    @GetMapping("/down.tokCors/{fileId}")
    public ResponseMessage down(@PathVariable String fileId, HttpSession session) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttribute(AppConfig.LOGIN_USER_KEY_IN_SESSION);
        UserFile file = userFileSer.getFile(fileId);
        if (file == null)
            return MessageUtil.fail("文件不存在");

        if (file.isPrivate() && (loginUser == null || loginUser.getId() != file.getUserId()))
            return MessageUtil.permissionDenied();

        return MessageUtil.ok(DownIdUtil.createId(fileId));
    }

    @GetMapping("/download/*")
    public ResponseMessage download(@RequestParam String downId, HttpServletResponse response, HttpSession session) throws Exception {
        if (!DownIdUtil.check(downId))
            return MessageUtil.permissionDenied();

        return doGet(DownIdUtil.getFileId(downId), response, session, true);
    }

    private ResponseMessage doGet(String fileId, HttpServletResponse response, HttpSession session, boolean isDown) throws IOException, InterruptedException {
        UserFile file = userFileSer.getFile(fileId);
        if (file == null)
            return MessageUtil.fail("文件不存在");

        // 判断是不是下载
        if (isDown) { // 下载直接下载，不验证信息
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8));
        } else { // 预览就要验证信息
            LoginUser loginUser = (LoginUser) session.getAttribute(AppConfig.LOGIN_USER_KEY_IN_SESSION);
            if (file.isPrivate() && (loginUser == null || loginUser.getId() != file.getUserId()))
                return MessageUtil.permissionDenied();
        }

        response.reset();
        Integrator integrator = tempFileManager.get(FileIdUtil.getMd5(fileId));
        ServletOutputStream outputStream = response.getOutputStream();
        while (integrator.hasNext()) {
            Thread.sleep(500);
            outputStream.write(integrator.next());
        }
        outputStream.close();

        return MessageUtil.ok("");
    }
}
