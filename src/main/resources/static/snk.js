function saveRoad() {
    $.post("/starkkrumm/sendV2",
    {"roadNumber": $("#roadNumber").val(),
    "carNumber": $("#carNumber").val(),
    "driverName": $("#driverName").val(),
    "departure": $("#departure").val(),
    "arrival": $("#arrival").val(),
    "date": $("#date").val(),
    "distanceBig": $("#distanceBig").val(),
    "distanceSmall": $("#distanceSmall").val(),
    "consumption1": $("#consumption1").val(),
    "consumption2": $("#consumption2").val(),
    "consumption3": $("#consumption3").val()})
}

function dateCarNumberChanged(date, carNumber) {
    $.get("/starkkrumm/month", { "date": date, "carNumber": carNumber },
     function (roads) {
        $("#table").html(convertToHtmlTable(roads));
     });
}

function convertToHtmlTable(roads) {
    var table = "";
    for (var i in roads) {
        table += "<tr>"
               + "<td>" + roads[i].departure + "</td>"
               + "<td>" + roads[i].arrival + "</td>"
               + "<td>" + roads[i].roadNumber + "</td>"
               + "<td>" + roads[i].driverName + "</td>"
               + "<td>" + roads[i].distance + "</td>"
               + "<td>" + roads[i].consumption + "</td>"
               + "</tr>";
    }
    return table;
}

function uploadToDrive(date, carNumber) {
    $.get("/starkkrumm/print", { "date": date, "carNumber": carNumber });
}