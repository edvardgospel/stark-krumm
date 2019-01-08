function monthChanged(month) {
     $.get("/starkkrumm/month?month="+month, function (months) {
             $("#commentField").html(months);
     })
}

$('#contact').submit(function (roadNumber,carNumber,driverName,departure,arrival,distance,departure) {
 $.post("/starkkrumm/send"+
             "?roadNumber="+roadNumber+
             "&carNumber="+carNumber+
             "&driverName="+driverName+
             "&departure="+departure+
             "&arrival="+arrival+
             "&distance="+distance+
             "&departure="+departure)
 return false;
});