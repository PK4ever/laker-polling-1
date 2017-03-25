var courses =
[
    {
        "crn": "123456",
        "name": "test0",
        "students": 5
    },
    {
        "crn": "654321",
        "name": "test1",
        "students": 7
    },
    {
        "crn": "246810",
        "name": "test2",
        "students": 9
    }
];

$(function () {
    //courses = GET.allCourses
    $('#courseTable').bootstrapTable({
        data: courses
    });
});

function identifierFormatter(value, row, index) {
    return [
            '<a href="#" >',
                value,
            '</a>'].join('');
}