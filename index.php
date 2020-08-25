<?php
session_start();
include ('conexao.php');

?>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <title>Login - BudhaStore</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="image/caixinhas2.png" />
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="fonts/iconic/css/material-design-iconic-font.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <!--===============================================================================================-->
</head>

<body>
    <div class="container-login100">
        <div style="padding-top:0px;">
            <form class="login100-form validate-form" method="post">
                <span class="login100-form-title">
                    Bem-Vindo
                </span>
                <img src="image\caixinhas4.png" style="height: 250px; width: 350px;">

                <div class="wrap-input100 validate-input" data-validate="Campo inválido">
                    <input class="input100" type="text" name="usuario">
                    <span class="focus-input100" data-placeholder="Usuário"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Campo inválido">
                    <span class="btn-show-pass">
                        <i class="zmdi zmdi-eye"></i> <!-- icone do olho -->
                    </span>
                    <input class="input100" type="password" name="senha">
                    <span class="focus-input100" data-placeholder="Senha"></span>
                </div>

                <center> 
                <div class="input100" style="height: auto">
                    <input type="checkbox" name="remember" value="1" /> Lembrar minha senha
                </div>
                </center>

                <div class="container-login100-form-btn" href="confirma_login.php">
                    <div class="wrap-login100-form-btn">
                        <div class="login100-form-bgbtn"></div>
                        <a role="button" class="login100-form-btn text-white" href="confirma_login.php">
                            Login
                        </a>
                    </div>
                </div>

                <div class="container-login100-form-btn">
                    <div class="wrap-login100-form-btn">
                        <div class="login100-form-bgbtn"></div>
                        <a role="button" class="login100-form-btn text-white" href="cadastro.php">
                            Criar Cadastro
                        </a>
                    </div>
                </div>
            </form>
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