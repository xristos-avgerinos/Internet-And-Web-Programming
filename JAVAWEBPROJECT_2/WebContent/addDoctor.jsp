<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Doctor</title>
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
<%
response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("userid")==null){
	response.sendRedirect("adminLogin.html");
}
%>

<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100">
				<div class="login100-form-title" style="background-image: url(images/signup.jpg); height: auto; width:auto;background-size: 700px 200px;">
					<span class="login100-form-title-1">
						Add Doctor
					</span>
				</div>

				<form class="login100-form validate-form" action='AdminServlet' name="frmeditDoctor" method="post" >
					<div class="wrap-input100 validate-input m-b-26">
						<span class="label-input100">Username</span>
						<input class="input100" type="text" name="username" placeholder="Enter username" required>
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-18">
						<span class="label-input100">Password</span>
						<input class="input100" type="password" name="pass" placeholder="Enter password" required>
						<span class="focus-input100"></span>
					</div>
					<div class="wrap-input100 validate-input m-b-18">
						<span class="label-input100">First name</span>
						<input class="input100" type="text" name="firstname" placeholder="Enter First name" required>
						<span class="focus-input100"></span>
					</div>
					<div class="wrap-input100 validate-input m-b-18">
						<span class="label-input100">Last name</span>
						<input class="input100" type="text" name="lastname" placeholder="Enter Last name" required>
						<span class="focus-input100"></span>
					</div>
					<div class="wrap-input100 validate-input m-b-18">
						<span class="label-input100">AMKA</span>
						<input class="input100" type="text" name="AMKA" placeholder="Enter AMKA" required maxlength="11">
						<span class="focus-input100"></span>
					</div>
					  
					<div class="wrap-input100_2  m-b-18">
					  
						<span class="label-input100"><br>Specialty</span>
						<br>
 						 <select name="specialty" >
     					 <option >Pathologist</option>
    					 <option >Ophthalmologist</option>
      					 <option >Orthopedic</option>
    					 </select>
						<span class="focus-input100"></span>
					</div>
					<br><br><br>
					<div class="container-login100-form-btn ">

						<button class="login100-form-btn" name="insert">
							Add Doctor
						</button>
					</div>
					
					<br><br><br>
				</form>
				
			</div>
		</div>
	</div>
</body>
</html>