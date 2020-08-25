<?php
include ('conexao.php');

?>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <title>Falha no Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="image/caixinhas2.png" />
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css">
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
        integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="fonts/iconic/css/material-design-iconic-font.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/estilos.css">
    <link rel="stylesheet" type="text/css" href="css/menu.css">
    <!--===============================================================================================-->
</head>

<body>
    <div class="limiter">
        <div class="container-login100">
            <div class="wrap-login100" style="padding-top: 0px;">
                <div class="login100-form validate-form">
                    <img src="image\caixinhas2.png" style="height: 250px; width: 350px;">
                    <br>
                    <div class="textoErro">
                        OPS! Erro 401.
                        <br>
                        Usuário e/ou senha inválidos.
                        <!-- por favor confira suas informações -->
                        <br>
                        <i class="fas fa-ghost CorIcone"></i>
                    </div>

                    <div class="wrap-login100-form-btn">
                        <div class="login100-form-bgbtn"></div>
                        <a role="button" class="login100-form-btn text-white" href="index.php">
                            Tentar novamente
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
   

    <!--===============================================================================================-->
    <script src="vendor/jquery/jquery-3.2.1.min.js"></script>
    <script src="vendor/animsition/js/animsition.min.js"></script>
    <script src="vendor/bootstrap/js/popper.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
    <script src="vendor/select2/select2.min.js"></script>
    <script src="js/main.js"></script>

</body>

</html>