-- drop database Alimentinhos;
create database Alimentinhos;
use Alimentinhos;

create table Cliente(
pk_cliente int primary key not null auto_increment,
cpf_cliente int(12) not null unique,
nome_cliente varchar(150) not null,
email_cliente varchar(150) not null,
datanasc_cliente date,
usuario_cliente varchar(10) not null,
senha_cliente varchar(10) not null
);

create table Refrigerador(
pk_refri int primary key not null auto_increment
);

create table Alimento(
pk_alim int primary key not null auto_increment,
nome_alim varchar(30) not null,
entrada_alim date not null,
validade_alim date
);

-- procedure de Cliente
Delimiter $$
create procedure InserirCliente(cpf_cliente int(12), nome_cliente varchar(150), email_cliente varchar(150),
datanasc_cliente date, usuario_cliente varchar(10), senha_cliente varchar(10))
begin
	if (cpf_cliente= '' or cpf_cliente= null)then
		select'CPF não preenchido';
	else 
		if ((select cpf_cliente from Cliente where Cliente.cpf_cliente= cpf_cliente) is not null )then
			select'CPF já cadastrado' as Erro;
		else
			insert into Cliente values (null, cpf_cliente,nome_cliente, email_cliente,
				datanasc_cliente, usuario_cliente,senha_cliente );	
			select 'Cliente inserido com sucesso' as Sucesso;
            
		end if;
	end if;
end;
$$ Delimiter ;
-- drop procedure InserirCliente;
call InserirCliente(01474985203,'Ana Carolina', 'anacrm33@gmail.com','2000-06-28','Aninha','admin'); 
