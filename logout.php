<?php
session_start();

if($_SESSION["usuario_logado"]){
  /*
  Limpa todas as variaveis de sessão
  */
  session_unset();
}
header("location:index.php");

?>