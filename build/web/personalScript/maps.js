/*
 
 var map = new google.maps.Map(document.getElementById("gmaps-canvas"), myOptions);
 var marker = new google.maps.Marker({
 position: new google.maps.LatLng(coord.vicini[0].lat, coord.vicini[0].lng),
 map: map
 });
 
 var infoWindow = new google.maps.InfoWindow();
 google.maps.event.addListener(marker, 'click', function () {
 infoWindow.setContent(coord.vicini[0].name);
 infoWindow.open(map, marker);
 });
 });
 }*/

function initMap(source) {
    $.get(source, function (data) {
        var coord = JSON.parse(data);
        var myLatLng = new google.maps.LatLng(coord.myLat, coord.myLng);

        var map = new google.maps.Map(document.getElementById('gmaps-canvas'), {
            zoom: 14,
            center: myLatLng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });
        
        for (i in coord.vicini) {
            var infowindow = new google.maps.InfoWindow({
                content: coord.vicini[i].name
            });
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(coord.vicini[i].lat, coord.vicini[i].lng),
                map: map
            });
            marker.addListener(marker,'click', (function (marker,infowindow) {
                infowindow.open(map, marker);
            })(marker,infowindow));
        }
    });

}
exports.initMap = initMap;
/*
 * function initialize() {
 
 
 var myOptions = {
 zoom: 14,
 center: myLatLng,
 mapTypeId: google.maps.MapTypeId.ROADMAP
 };
 
 var map = new google.maps.Map(document.getElementById("gmaps-canvas"), myOptions);
 
 <c:forEach items="${ristorante.getVicini()}" var="rist">
 
 var marker<c:out value="${rist.getId()}"/> = new google.maps.Marker({
 position: new google.maps.LatLng(<c:out value="${rist.getLuogo().getLat()}"/>, <c:out value="${rist.getLuogo().getLng()}"/>),
 map: map,
 title: "<c:out value="${rist.getName()}"/>"
 });
 
 var contentString<c:out value="${rist.getId()}"/> = '<c:out value="${rist.getName()}"/>';
 var infoWindow<c:out value="${rist.getId()}"/> = new google.maps.InfoWindow({
 content: contentString<c:out value="${rist.getId()}"/>
 });
 
 google.maps.event.addListener(marker<c:out value="${rist.getId()}"/>, 'click', function () {
 infoWindow<c:out value="${rist.getId()}"/>.open(map, marker<c:out value="${rist.getId()}"/>);
 });
 
 </c:forEach>
 
 }
 
 for (i = 0; i < coord.vicini.lenght; i++) {
 marker = new google.maps.Marker({
 position: new google.maps.LatLng(coord.vicini[i].lat, coord.vicini[i].lng),
 map: map,
 });
 google.maps.event.addListener(marker, 'click', (function (marker, i) {
 return function () {
 infowindow.setContent(coord.vicini[i].name);
 infowindow.open(map, marker);
 }
 })(marker, i));
 }
 */

