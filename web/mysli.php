/*
*   TK2 projekti
*   Mysql-skriptit
*/

<?php

    //Include functions
    require('functions.php');
    require('dbvar.php');

    define('METHOD', $_GET['metodi']);

    $username = filterInput($_POST['username_reg']));
    $password = filterInput(md5($_POST['pasw_reg']));

    //Start remembering
    ob_start();

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $db_user, $db_pasw);

        // set the PDO error mode to exception
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        switch(METHOD){
            case 'REGISTER':
                $statement = $conn->prepare("INSERT INTO users(name, password, regdate) VALUES (?, ?, ?)");
                $statement->execute(array($username, $password, new date('Y-m-d')));

                echo "SUCCESS";

                //Register session variables
                session_register('username');
                session_register('password');

                break;

            case 'LOGIN':
                $statement = $conn->prepare("SELECT U.username FROM USERS as U WHERE U.username = '$username' AND U.password = '$password'");
                $statement->execute(array($username, $password));

                if($statement->rowCount() === 1){
                    echo "LOGINSUCCESS"
                    //Register session variables
                    session_register('username');
                    session_register('password');
                } else{
                    echo "LOGINFAILURE"
                }
                break;
            case 'FETCH':
                $statement = $conn->prepare("SELECT L.lajinimi");
        }

    }catch(PDOException $e) {
        // roll back the transaction if something failed
        $conn->rollback();
        echo "Database error: " . $e->getMessage();
    }

    //Output
    ob_end_flush();

    //Close connection
    $conn = null;

?>