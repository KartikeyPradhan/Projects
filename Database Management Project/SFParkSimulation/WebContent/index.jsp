<!DOCTYPE html>
<html>
  <head>
  	<link rel="stylesheet" type="text/css" href="css/index.css"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>SF Park Routing</title>
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
      form {
    	display: inline;
    	padding: 0px;
    	spacing: 0px;
	}
    </style>
    <script src="js/jquery-1.11.2.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
    <script>


    	
		//for dragging origin and destinations
		var rendererOptions = {
		  draggable: true
		};
		
		//for direction display and routing
		var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);;
		var directionsService = new google.maps.DirectionsService();
		
		//declaring the map
		var map;
		
		//declaring the polyline
		var line;
		
		//initializing origin and destination (some default value)
		var start = new google.maps.LatLng(37.8075689000,-122.4214464000);
		var end = new google.maps.LatLng(37.8056520000,-122.4136170000);
		
		//setting the center of the map 
		var centerMap = new google.maps.LatLng(37.8054164152, -122.4236028968);
		
		//for building the polyline path
		var lineCoordinates = [start,end];
		
		//for clearing the interval
		var clrInterval;
		
		//start time
		var startTime;

		var isTerminate;
		
		var move = 0;
		
		var walkingTime = 0;
		
		//called when page loads
		function initialize() {

		  //for setting up map attributes
		  var mapOptions = {
			zoom: 20,
			center: centerMap
		  };
		  
		  //initializing the map
		  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);		  
		  
		  //listener called when car route changes
		  google.maps.event.addListener(directionsDisplay, 'directions_changed', function() {
			computeTotalDistance(directionsDisplay.getDirections());
		  });
		  
			  
		  //setting the route on map
		  directionsDisplay.setMap(map);
		  
		  //for displaying the route direction panel
		  directionsDisplay.setPanel(document.getElementById('directionsPanel'));
		  
		  //calculating the route
		  calcRoute(start,end);
		  
		}
		
		
		/*method for calculating the route*/
		function calcRoute(start,end) {
			var request = {
				origin: start,
				destination: end,
				travelMode: google.maps.TravelMode.DRIVING
			  };
			  directionsService.route(request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) { 
				  directionsDisplay.setDirections(response);
				}
			  });
		}
		
		/*method for updating the new route*/
		function calcNewRoute(start,end) {	
			var requestNew = {
				origin: start,
				destination: end,
				travelMode: google.maps.TravelMode.DRIVING
			  };
			directionsService.route(requestNew,function(response, status,statusCheck) {
			  if (status == google.maps.DirectionsStatus.OK) {
				  directionsDisplay.setDirections(response);
				  movementSimulator();
			  }
			});
		}

		
		/*method for calculating the distance and time*/
		function computeTotalDistance(result) {
		  //getting all the co-ordinates for building the polyline path 
		  lineCoordinates = getAllPoints(result);

		}
		
		function showRoute(){
			
			if(document.getElementById("inputStart").value == ""){
				
				alert("Please enter start location");
				return false;
			}
			
			if(document.getElementById("inputEnd").value == ""){
				alert("Please enter destination location");
				return false;
			}
			
			if(document.getElementById("selectAlgorithm").value == "Select Algorithm"){
				alert("Please select the algorithm");
				return false;
			}else{
				var algo = document.getElementById("selectAlgorithm").value;
				document.getElementById("algoType").value = algo;
			}
			
			if(document.getElementById("inputStart").value != ""){
				start = document.getElementById("inputStart").value;
			}
			if(document.getElementById("inputEnd").value != ""){
				end = document.getElementById("inputEnd").value;
			}
			calcRoute(start,end);
			
		}
		
		function initiateMovement(){
			if(document.getElementById("inputStart").value == ""){
				
				alert("Please enter start location");
				return false;
			}
			
			if(document.getElementById("inputEnd").value == ""){
				alert("Please enter destination location");
				return false;
			}
			
			if(document.getElementById("selectAlgorithm").value == "Select Algorithm"){
				alert("Please select the algorithm");
				return false;
			}else{
				var algo = document.getElementById("selectAlgorithm").value;
				document.getElementById("algoType").value = algo;
			}
			
			if(document.getElementById("inputStart").value != ""){
				start = document.getElementById("inputStart").value;
			}
			if(document.getElementById("inputEnd").value != ""){
				end = document.getElementById("inputEnd").value;
			}
			document.getElementById("getDest").value = end;
			document.getElementById("end").innerHTML = end
			calcRoute(start,end);
			startTime = performance.now();
			isTerminate = "CONTINUE";
			document.getElementById("start").innerHTML = start;
			movementSimulator();
		}

		
		/*to simulate movement*/
		function movementSimulator() {
		   
		
			
		   //initializing the polyline
		   line = new google.maps.Polyline({
				path: lineCoordinates,
				map: map,
				strokeColor: '#FF0000',
				strokeOpacity: 0.00001,
				strokeWeight: 0
		    });
			
			
			//for testing purpose only
			//var test = "(37.80543, -122.41527)";
			
			//for displaying markers at intervals to simulate movement
			clrInterval = window.setInterval(function() {	
				move = 0;
				if(move<line.getPath().getArray().length && isTerminate == "CONTINUE"){

					start = line.getPath().getArray()[move+1];
					if(line.getPath().getArray().length!=2 && isTerminate == "CONTINUE")
					{
						document.getElementById("getPoints").value = start;
						
						document.getElementById('invokeAlgo').click();
						isTerminate = "TERMINATE";
					}else{
						start = end;
						isTerminate = "TERMINATE";
						var endTime = performance.now();
						var timeElapsed = (endTime - startTime)/1000.0;
						timeElapsed = timeElapsed + parseInt(walkingTime);
						document.getElementById("totalTimeTaken").innerHTML = "Slot acquired in " + timeElapsed + " seconds";
					}

					calcRoute(start,end);
					move++;	
				}
			}, 3000);			
		}
		
		/*method for getting all the points on the polyline path*/
		function getAllPoints(result)
		{
			var route = result.routes[0];
			var points = new Array();
			var legs = route.legs;
			for (i = 0; i < legs.length; i++) {
				var steps = legs[i].steps;
				for (j = 0; j < steps.length; j++) {
					var nextSegment = steps[j].path;
					for (k = 0; k < nextSegment.length; k++) {
						points.push(nextSegment[k]);
					}
				}
			}
			return points;
		}
		
		
		/*for making an AJAX call*/
		$(document).ready(function(){
			$('#invokeAlgo').click(function(){
				$.ajax({
					url: 'InvokeAlgorithm',
					type: 'POST',
					datatype: 'json',
					data: {
						getPoints : $('#getPoints').val(),
						getDest : $('#getDest').val(),
						getAlgoType : $('#algoType').val(),
						getIteration : $('#iteration').val(),
		            },
					success: function(response){
						//setting the endpoint co-ordinate
						//alert("started");
						if(response != ""){
							end = response.split("|")[0];
							document.getElementById("routeTo").innerHTML = end;
							walkingTime = response.split("|")[1];
						}
							
						
						//alert("end--->"+end);
						
						
						
						//clearing the current excuting timer
						clearInterval(clrInterval);
						
						isTerminate = "CONTINUE";
						//routing the user to the new route
						calcNewRoute(start,end);

						
						
					}
				});
				return false
			});
			
			 $('.minibtn').click(function() {
				    // Show pop-up and disable background using #mask
				    $("#pop").fadeIn('slow');
				    $("#mask").fadeIn('slow');
				    // Load content.
				    $.post("contentPopup.html", function(data) {
				        $("#contentPop").html(data);
				    });
				    });
				       
				    // Hide pop-up and mask
				    $("#mask").hide();
				    $("#pop").hide();


				    // Size pop-up
				    var img_w = 500;
				    var img_h = 240;

				    // width and height in css.
				    $("#pop").css('width', img_w + 'px');
				    $("#pop").css('height', img_h + 'px');

				    // Get values ​​from the browser window
				    var w = $(this).width();
				    var h = $(this).height();

				    // Centers the popup  
				    w = (w / 2) - (img_w / 2);
				    h = (h / 2) - (img_h / 2);
				    $("#pop").css("left", w + "px");
				    $("#pop").css("top", h + "px");

				    // Function to close the pop-up
				    $("#close").click(function() {
				        $("#pop").fadeOut('slow');
				        $("#mask").fadeOut('slow');
				    });
			
		});
		
		
		
		google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <form method="post">
	  <body>
	  	<%System.out.println("On page"); %>
	    <div id="map-canvas" style="float:right;width:70%; height:100%"></div>
	    <div id="directionsPanel" style="float:left;width:30%;height: 100%">
			<!-- <p>Total Distance: <span id="total"></span></p><br>
			<p>Total Time: <span id="time"></span></p> -->
			<div style="padding: 10px;">
				<div id="text">Start Location</div>
				<input type="text" name="inputStart" id="inputStart" />
				<br />
				<br />
				<div id="text">End Location</div>
				<input type="text" name="inputEnd" id="inputEnd" />
				<br />
				<br />
				<!-- <div id="text">Select Algorithm</div> -->
				<select id = "selectAlgorithm">
					<option>Select Algorithm</option>
					<option>RT Distance Based Gravitational Force</option>
					<option>RT Cost Based Gravitational Force</option>
					<option>RT Greedy Algorithm</option>
				</select><br><br>
				<input type = "button" id= "routeDisplay" onclick="showRoute();" value = "Show Route" />
				<input type = "button" id= "Navigate" value="Navigate" onclick = "initiateMovement()">
				<input type = "button" class= "minibtn" onclick="show();" value = "Show Results" />
			</div>
	    </div>
		
		Time Taken: <span id = "timeTaken"></span><br>
		
		<!-- For testing purpose -->
		<!-- Points: <span id = "points" name="points"></span><br>
		<input type = "hidden" id="getPoints" name="getPoints" value="test"></input> -->
		
		<input type = "hidden" id="getPoints" name="getPoints" value="test"></input>
		<!-- <input type = "hidden" id="terminate" name="getPoints" value="CONTINUE"></input> -->
		<input type = "hidden" id="getDest" name="getDest" value="test"></input>
		<input type = "hidden" id="algoType" name="algoType" value="test"></input>
		<input type = "hidden" id="iteration" name="iteration" value="first"></input>
		
		
		<input type="submit" id="invokeAlgo" style="display: none;" value="test" onclick="InvokeAlgorithm"/> 
		<input type="submit" id="stopAlgo" style="display: none;" onclick = "TerminateAlgorithm"/>
		
		<!-- <input type="text" id="start" value = "test"/>
		<input type="text" id="end" value = "test"/><br><br -->
		
		
		<div id="pop">
            <div id="close">X</div>
            <div id="contentPop" align="left" style="padding: 20px;">
            
            	
            	<label id = "textstyle">Start Location:<label> 
				<span id="start"></span><br><br><br>
				End Location: 
				<span id="end"></span><br><br><br>
				Final Location: 
				<span id="routeTo"></span><br><br><br>
				<span id="totalTimeTaken"></span>
            
            </div>
        </div>

        <div id="mask">
            <div id="page-wrap">
            
				
            </div>
        </div>

 
  </body>
  </form>
</html>


