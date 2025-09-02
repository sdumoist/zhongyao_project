package com.xpxp.service.impl;

import com.xpxp.pojo.dto.PythonTaskDTO;
import com.xpxp.service.PythonTaskService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PythonTaskServiceImpl implements PythonTaskService {

    @Override
    public void runPythonTask(PythonTaskDTO dto, File outputDir) throws Exception {
        String inputPath = dto.getInputPath();  // 需确保 DTO 中有这个字段
        File logFile = new File(outputDir, "task.log");

        String cmd = String.format("./delta_rt_matcher --input_file %s > %s 2>&1",
                new File(inputPath).getAbsolutePath(),
                logFile.getAbsolutePath());

        Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Python 脚本执行失败，请查看日志：" + logFile.getAbsolutePath());
        }
    }
}
