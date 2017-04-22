var answers = [3,4,5,7,2]
var token
var course_Id
var question_Id

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
                console.log(stuff)
                answers = stuff.answers


                AmCharts.makeChart("chartdiv",
                    {
                        "type": "serial",
                        "categoryField": "category",
                        "columnSpacing3D": 9,
                        "columnWidth": 0.34,
                        "autoMarginOffset": 40,
                        "marginRight": 60,
                        "marginTop": 60,
                        "plotAreaFillColors": "#000000",
                        "startDuration": 1,
                        "startEffect": "easeOutSine",
                        "backgroundColor": "transparent",
                        "borderColor": "#D4D4D4",
                        //"color": "#FFFFFF",
                        "color": "black",
                        "creditsPosition": "bottom-right",
                        "fontSize": 13,
                        "theme": "black",
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

                        "graphs": [
                            {
                                "balloonText": "[[category]]:<br>[[percents]]%",
                                "fillAlphas": 10,
                                "fillColors": "#fed136" ,
                                "gapPeriod": 0,
                                "id": "AmGraph-1",
                                //"labelText": parseFloat("[[value]]") + "",
                                "labelText": "[[percents]]%",

                                "labelPosition": "top",
                                "color":"black",
                                "title": "graph 1",
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
                            {
                                "category": "A",
                                "column-1": answers[0]
                            },
                            {
                                "category": "B",
                                "column-1": answers[1]
                            },
                            {
                                "category": "C",
                                "column-1": answers[2]
                            },
                            {
                                "category": "D",
                                "column-1": answers[3]
                            },
                            {
                                "category": "E",
                                "column-1": answers[4]
                            }
                        ]
                    })//End amchart
            }
        })//end answers
    }
    })
    });

function prepareQuestionId(cId, qId) {
    course_Id = cId;
    question_Id = qId;
};



