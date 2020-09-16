<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

  
	$email = $_POST['email'];
	
	//adding salt to the plain password
    $plain_password = $_POST['password']."-mywant";
	
	//hashing the salted plain text password
	$password = hash('sha256', $plain_password);

    require_once 'connect.php';

    $sql = "SELECT * FROM customer WHERE Email='$email'";

    $response = mysqli_query($conn, $sql);
	
    $result = array();
    $result['login'] = array();
    
    if (mysqli_num_rows($response) === 1 ) {
        
        $row = mysqli_fetch_assoc($response);

        if($password == $row['Password'])  {
            
            $index['email'] = $row['Email'];

            array_push($result['login'], $index);

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

            mysqli_close($conn);

        } else {

            $result['success'] = "0";
            $result['message'] = "Incorrect Password";
            echo json_encode($result);

            mysqli_close($conn);

        }

    }else{
		$result['success'] = "2";
		$result['message'] = "Account not found";
		echo json_encode($result);
		
		mysqli_close($conn);
	}

}

?>