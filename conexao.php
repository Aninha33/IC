<?php
$HOST = "localhost";
$USER = "root";
$PASSWORD = "root";
$DATABASE = "Alimentinhos";

$conn = new PDO('mysql:host=' . $HOST . ';dbname=' . $DATABASE, $USER, $PASSWORD);
?>
