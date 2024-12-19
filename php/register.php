<?php 
if(!empty($_POST['username']) && !empty($_POST['password'])) {
	$username = $_POST['username'];
    $password = hash("sha256", $_POST["password"]);
	$conn = mysqli_connect('localhost','root','','final');
	if($conn) {
		$sql = "INSERT INTO users (username, password) values ('".$username."', '".$password."')";
		if(mysqli_query($conn, $sql)){
			echo "Success";
		}
		else echo "Failed to insert data";
	} 
	else echo "Failed to connect to database";
}
else echo "Invalid data!";
?>