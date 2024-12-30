function getAllUser(){
        var response;
         var $ajax =$.ajax({
                                        method: 'get',
                                        url: ctx+'/Users',
                                        contentType: 'application/json',
                                        async: false,
                                        success: function (res) {
                                          response =res
                                        }, error: function (err) {
                                            console.log(err)
                                        }
                                    });
            return response


}



function getUser(account)
{
         var response;
         var $ajax =$.ajax({
                                        method: 'get',
                                        url: ctx+'/Users/' + account,
                                        contentType: 'application/json',
                                        async: false,
                                        success: function (res) {
                                          response =res
                                        }, error: function (err) {
                                            console.log(err)
                                        }
                                    });
            return response
}



function getUnits(){
     var response;
     var $ajax =$.ajax({
                                    method: 'get',
                                    url: ctx+'/Units/findAll',
                                    contentType: 'application/json',
                                    async: false,
                                    success: function (res) {
                                      response =res
                                    }, error: function (err) {
                                        console.log(err)
                                    }
                                });
        return response
}



function getSection(){

        var unitsArray = getUnits();
        var empty ={value:"All",text:"全部"}
        var units = [empty].concat(unitsArray.filter(u=> u.superUnit == null).map(u=> {return { superUnit:u.superUnit,unitCode:u.unitCode,value:u.unitCode,text:u.unitName  }  }))
        var sections = [empty].concat(unitsArray.filter(u=> u.superUnit != null).map(u=> {return { superUnit:u.superUnit,unitCode:u.unitCode,value:u.unitCode,text:u.unitName  }  }))
        return {"units":units,"sections":sections}
}

function getSectionName(sectionId){
    try{
        return sections.find(s=>s.unitCode==sectionId).text
    }catch(e){
        try{
            if(sectionId == unit1996Data.unitCode) return unit1996Data.name
        }catch(e){
          return ''
        }
    }
}

function getUnitName(unitId){
    try{
        return units.find(s=>s.unitCode==unitId).text
    }catch(e){
        try{
            if(unitId == unit1996Data.unitCode) return unit1996Data.name
        }catch(e){
                 return ''
        }
    }
}


function getAppCode(category){
     var response;
     var $ajax =$.ajax({
                                    method: 'get',
                                    url: ctx+'/AppCode/findAllByCategory/' + category,
                                    contentType: 'application/json',
                                    async: false,
                                    success: function (res) {
                                      response =res
                                    }, error: function (err) {
                                        console.log(err)
                                    }
                                });
        return response
}

function getServiceType(){
     var response;
     var $ajax =$.ajax({
                                    method: 'get',
                                    url: ctx+'/ServiceType',
                                    contentType: 'application/json',
                                    async: false,
                                    success: function (res) {
                                      response =res
                                    }, error: function (err) {
                                        console.log(err)
                                    }
                                });
        return response
}


function getServiceItem(){
     var response;
     var $ajax =$.ajax({
                                    method: 'get',
                                    url: ctx+'/ServiceItem',
                                    contentType: 'application/json',
                                    async: false,
                                    success: function (res) {
                                      response =res
                                    }, error: function (err) {
                                        console.log(err)
                                    }
                                });
        return response
}