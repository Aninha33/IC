<?php
session_start();
// include ('verifica_login.php');
// include ('conexao.php');
?>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <title>Menu Inicial</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="image/caixinhas2.png" />
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css">
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="fonts/iconic/css/material-design-iconic-font.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/estilos.css">
    <link rel="stylesheet" type="text/css" href="css/menu.css">
    <!--===============================================================================================-->
</head>

<body>

    <!-- <div class="w3-main"> -->
    <!-- <div class="cor"> -->
    <nav class="navbar cor">
        <a class="navbar-brand  text-white" href="menu.php">
            <img src="image\caixinha.png" width="auto" height="40" class="d-inline-block align-top" alt="">
            Menu Inicial
        </a>

        <div id="mySidebar" class="sidebar">
            <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">×</a>
            <a href="javascript:void(0)" class="w3-button negrito" onclick="closeNav()">Fechar</a>
            <a href="armazenarProduto.php" class="w3-button">Armazenamento de Dados</a>
            <a href="consultarDados.php" class="w3-button">Consultar Dados</a>
            <a href="renovarExcluir.php" class="w3-button">Renovar/Excluir</a>
            <a href="logout.php" class=" w3-button">Sair da Conta</a>
        </div>
        <div id="main">
            <button class="openbtn" onclick="openNav()">☰ </button>
        </div>
    </nav>

    <br>
    <center>
        <div>
            <h2>Bem-Vindo </h2>
            <!-- <?php echo $_SESSION['nome_cliente']; ?> </div> -->
    </center>
    <br>

    <div class="container" style="margin-right:200px">
        <div class="row">
            <div class="col-md-6 col-xs-12">
                <img src="image\caixinhas2.png" style="max-height:398px; max-width: 504px;" class="redimensiona">
            </div>

            <div class="textoMenu">
                Programa desenvolvido pelos Alunos da UTF-PR campus Apucarana.<br>
                <p>
                    Desenvolvedores: Ana Carolina Ribeiro, Darlan Oliveira, Rodrigo Badega.
                </p>
            </div>
        </div>
    </div>

    <script>
    // function w3_open() {
    //   document.getElementById("mySidebar").style.display = "block";
    // }
    // function w3_close() {
    //   document.getElementById("mySidebar").style.display = "none";
    // }
    function openNav() {
        document.getElementById("mySidebar").style.width = "250px";
        document.getElementById("main").style.marginLeft = "250px";
    }

    function closeNav() {
        document.getElementById("mySidebar").style.width = "0";
        document.getElementById("main").style.marginLeft = "0";
    }
    </script>
</body>

</html>