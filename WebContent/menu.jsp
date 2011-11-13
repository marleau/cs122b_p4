<script type="text/javascript">
	function liveSearch(str) {
		var ajaxRequest;
	    if (str.length==0) { 
	        document.getElementById("liveSearch").innerHTML="";
	        return;
	    }
	    if (window.XMLHttpRequest) {/* code for IE7+, Firefox, Chrome, Opera, Safari*/
	        xmlhttp=new XMLHttpRequest();
	    }
	    else {/* code for IE6, IE5*/
	        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    xmlhttp.onreadystatechange=function() {
	        if (xmlhttp.readyState==4) {
	            document.getElementById("liveSearch").innerHTML=xmlhttp.responseText;
	        }
	    }
	    xmlhttp.open("GET","LiveSearch?arg="+str,true);
	    xmlhttp.send(null);
	}
	
	function showPopup(id) {
		document.getElementById(id).style.display="block";
	}
	
	function hidePopup(id) {
		document.getElementById(id).style.display="none";
	}
</script>
	
<div class="menu">
	<ul class="main">
		<li class="first"><a href="index.jsp" class="first">Fabflix</a></li>
		<li><a href="ListResults">Browse</a></li>
		<li>
			<FORM ACTION="ListResults" METHOD="GET">
				<INPUT TYPE="TEXT" NAME="arg" onkeyup="liveSearch(this.value);">
				<INPUT TYPE="HIDDEN" NAME=rpp VALUE="5">
				<input TYPE="SUBMIT" VALUE="Search Movies">
			</FORM>
		</li>
		<li><a href="AdvancedSearch">Advanced Search</a></li>
		<li><a href="cart">View Cart</a></li>
		<li><a href="checkout">Check out</a></li>
		<li><a href="logout">Logout</a></li>
	</ul>
</div>

<div id="liveSearch"></div>