package com.xpxp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Python 算法调用任务，用于封装每一次执行的指令及输出目录
 */
@Slf4j
@AllArgsConstructor
public class PythonTask implements Runnable {

    private final String inputFilePath;     // 输入CSV文件路径
    private final String referenceDirPath;  // 身份证库文件夹路径
    private final String outputDirPath;     // 算法结果保存路径
    private final String exePath;           // 可执行的打包算法路径，如 ./delta_rt_matcher

    @Override
    public void run() {
        try {
            // 构造输出子目录（带UUID防重）
            String taskId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            File subOutput = new File(outputDirPath, "task_" + taskId);
            if (!subOutput.exists()) subOutput.mkdirs();

            // 构建命令：可根据你的算法参数进一步拓展
            String[] cmd = new String[]{
                    exePath,
                    "--input_file", inputFilePath,
                    "--directory", referenceDirPath,
                    "--output_dir", subOutput.getAbsolutePath()
            };

            log.info("执行命令：{}", String.join(" ", cmd));

            // 调用执行
            PythonExecutor.exec(cmd, subOutput);
        } catch (Exception e) {
            log.error("执行 Python 任务失败", e);
        }
    }
}
