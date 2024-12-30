package com.consilium.web.controller;


import com.consilium.service.ApiService;
import com.consilium.service.QAItem;
import com.consilium.service.QATopView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/Api")
public class ApiController {
    private static final Logger logger = Logger.getLogger(ApiController.class);

    public static class UpperStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        public UpperStrategy() {
        }

        public String translate(String input) {
            return input.toUpperCase();
        }
    }

    public static final PropertyNamingStrategy UPPER = new UpperStrategy();

    @Autowired
    ApiService apiService;

    @RequestMapping(value = "/QaAllView", method = RequestMethod.GET)
    public ResponseEntity<byte[]> qaAllView(HttpServletResponse response) throws Exception {
        try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
            List<QAItem> items = apiService.findQaAllView();
            QATopView QATopView = new QATopView();
            QATopView.setItems(items);

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.setPropertyNamingStrategy(UPPER);

            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(bs, QATopView);
            //String xml = xmlMapper.writeValueAsString(QATopView);
            String xml = bs.toString(StandardCharsets.UTF_8.name());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", "QaAllView.xml"))
                    .body(xml.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }


    @RequestMapping(value = "/QaTopView", method = RequestMethod.GET)
    public ResponseEntity<byte[]> QATopView(HttpServletResponse response) throws Exception {
        try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {

            List<QAItem> items = apiService.findQaAllView().stream().limit(20).collect(Collectors.toList());

            QATopView QATopView = new QATopView();
            QATopView.setItems(items);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.setPropertyNamingStrategy(UPPER);

            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(bs, QATopView);

            //String newXml =  new String(xml.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
            String xml = bs.toString(StandardCharsets.UTF_8.name());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", "QaTopView.xml"))
                    .body(xml.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

}
