var answers = [3,4,5,7,2]
var token

$(document).ready(function(){
$.ajax({
    url: '/user/auth',
    type: 'GET',
    async: false,
    success: function(stuff) {
        token = stuff.data.token;
        var course_Id = $(this).data('course-id');
        $.ajax({
            url: '/api/question/active',
            type: 'GET',
            data: {
                access_token: token,
                course_id: course_Id
            },
            success: function(){
                 $.ajax({
            url: '/api/question/answer',
            data: {
                access_token: token,
                question_id: data.id
            },
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
					"backgroundColor": "#D4D4D4",
					"borderColor": "#D4D4D4",
					"color": "#FFFFFF",
					"creditsPosition": "bottom-right",
					"fontSize": 13,
					"theme": "black",
					"categoryAxis": {
						"gridPosition": "start",
						"axisColor": "#FF0000",
						"boldLabels": true,
						"color": "#FFFFFF",
						"fontSize": 70,
						"inside": true,
						"labelOffset": 4,
						"minHorizontalGap": 50,
						"minorGridAlpha": 0,
						"title": "",
						"titleColor": "#008000",
						"titleFontSize": 36,
						"titleRotation": 0
					},
					"trendLines": [],
					"graphs": [
						{
							"balloonText": "[[title]] of [[category]]:[[value]]",
							"fillAlphas": 1,
							"gapPeriod": 0,
							"id": "AmGraph-1",
							"labelText": "",
							"title": "graph 1",
							"type": "column",
							"valueField": "column-1"
						}
					],
					"guides": [],
					"valueAxes": [
						{
							"id": "ValueAxis-1",
							"title": "Number Of Students",
							"titleFontSize": 21
						}
					],
					"allLabels": [],
					"balloon": {},
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
				}
			)

            },
            error: function(err) {
                console.log(err)
            }
        });
    
            }
        });   
    },
    error: function(err) {
        console.log(err)
    }
});
});
