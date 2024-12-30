package com.consilium.excel.models.caseresponse;

import java.util.ArrayList;

public class CaseResponseSectionObject {

    protected String sectionName = "";


    protected ArrayList<CaseResponseDataObject> dataList = null;

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setDataList(ArrayList<CaseResponseDataObject> dataList) {
        this.dataList = dataList;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public String getDataAmount() {
        if (dataList == null || dataList.size() == 0) {
            return "--";
        }

        int size = 0;
        for (CaseResponseDataObject caseResponseDataObject : dataList) {
            if (!"".equals(caseResponseDataObject.getActionId())) {
                size++;
            }
        }

        return "共 " + size + " 件";
    }

    public String getDataAmount1() {
        if (dataList == null || dataList.size() == 0) {
            return "--";
        }


		int size = 0;
		for (CaseResponseDataObject caseResponseDataObject : dataList) {
			if (!"".equals(caseResponseDataObject.getActionId1())) {
				size++;
			}
		}

		return "共 " + size + " 件";
    }

    public ArrayList<CaseResponseDataObject> getDataList() {
        return this.dataList;
    }
}
