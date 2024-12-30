package com.consilium.service;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "QATOPVIEW")
public class QATopView {


    @JacksonXmlProperty(localName = "QAITEM")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<QAItem> items = new ArrayList<>();


    public List<QAItem> getItems() {
        return items;
    }

    public void setItems(List<QAItem> items) {
        this.items = items;
    }
}
