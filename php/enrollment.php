<?php 
if(!empty($_POST['action'])){
	$conn = mysqli_connect('localhost','root','','final');
    if(!$conn) {echo "Failed to connect to database";}
    switch($_POST['action']){
        case 'select':
            $sql = 
                "SELECT subjects.name, subjects.credit FROM enrollment 
                LEFT JOIN users
                ON enrollment.studentid = users.userid
                LEFT JOIN subjects
                ON enrollment.subjectid = subjects.subjectid 
                WHERE username = '".$_POST['username']."'";
            $result = mysqli_query($conn, $sql);

            $rows = [];
            while ($row = mysqli_fetch_assoc($result)) {
                $rows[] = $row;
            }

            if($result){
                echo json_encode($rows);
            }
            else echo "error";
            break;
        case 'subjects':
            $sql = 
                "SELECT name FROM subjects";
            $result = mysqli_query($conn, $sql);

            $rows = [];
            while ($row = mysqli_fetch_assoc($result)) {
                $rows[] = $row;
            }

            if($result){
                echo json_encode($rows);
            }
            else echo "error";
            break;
        case 'sum':
            $sql = 
                "SELECT SUM(credit) FROM subjects
                JOIN enrollment
                ON subjects.subjectid = enrollment.subjectid
                JOIN users
                ON users.userid = enrollment.studentid
                WHERE users.username = '".$_POST['username']."'";
            $result = mysqli_query($conn, $sql);
            if($result){
                echo $result->fetch_assoc()['SUM(credit)'];
            } else{
                echo "error";
            }
            break;
        case 'credit':
            $sql =
                "SELECT credit
                FROM subjects
                WHERE name = '".$_POST['subject']."'";
            $result = mysqli_query($conn, $sql);
            if($result){
                echo $result->fetch_assoc()['credit'];
            } else{
                echo "error";
            }
            break;
        case 'enroll':
            $sql = "SELECT credit FROM subjects WHERE name = '".$_POST['subject']."'";
            $result = mysqli_query($conn, $sql);
            if($result){
                $credit = $result->fetch_assoc()['credit'];
                if((int)$credit + (int)$_POST['sumCredits'] > 24){
                    echo "overcredit";
                    break;
                }
            } else{
                echo "error";
            }
            $sql =
                "INSERT INTO enrollment (studentid, subjectid)
                SELECT u.userid, s.subjectid
                FROM users u, subjects s
                WHERE u.username = '".$_POST['username']."' 
                AND s.name = '".$_POST['subject']."'";
            if(mysqli_query($conn, $sql)){
                echo "Success";
            } else{
                echo "error";
            }
            break;
        case 'unenroll':
            $sql = 
                "DELETE enrollment FROM enrollment 
                LEFT JOIN users
                ON enrollment.studentid = users.userid
                LEFT JOIN subjects
                ON enrollment.subjectid = subjects.subjectid 
                WHERE users.username = '".$_POST['username']."' 
                AND subjects.name = '".$_POST['subject']."'";
            if(mysqli_query($conn, $sql)){
                echo "Success";
            } else{
                echo "error";
            }
            break;
    }
}
else echo "error";
?>