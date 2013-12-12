<!DOCTYPE HTML>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="style.css">
	<script language="javascript" type="text/javascript" src="flot/jquery.js"></script>
	<script language="javascript" type="text/javascript" src="flot/jquery.flot.js"></script>
	<script type="text/javascript">

	<!--Load data from the database-->
	var data_array = [];
	var data_array_formated = []; 
	<?php
	require 'constants.php';
	$con=mysqli_connect(dbhost,dbuser,dbpassword,dbname,dbport);
	$result = mysqli_query($con,"SELECT * FROM datapoint ORDER BY time ASC");
	while($row = mysqli_fetch_array($result))
	{
		echo "data_array.push(['".$row['time']."','".$row['val']."']);";
		echo "var d = new Date(0);"; 
		echo "d.setUTCSeconds(".$row['time'].");";
		echo "data_array_formated.push([d,'".$row['val']."']);";
	}
	if (mysqli_connect_errno())
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	?>

	<!--Initial setup of the document-->
	$(function() {
		<!--Set start date and end date-->
		setData(1);
		var stardate = document.getElementById('stardate'); 
		var enddate = document.getElementById('enddate'); 
		stardate.innerHTML += data_array_formated[0][0]; 
		enddate.innerHTML += data_array_formated[data_array.length-1][0]; 

		<!--Create a table report of the data-->
		var theTable = document.createElement('table');
		var tr, td; 
		var header = theTable.createTHead();
		tr = header.insertRow(0);  
		var h1 = document.createElement('th');
		h1.innerHTML = "Time";   
		tr.appendChild(h1);
		var h2 = document.createElement('th');
		h2.innerHTML = "Value";   
		tr.appendChild(h2);
		for (var i = 0; i < data_array_formated.length; i++) {
			tr = document.createElement('tr');
			td = document.createElement('td');
			td.innerHTML = data_array_formated[i][0];
			tr.appendChild(td);
			td = document.createElement('td');
			td.innerHTML = data_array_formated[i][1];
			tr.appendChild(td);
			theTable.appendChild(tr);
		}
		document.getElementById('dataTable').appendChild(theTable);
	});

<!--Function that changes the interval on the data x-axis-->
function setData(interval)
{
	var data = []; 
	var starttime = data_array[0][0]; 
	for(var i=0; i<data_array.length; i++){
		var point = [data_array[i][0], data_array[i][1]];  
		point[0] = (point[0] - starttime)/parseInt(interval); 
		data.push(point); 
	}
		<!--Code from: https://github.com/flot -->
		$.plot("#placeholder", [ data ]);
		$("#footer").prepend("Flot " + $.plot.version + " &ndash; ");
	}
	</script>
</head>



<body>
	<h1>Data diplay app</h1>
	<p>The graph below shows time vs. data value</p>
	<!--Placeholder: For displaying the graph-->
	<div id="placeholder"></div>
	<div id="stardate" style="float: left">Start date: </br></div>
	<div id="enddate"style="float: right">End date: </br></div>

	<h2>Choose an interval below</h2>
	<button onclick="setData('1')">Second</button>
	<button onclick="setData('60')">Minute</button>
	<button onclick="setData('3600')">Hour</button>
	<button onclick="setData('86400')">Day</button>

	<h2>Table report</h2>
	<table id="dataTable" class="gridtable" style="display: inline-block"></table>
</body>
</html>