function monthChanged(month) {
     $.get("/starkkrumm/month?month="+month, function (months) {
             $("#commentField").html(months);
     })
}