var answers = [3,4,5,7,2]
var token
var course_Id
var question_Id
var correct

$(document).ready(function(){
    $.ajax({
    url: '/user/auth',
    type: 'GET',
    async: false,
    success: function(stuff) {
        token = stuff.data.token;
        $.ajax({
            url: '/api/question/answer?access_token='+token+'&course_id='+course_Id+'&question_id='+question_Id,
            type: 'GET',
            async: false,
            success: function(stuff) {

                //answers = stuff.answers,
                    correct = stuff.correct

                var chart = AmCharts.makeChart("chartdiv",
                    {

                        "type": "serial",
                        "categoryField": "category",
                        "columnSpacing3D": 9,
                        "columnWidth": 0.34,
                        "autoMarginOffset": 40,
                        "marginRight": 60,
                        "marginTop": 60,
                        "plotAreaFillColors": "#000000",
                        "startDuration": 1.5,
                        "startEffect": "easeOutSine",
                        "backgroundColor": "transparent",
                        "borderColor": "#D4D4D4",
                        //"color": "#FFFFFF",
                        "color": "black",
                        "creditsPosition": "bottom-right",
                        "fontSize": 13,
                        "theme": "black",
                        "showLegend": true,
                        "categoryAxis": {
                            "gridPosition": "start",
                            //"axisColor": "#FF0000",
                            "axisColor": "transparent",
                            "boldLabels": true,
                            "color": "gray",
                            //"fontSize": 70,
                            "fontSize": 20,
                            //"inside": true,
                            "bottom": true,

                            "labelOffset": 0,
                            "minHorizontalGap": 50,
                            "minorGridAlpha": 0,
                            "title": "",
                            "titleColor": "#008000",
                            //"titleFontSize": 36,
                            "titleFontSize": 36,
                            "titleRotation": 0
                        },
                        "trendLines": [],
                        "legend": {
                             "color": "black",
                            "accessibleLabel": "Correct Answer",
                            "switchable": false,
                        },
                        "graphs": [
                            {
                                "colorField": "color",
                                "showBalloon": false,
                                "fillAlphas": 10,
                                "fillColors": "#008000",
                                "lineAlpha": 0,
                                "gapPeriod": 0,
                                "legendColor": "#008000",
                                "id": "AmGraph-1",
                                //"labelText": parseFloat("[[value]]") + "",
                                "labelText": "[[percents]]%",
                                "labelFunction": function(item) {

                                    var total = 0;
                                    for (var i = 0; i < chart.dataProvider.length; i++) {
                                        total += chart.dataProvider[i][item.graph.valueField];
                                    }

                                    var percent = Math.round(( item.values.value / total ) * 1000) / 10;
                                    if(percent == 0 || isNaN(percent)){
                                        return "";
                                    }else {
                                        return percent + "%";
                                    }
                                },

                                "labelPosition": "top",
                                "color":"black",
                                "title": "Correct Answers",
                                "type": "column",
                                "valueField": "column-1",
                                "showAllValueLabels": true

                            },

                        ],
                        "guides": [],
                        "valueAxes": [
                            {
                                "id": "ValueAxis-1",
                                "title": "Number Of Students",
                                "titleFontSize": 21,
                                "integersOnly": true,
                                "axisColor": "black",
                                "minimum":0
                            }
                        ],
                        "allLabels": [],
                        "titles": [
                            {
                                "color": "#008000",
                                "id": "Title-1",
                                "size": 30,
                                "text": "RESULTS"
                            }
                        ],
                        "dataProvider": [

                        ]
                    })//End amchart

//Javascript to manipulate data
                 var chartData = chart.dataProvider = [];
                    for (var i = 0; i <= 4; i++) {
                        if(i == 0){
                            if(correct[i] == false){
                                chartData[i] = {
                                    "category":"A",
                                    "column-1": answers[i],
                                    "color": "#cc0000"
                                };
                            }else{
                                chartData[i] = {
                                    "category":"A",
                                    "column-1": answers[i],
                                };
                            }

                        }else if(i == 1){
                            if(correct[i] == false){
                                chartData[i] = {
                                    "category":"B",
                                    "column-1": answers[i],
                                    "color": "#cc0000"
                                };
                            }else{
                                chartData[i] = {
                                    "category":"B",
                                    "column-1": answers[i],
                                };
                            }
                        }else if(i == 2){
                            if(correct[i] == false){
                                chartData[i] = {
                                    "category":"C",
                                    "column-1": answers[i],
                                    "color": "#cc0000"
                                };
                            }else{
                                chartData[i] = {
                                    "category":"C",
                                    "column-1": answers[i],
                                };
                            }
                        }else if(i == 3){
                            if(correct[i] == false){
                                chartData[i] = {
                                    "category":"D",
                                    "column-1": answers[i],
                                    "color": "#cc0000"
                                };
                            }else{
                                chartData[i] = {
                                    "category":"D",
                                    "column-1": answers[i],
                                };
                            }
                        }
                        else if(i == 4){
                            if(correct[i] == false){
                                chartData[i] = {
                                    "category":"E",
                                    "column-1": answers[i],
                                    "color": "#cc0000"
                                };
                            }else{
                                chartData[i] = {
                                    "category":"E",
                                    "column-1": answers[i],
                                };
                            }
                        }

                    }
                    chart.validateData();


            }
        })//end answers
    }
    })
    });

function prepareQuestionId(cId, qId) {
    course_Id = cId;
    question_Id = qId;
};



