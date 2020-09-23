<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Welcome</title>
</head>
<body>
<?php
  if ($_SERVER['REQUEST_METHOD']=='POST') {

	$fName = $_POST['firstName'];
	$lName = $_POST['lastName'];
	$email = $_POST['email'];
	$date = Date("Y-m-d");
	
	//adding salt to the plain password
    $plain_password = $_POST['password']."-mywant";
	
	//hashing the salted plain text password
	$password = hash('sha256', $plain_password);
	

    require_once 'connect.php';
	$sql = "SELECT * FROM customer WHERE Email='$email'";
	
	$validation = mysqli_query($conn, $sql);
	
	//check to see if Email alreay exists
	if (mysqli_num_rows($validation) === 1 ) {
		?> <p> <?php echo "This Email address is already taken" ?></p><?php
	}else{
	
	$insertsql = "INSERT INTO customer VALUES('$email','$password','$fName','$lName','$date');";
	$result = mysqli_query($conn, $insertsql);
	mysqli_close($conn);
	
	?> <p> <?php echo "Account Registered Successfully!" ?> </p> <?php
	
	}
  
  }
  
  
?>
</body>
</html>