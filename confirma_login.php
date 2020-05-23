<?php
session_start();
include ('conexao.php');

$usuario= $_POST['usuario'];
$senha = $_POST['senha'];


$verifica = "SELECT pk_cliente, cpf_cliente, nome_cliente, email_cliente, datanasc_cliente, usuario cliente, senha cliente  
FROM Cliente
WHERE usuario_cliente=:usuario_cliente AND senha_cliente= :senha_cliente;";

$consulta = $conn->prepare( $verifica );

$consulta->bindParam(':usuario_cliente', $usuario);
$consulta->bindParam(':senha_cliente', $senha);
// $consulta->bindParam(':nome_cliente', $nome_cliente);

$_SESSION['usuario']= $usuario;
$_SESSION['senha']= $senha;

$resultado = $consulta->execute();
if($resultado && $consulta->rowCount() == 1) {

  $usuario = $consulta->fetch(PDO::FETCH_OBJ);

  $_SESSION['nome_cliente'] = $usuario->usuario_cliente;
  $_SESSION['cpf_cliente'] = $usuario->cpf_cliente;

  $_SESSION["usuario_logado"]=true;

  header("Location:../menu.php");

} else {
  header("Location: erro_login.php");
}
?>
