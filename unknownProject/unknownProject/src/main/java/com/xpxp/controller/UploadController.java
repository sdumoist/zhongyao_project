package com.xpxp.controller;

import com.xpxp.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping
    public Result<?> uploadAndRun(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 构建带时间戳的临时目录 uploads/run_20250729_213300
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path targetDir = Paths.get("uploads", "run_" + timestamp);
            Files.createDirectories(targetDir);

            // 2. 保存上传的 CSV 文件
            Path inputFile = targetDir.resolve(file.getOriginalFilename());
            file.transferTo(inputFile.toFile());

            // 3. 构造命令：执行 delta_rt_matcher（注意确保它有执行权限）
            String cmd = String.format("/Users/moist/Desktop/work/中药/中药交接/2025_3_8展示/算法一_TopN/dist/delta_rt_matcher --input_file %s", inputFile.toAbsolutePath());

            // 4. 执行命令（阻塞）
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return Result.error("❌ 算法执行失败，请检查 delta_rt_matcher 是否正常工作。");
            }

            // 5. 搜索输出结果文件（假设输出在 match_results_时间戳 目录中）
            Path parentDir = targetDir.getParent();  // uploads/
            Optional<Path> resultCsv = Files.walk(parentDir, 2)
                    .filter(path -> path.getFileName().toString().equals("top_match_results.csv"))
                    .findFirst();

            if (resultCsv.isEmpty()) {
                return Result.error("⚠️ 算法执行成功，但未找到结果文件 top_match_results.csv");
            }

            // 6. 解析 CSV 文件，转为 List<Map<String, String>>
            List<Map<String, String>> parsed = parseCsvToList(resultCsv.get());

            // 7. 返回 JSON 结构
            return Result.success(parsed);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("⚠️ 系统异常：" + e.getMessage());
        }
    }

    // 工具方法：CSV -> List<Map<String, String>>
    private List<Map<String, String>> parseCsvToList(Path csvPath) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String headerLine = reader.readLine();
            if (headerLine == null) return records;

            String[] headers = headerLine.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    map.put(headers[i].trim(), values[i].trim());
                }
                records.add(map);
            }
        }
        return records;
    }
}
