package com.xpxp.service;

import com.xpxp.pojo.dto.PythonTaskDTO;

import java.io.File;

public interface PythonTaskService {
    void runPythonTask(PythonTaskDTO dto, File outputDir) throws Exception;
}
