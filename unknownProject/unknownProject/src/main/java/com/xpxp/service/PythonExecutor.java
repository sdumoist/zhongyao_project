package com.xpxp.service;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 执行本地打包后的 Python 算法，并实时获取输出
 */
@Slf4j
public class PythonExecutor {

    /**
     * 执行 Python 命令并实时记录输出日志
     * @param command 命令行数组，例如 {"./delta_rt_matcher", "--input_file", "..."}
     * @param outputDir 输出目录，用于存放 log.txt
     * @throws IOException 执行过程中发生错误
     */
    public static void exec(String[] command, File outputDir) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(outputDir); // 设置工作目录
        pb.redirectErrorStream(true); // 合并 stderr 和 stdout

        Process process = null;

        File logFile = new File(outputDir, "log.txt");
        try (
                BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile))
        ) {
            process = pb.start();

            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[PY] {}", line);
                    logWriter.write(line);
                    logWriter.newLine();
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Python 脚本运行异常，退出码: {}", exitCode);
                logWriter.write("Python 执行失败，退出码: " + exitCode);
                logWriter.newLine();
                throw new IOException("Python 脚本执行失败，exit code = " + exitCode);
            } else {
                log.info("Python 脚本执行成功");
                logWriter.write("Python 脚本执行成功");
                logWriter.newLine();
            }

        } catch (Exception e) {
            log.error("Python 执行异常", e);
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true))) {
                logWriter.write("异常信息: " + e.getMessage());
                logWriter.newLine();
            }
            throw new IOException("执行 Python 过程出错", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
