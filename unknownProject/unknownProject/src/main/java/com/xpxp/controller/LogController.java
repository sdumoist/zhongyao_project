package com.xpxp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/logs")
public class LogController {

    /**
     * 访问日志页面 HTML
     * 例：/logs/20250729 → 显示 log.html 并加载对应 taskId 日志
     */
    @GetMapping("/{taskId}")
    public String logPage(@PathVariable String taskId, Model model) {
        model.addAttribute("taskId", taskId);  // 可在前端模板中使用
        return "log";  // 返回 log.html 页面（位于 templates/log.html）
    }

    /**
     * 获取纯文本日志内容
     * 例：/logs/20250729/text → 返回 log.txt 内容
     */
    @GetMapping("/{taskId}/text")
    @ResponseBody
    public String getLogText(@PathVariable String taskId) throws IOException {
        Path logPath = Paths.get("output/task_" + taskId, "log.txt");
        if (Files.exists(logPath)) {
            return Files.readString(logPath);
        } else {
            return "⚠️ 未找到日志文件: " + logPath.toString();
        }
    }
}
