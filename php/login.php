<?php 
if(!empty($_POST['username']) && !empty($_POST['password'])) {
	$username = $_POST['username'];
    $password = hash("sha256", $_POST["password"]);
	$conn = mysqli_connect('localhost','root','','final');
	if($conn) {
		$sql = "SELECT * FROM users WHERE username = '".$username."' AND password = '".$password."'";
		if(mysqli_query($conn, $sql)->num_rows > 0){
			echo "Success";
		}
		else echo "Failed to login data";
	} 
	else echo "Failed to connect to database";
}
else echo "Invalid data!";
?>