package com.xpxp.controller;

import com.xpxp.pojo.dto.PythonTaskDTO;
import com.xpxp.result.Result;
import com.xpxp.service.PythonTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/python")
public class PythonTaskController {

    @Autowired
    private PythonTaskService pythonTaskService;

    /**
     * 发起一个新的 Python 脚本任务
     * @param dto 请求参数（包含输入路径）
     * @return 执行结果
     */
    @PostMapping("/run")
    public Result<String> runTask(@RequestBody PythonTaskDTO dto) {
        try {
            // 构造时间戳任务目录
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputDir = new File("output/task_" + timestamp);
            outputDir.mkdirs();

            // 调用 service 进行实际任务处理
            pythonTaskService.runPythonTask(dto, outputDir);

            return Result.success("✅ 任务执行成功，日志路径：" + outputDir.getAbsolutePath());
        } catch (Exception e) {
            return Result.error("❌ 执行失败：" + e.getMessage());
        }
    }
}
