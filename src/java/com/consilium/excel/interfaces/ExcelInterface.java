package com.consilium.excel.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ExcelInterface {


    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception;
}
