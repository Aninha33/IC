package br.edu.utfpr;

import org.mindrot.jbcrypt.BCrypt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

import org.springframework.util.ResourceUtils;

import java.io.Serializable;
import java.net.URL;

import java.nio.charset.StandardCharsets;

//System.out.println: Log na saida do terminal
//output.add: Log na pagina Web

@Controller
public class LoginController {

  //Descomentar as 2 (duas) linhas abaixo para usar BD da nuvem Heroku - Remover no Main.java tambem
  @Value("${spring.datasource.url}")
  private String dbUrl;
  //private String dbUrl = "jdbc:postgresql://127.0.0.1:5432/database?user=lucio&password=lucio";

  @Autowired
  private DataSource dataSource;

  //---Importacao
  //Numero de campos da linha, do arquivo de saida filtrado
  private int NUM_CAMPOS = 106;
  private int ID = 0; //numero da ficha
  private int NUM_ARQUIVOS = 2070;

  //ARQUIVO_ENTRADA = "/home/lucio/biolar_leitor/arq_saida_filtrado_"+MIN_INDICE+"_"+MAX_INDICE+".txt";
  //String ARQUIVO_ENTRADA = "/home/lucio/biolar_leitor/arq_saida_filtrado_1_90.txt";
  private int MIN_INDICE = 1;    //indice do cadastro inicial
  private int MAX_INDICE = 90;   //indice do cadastro final
  private int NUM_INDICES = 90;  //quantidade de cadastros lidos em cada arquivo

  //    private final String ARQUIVO_ENTRADA = "/home/lucio/biolar_leitor/arq_saida_filtrado_1_90.txt";
  private final String ARQUIVO_ENTRADA = "public/arquivos/arq_saida_filtrado_";
  //---

  //Inicia os valores das variaveis de instancia para
  //ter sempre os valores iniciais nas importacoes
  public void iniciarValores(){
    //Numero de campos da linha, do arquivo de saida filtrado
    this.NUM_CAMPOS = 106;
    this.ID = 0; //numero da ficha
    this.NUM_ARQUIVOS = 2070;

    this.MIN_INDICE = 1;    //indice do cadastro inicial
    this.MAX_INDICE = 90;   //indice do cadastro final
    this.NUM_INDICES = 90;  //quantidade de cadastros lidos em cada arquivo
  }

  @PostMapping("/iniciarBases")
  public String iniciarBases(Model model){

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();
      Statement stmt = connection.createStatement();
      ////////////////
      stmt.executeUpdate("create table if not exists avaliacao ( "
      + "id serial not null primary key, "
      + "cpf varchar(20), "
      + "dataAvaliacao varchar(20), "
      + "dataNascimento varchar(20), "
      + "idade integer, "
      + "nome varchar(500), "
      + "objetivo varchar(500), "
      + "exercicios varchar(500), "
      + "outrasAF varchar(500),  "
      + "especificar varchar(500), "
      + "diagnostico varchar(500), "
      + "medicacao varchar(500), "
      + "peso float, "
      + "altura float, "
      + "imc float, "
      + "fc float )");
      output.add("Tabela 'avaliacao' criada. [OK]");

      stmt.executeUpdate("create table if not exists goniometria ( "
      + "id serial not null references avaliacao (id), "
      + "ofdir integer, "
      + "ofesq integer, "
      + "dor1 varchar(20), "
      + "ana1 varchar(20), "
      + "local1 varchar(20), "

      + "oredir integer, "
      + "oreesq integer, "
      + "dor2 varchar(20),  "
      + "ana2 varchar(20), "
      + "local2 varchar(20), "

      + "qfdir integer, "
      + "qfesq integer, "
      + "dor3 varchar(20), "
      + "ana3 varchar(20), "
      + "local3 varchar(20),"

      + "qredir integer, "
      + "qreesq integer, "
      + "dor4 varchar(20), "
      + "ana4 varchar(20), "
      + "local4 varchar(20),"

      + "jfdir integer, "
      + "jfesq integer, "
      + "dor5 varchar(20), "
      + "ana5 varchar(20), "
      + "local5 varchar(20),"

      + "tfddir integer, "
      + "tfdesq integer, "
      + "dor6 varchar(20), "
      + "ana6 varchar(20), "
      + "local6 varchar(20),"

      + "tfpdir integer, "
      + "tfpesq integer, "
      + "dor7 varchar(20), "
      + "ana7 varchar(20), "
      + "local7 varchar(20),"

      + "obs varchar(500), "
      + "sentar varchar(500) "
      +")");
      output.add("Tabela 'goniometria' criada. [OK]");

      stmt.executeUpdate("create table if not exists preensao ( "
      + "id serial not null references avaliacao (id), "
      + "ladodominante varchar(20), "
      + "dir1 float, "
      + "esq1 float, "
      + "dir2 float, "
      + "esq2 float, "
      + "dir3 float, "
      + "esq3 float "

      +")");
      output.add("Tabela 'preensao' criada. [OK]");

      stmt.executeUpdate("create table if not exists composicao ( "
      + "id serial not null references avaliacao (id), "
      + "cintura float, "
      + "prof varchar(20), "
      + "dbraco float, "
      + "ebraco float, "
      + "quadril float, "
      + "dpantu float, "
      + "epantu float, "
      + "rqc float, "
      + "dcoxa float, "
      + "ecoxa float, "
      + "emediacoxa float, "
      + "dmediacoxa float, "
      + "resistencia float, "
      + "reactancia float, "
      + "agua float, "
      + "mmagra float, "
      + "mgordura float, "
      + "massacel float, "
      + "triceps float, "
      + "subes float, "
      + "supra float, "
      + "abd float, "
      + "coxa float, "
      + "total float, "
      + "compo_15_2 float, "
      + "compo_16_2 float, "
      + "compo_17_2 float, "
      + "compo_25 float, "
      + "compo_26 float, "
      + "compo_27 float, "
      + "compo_28 float, "
      + "compo_29 float, "
      + "compo_30 float, "
      + "compo_31 varchar(500), "
      + "compo_32 float, "
      + "compo_33 float, "
      + "compo_34 float, "
      + "compo_35 varchar(500), "
      + "compo_36 float "

      +")");
      output.add("Tabela 'composicao' criada. [OK]");

      stmt.executeUpdate("create table if not exists avaliacaodor ( "
      + "id serial not null references avaliacao (id), "
      + "dor1 varchar(500), "
      + "dor2 integer, "
      + "dor3 varchar(500), "
      + "dor4 integer, "
      + "dor5 varchar(500), "
      + "dor6 integer, "
      + "dor7 varchar(500), "
      + "dor8 integer, "
      + "dor9 varchar(500), "
      + "dor10 integer, "
      + "dor11 varchar(500), "
      + "dor12 integer, "
      + "dor13 varchar(500), "
      + "dor14 integer, "
      + "dor15 varchar(500), "
      + "dor16 integer, "
      + "dor_obs varchar(500) "

      +")");
      output.add("Tabela 'avaliacaodor' criada. [OK]");

      stmt.executeUpdate("create table if not exists evolucaodor ( "
      + "id serial not null references avaliacao (id), "
      + "dor1 varchar(500), "
      + "dor2 varchar(500), "
      + "dor3 varchar(500), "
      + "dor4 varchar(500), "
      + "dor5 varchar(500), "
      + "dor6 varchar(500), "
      + "dor7 varchar(500), "
      + "dor8 varchar(500), "
      + "dor9 varchar(500), "
      + "dor10 varchar(500), "
      + "dor11 varchar(500), "
      + "dor12 varchar(500), "
      + "dor13 varchar(500), "
      + "dor14 varchar(500), "
      + "dor15 varchar(500), "
      + "dor16 varchar(500), "
      + "dor_obs varchar(500) "

      +")");
      output.add("Tabela 'evolucaodor' criada. [OK]");

      stmt.executeUpdate("create table if not exists gural ( "
      + "id serial not null references avaliacao (id), "
      + "gural1 varchar(500), "
      + "gural2 varchar(500), "
      + "gural3 varchar(500), "
      + "gural4 varchar(500), "
      + "gural5 varchar(500), "
      + "gural6 varchar(500), "
      + "gural7 varchar(500), "
      + "gural8 varchar(500), "
      + "gural9 varchar(500), "
      + "gural10 varchar(500), "
      + "gural11 varchar(500), "
      + "gural12 varchar(500) "

      +")");
      output.add("Tabela 'gural' criada. [OK]");

      stmt.executeUpdate("create table if not exists qualidade ( "
      + "id serial not null references avaliacao (id), "
      + "qualidade1 varchar(500), "
      + "qualidade2 varchar(500), "
      + "qualidade3 varchar(500), "
      + "qualidade4 varchar(500), "
      + "qualidade5 varchar(500), "
      + "qualidade6 varchar(500), "
      + "qualidade7 varchar(500), "
      + "qualidade8 varchar(500), "
      + "qualidade9 varchar(500), "
      + "qualidade10 varchar(500), "
      + "qualidade11 varchar(500), "
      + "qualidade12 varchar(500), "
      + "qualidade13 varchar(500), "
      + "qualidade14 varchar(500), "
      + "qualidade15 varchar(500), "
      + "qualidade16 varchar(500), "
      + "qualidade17 varchar(500), "
      + "qualidade18 varchar(500) "

      +")");
      output.add("Tabela 'qualidade' criada. [OK]");

      stmt.executeUpdate("create table if not exists forca ( "
      + "id serial not null references avaliacao (id), "
      + "forca1 varchar(500), "
      + "forca2 varchar(500), "
      + "forca3 varchar(500), "
      + "forca4 varchar(500), "
      + "forca5 varchar(500), "
      + "forca6 varchar(500), "
      + "forca7 varchar(500), "
      + "forca8 varchar(500), "
      + "forca9 varchar(500), "
      + "forca10 varchar(500), "
      + "forca11 varchar(500), "
      + "forca12 varchar(500), "
      + "forca13 varchar(500), "
      + "forca14 varchar(500) "

      +")");
      output.add("Tabela 'forca' criada. [OK]");

      stmt.executeUpdate("create table if not exists pressao ( "
      + "id serial not null references avaliacao (id), "
      + "pressao1 varchar(500), "
      + "pressao2 varchar(500), "
      + "pressao3 varchar(500), "
      + "pressao4 varchar(500), "
      + "pressao5 varchar(500) "

      +")");
      output.add("Tabela 'pressao' criada. [OK]");

      stmt.executeUpdate("create table if not exists usuarios ( "
      + "id serial NOT NULL, "
      + "nome varchar(500), "
      + "email varchar(500) PRIMARY KEY, "
      + "senha varchar(500) )");
      output.add("Tabela 'usuarios' criada. [OK]");
      String senha="123admin123";
      String hashed = BCrypt.hashpw(senha, BCrypt.gensalt());
      stmt.executeUpdate("insert into usuarios (nome,email,senha) values ( 'admin', 'admin@admin', '"+hashed+"')");
      output.add("Usuario 'admin' criado. [OK]");


      model.addAttribute("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.addAttribute("message", output);
      return "error";
    }
  }

  @PostMapping("/importar1")
  String importar1( Map<String, Object> model) {

    iniciarValores();

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();

      //////////////////////////////////
      int numArquivos = 1;
      int id = ID;
      while (numArquivos < NUM_ARQUIVOS) {

        StringBuffer conteudo = new StringBuffer();

        //Abre o arquivo
        String arq = ARQUIVO_ENTRADA + MIN_INDICE + "_" + MAX_INDICE + ".txt";
        //---Local
        //BufferedReader file = new BufferedReader(new FileReader(arq));
        //---Local/Remote
        String linha="";
        ClassPathResource resource = new ClassPathResource(arq);
        BufferedReader file = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        //Primeira linha soh possui os cabecalhos das colunas
        linha = file.readLine();
        output.add(linha);

        //Proxima linha
        linha = file.readLine();
        output.add(linha);
        //System.out.println(linha);

        String[] campo = new String[NUM_CAMPOS];
        StringTokenizer token = null;
        int numCampos = 0;
        int i = 0;

        //Prepara as variaveis para gravacao na BD
        Statement stmt = connection.createStatement();

        ////
        //Le os campos que serao gravados na BD, linha a linha
        //
        //id eh um numero sequencial
        while (linha != null) {
          token = new StringTokenizer(linha, "|");
          numCampos = 0;
          i = 0;
          while (numCampos < NUM_CAMPOS) {

            //Fix1: Fixa campos nao preenchidos
            campo[i] = (String) token.nextToken().trim();
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }
            //Fix2: Substitui aspas simples, caso existam, nos campos
            campo[i] = campo[i].replaceAll("'", "min");

            System.out.println(" (" + i + ")[" + campo[i] + "]");
            output.add(" (" + i + ")[" + campo[i] + "]");

            i++;
            numCampos++;
          }//fim while

          ///
          i = 0;
          System.out.println("\n");
          System.out.println("id: 0");
          System.out.println("CPF: 0");
          System.out.println("(1)dataavaliacao: " + campo[1]);
          System.out.println("(6)datanascimento: " + campo[6]);
          System.out.println("(2)idade: " + campo[2]);
          System.out.println("(0)nome: " + campo[0]);
          System.out.println("()objetivo: ");
          System.out.println("(3)exercicios: " + campo[3]);
          System.out.println("(4)outrasAF: " + campo[4]);
          System.out.println("(5)especificar: " + campo[5]);
          System.out.println("(7)diagnostico: " + campo[7]);
          System.out.println("(8)medicacao: " + campo[8]);

          output.add("\n");
          output.add("id: 0");
          output.add("CPF: 0");
          output.add("(1)dataavaliacao: " + campo[1]);
          output.add("(6)datanascimento: " + campo[6]);
          output.add("(2)idade: " + campo[2]);
          output.add("(0)nome: " + campo[0]);
          output.add("()objetivo: ");
          output.add("(3)exercicios: " + campo[3]);
          output.add("(4)outrasAF: " + campo[4]);
          output.add("(5)especificar: " + campo[5]);
          output.add("(7)diagnostico: " + campo[7]);
          output.add("(8)medicacao: " + campo[8]);

          //////
          String REGEX = "";
          Matcher matcher;
          Pattern pattern;
          REGEX = "(.*)kg";
          pattern = Pattern.compile(REGEX);
          matcher = pattern.matcher(campo[9]);
          if (matcher.find()) {
            campo[9] = matcher.group(1).trim();
            //Caso o campo nao tenha o numero, apenas a letra da unidade
            if (campo[9].length() == 0) {
              campo[9] = "0";
            }
            campo[9] = campo[9].replace(",", ".");
          }
          System.out.println("(9)peso: " + campo[9]);
          output.add("(9)peso: " + campo[9]);

          /////
          //////
          REGEX = "(.*)m";
          pattern = Pattern.compile(REGEX);
          matcher = pattern.matcher(campo[10]);
          if (matcher.find()) {
            campo[10] = matcher.group(1).trim();
            //Caso o campo nao tenha o numero, apenas a letra da unidade
            if (campo[10].length() == 0) {
              campo[10] = "0";
            }
            campo[10] = campo[10].replace(",", ".");
          }
          System.out.println("(10)altura: " + campo[10]);
          output.add("(10)altura: " + campo[10]);

          /////
          //////
          REGEX = "(.*)kg";   //<-- kg/m
          pattern = Pattern.compile(REGEX);
          matcher = pattern.matcher(campo[11]);
          if (matcher.find()) {
            //System.out.println("---"+matcher.group(1).trim());
            campo[11] = matcher.group(1).trim();
            //Caso o campo nao tenha o numero, apenas a letra da unidade
            if (campo[11].length() == 0) {
              campo[11] = "0";
            }
            campo[11] = campo[11].replace(",", ".");
          }
          System.out.println("(11)imc: " + campo[11]);
          output.add("(11)imc: " + campo[11]);

          /////
          //////
          REGEX = "(.*)bpm";   //<-- bpm
          pattern = Pattern.compile(REGEX);
          matcher = pattern.matcher(campo[12]);
          if (matcher.find()) {
            campo[12] = matcher.group(1).trim();
            //Caso o campo nao tenha o numero, apenas a letra da unidade
            if (campo[12].length() == 0) {
              campo[12] = "0";
            }
            campo[12] = campo[12].replace(",", ".");

          }
          System.out.println("(12)fc: " + campo[12]);
          output.add("(12)fc: " + campo[12]);

          /////
          //Insere os campos na Base de Dados
          i = 0;
          StringBuffer inserirLinha = new StringBuffer();
          inserirLinha.append("insert into avaliacao("
          + "cpf,"
          + "dataavaliacao,"
          + "datanascimento,"
          + "idade,"
          + "nome,"
          + "objetivo,"
          + "exercicios,"
          + "outrasaf,"
          + "especificar,"
          + "diagnostico,"
          + "medicacao,"
          + "peso,"
          + "altura,"
          + "imc,"
          + "fc"
          + ") values ( "
          + "'" + id + "', "
          + //cpf
          "'" + campo[1] + "', "
          + //dataavaliacao
          "'" + campo[6] + "', "
          + //datanascimento
          campo[2] + ", "
          + //idade
          "'" + campo[0] + "', "
          + //nome
          "'0'" + ", "
          + //objetivo
          "'" + campo[3] + "', "
          + //exercicios
          "'" + campo[4] + "', "
          + //outrasaf
          "'" + campo[5] + "', "
          + //especificar
          "'" + campo[7] + "', "
          + //diagnostico
          "'" + campo[8] + "', "
          + //medicacao
          campo[9] + ", "
          + //peso
          campo[10] + ", "
          + //altura
          campo[11] + ", "
          + //imc
          campo[12]
          + //fc
          ")");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          //Proxima linha do arquivo de cadastros filtrados
          linha = file.readLine();

          System.out.println(id + "-------");
          output.add(id + "-------");

          //Incrementa o numero da ficha
          id++;

        }//fim while

        /////////////////////////////
        MIN_INDICE = MAX_INDICE + 1;
        MAX_INDICE += NUM_INDICES;
        numArquivos = MAX_INDICE;

        //Proximo arquivo
      }//fim while

      output.add("Importacao realizada com sucesso!");
      model.put("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.put("message", output);
      return "error";
    }

  }//fim importar

  @PostMapping("/importar2")
  String importar2( Map<String, Object> model) {

    iniciarValores();

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();

      //////////////////////////////////
      int numArquivos = 1;
      int id = this.ID;
      while (numArquivos < NUM_ARQUIVOS) {

        StringBuffer conteudo = new StringBuffer();

        //Abre o arquivo
        String arq = ARQUIVO_ENTRADA + MIN_INDICE + "_" + MAX_INDICE + ".txt";
        //---Local
        //BufferedReader file = new BufferedReader(new FileReader(arq));
        //---Local/Remote
        String linha="";
        ClassPathResource resource = new ClassPathResource(arq);
        BufferedReader file = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        //Primeira linha soh possui os cabecalhos das colunas
        linha = file.readLine();
        output.add(linha);

        //Proxima linha
        linha = file.readLine();
        output.add(linha);
        System.out.println(linha);

        String[] campo = new String[NUM_CAMPOS];
        StringTokenizer token = null;
        int numCampos = 0;
        int i = 0;

        //Prepara as variaveis para gravacao na BD
        Statement stmt = connection.createStatement();

        ////
        //Le os campos que serao gravados na BD, linha a linha

        //id eh um numero sequencial
        while (linha != null) {
          token = new StringTokenizer(linha, "|");
          numCampos = 0;

          //Salta os campos da secao anterior (Avaliacao)
          int j=0;
          while(j<13){
            token.nextToken();
            numCampos++;
            j++;
          }//

          //i=0
          i = 13;
          while (numCampos < NUM_CAMPOS) {

            //Fix1: Fixa campos nao preenchidos
            campo[i] = (String) token.nextToken().trim();
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }
            //Fix2: Substitui o simbolo de graus simples, caso existam, nos campos
            campo[i] = campo[i].replaceAll("°", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            System.out.println(" (" + i + ")[" + campo[i] + "]");
            output.add(" (" + i + ")[" + campo[i] + "]");

            i++;
            numCampos++;
          }//fim while

          ///
          i = 13;
          System.out.println("\n");
          System.out.println("id: 0");
          System.out.println("("+i+")ofdir: " + campo[i++]);
          System.out.println("("+i+")ofesq: " + campo[i++]);
          System.out.println("("+i+")dor1: " + campo[i++]);
          System.out.println("()ana1: 0"); //Dor ou Anatomico
          System.out.println("()local1: 0"); //nao ha nas fichas .doc

          System.out.println("("+i+")oredir: " + campo[i++]);
          System.out.println("("+i+")oreesq: " + campo[i++]);
          System.out.println("("+i+")dor2: " + campo[i++]);
          System.out.println("()ana2: 0");
          System.out.println("()local2: 0");

          System.out.println("("+i+")qfdir: " + campo[i++]);
          System.out.println("("+i+")qfesq: " + campo[i++]);
          System.out.println("("+i+")dor3: " + campo[i++]);
          System.out.println("()ana3: 0");
          System.out.println("()local3: 0");

          System.out.println("("+i+")qredir: " + campo[i++]);
          System.out.println("("+i+")qreesq: " + campo[i++]);
          System.out.println("("+i+")dor4: " + campo[i++]);
          System.out.println("()ana4: 0");
          System.out.println("()local4: 0");

          System.out.println("("+i+")jfdir: " + campo[i++]);
          System.out.println("("+i+")jfesq: " + campo[i++]);
          System.out.println("("+i+")dor5: " + campo[i++]);
          System.out.println("()ana5: 0");
          System.out.println("()local5: 0");

          System.out.println("("+i+")tfddir: " + campo[i++]);
          System.out.println("("+i+")tfdesq: " + campo[i++]);
          System.out.println("("+i+")dor6: " + campo[i++]);
          System.out.println("()ana6: 0");
          System.out.println("()local6: 0");

          System.out.println("("+i+")tfpdir: " + campo[i++]);
          System.out.println("("+i+")tfpesq: " + campo[i++]);
          System.out.println("("+i+")dor7: " + campo[i++]);
          System.out.println("()ana7: 0");
          System.out.println("()local7: 0");

          System.out.println("("+i+")obs: " + campo[i++]);
          System.out.println("()sentar: 0");

          ////////////////
          i=13;
          output.add("\n");
          output.add("id: 0");
          output.add("("+i+")ofdir: " + campo[i++]);
          output.add("("+i+")ofesq: " + campo[i++]);
          output.add("("+i+")dor1: " + campo[i++]);
          output.add("()ana1: 0"); //Dor ou Anatomico
          output.add("()local1: 0"); //nao ha nas fichas .doc

          output.add("("+i+")oredir: " + campo[i++]);
          output.add("("+i+")oreesq: " + campo[i++]);
          output.add("("+i+")dor2: " + campo[i++]);
          output.add("()ana2: 0");
          output.add("()local2: 0");

          output.add("("+i+")qfdir: " + campo[i++]);
          output.add("("+i+")qfesq: " + campo[i++]);
          output.add("("+i+")dor3: " + campo[i++]);
          output.add("()ana3: 0");
          output.add("()local3: 0");

          output.add("("+i+")qredir: " + campo[i++]);
          output.add("("+i+")qreesq: " + campo[i++]);
          output.add("("+i+")dor4: " + campo[i++]);
          output.add("()ana4: 0");
          output.add("()local4: 0");

          output.add("("+i+")jfdir: " + campo[i++]);
          output.add("("+i+")jfesq: " + campo[i++]);
          output.add("("+i+")dor5: " + campo[i++]);
          output.add("()ana5: 0");
          output.add("()local5: 0");

          output.add("("+i+")tfddir: " + campo[i++]);
          output.add("("+i+")tfdesq: " + campo[i++]);
          output.add("("+i+")dor6: " + campo[i++]);
          output.add("()ana6: 0");
          output.add("()local6: 0");

          output.add("("+i+")tfpdir: " + campo[i++]);
          output.add("("+i+")tfpesq: " + campo[i++]);
          output.add("("+i+")dor7: " + campo[i++]);
          output.add("()ana7: 0");
          output.add("()local7: 0");

          output.add("("+i+")obs: " + campo[i++]);
          output.add("()sentar: 0");

          /////
          //Insere os campos na Base de Dados
          i = 13;

          StringBuffer inserirLinha = new StringBuffer();
          inserirLinha.append("insert into goniometria("
          + "ofdir,"
          + "ofesq,"
          + "dor1,"
          + "ana1,"
          + "local1,"

          + "oredir,"
          + "oreesq,"
          + "dor2,"
          + "ana2,"
          + "local2,"

          + "qfdir,"
          + "qfesq,"
          + "dor3,"
          + "ana3,"
          + "local3,"

          + "qredir,"
          + "qreesq,"
          + "dor4,"
          + "ana4,"
          + "local4,"

          + "jfdir,"
          + "jfesq,"
          + "dor5,"
          + "ana5,"
          + "local5,"

          + "tfddir,"
          + "tfdesq,"
          + "dor6,"
          + "ana6,"
          + "local6,"

          + "tfpdir,"
          + "tfpesq,"
          + "dor7,"
          + "ana7,"
          + "local7,"

          + "obs,"
          + "sentar"

          + ") values ( "
          +
          campo[i++] + ", "
          + //ofdir
          campo[i++] + ", "
          + //ofesq
          "'" + campo[i++] + "', "
          + //dor1
          "'0', "
          + //ana1
          "'0', "
          + //local1

          campo[i++] + ", "
          + //oredir
          campo[i++] + ", "
          + //oreesq
          "'" + campo[i++] + "', "
          + //dor2
          "'0', "
          + //ana2
          "'0', "
          + //local2

          campo[i++] + ", "
          + //qfdir
          campo[i++] + ", "
          + //qfesq
          "'" + campo[i++] + "', "
          + //dor3
          "'0', "
          + //ana3
          "'0', "
          + //local3

          campo[i++] + ", "
          + //qredir
          campo[i++] + ", "
          + //qreesq
          "'" + campo[i++] + "', "
          + //dor4
          "'0', "
          + //ana4
          "'0', "
          + //local4

          campo[i++] + ", "
          + //jfdir
          campo[i++] + ", "
          + //jfesq
          "'" + campo[i++] + "', "
          + //dor5
          "'0', "
          + //ana5
          "'0', "
          + //local5

          campo[i++] + ", "
          + //tfddir
          campo[i++] + ", "
          + //tfdesq
          "'" + campo[i++] + "', "
          + //dor6
          "'0', "
          + //ana6
          "'0', "
          + //local6

          campo[i++] + ", "
          + //tfpdir
          campo[i++] + ", "
          + //tfpesq
          "'" + campo[i++] + "', "
          + //dor7
          "'0', "
          + //ana7
          "'0', "
          + //local7

          "'" + campo[i++] + "', "
          + //obs
          "'0' "
          + //sentar
          ")");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          //Proxima linha do arquivo de cadastros filtrados
          linha = file.readLine();

          System.out.println(id + "-------");
          output.add(id + "-------");

          //Incrementa o numero da ficha
          id++;

        }//fim while

        /////////////////////////////
        MIN_INDICE = MAX_INDICE + 1;
        MAX_INDICE += NUM_INDICES;
        numArquivos = MAX_INDICE;

        //Proximo arquivo
      }//fim while

      output.add("Importacao realizada com sucesso!");
      model.put("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.put("message", output);
      return "error";
    }

  }//fim importar2

  //Nao ha registros de preensao. Simplesmente crio os campos vazios
  @PostMapping("/importar3")
  String importar3( Map<String, Object> model) {

    iniciarValores();

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();

      //////////////////////////////////
      int numArquivos = 1;
      int id = this.ID;
      while (numArquivos < NUM_ARQUIVOS) {

        StringBuffer conteudo = new StringBuffer();

        //Abre o arquivo
        String arq = ARQUIVO_ENTRADA + MIN_INDICE + "_" + MAX_INDICE + ".txt";
        //---Local
        //BufferedReader file = new BufferedReader(new FileReader(arq));
        //---Local/Remote
        String linha="";
        ClassPathResource resource = new ClassPathResource(arq);
        BufferedReader file = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        //Primeira linha soh possui os cabecalhos das colunas
        linha = file.readLine();
        output.add(linha);

        //Proxima linha
        linha = file.readLine();
        output.add(linha);
        System.out.println(linha);

        String[] campo = new String[NUM_CAMPOS];
        StringTokenizer token = null;
        int numCampos = 0;
        int i = 0;

        //Prepara as variaveis para gravacao na BD
        Statement stmt = connection.createStatement();

        //id eh um numero sequencial
        while (linha != null) {
          ///////
          //////

          StringBuffer inserirLinha = new StringBuffer();
          inserirLinha.append("insert into preensao("
          + "ladodominante,"
          + "dir1, "
          + "esq1, "
          + "dir2, "
          + "esq2, "
          + "dir3, "
          + "esq3 "

          + ") values ( "
          +
          "'Esquerdo', "
          + //ladodominante
          "0, "
          + //dir1
          "0, "
          + //esq1
          "0, "
          + //dir2
          "0, "
          + //esq2
          "0, "
          + //dir3
          "0 "
          + //esq3
          ")");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          //Proxima linha do arquivo de cadastros filtrados
          linha = file.readLine();

          System.out.println(id + "-------");
          output.add(id + "-------");

          //Incrementa o numero da ficha
          id++;

        }//fim while

        /////////////////////////////
        MIN_INDICE = MAX_INDICE + 1;
        MAX_INDICE += NUM_INDICES;
        numArquivos = MAX_INDICE;

        //Proximo arquivo
      }//fim while

      output.add("Importacao realizada com sucesso!");
      model.put("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.put("message", output);
      return "error";
    }

  }//fim importar3

  @PostMapping("/importar4")
  String importar4( Map<String, Object> model) {

    iniciarValores();

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();

      //////////////////////////////////
      int numArquivos = 1;
      int id = this.ID;
      while (numArquivos < NUM_ARQUIVOS) {

        StringBuffer conteudo = new StringBuffer();

        //Abre o arquivo
        String arq = ARQUIVO_ENTRADA + MIN_INDICE + "_" + MAX_INDICE + ".txt";
        //---Local
        //BufferedReader file = new BufferedReader(new FileReader(arq));
        //---Local/Remote
        String linha="";
        ClassPathResource resource = new ClassPathResource(arq);
        BufferedReader file = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        //Primeira linha soh possui os cabecalhos das colunas
        linha = file.readLine();
        output.add(linha);

        //Proxima linha
        linha = file.readLine();
        output.add(linha);
        System.out.println(linha);

        String[] campo = new String[NUM_CAMPOS];
        StringTokenizer token = null;
        int numCampos = 0;
        int i = 0;

        //Prepara as variaveis para gravacao na BD
        Statement stmt = connection.createStatement();

        ////
        //Le os campos que serao gravados na BD, linha a linha

        //id eh um numero sequencial
        while (linha != null) {
          token = new StringTokenizer(linha, "|");
          numCampos = 0;

          //Salta os campos da secao anterior (Avaliacao)
          int j=0;
          while(j<35){
            token.nextToken();
            numCampos++;
            j++;
          }//

          i = 35;
          while (numCampos < NUM_CAMPOS) {

            //Fix1: Fixa campos nao preenchidos
            campo[i] = (String) token.nextToken().trim();
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }
            //Fix2: Substitui o simbolo de graus simples, caso existam, nos campos
            campo[i] = campo[i].replaceAll(",", ".");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix3: Substitui o simbolo de %
            campo[i] = campo[i].replaceAll("%", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix4: Substitui o simbolo de kg
            campo[i] = campo[i].replaceAll("kg", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix5: Substitui o simbolo de -
            campo[i] = campo[i].replaceAll("-", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix6: Substitui o simbolo de x
            campo[i] = campo[i].replaceAll("x", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix7: Substitui o simbolo de *
            campo[i] = campo[i].replaceAll("\\*", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix8: Substitui o simbolo de '
            campo[i] = campo[i].replaceAll("'", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            //Fix9: Substitui o simbolo de +
            campo[i] = campo[i].replaceAll("\\+", "");
            //Se apos a substituicao for nulo
            if (campo[i].length() == 0) {
              campo[i] = "0";
            }

            System.out.println(" (" + i + ")[" + campo[i] + "]");
            output.add(" (" + i + ")[" + campo[i] + "]");

            i++;
            numCampos++;
          }//fim while

          ///
          i = 35;
          System.out.println("\n");
          System.out.println("id: 0");
          System.out.println("("+i+")cintura: " + campo[i++]);
          System.out.println("("+i+")prof: " + campo[i++]);
          System.out.println("("+i+")dbraco: " + campo[i++]);
          System.out.println("("+i+")ebraco: " + campo[i++]);
          System.out.println("("+i+")quadril: " + campo[i++]);
          i++;
          System.out.println("("+i+")#dpantu: " + campo[i++]);
          int salto1 = i-1;
          System.out.println("("+i+")epantu: " + campo[i++]);
          System.out.println("("+i+")rqc: " + campo[i++]);
          i++;
          System.out.println("("+i+")dcoxa: " + campo[i++]);
          int salto2=i-1;
          System.out.println("("+i+")ecoxa: " + campo[i++]);
          System.out.println("("+i+")emediacoxa: " + campo[i++]);
          System.out.println("("+i+")dmediacoxa: " + campo[i++]);
          System.out.println("("+i+")resistencia: " + campo[i++]);
          System.out.println("("+i+")reactancia: " + campo[i++]);
          System.out.println("("+i+")agua float: " + campo[i++]);
          System.out.println("("+i+")mmagra float: " + campo[i++]);
          System.out.println("("+i+")mgordura float: " + campo[i++]);
          System.out.println("("+i+")massacel float: " + campo[i++]);
          System.out.println("("+i+")triceps float: " + campo[i++]);
          System.out.println("("+i+")subes float: " + campo[i++]);
          System.out.println("("+i+")supra float: " + campo[i++]);
          System.out.println("("+i+")abd float: " + campo[i++]);
          System.out.println("("+i+")coxa float: " + campo[i++]);
          System.out.println("("+i+")total  float: " + campo[i++]);

          ////////////////
          i=35;
          output.add("\n");
          output.add("id: 0");
          output.add("("+i+")cintura: " + campo[i++]);
          output.add("("+i+")prof: " + campo[i++]);
          output.add("("+i+")dbraco: " + campo[i++]);
          output.add("("+i+")ebraco: " + campo[i++]);
          output.add("("+i+")quadril: *" + campo[i++]);
          i++;
          output.add("("+i+")*dpantu: " + campo[i++]);
          output.add("("+i+")epantu: " + campo[i++]);
          output.add("("+i+")rqc: " + campo[i++]);
          i++;
          output.add("("+i+")dcoxa: " + campo[i++]);
          output.add("("+i+")ecoxa: " + campo[i++]);
          output.add("("+i+")emediacoxa: " + campo[i++]);
          output.add("("+i+")dmediacoxa: " + campo[i++]);
          output.add("("+i+")resistencia: " + campo[i++]);
          output.add("("+i+")reactancia: " + campo[i++]);
          output.add("("+i+")agua float: " + campo[i++]);
          output.add("("+i+")mmagra float: " + campo[i++]);
          output.add("("+i+")mgordura float: " + campo[i++]);
          output.add("("+i+")massacel float: " + campo[i++]);
          output.add("("+i+")triceps float: " + campo[i++]);
          output.add("("+i+")subes float: " + campo[i++]);
          output.add("("+i+")supra float: " + campo[i++]);
          output.add("("+i+")abd float: " + campo[i++]);
          output.add("("+i+")coxa float: " + campo[i++]);
          output.add("("+i+")total  float: " + campo[i++]);

          /////
          //Insere os campos na Base de Dados
          i = 35;

          StringBuffer inserirLinha = new StringBuffer();
          inserirLinha.append("insert into composicao("
          + "cintura, "
          + "prof, "
          + "dbraco, "
          + "ebraco, "
          + "quadril, "
          + "dpantu, "
          + "epantu, "
          + "rqc, "
          + "dcoxa, "
          + "ecoxa, "
          + "emediacoxa, "
          + "dmediacoxa, "
          + "resistencia, "
          + "reactancia, "
          + "agua, "
          + "mmagra, "
          + "mgordura, "
          + "massacel, "
          + "triceps, "
          + "subes, "
          + "supra, "
          + "abd, "
          + "coxa, "
          + "total, "
          + "compo_15_2,"
          + "compo_16_2,"
          + "compo_17_2"

          + ") values ( "
          +
          campo[i++] + ", "
          + //cintura
          "'"+ campo[i++] + "', "
          + //prof
          campo[i++] + ", "
          + //dbraco
          campo[i++] + ", "
          + //ebraco
          campo[i++] + ", "
          + //quadril
          campo[salto1++] + ", "
          + //dpantu

          campo[salto1++] + ", "
          + //epantu
          campo[salto1++] + ", "
          + //rqc
          campo[salto2++] + ", "
          + //dcoxa
          campo[salto2++] + ", "
          + //ecoxa
          campo[salto2++] + ", "
          + //emediacoxa
          campo[salto2++] + ", "
          + //dmediacoxa
          campo[salto2++] + ", "
          + //resistencia
          campo[salto2++] + ", "
          + //reactancia
          campo[salto2++] + ", "
          + //agua
          campo[salto2++] + ", "
          + //mmagra
          campo[salto2++] + ", "
          + //mgordura
          campo[salto2++] + ", "
          + //massacel
          campo[salto2++] + ", "
          + //triceps
          campo[salto2++] + ", "
          + //subes
          campo[salto2++] + ", "
          + //supra
          campo[salto2++] + ", "
          + //abd
          campo[salto2++] + ", "
          + //coxa
          campo[salto2++]  + ", "
          + //total
          "0, 0, 0" +

          ")");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          //Proxima linha do arquivo de cadastros filtrados
          linha = file.readLine();

          System.out.println(id + "-------");
          output.add(id + "-------");

          //Incrementa o numero da ficha
          id++;

        }//fim while

        /////////////////////////////
        MIN_INDICE = MAX_INDICE + 1;
        MAX_INDICE += NUM_INDICES;
        numArquivos = MAX_INDICE;

        //Proximo arquivo
      }//fim while

      output.add("Importacao realizada com sucesso!");
      model.put("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.put("message", output);
      return "error";
    }

  }//fim importar4

  @PostMapping("/importar5")
  String importar5( Map<String, Object> model) {

    iniciarValores();

    ArrayList<String> output = new ArrayList<String>();
    try {
      Connection connection = dataSource.getConnection();

      //////////////////////////////////
      int numArquivos = 1;
      int id = ID;
      while (numArquivos < NUM_ARQUIVOS) {

        StringBuffer conteudo = new StringBuffer();

        //Abre o arquivo
        String arq = ARQUIVO_ENTRADA + MIN_INDICE + "_" + MAX_INDICE + ".txt";
        //---Local
        //BufferedReader file = new BufferedReader(new FileReader(arq));
        //---Local/Remote
        String linha="";
        ClassPathResource resource = new ClassPathResource(arq);
        BufferedReader file = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        //Prepara as variaveis para gravacao na BD
        Statement stmt = connection.createStatement();

        linha = file.readLine();
        output.add(linha);

        //Proxima linha
        linha = file.readLine();
        output.add(linha);

        while (linha != null) {

          StringBuffer inserirLinha = new StringBuffer();

          //5.Avaliacaodor
          inserirLinha = new StringBuffer();
          inserirLinha.append("insert into avaliacaodor(dor1, dor2, dor3, dor4, dor5, dor6, dor7, dor8, dor9, dor10, dor11, dor12, dor13, dor14, dor15, dor16, dor_obs) values ( '0',0,'0',0,'0',0,'0',0,'0',0,'0',0,'0',0,'0',0, '0')");

          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          System.out.println(id + "-------");
          output.add(id + "-------");
          /////

          //6.Evolucaodor
          inserirLinha = new StringBuffer();
          inserirLinha.append("insert into evolucaodor(dor1, dor2, dor3, dor4, dor5, dor6, dor7, dor8, dor9, dor10, dor11, dor12, dor13, dor14, dor15, dor16, dor_obs) values ( '0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0')");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          System.out.println(id + "-------");
          output.add(id + "-------");

          //7.Gural
          inserirLinha = new StringBuffer();
          inserirLinha.append("insert into gural(gural1, gural2, gural3, gural4, gural5, gural6, gural7, gural8, gural9, gural10, gural11, gural12) values ( '0','0','0','0','0','0','0','0','0','0','0','0')");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          System.out.println(id + "-------");
          output.add(id + "-------");

          //8.Qualidade
          inserirLinha = new StringBuffer();
          inserirLinha.append("insert into qualidade(qualidade1, qualidade2, qualidade3, qualidade4, qualidade5, qualidade6, qualidade7, qualidade8, qualidade9, qualidade10, qualidade11, qualidade12, qualidade13, qualidade14, qualidade15, qualidade16, qualidade17, qualidade18) values ( '0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0')");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          System.out.println(id + "-------");
          output.add(id + "-------");

          //9.Forca
          inserirLinha = new StringBuffer();
          inserirLinha.append("insert into forca(forca1, forca2, forca3, forca4, forca5, forca6, forca7, forca8, forca9, forca10, forca11, forca12, forca13, forca14) values ( '0','0','0','0','0','0','0','0','0','0','0','0','0','0')");
          System.out.println(inserirLinha);
          output.add(inserirLinha.toString());
          stmt.executeUpdate(inserirLinha.toString());

          System.out.println(id + "-------");
          output.add(id + "-------");


          //Proxima linha do arquivo de cadastros filtrados
          linha = file.readLine();

          //Incrementa o numero da ficha
          id++;

        }//fim while

        /////////////////////////////
        MIN_INDICE = MAX_INDICE + 1;
        MAX_INDICE += NUM_INDICES;
        numArquivos = MAX_INDICE;

        //Proximo arquivo
      }//fim while

      output.add("Importacao realizada com sucesso!");
      model.put("message", output);
      return "error";

    } catch (Exception e) {
      output.add("Excecao1: " + e.getMessage());
      model.put("message", output);
      return "error";
    }

  }//fim importar5


  @PostMapping("/db")
  String db (Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("(1)"+(Integer) rs.getObject(1)+"|"+
        "(2)"+(String) rs.getObject(2)+"|"+
        "(3)"+(String) rs.getObject(3)+"|"+
        "(4)"+(String) rs.getObject(4)+"|"+
        "(5)"+(Integer) rs.getObject(5)+"|"+
        "(6)"+(String) rs.getObject(6)+"|"+
        "(7)"+(String) rs.getObject(7)+"|"+
        "(8)"+(String) rs.getObject(8)+"|"+
        "(9)"+(String) rs.getObject(9)+"|"+
        "(10)"+(String) rs.getObject(10)+"|"+
        "(11)"+(String) rs.getObject(11)+"|"+
        "(12)"+(String) rs.getObject(12)+"|"+
        "(13)"+(Double) rs.getObject(13)+"|"+
        "(14)"+(Double) rs.getObject(14)+"|"+
        "(15)"+(Double) rs.getObject(15));
      }

      model.put("records", output);

      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping("/exportar")
  public String exportar (
  @RequestParam("ava_0") String ava_0,
  @RequestParam("ava_cpf") String ava_cpf,
  @RequestParam("ava_1") String ava_1,
  @RequestParam("ava_2") String ava_2,
  @RequestParam("ava_3") String ava_3,
  @RequestParam("ava_4") String ava_4,
  @RequestParam("ava_5") String ava_5,
  @RequestParam("ava_6") String ava_6,
  @RequestParam("ava_7") String ava_7,
  @RequestParam("ava_8") String ava_8,
  @RequestParam("ava_9") String ava_9,
  @RequestParam("ava_10") String ava_10,
  @RequestParam("ava_11") String ava_11,
  @RequestParam("ava_12") String ava_12,
  @RequestParam("ava_13") String ava_13,
  @RequestParam("ava_14") String ava_14,

  @RequestParam("gonio_1") String gonio_1,
  @RequestParam("gonio_2") String gonio_2,
  @RequestParam("gonio_3") String gonio_3,
  @RequestParam("gonio_4") String gonio_4,
  @RequestParam("gonio_5") String gonio_5,
  @RequestParam("gonio_6") String gonio_6,
  @RequestParam("gonio_7") String gonio_7,
  @RequestParam("gonio_8") String gonio_8,
  @RequestParam("gonio_9") String gonio_9,
  @RequestParam("gonio_10") String gonio_10,
  @RequestParam("gonio_11") String gonio_11,
  @RequestParam("gonio_12") String gonio_12,
  @RequestParam("gonio_13") String gonio_13,
  @RequestParam("gonio_14") String gonio_14,
  @RequestParam("gonio_15") String gonio_15,
  @RequestParam("gonio_16") String gonio_16,
  @RequestParam("gonio_17") String gonio_17,
  @RequestParam("gonio_18") String gonio_18,
  @RequestParam("gonio_19") String gonio_19,
  @RequestParam("gonio_20") String gonio_20,
  @RequestParam("gonio_21") String gonio_21,
  @RequestParam("gonio_22") String gonio_22,
  @RequestParam("gonio_23") String gonio_23,
  @RequestParam("gonio_24") String gonio_24,
  @RequestParam("gonio_25") String gonio_25,
  @RequestParam("gonio_26") String gonio_26,
  @RequestParam("gonio_27") String gonio_27,
  @RequestParam("gonio_28") String gonio_28,
  @RequestParam("gonio_29") String gonio_29,
  @RequestParam("gonio_30") String gonio_30,
  @RequestParam("gonio_31") String gonio_31,
  @RequestParam("gonio_32") String gonio_32,
  @RequestParam("gonio_33") String gonio_33,
  @RequestParam("gonio_34") String gonio_34,
  @RequestParam("gonio_35") String gonio_35,
  @RequestParam("gonio_36") String gonio_36,
  @RequestParam("gonio_37") String gonio_37,

  @RequestParam("preensao_1") String preensao_1,
  @RequestParam("preensao_2") String preensao_2,
  @RequestParam("preensao_3") String preensao_3,
  @RequestParam("preensao_4") String preensao_4,
  @RequestParam("preensao_5") String preensao_5,
  @RequestParam("preensao_6") String preensao_6,
  @RequestParam("preensao_7") String preensao_7,

  @RequestParam("compo_1") String compo_1,
  @RequestParam("compo_2") String compo_2,
  @RequestParam("compo_3") String compo_3,
  @RequestParam("compo_4") String compo_4,
  @RequestParam("compo_5") String compo_5,
  @RequestParam("compo_6") String compo_6,
  @RequestParam("compo_7") String compo_7,
  @RequestParam("compo_8") String compo_8,
  @RequestParam("compo_9") String compo_9,
  @RequestParam("compo_10") String compo_10,
  @RequestParam("compo_11") String compo_11,
  @RequestParam("compo_12") String compo_12,
  @RequestParam("compo_13") String compo_13,
  @RequestParam("compo_14") String compo_14,
  @RequestParam("compo_15") String compo_15,
  @RequestParam("compo_15_2") String compo_15_2,
  @RequestParam("compo_16") String compo_16,
  @RequestParam("compo_16_2") String compo_16_2,
  @RequestParam("compo_17") String compo_17,
  @RequestParam("compo_17_2") String compo_17_2,
  @RequestParam("compo_18") String compo_18,
  @RequestParam("compo_19") String compo_19,
  @RequestParam("compo_20") String compo_20,
  @RequestParam("compo_21") String compo_21,
  @RequestParam("compo_22") String compo_22,
  @RequestParam("compo_23") String compo_23,
  @RequestParam("compo_24") String compo_24,
  @RequestParam("compo_25") String compo_25,
  @RequestParam("compo_26") String compo_26,
  @RequestParam("compo_27") String compo_27,
  @RequestParam("compo_28") String compo_28,
  @RequestParam("compo_29") String compo_29,
  @RequestParam("compo_30") String compo_30,
  @RequestParam("compo_31") String compo_31,
  @RequestParam("compo_32") String compo_32,
  @RequestParam("compo_33") String compo_33,
  @RequestParam("compo_34") String compo_34,
  @RequestParam("compo_35") String compo_35,
  @RequestParam("compo_36") String compo_36,

  @RequestParam("dor_1") String dor_1,
  @RequestParam("dor_2") String dor_2,
  @RequestParam("dor_3") String dor_3,
  @RequestParam("dor_4") String dor_4,
  @RequestParam("dor_5") String dor_5,
  @RequestParam("dor_6") String dor_6,
  @RequestParam("dor_7") String dor_7,
  @RequestParam("dor_8") String dor_8,
  @RequestParam("dor_9") String dor_9,
  @RequestParam("dor_10") String dor_10,
  @RequestParam("dor_11") String dor_11,
  @RequestParam("dor_12") String dor_12,
  @RequestParam("dor_13") String dor_13,
  @RequestParam("dor_14") String dor_14,
  @RequestParam("dor_15") String dor_15,
  @RequestParam("dor_16") String dor_16,
  @RequestParam("dor_obs") String dor_obs,

  @RequestParam("evolucaodor_1") String evolucaodor_1,
  @RequestParam("evolucaodor_2") String evolucaodor_2,
  @RequestParam("evolucaodor_3") String evolucaodor_3,
  @RequestParam("evolucaodor_4") String evolucaodor_4,
  @RequestParam("evolucaodor_5") String evolucaodor_5,
  @RequestParam("evolucaodor_6") String evolucaodor_6,
  @RequestParam("evolucaodor_7") String evolucaodor_7,
  @RequestParam("evolucaodor_8") String evolucaodor_8,
  @RequestParam("evolucaodor_9") String evolucaodor_9,
  @RequestParam("evolucaodor_10") String evolucaodor_10,
  @RequestParam("evolucaodor_11") String evolucaodor_11,
  @RequestParam("evolucaodor_12") String evolucaodor_12,
  @RequestParam("evolucaodor_13") String evolucaodor_13,
  @RequestParam("evolucaodor_14") String evolucaodor_14,
  @RequestParam("evolucaodor_15") String evolucaodor_15,
  @RequestParam("evolucaodor_16") String evolucaodor_16,
  @RequestParam("evolucaodor_obs") String evolucaodor_obs,

  @RequestParam("gural_1") String gural_1,
  @RequestParam("gural_2") String gural_2,
  @RequestParam("gural_3") String gural_3,
  @RequestParam("gural_4") String gural_4,
  @RequestParam("gural_5") String gural_5,
  @RequestParam("gural_6") String gural_6,
  @RequestParam("gural_7") String gural_7,
  @RequestParam("gural_8") String gural_8,
  @RequestParam("gural_9") String gural_9,
  @RequestParam("gural_10") String gural_10,
  @RequestParam("gural_11") String gural_11,
  @RequestParam("gural_12") String gural_12,

  @RequestParam("qualidade_1") String qualidade_1,
  @RequestParam("qualidade_2") String qualidade_2,
  @RequestParam("qualidade_3") String qualidade_3,
  @RequestParam("qualidade_4") String qualidade_4,
  @RequestParam("qualidade_5") String qualidade_5,
  @RequestParam("qualidade_6") String qualidade_6,
  @RequestParam("qualidade_7") String qualidade_7,
  @RequestParam("qualidade_8") String qualidade_8,
  @RequestParam("qualidade_9") String qualidade_9,
  @RequestParam("qualidade_10") String qualidade_10,
  @RequestParam("qualidade_11") String qualidade_11,
  @RequestParam("qualidade_12") String qualidade_12,
  @RequestParam("qualidade_13") String qualidade_13,
  @RequestParam("qualidade_14") String qualidade_14,
  @RequestParam("qualidade_15") String qualidade_15,
  @RequestParam("qualidade_16") String qualidade_16,
  @RequestParam("qualidade_17") String qualidade_17,
  @RequestParam("qualidade_18") String qualidade_18,

  @RequestParam("forca_1") String forca_1,
  @RequestParam("forca_2") String forca_2,
  @RequestParam("forca_3") String forca_3,
  @RequestParam("forca_4") String forca_4,
  @RequestParam("forca_5") String forca_5,
  @RequestParam("forca_6") String forca_6,
  @RequestParam("forca_7") String forca_7,
  @RequestParam("forca_8") String forca_8,
  @RequestParam("forca_9") String forca_9,
  @RequestParam("forca_10") String forca_10,
  @RequestParam("forca_11") String forca_11,
  @RequestParam("forca_12") String forca_12,
  @RequestParam("forca_13") String forca_13,
  @RequestParam("forca_14") String forca_14,

  @RequestParam("pressao_1") String pressao_1,
  @RequestParam("pressao_2") String pressao_2,
  @RequestParam("pressao_3") String pressao_3,
  @RequestParam("pressao_4") String pressao_4,
  @RequestParam("pressao_5") String pressao_5,

  Model model) {

    model.addAttribute("ava_0", ava_0);
    model.addAttribute("ava_1", ava_1);
    model.addAttribute("ava_2", ava_2);
    model.addAttribute("ava_3", ava_3);
    model.addAttribute("ava_4", ava_4);
    model.addAttribute("ava_5", ava_5);
    model.addAttribute("ava_6", ava_6);
    model.addAttribute("ava_7", ava_7);
    model.addAttribute("ava_8", ava_8);
    model.addAttribute("ava_9", ava_9);
    model.addAttribute("ava_10", ava_10);
    model.addAttribute("ava_11", ava_11);
    model.addAttribute("ava_12", ava_12);
    model.addAttribute("ava_13", ava_13);
    model.addAttribute("ava_14", ava_14);

    model.addAttribute("gonio_1", gonio_1);
    model.addAttribute("gonio_2", gonio_2);
    model.addAttribute("gonio_3", gonio_3);
    model.addAttribute("gonio_4", gonio_4);
    model.addAttribute("gonio_5", gonio_5);
    model.addAttribute("gonio_6", gonio_6);
    model.addAttribute("gonio_7", gonio_7);
    model.addAttribute("gonio_8", gonio_8);
    model.addAttribute("gonio_9", gonio_9);
    model.addAttribute("gonio_10", gonio_10);
    model.addAttribute("gonio_11", gonio_11);
    model.addAttribute("gonio_12", gonio_12);
    model.addAttribute("gonio_13", gonio_13);
    model.addAttribute("gonio_14", gonio_14);
    model.addAttribute("gonio_15", gonio_15);
    model.addAttribute("gonio_16", gonio_16);
    model.addAttribute("gonio_17", gonio_17);
    model.addAttribute("gonio_18", gonio_18);
    model.addAttribute("gonio_19", gonio_19);
    model.addAttribute("gonio_20", gonio_20);
    model.addAttribute("gonio_21", gonio_21);
    model.addAttribute("gonio_22", gonio_22);
    model.addAttribute("gonio_23", gonio_23);
    model.addAttribute("gonio_24", gonio_24);
    model.addAttribute("gonio_25", gonio_25);
    model.addAttribute("gonio_26", gonio_26);
    model.addAttribute("gonio_27", gonio_27);
    model.addAttribute("gonio_28", gonio_28);
    model.addAttribute("gonio_29", gonio_29);
    model.addAttribute("gonio_30", gonio_30);
    model.addAttribute("gonio_31", gonio_31);
    model.addAttribute("gonio_32", gonio_32);
    model.addAttribute("gonio_33", gonio_33);
    model.addAttribute("gonio_34", gonio_34);
    model.addAttribute("gonio_35", gonio_35);
    model.addAttribute("gonio_36", gonio_36);
    model.addAttribute("gonio_37", gonio_37);

    model.addAttribute("preensao_1", preensao_1);
    model.addAttribute("preensao_2", preensao_2);
    model.addAttribute("preensao_3", preensao_3);
    model.addAttribute("preensao_4", preensao_4);
    model.addAttribute("preensao_5", preensao_5);
    model.addAttribute("preensao_6", preensao_6);
    model.addAttribute("preensao_7", preensao_7);

    model.addAttribute("compo_1", compo_1);
    model.addAttribute("compo_2", compo_2);
    model.addAttribute("compo_3", compo_3);
    model.addAttribute("compo_4", compo_4);
    model.addAttribute("compo_5", compo_5);
    model.addAttribute("compo_6", compo_6);
    model.addAttribute("compo_7", compo_7);
    model.addAttribute("compo_8", compo_8);
    model.addAttribute("compo_9", compo_9);
    model.addAttribute("compo_10", compo_10);
    model.addAttribute("compo_11", compo_11);
    model.addAttribute("compo_12", compo_12);
    model.addAttribute("compo_13", compo_13);
    model.addAttribute("compo_14", compo_14);
    model.addAttribute("compo_15", compo_15);
    model.addAttribute("compo_15_2", compo_15_2);
    model.addAttribute("compo_16", compo_16);
    model.addAttribute("compo_16_2", compo_16_2);
    model.addAttribute("compo_17", compo_17);
    model.addAttribute("compo_17_2", compo_17);
    model.addAttribute("compo_18", compo_17);
    model.addAttribute("compo_19", compo_19);
    model.addAttribute("compo_20", compo_20);
    model.addAttribute("compo_21", compo_21);
    model.addAttribute("compo_22", compo_22);
    model.addAttribute("compo_23", compo_23);
    model.addAttribute("compo_24", compo_24);
    model.addAttribute("compo_25", compo_25);
    model.addAttribute("compo_26", compo_26);
    model.addAttribute("compo_27", compo_27);
    model.addAttribute("compo_28", compo_28);
    model.addAttribute("compo_29", compo_29);
    model.addAttribute("compo_30", compo_30);
    model.addAttribute("compo_31", compo_31);
    model.addAttribute("compo_32", compo_32);
    model.addAttribute("compo_33", compo_33);
    model.addAttribute("compo_34", compo_34);
    model.addAttribute("compo_35", compo_35);
    model.addAttribute("compo_36", compo_36);

    model.addAttribute("dor_1", dor_1);
    model.addAttribute("dor_2", dor_2);
    model.addAttribute("dor_3", dor_3);
    model.addAttribute("dor_4", dor_4);
    model.addAttribute("dor_5", dor_5);
    model.addAttribute("dor_6", dor_6);
    model.addAttribute("dor_7", dor_7);
    model.addAttribute("dor_8", dor_8);
    model.addAttribute("dor_9", dor_9);
    model.addAttribute("dor_10", dor_10);
    model.addAttribute("dor_11", dor_11);
    model.addAttribute("dor_12", dor_12);
    model.addAttribute("dor_13", dor_13);
    model.addAttribute("dor_14", dor_14);
    model.addAttribute("dor_15", dor_15);
    model.addAttribute("dor_16", dor_16);
    model.addAttribute("dor_obs", dor_obs);

    model.addAttribute("evolucaodor_1", evolucaodor_1);
    model.addAttribute("evolucaodor_2", evolucaodor_2);
    model.addAttribute("evolucaodor_3", evolucaodor_3);
    model.addAttribute("evolucaodor_4", evolucaodor_4);
    model.addAttribute("evolucaodor_5", evolucaodor_5);
    model.addAttribute("evolucaodor_6", evolucaodor_6);
    model.addAttribute("evolucaodor_7", evolucaodor_7);
    model.addAttribute("evolucaodor_8", evolucaodor_8);
    model.addAttribute("evolucaodor_9", evolucaodor_9);
    model.addAttribute("evolucaodor_10", evolucaodor_10);
    model.addAttribute("evolucaodor_11", evolucaodor_11);
    model.addAttribute("evolucaodor_12", evolucaodor_12);
    model.addAttribute("evolucaodor_13", evolucaodor_13);
    model.addAttribute("evolucaodor_14", evolucaodor_14);
    model.addAttribute("evolucaodor_15", evolucaodor_15);
    model.addAttribute("evolucaodor_16", evolucaodor_16);
    model.addAttribute("evolucaodor_obs", evolucaodor_obs);

    model.addAttribute("gural_1", gural_1);
    model.addAttribute("gural_2", gural_2);
    model.addAttribute("gural_3", gural_3);
    model.addAttribute("gural_4", gural_4);
    model.addAttribute("gural_5", gural_5);
    model.addAttribute("gural_6", gural_6);
    model.addAttribute("gural_7", gural_7);
    model.addAttribute("gural_8", gural_8);
    model.addAttribute("gural_9", gural_9);
    model.addAttribute("gural_10", gural_10);
    model.addAttribute("gural_11", gural_11);
    model.addAttribute("gural_12", gural_12);

    model.addAttribute("qualidade_1", qualidade_1);
    model.addAttribute("qualidade_2", qualidade_2);
    model.addAttribute("qualidade_3", qualidade_3);
    model.addAttribute("qualidade_4", qualidade_4);
    model.addAttribute("qualidade_5", qualidade_5);
    model.addAttribute("qualidade_6", qualidade_6);
    model.addAttribute("qualidade_7", qualidade_7);
    model.addAttribute("qualidade_8", qualidade_8);
    model.addAttribute("qualidade_9", qualidade_9);
    model.addAttribute("qualidade_10", qualidade_10);
    model.addAttribute("qualidade_11", qualidade_11);
    model.addAttribute("qualidade_12", qualidade_12);
    model.addAttribute("qualidade_13", qualidade_13);
    model.addAttribute("qualidade_14", qualidade_14);
    model.addAttribute("qualidade_15", qualidade_15);
    model.addAttribute("qualidade_16", qualidade_16);
    model.addAttribute("qualidade_17", qualidade_17);
    model.addAttribute("qualidade_18", qualidade_18);

    model.addAttribute("forca_1", forca_1);
    model.addAttribute("forca_2", forca_2);
    model.addAttribute("forca_3", forca_3);
    model.addAttribute("forca_4", forca_4);
    model.addAttribute("forca_5", forca_5);
    model.addAttribute("forca_6", forca_6);
    model.addAttribute("forca_7", forca_7);
    model.addAttribute("forca_8", forca_8);
    model.addAttribute("forca_9", forca_9);
    model.addAttribute("forca_10", forca_10);
    model.addAttribute("forca_11", forca_11);
    model.addAttribute("forca_12", forca_12);
    model.addAttribute("forca_13", forca_13);
    model.addAttribute("forca_14", forca_14);

    model.addAttribute("pressao_1", pressao_1);
    model.addAttribute("pressao_2", pressao_2);
    model.addAttribute("pressao_3", pressao_3);
    model.addAttribute("pressao_4", pressao_4);
    model.addAttribute("pressao_5", pressao_5);

    return "dashboard_exportar";
  }

  @PostMapping("/analisepeso")
  String analisepeso(@RequestParam("ava_0") String ava_0,
  @RequestParam("ava_log") String ava_log,
  Model model) {

    //Fix empty fields
    //if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao WHERE id="+ava_0);

      ArrayList<String> output = new ArrayList<String>();
      //Se achou a ficha
      if (rs.next()) {
        model.addAttribute("ava_0", (Integer) rs.getObject(1));
        model.addAttribute("ava_1", (String) rs.getObject(2));
        model.addAttribute("ava_2", (String) rs.getObject(3));
        model.addAttribute("ava_3", (Integer) rs.getObject(4));
        model.addAttribute("ava_4", (String) rs.getObject(5));
        model.addAttribute("ava_5", (String) rs.getObject(6));
        model.addAttribute("ava_6", (String) rs.getObject(7));
        model.addAttribute("ava_7", (String) rs.getObject(8));
        model.addAttribute("ava_8", (String) rs.getObject(9));
        model.addAttribute("ava_9", (String) rs.getObject(10));
        model.addAttribute("ava_10", (String) rs.getObject(11));
        model.addAttribute("ava_11", (Double) rs.getObject(12));
        model.addAttribute("ava_12", (Double) rs.getObject(13));
        model.addAttribute("ava_13", (Double) rs.getObject(14));
        model.addAttribute("ava_14", (Double) rs.getObject(15));

        model.addAttribute("ava_log", "Ficha aberta com sucesso!");

        //Nome
        String nome = (String) rs.getObject(5);
        //rs = stmt.executeQuery("SELECT peso FROM avaliacao WHERE nome='"+ava_4+"'");
        rs = stmt.executeQuery("SELECT peso,dataavaliacao FROM avaliacao where nome='"+nome+"' ORDER BY to_date(dataavaliacao,'DD/MM/YYYY') asc");
        String ava_peso="";
        while (rs.next()) {
          ava_peso += (Double) rs.getObject(1) + " ";
        }
        model.addAttribute("ava_peso", ava_peso);

      } else
      model.addAttribute("ava_log", "Ficha não encontrada.");
      return "dashboard_ficha";
    } catch (Exception e) {
      //model.put("message", e.getMessage());
      model.addAttribute("ava_log", e.getMessage());
      return "dashboard_ficha";
    }
  }



  @PostMapping("/abrir1")
  String abrir1(@RequestParam("ava_0") String ava_0,
  @RequestParam("ava_log") String ava_log,
  Model model) {

    //Fix empty fields
    //if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao WHERE id="+ava_0);

      ArrayList<String> output = new ArrayList<String>();
      //Se achou a ficha
      int i=1;
      if (rs.next()) {
        model.addAttribute("ava_0", (Integer) rs.getObject(i++)); //id
        model.addAttribute("ava_cpf", (String) rs.getObject(i++)); //cpf
        model.addAttribute("ava_1", (String) rs.getObject(i++)); //dataavaliacao
        model.addAttribute("ava_2", (String) rs.getObject(i++)); //datanascimento
        model.addAttribute("ava_3", (Integer) rs.getObject(i++)); //idade
        model.addAttribute("ava_4", (String) rs.getObject(i++)); //nome
        model.addAttribute("ava_5", (String) rs.getObject(i++)); //objetivo
        model.addAttribute("ava_6", (String) rs.getObject(i++)); //exercicios
        model.addAttribute("ava_7", (String) rs.getObject(i++)); //outrasAF
        model.addAttribute("ava_8", (String) rs.getObject(i++)); //especificar
        model.addAttribute("ava_9", (String) rs.getObject(i++)); //diagnostico
        model.addAttribute("ava_10", (String) rs.getObject(i++)); //medicacao
        model.addAttribute("ava_11", (Double) rs.getObject(i++)); //peso
        model.addAttribute("ava_12", (Double) rs.getObject(i++)); //altura
        model.addAttribute("ava_13", (Double) rs.getObject(i++)); //imc
        model.addAttribute("ava_14", (Double) rs.getObject(i++)); //fc

        model.addAttribute("ava_log", "Ficha aberta com sucesso!");

        //////////////////////
        //Nome
        String ava_4 = (String) rs.getObject(6);
        rs = stmt.executeQuery("SELECT peso FROM avaliacao WHERE nome='"+ava_4+"'");
        String ava_peso="";
        while (rs.next()) {
          ava_peso += (Double) rs.getObject(1) + " ";
        }
        model.addAttribute("ava_peso", ava_peso);
        ////////////////////////
      } else
      model.addAttribute("ava_log", "Ficha não encontrada.");


      //2.Goniometria
      rs = stmt.executeQuery("SELECT * FROM goniometria WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        model.addAttribute("gonio_1", (Integer) rs.getObject(2)); //ofdir
        model.addAttribute("gonio_2", (Integer) rs.getObject(3)); //ofesq
        String dor="Não";
        if ( !((String) rs.getObject(4)).equals("0")) dor="Sim";
        model.addAttribute("gonio_3", dor); //dor1
        model.addAttribute("gonio_4", (String) rs.getObject(5)); //ana1
        model.addAttribute("gonio_5", (String) rs.getObject(6)); //local1

        model.addAttribute("gonio_6", (Integer) rs.getObject(7)); //oredir
        model.addAttribute("gonio_7", (Integer) rs.getObject(8)); //oreesq
        dor="Não";
        if ( !((String) rs.getObject(9)).equals("0")) dor="Sim";
        model.addAttribute("gonio_8", dor); //dor2
        model.addAttribute("gonio_9", (String) rs.getObject(10)); //ana2
        model.addAttribute("gonio_10", (String) rs.getObject(11)); //local2

        model.addAttribute("gonio_11", (Integer) rs.getObject(12)); //qfdir
        model.addAttribute("gonio_12", (Integer) rs.getObject(13)); //qfesq
        dor="Não";
        if ( !((String) rs.getObject(14)).equals("0")) dor="Sim";
        model.addAttribute("gonio_13", dor); //dor3
        model.addAttribute("gonio_14", (String) rs.getObject(15)); //ana3
        model.addAttribute("gonio_15", (String) rs.getObject(16)); //local3

        model.addAttribute("gonio_16", (Integer) rs.getObject(17)); //qrdir
        model.addAttribute("gonio_17", (Integer) rs.getObject(18)); //qeesq
        dor="Não";
        if ( !((String) rs.getObject(19)).equals("0")) dor="Sim";
        model.addAttribute("gonio_18", dor); //dor4
        model.addAttribute("gonio_19", (String) rs.getObject(20)); //ana4
        model.addAttribute("gonio_20", (String) rs.getObject(21)); //local4

        model.addAttribute("gonio_21", (Integer) rs.getObject(22)); //jfdir
        model.addAttribute("gonio_22", (Integer) rs.getObject(23)); //jfesq
        dor="Não";
        if ( !((String) rs.getObject(24)).equals("0")) dor="Sim";
        model.addAttribute("gonio_23", dor); //dor5
        model.addAttribute("gonio_24", (String) rs.getObject(25)); //ana5
        model.addAttribute("gonio_25", (String) rs.getObject(26)); //local5

        model.addAttribute("gonio_26", (Integer) rs.getObject(27)); //tfddir
        model.addAttribute("gonio_27", (Integer) rs.getObject(28)); //tfdesq
        dor="Não";
        if ( !((String) rs.getObject(29)).equals("0")) dor="Sim";
        model.addAttribute("gonio_28", dor); //dor6
        model.addAttribute("gonio_29", (String) rs.getObject(30)); //ana6
        model.addAttribute("gonio_30", (String) rs.getObject(31)); //local6

        model.addAttribute("gonio_31", (Integer) rs.getObject(32)); //tfpdir
        model.addAttribute("gonio_32", (Integer) rs.getObject(33)); //tfpesq
        dor="Não";
        if ( !((String) rs.getObject(34)).equals("0")) dor="Sim";
        model.addAttribute("gonio_33", dor); //dor7
        model.addAttribute("gonio_34", (String) rs.getObject(35)); //ana7
        model.addAttribute("gonio_35", (String) rs.getObject(36)); //local7

        model.addAttribute("gonio_36", (String) rs.getObject(37)); //obs
        model.addAttribute("gonio_37", (String) rs.getObject(38)); //sentar

        model.addAttribute("gonio_log", "Ficha aberta com sucesso!");
      } else
      model.addAttribute("gonio_log", "Ficha não encontrada.");


      ////////////////////
      //3.Preensao
      rs = stmt.executeQuery("SELECT * FROM preensao WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        model.addAttribute("preensao_1", (String) rs.getObject(2)); //ladodominante
        model.addAttribute("preensao_2", (Double) rs.getObject(3)); //dir1
        model.addAttribute("preensao_3", (Double) rs.getObject(4)); //esq1
        model.addAttribute("preensao_4", (Double) rs.getObject(5)); //dir2
        model.addAttribute("preensao_5", (Double) rs.getObject(6)); //esq2
        model.addAttribute("preensao_6", (Double) rs.getObject(7)); //dir3
        model.addAttribute("preensao_7", (Double) rs.getObject(8)); //esq3

        model.addAttribute("preensao_log", "Ficha aberta com sucesso!");
      } else
      model.addAttribute("preensao_log", "Ficha não encontrada.");

      ////////////////////
      //4.Composicao
      rs = stmt.executeQuery("SELECT * FROM composicao WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("compo_1", (Double) rs.getObject(i++)); //cintura
        model.addAttribute("compo_2", (String) rs.getObject(i++)); //prof
        model.addAttribute("compo_3", (Double) rs.getObject(i++)); //dbraco
        model.addAttribute("compo_4", (Double) rs.getObject(i++)); //ebraco
        model.addAttribute("compo_5", (Double) rs.getObject(i++)); //quadril
        model.addAttribute("compo_6", (Double) rs.getObject(i++)); //dpantu
        model.addAttribute("compo_7", (Double) rs.getObject(i++)); //epantu
        model.addAttribute("compo_8", (Double) rs.getObject(i++)); //rqc
        model.addAttribute("compo_9", (Double) rs.getObject(i++)); //dcoxa
        model.addAttribute("compo_10", (Double) rs.getObject(i++)); //ecoxa
        model.addAttribute("compo_11", (Double) rs.getObject(i++)); //emediacoxa
        model.addAttribute("compo_12", (Double) rs.getObject(i++)); //dmediacoxa
        model.addAttribute("compo_13", (Double) rs.getObject(i++)); //resistencia
        model.addAttribute("compo_14", (Double) rs.getObject(i++)); //reactancia
        model.addAttribute("compo_15", (Double) rs.getObject(i++)); //agua
        model.addAttribute("compo_16", (Double) rs.getObject(i++)); //mmagra
        model.addAttribute("compo_17", (Double) rs.getObject(i++)); //mgordura
        model.addAttribute("compo_18", (Double) rs.getObject(i++)); //massacel
        model.addAttribute("compo_19", (Double) rs.getObject(i++)); //triceps
        model.addAttribute("compo_20", (Double) rs.getObject(i++)); //subes
        model.addAttribute("compo_21", (Double) rs.getObject(i++)); //supra
        model.addAttribute("compo_22", (Double) rs.getObject(i++)); //abd
        model.addAttribute("compo_23", (Double) rs.getObject(i++)); //coxa
        model.addAttribute("compo_24", (Double) rs.getObject(i++)); //total
        model.addAttribute("compo_15_2", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_16_2", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_17_2", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_25", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_26", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_27", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_28", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_29", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_30", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_31", (String) rs.getObject(i++)); //
        model.addAttribute("compo_32", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_33", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_34", (Double) rs.getObject(i++)); //
        model.addAttribute("compo_35", (String) rs.getObject(i++)); //
        model.addAttribute("compo_36", (Double) rs.getObject(i++)); //

        model.addAttribute("compo_log", "Ficha aberta com sucesso!");
      } else
      model.addAttribute("compo_log", "Ficha não encontrada.");

      ////////////////////
      //5.Avaliacao da Dor
      rs = stmt.executeQuery("SELECT * FROM avaliacaodor WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("dor_1", (String) rs.getObject(i++)); //
        model.addAttribute("dor_2", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_3", (String) rs.getObject(i++)); //
        model.addAttribute("dor_4", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_5", (String) rs.getObject(i++)); //
        model.addAttribute("dor_6", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_7", (String) rs.getObject(i++)); //
        model.addAttribute("dor_8", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_9", (String) rs.getObject(i++)); //
        model.addAttribute("dor_10", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_11", (String) rs.getObject(i++)); //
        model.addAttribute("dor_12", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_13", (String) rs.getObject(i++)); //
        model.addAttribute("dor_14", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_15", (String) rs.getObject(i++)); //
        model.addAttribute("dor_16", (Integer) rs.getObject(i++)); //
        model.addAttribute("dor_obs", (String) rs.getObject(i++)); //

        model.addAttribute("dor_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("dor_log", "Ficha não encontrada.");

      ////////////////
      //6.Evolucao da Dor
      rs = stmt.executeQuery("SELECT * FROM evolucaodor WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("evolucaodor_1", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_2", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_3", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_4", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_5", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_6", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_7", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_8", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_9", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_10", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_11", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_12", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_13", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_14", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_15", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_16", (String) rs.getObject(i++)); //
        model.addAttribute("evolucaodor_obs", (String) rs.getObject(i++)); //

        model.addAttribute("evolucaodor_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("evolucaodor_log", "Ficha não encontrada.");

      ////////////////
      //7.Gural
      rs = stmt.executeQuery("SELECT * FROM gural WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("gural_1", (String) rs.getObject(i++)); //
        model.addAttribute("gural_2", (String) rs.getObject(i++)); //
        model.addAttribute("gural_3", (String) rs.getObject(i++)); //
        model.addAttribute("gural_4", (String) rs.getObject(i++)); //
        model.addAttribute("gural_5", (String) rs.getObject(i++)); //
        model.addAttribute("gural_6", (String) rs.getObject(i++)); //
        model.addAttribute("gural_7", (String) rs.getObject(i++)); //
        model.addAttribute("gural_8", (String) rs.getObject(i++)); //
        model.addAttribute("gural_9", (String) rs.getObject(i++)); //
        model.addAttribute("gural_10", (String) rs.getObject(i++)); //
        model.addAttribute("gural_11", (String) rs.getObject(i++)); //
        model.addAttribute("gural_12", (String) rs.getObject(i++)); //

        model.addAttribute("gural_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("gural_log", "Ficha não encontrada.");

      ////////////////
      //8.Qualidade
      rs = stmt.executeQuery("SELECT * FROM qualidade WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("qualidade_1", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_2", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_3", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_4", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_5", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_6", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_7", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_8", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_9", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_10", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_11", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_12", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_13", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_14", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_15", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_16", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_17", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_18", (String) rs.getObject(i++)); //
        model.addAttribute("qualidade_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("qualidade_log", "Ficha não encontrada.");

      ////////////////
      //9.Forca
      rs = stmt.executeQuery("SELECT * FROM forca WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("forca_1", (String) rs.getObject(i++)); //
        model.addAttribute("forca_2", (String) rs.getObject(i++)); //
        model.addAttribute("forca_3", (String) rs.getObject(i++)); //
        model.addAttribute("forca_4", (String) rs.getObject(i++)); //
        model.addAttribute("forca_5", (String) rs.getObject(i++)); //
        model.addAttribute("forca_6", (String) rs.getObject(i++)); //
        model.addAttribute("forca_7", (String) rs.getObject(i++)); //
        model.addAttribute("forca_8", (String) rs.getObject(i++)); //
        model.addAttribute("forca_9", (String) rs.getObject(i++)); //
        model.addAttribute("forca_10", (String) rs.getObject(i++)); //
        model.addAttribute("forca_11", (String) rs.getObject(i++)); //
        model.addAttribute("forca_12", (String) rs.getObject(i++)); //
        model.addAttribute("forca_13", (String) rs.getObject(i++)); //
        model.addAttribute("forca_14", (String) rs.getObject(i++)); //
        model.addAttribute("forca_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("forca_log", "Ficha não encontrada.");

      ////////////////
      //10.Pressao
      rs = stmt.executeQuery("SELECT * FROM pressao WHERE id="+ava_0);
      //Se achou a ficha
      if (rs.next()) {
        //model.addAttribute("ava_0", (Integer) rs.getObject(1)); //id
        i=2;
        model.addAttribute("pressao_1", (String) rs.getObject(i++)); //
        model.addAttribute("pressao_2", (String) rs.getObject(i++)); //
        model.addAttribute("pressao_3", (String) rs.getObject(i++)); //
        model.addAttribute("pressao_4", (String) rs.getObject(i++)); //
        model.addAttribute("pressao_5", (String) rs.getObject(i++)); //

        model.addAttribute("pressao_log", "Ficha aberta com sucesso!");

      } else
      model.addAttribute("pressao_log", "Ficha não encontrada.");

      return "dashboard_ficha";

    } catch (Exception e) {
      //model.put("message", e.getMessage());
      model.addAttribute("ava_log", "LOG: " + e.getMessage() + " ava_0: " + ava_0);
      return "dashboard_ficha";
    }
  }


  @PostMapping("/novo1")
  String novo1(//@RequestParam("ava_0") String ava_0,
  @RequestParam("ava_log") String ava_log,
  Model model) {

    /*	//Fix empty fields
    if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);
    //Retorna um valor de numero de ficha valido
    try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    ResultSet rs = stmt.executeQuery("SELECT id FROM avaliacao");
    ArrayList<String> output = new ArrayList<String>();
    //Percorre todas as fichas, e adquire o índice da ultima
    int numeroFicha=0;
    while (rs.next()) {
    numeroFicha=(Integer)rs.getObject(1);
  }
  numeroFicha++;
  model.addAttribute("ava_0",numeroFicha);
  model.addAttribute("ava_log","Nova ficha aberta.");
  return "dashboard_ficha";
} catch (Exception e) {
model.addAttribute("ava_log", "LOG: " + e.getMessage() + " ava_0: " + ava_0);
return "dashboard_ficha";
}
*/
return "dashboard_ficha";
}//fim novo1

@PostMapping("/abrirAnalises")
String abrirAnalises(@RequestParam("ava_0") String ava_0,
@RequestParam("ava_log") String ava_log,
Model model) {

  //Fix empty fields
  //if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);

  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao WHERE id="+ava_0);

    ArrayList<String> output = new ArrayList<String>();
    //Se achou a ficha
    int i=1;
    if (rs.next()) {
      model.addAttribute("ava_0", (Integer) rs.getObject(i++)); //id
      model.addAttribute("ava_cpf", (String) rs.getObject(i++)); //cpf
      model.addAttribute("ava_1", (String) rs.getObject(i++)); //dataavaliacao
      model.addAttribute("ava_2", (String) rs.getObject(i++)); //datanascimento
      model.addAttribute("ava_3", (Integer) rs.getObject(i++)); //idade
      model.addAttribute("ava_4", (String) rs.getObject(i++)); //nome
      model.addAttribute("ava_5", (String) rs.getObject(i++)); //objetivo
      model.addAttribute("ava_6", (String) rs.getObject(i++)); //exercicios
      model.addAttribute("ava_7", (String) rs.getObject(i++)); //outrasAF
      model.addAttribute("ava_8", (String) rs.getObject(i++)); //especificar
      model.addAttribute("ava_9", (String) rs.getObject(i++)); //diagnostico
      model.addAttribute("ava_10", (String) rs.getObject(i++)); //medicacao
      model.addAttribute("ava_11", (Double) rs.getObject(i++)); //peso
      model.addAttribute("ava_12", (Double) rs.getObject(i++)); //altura
      model.addAttribute("ava_13", (Double) rs.getObject(i++)); //imc
      model.addAttribute("ava_14", (Double) rs.getObject(i++)); //fc

      model.addAttribute("ava_log", "Ficha aberta com sucesso!");

      //////////////////////
      //Nome
      String ava_4 = (String) rs.getObject(6);
      rs = stmt.executeQuery("SELECT peso FROM avaliacao WHERE nome='"+ava_4+"'");
      String ava_peso="";
      while (rs.next()) {
        ava_peso += (Double) rs.getObject(1) + " ";
      }
      model.addAttribute("ava_peso", ava_peso);
      ////////////////////////
    } else
    model.addAttribute("ava_log", "Ficha não encontrada.");

    return "cadastro_alimentos";

  } catch (Exception e) {
    //model.put("message", e.getMessage());
    model.addAttribute("ava_log", "LOG: " + e.getMessage() + " ava_0: " + ava_0);
    return "cadastro_alimentos";
  }
}

@PostMapping("/remover1")
String remover1(@RequestParam("ava_0") String ava_0,
@RequestParam("ava_log") String ava_log,
Model model) {

  //Fix empty fields
  //if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);

  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao WHERE id="+ava_0);

    ArrayList<String> output = new ArrayList<String>();
    //Se achou a ficha
    if (rs.next()) {


      stmt.executeUpdate("DELETE FROM goniometria WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM preensao WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM composicao WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM goniometria WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM avaliacaodor WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM evolucaodor WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM gural WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM qualidade WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM forca WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM pressao WHERE id="+ava_0);
      stmt.executeUpdate("DELETE FROM avaliacao WHERE id="+ava_0);//chave primaria

      model.addAttribute("ava_log", "Ficha excluída com sucesso!");
    } else
    model.addAttribute("ava_log", "Ficha não encontrada.");
    return "dashboard_ficha";
  } catch (Exception e) {
    //model.put("message", e.getMessage());
    model.addAttribute("ava_log", e.getMessage());
    return "dashboard_ficha";
  }
}

@PostMapping("/login")
public String loginPost( //login q trata da parte visual
@RequestParam("email") String email, //parte remota - parte local
@RequestParam("senha") String senha,
Model model) { //mapeamento com o modelo da parte visual

  model.addAttribute("email", email);
  model.addAttribute("senha", senha);

  //if ( email.equals("a@a") && senha.equals("a") )
  //    return "dashboard_ficha";

  ArrayList<String> output = new ArrayList<String>();
  try {
    Connection connection = dataSource.getConnection();
    //Prepara as variaveis para gravacao na BD
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT senha FROM usuarios WHERE email='"+email+"'");

    if (rs.next()) {
      //Verifica a senha do e-mail
      String candidate=senha;
      String hashed=(String)rs.getObject(1); //get le o objeto
      if (BCrypt.checkpw(candidate, hashed)){ //classe q verifica se a senha é a mesma na bd
        output.add("Usuario e senha corretos!");
        return "menugeladeira"; //onde retorna na parte visual após logar
      }
      else
      output.add("Senha invalida!");
    } else
    //Na verdade, apenas o usuario nao foi encontrado
    output.add("Usuário ou Senha invalida!");

    model.addAttribute("message", output);
    return "error";

  } catch (Exception e) {
    output.add("Excecao1: " + e.getMessage());
    model.addAttribute("message", output);
    return "error";
  }

}

@PostMapping("/inserirUsuario")
String inserirUsuario( @RequestParam("inserir_1") String nome,
@RequestParam("inserir_2") String email,
@RequestParam("inserir_3") String senha,
@RequestParam("inserir_4") String confirmasenha,
@RequestParam("inserir_log") String inserir_log,
Model model) {

  model.addAttribute("inserir_1", nome);
  model.addAttribute("inserir_2", email);
  model.addAttribute("inserir_log", inserir_log);

  ArrayList<String> output = new ArrayList<String>();
  if (!senha.equals(confirmasenha)){
    output.add("Erro: Senhas não conferem. Usuário NÃO foi cadastrado.");
    model.addAttribute("message", output);
    model.addAttribute("inserir_log", output);
    return "dashboard_usuarios";
  }

  // Hash a password for the first time
  String hashed = BCrypt.hashpw(senha, BCrypt.gensalt());
  try {
    Connection connection = dataSource.getConnection();

    //Prepara as variaveis para gravacao na BD
    Statement stmt = connection.createStatement();

    //Verificar se jah existe usuario. Se sim, atualiza.
    ResultSet rs = stmt.executeQuery("SELECT email FROM usuarios WHERE email='"+email+"'");
    if (rs.next()){
      stmt.executeUpdate("UPDATE usuarios SET nome='"+ nome +"', senha='"+hashed+"' WHERE email='"+email+"'");
      output.add("LOG: Registro atualizado!");
      model.addAttribute("inserir_log", output);
      return "dashboard_usuarios";
    } else {
      //Se nao existe, insere um novo

      /////
      //Insere os campos na Base de Dados
      StringBuffer inserirLinha = new StringBuffer();
      //'id' eh incrementado para cada novo usuario
      inserirLinha.append("insert into usuarios("
      + "nome,"
      + "email,"
      + "senha "
      + ") values ( "
      + //nome
      "'" + nome + "', "
      + //email
      "'" + email + "', "
      + //senha encriptada
      "'" + hashed + "'" +
      ")");
      System.out.println(inserirLinha);
      //output.add(inserirLinha.toString());
      stmt.executeUpdate(inserirLinha.toString());

      output.add("LOG: Usuário cadastrado com sucesso!");
      model.addAttribute("inserir_log", output);
      return "dashboard_usuarios";
    }//fim else

  } catch (Exception e) {
    output.add("Excecao1: " + e.getMessage());
    model.addAttribute("inserir_log", output);
    return "dashboard_usuarios";
  }

}//fim inserirUsuarios

@PostMapping("/removerUsuario")
String removerUsuario(
@RequestParam("remover_1") String email,
@RequestParam("remover_log") String remover_log,
Model model) {

  model.addAttribute("email", email);
  model.addAttribute("remover_log", remover_log);

  ArrayList<String> output = new ArrayList<String>();
  if (email.equals("admin@admin")){
    output.add("LOG: Usuário 'admin' não pode ser removido!");

    model.addAttribute("remover_log", output);
    return "dashboard_usuarios";
  }

  try {
    Connection connection = dataSource.getConnection();

    //Prepara as variaveis para gravacao na BD
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT email FROM usuarios WHERE email='"+email+"'");
    if (rs.next()){
      stmt.executeUpdate("delete from usuarios where email='"+email+"'");
      output.add("LOG: Usuário removido com sucesso!");
    } else
    output.add("LOG: Usuário não encontrado!");

    model.addAttribute("remover_log", output);
    return "dashboard_usuarios";

  } catch (Exception e) {
    output.add("Excecao1: " + e.getMessage());
    model.addAttribute("remover_log", output);
    return "dashboard_usuarios";
  }

}//fim removerUsuario

@PostMapping("/atualizarUsuario")
String atualizarUsuario( @RequestParam("inserir_1") String nome,
@RequestParam("inserir_2") String email,
Model model) {

  model.addAttribute("inserir_1", nome);
  model.addAttribute("inserir_2", email);

  return "dashboard_usuarios";

}//atualizarUsuario

@PostMapping("/dashboard_lista")
public String dashboard_lista(Model model) {

  //Mapeamento da lista de fichas
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT id,nome FROM avaliacao");

    ArrayList<Ficha> output = new ArrayList<Ficha>();
    while (rs.next())
    output.add(new Ficha((Integer) rs.getObject(1), (String) rs.getObject(2)));

    model.addAttribute("listaFichas", output);

    return "dashboard_lista";

  } catch (Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }

}


@PostMapping("/cadastro_alimentos")
public String cadastro_alimentos(Model model) {

  return "cadastro_alimentos";

}

@PostMapping("/dashboard_usuarios")
public String dashboard_usuarios(Model model) {

  return "dashboard_usuarios";

}


@PostMapping("/listarUsuarios")
public String listarUsuarios(Model model) {

  //Mapeamento da lista de fichas
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT id,nome,email FROM usuarios");

    ArrayList<Usuario> output = new ArrayList<Usuario>();
    while (rs.next())
    output.add(new Usuario((Integer) rs.getObject(1), (String) rs.getObject(2), (String) rs.getObject(3)));

    model.addAttribute("listaUsuarios", output);

    return "dashboard_listausuarios";

  } catch (Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }

}

@PostMapping("/importarTudo")
public String importarTudo(Map<String, Object> model) {

  importar1(model);
  importar2(model);
  importar3(model);
  importar4(model);
  importar5(model);

  ArrayList<String> output = new ArrayList<String>();
  output.add("Importação realizada com sucesso!");
  model.put("message", output);
  return "error";
}

@PostMapping("/excluirTudo")
public String excluirTudo(Map<String, Object> model) {

  String ava_log="";
  ArrayList<String> output = new ArrayList<String>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    stmt.executeUpdate("drop table avaliacaodor");
    //--
    stmt.executeUpdate("drop table composicao");
    stmt.executeUpdate("drop table evolucaodor");
    stmt.executeUpdate("drop table forca");
    //--
    stmt.executeUpdate("drop table goniometria");
    stmt.executeUpdate("drop table gural");
    //--
    stmt.executeUpdate("drop table preensao");
    stmt.executeUpdate("drop table pressao");
    stmt.executeUpdate("drop table qualidade");
    //--
    stmt.executeUpdate("drop table usuarios");
    //--
    stmt.executeUpdate("drop table avaliacao");

    output.add("[drop table: OK] ");
    model.put("message", output);
    return "error";

  } catch (Exception e) {

    output.add("[drop table: FAIL " + e.getMessage() + "]");
    model.put("message", output);
    return "error";
  }

}


@PostMapping("/excluirbase1")
public String excluirbase1(Map<String, Object> model) {

  String ava_log="";
  ArrayList<String> output = new ArrayList<String>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("drop table avaliacao");

    output.add("[drop table: OK] ");
    model.put("message", output);
    return "error";

  } catch (Exception e) {

    output.add("[drop table: FAIL " + e.getMessage() + "]");
    model.put("message", output);
    return "error";
  }

}

@PostMapping("/excluirbase5")
public String excluirbase5(Map<String, Object> model) {

  String ava_log="";
  ArrayList<String> output = new ArrayList<String>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("drop table evolucaodor");

    output.add("[drop table: OK] ");
    model.put("message", output);
    return "error";

  } catch (Exception e) {

    output.add("[drop table: FAIL " + e.getMessage() + "]");
    model.put("message", output);
    return "error";
  }

}//fim excluirbase5

@PostMapping("/excluirbase6")
public String excluirbase6(Map<String, Object> model) {

  String ava_log="";
  ArrayList<String> output = new ArrayList<String>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("drop table usuarios");

    output.add("[drop table: OK] ");
    model.put("message", output);
    return "error";

  } catch (Exception e) {

    output.add("[drop table: FAIL " + e.getMessage() + "]");
    model.put("message", output);
    return "error";
  }

}//fim excluirbase6




@PostMapping("/excluirbase4")
public String excluirbase4(Map<String, Object> model) {

  String ava_log="";
  ArrayList<String> output = new ArrayList<String>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("drop table composicao");

    output.add("[drop table: OK] ");
    model.put("message", output);
    return "error";

  } catch (Exception e) {

    output.add("[drop table: FAIL " + e.getMessage() + "]");
    model.put("message", output);
    return "error";
  }

}

@PostMapping("/gravar10")
public String gravar10(
@RequestParam("ava_0") String ava_0,
@RequestParam("ava_cpf") String ava_cpf,
@RequestParam("ava_1") String ava_1,
@RequestParam("ava_2") String ava_2,
@RequestParam("ava_3") String ava_3,
@RequestParam("ava_4") String ava_4,
@RequestParam("ava_5") String ava_5,
@RequestParam("ava_6") String ava_6,
@RequestParam("ava_7") String ava_7,
@RequestParam("ava_8") String ava_8,
@RequestParam("ava_9") String ava_9,
@RequestParam("ava_10") String ava_10,
@RequestParam("ava_11") String ava_11,
@RequestParam("ava_12") String ava_12,
@RequestParam("ava_13") String ava_13,
@RequestParam("ava_14") String ava_14,
@RequestParam("ava_log") String ava_log,

//////////////////////////
//@RequestParam("ava_0") String ava_0,
@RequestParam("gonio_1") String gonio_1, //ofdir
@RequestParam("gonio_2") String gonio_2, //ofesq
@RequestParam("gonio_3") String gonio_3, //dor1
@RequestParam("gonio_4") String gonio_4, //ana1
@RequestParam("gonio_5") String gonio_5, //local1
@RequestParam("gonio_6") String gonio_6, //oredir
@RequestParam("gonio_7") String gonio_7, //oreesq
@RequestParam("gonio_8") String gonio_8, //dor2
@RequestParam("gonio_9") String gonio_9, //ana2
@RequestParam("gonio_10") String gonio_10, //local2
@RequestParam("gonio_11") String gonio_11, //qfdir
@RequestParam("gonio_12") String gonio_12, //qfesq
@RequestParam("gonio_13") String gonio_13, //dor3
@RequestParam("gonio_14") String gonio_14, //ana3
@RequestParam("gonio_15") String gonio_15, //local3
@RequestParam("gonio_16") String gonio_16, //qredir
@RequestParam("gonio_17") String gonio_17, //qreesq
@RequestParam("gonio_18") String gonio_18, //dor4
@RequestParam("gonio_19") String gonio_19, //ana4
@RequestParam("gonio_20") String gonio_20, //local4
@RequestParam("gonio_21") String gonio_21, //jfdir
@RequestParam("gonio_22") String gonio_22, //jfesq
@RequestParam("gonio_23") String gonio_23, //dor5
@RequestParam("gonio_24") String gonio_24, //ana5
@RequestParam("gonio_25") String gonio_25, //local5
@RequestParam("gonio_26") String gonio_26, //tfddir
@RequestParam("gonio_27") String gonio_27, //tfdesq
@RequestParam("gonio_28") String gonio_28, //dor6
@RequestParam("gonio_29") String gonio_29, //ana6
@RequestParam("gonio_30") String gonio_30, //local6
@RequestParam("gonio_31") String gonio_31, //tfpdir
@RequestParam("gonio_32") String gonio_32, //tfpesq
@RequestParam("gonio_33") String gonio_33, //dor7
@RequestParam("gonio_34") String gonio_34, //ana7
@RequestParam("gonio_35") String gonio_35, //local7
@RequestParam("gonio_36") String gonio_36, //obs
@RequestParam("gonio_37") String gonio_37,	//sentar
@RequestParam("gonio_log") String gonio_log,

@RequestParam("preensao_1") String preensao_1,	//
@RequestParam("preensao_2") String preensao_2,	//
@RequestParam("preensao_3") String preensao_3,	//
@RequestParam("preensao_4") String preensao_4,	//
@RequestParam("preensao_5") String preensao_5,	//
@RequestParam("preensao_6") String preensao_6,	//
@RequestParam("preensao_7") String preensao_7,	//
@RequestParam("preensao_log") String preensao_log,	//

/////////
@RequestParam("compo_1") String compo_1,	//
@RequestParam("compo_2") String compo_2,	//
@RequestParam("compo_3") String compo_3,	//
@RequestParam("compo_4") String compo_4,	//
@RequestParam("compo_5") String compo_5,	//
@RequestParam("compo_6") String compo_6,	//
@RequestParam("compo_7") String compo_7,	//
@RequestParam("compo_8") String compo_8,	//
@RequestParam("compo_9") String compo_9,	//
@RequestParam("compo_10") String compo_10,	//
@RequestParam("compo_11") String compo_11,	//
@RequestParam("compo_12") String compo_12,	//
@RequestParam("compo_13") String compo_13,	//
@RequestParam("compo_14") String compo_14,	//
@RequestParam("compo_15") String compo_15,	//
@RequestParam("compo_15_2") String compo_15_2,	//
@RequestParam("compo_16") String compo_16,	//
@RequestParam("compo_16_2") String compo_16_2,	//
@RequestParam("compo_17") String compo_17,	//
@RequestParam("compo_17_2") String compo_17_2,	//
@RequestParam("compo_18") String compo_18,	//
@RequestParam("compo_19") String compo_19,	//
@RequestParam("compo_20") String compo_20,	//
@RequestParam("compo_21") String compo_21,	//
@RequestParam("compo_22") String compo_22,	//
@RequestParam("compo_23") String compo_23,	//
@RequestParam("compo_24") String compo_24,	//
@RequestParam("compo_25") String compo_25,	//
@RequestParam("compo_26") String compo_26,	//
@RequestParam("compo_27") String compo_27,	//
@RequestParam("compo_28") String compo_28,	//
@RequestParam("compo_29") String compo_29,	//
@RequestParam("compo_30") String compo_30,	//
@RequestParam("compo_31") String compo_31,	//
@RequestParam("compo_32") String compo_32,	//
@RequestParam("compo_33") String compo_33,	//
@RequestParam("compo_34") String compo_34,	//
@RequestParam("compo_35") String compo_35,	//
@RequestParam("compo_36") String compo_36,	//
@RequestParam("compo_log") String compo_log,	//

@RequestParam("dor_1") String dor_1,	//
@RequestParam("dor_2") String dor_2,	//
@RequestParam("dor_3") String dor_3,	//
@RequestParam("dor_4") String dor_4,	//
@RequestParam("dor_5") String dor_5,	//
@RequestParam("dor_6") String dor_6,	//
@RequestParam("dor_7") String dor_7,	//
@RequestParam("dor_8") String dor_8,	//
@RequestParam("dor_9") String dor_9,	//
@RequestParam("dor_10") String dor_10,	//
@RequestParam("dor_11") String dor_11,	//
@RequestParam("dor_12") String dor_12,	//
@RequestParam("dor_13") String dor_13,	//
@RequestParam("dor_14") String dor_14,	//
@RequestParam("dor_15") String dor_15,	//
@RequestParam("dor_16") String dor_16,	//
@RequestParam("dor_obs") String dor_obs,	//
@RequestParam("dor_log") String dor_log,	//

@RequestParam("evolucaodor_1") String evolucaodor_1,	//
@RequestParam("evolucaodor_2") String evolucaodor_2,	//
@RequestParam("evolucaodor_3") String evolucaodor_3,	//
@RequestParam("evolucaodor_4") String evolucaodor_4,	//
@RequestParam("evolucaodor_5") String evolucaodor_5,	//
@RequestParam("evolucaodor_6") String evolucaodor_6,	//
@RequestParam("evolucaodor_7") String evolucaodor_7,	//
@RequestParam("evolucaodor_8") String evolucaodor_8,	//
@RequestParam("evolucaodor_9") String evolucaodor_9,	//
@RequestParam("evolucaodor_10") String evolucaodor_10,	//
@RequestParam("evolucaodor_11") String evolucaodor_11,	//
@RequestParam("evolucaodor_12") String evolucaodor_12,	//
@RequestParam("evolucaodor_13") String evolucaodor_13,	//
@RequestParam("evolucaodor_14") String evolucaodor_14,	//
@RequestParam("evolucaodor_15") String evolucaodor_15,	//
@RequestParam("evolucaodor_16") String evolucaodor_16,	//
@RequestParam("evolucaodor_obs") String evolucaodor_obs,	//
@RequestParam("evolucaodor_log") String evolucaodor_log,	//

@RequestParam("gural_1") String gural_1,	//
@RequestParam("gural_2") String gural_2,	//
@RequestParam("gural_3") String gural_3,	//
@RequestParam("gural_4") String gural_4,	//
@RequestParam("gural_5") String gural_5,	//
@RequestParam("gural_6") String gural_6,	//
@RequestParam("gural_7") String gural_7,	//
@RequestParam("gural_8") String gural_8,	//
@RequestParam("gural_9") String gural_9,	//
@RequestParam("gural_10") String gural_10,	//
@RequestParam("gural_11") String gural_11,	//
@RequestParam("gural_12") String gural_12,	//
@RequestParam("gural_log") String gural_log,	//

@RequestParam("qualidade_1") String qualidade_1,	//
@RequestParam("qualidade_2") String qualidade_2,	//
@RequestParam("qualidade_3") String qualidade_3,	//
@RequestParam("qualidade_4") String qualidade_4,	//
@RequestParam("qualidade_5") String qualidade_5,	//
@RequestParam("qualidade_6") String qualidade_6,	//
@RequestParam("qualidade_7") String qualidade_7,	//
@RequestParam("qualidade_8") String qualidade_8,	//
@RequestParam("qualidade_9") String qualidade_9,	//
@RequestParam("qualidade_10") String qualidade_10,	//
@RequestParam("qualidade_11") String qualidade_11,	//
@RequestParam("qualidade_12") String qualidade_12,	//
@RequestParam("qualidade_13") String qualidade_13,	//
@RequestParam("qualidade_14") String qualidade_14,	//
@RequestParam("qualidade_15") String qualidade_15,	//
@RequestParam("qualidade_16") String qualidade_16,	//
@RequestParam("qualidade_17") String qualidade_17,	//
@RequestParam("qualidade_18") String qualidade_18,	//
@RequestParam("qualidade_log") String qualidade_log,	//

@RequestParam("forca_1") String forca_1,	//
@RequestParam("forca_2") String forca_2,	//
@RequestParam("forca_3") String forca_3,	//
@RequestParam("forca_4") String forca_4,	//
@RequestParam("forca_5") String forca_5,	//
@RequestParam("forca_6") String forca_6,	//
@RequestParam("forca_7") String forca_7,	//
@RequestParam("forca_8") String forca_8,	//
@RequestParam("forca_9") String forca_9,	//
@RequestParam("forca_10") String forca_10,	//
@RequestParam("forca_11") String forca_11,	//
@RequestParam("forca_12") String forca_12,	//
@RequestParam("forca_13") String forca_13,	//
@RequestParam("forca_14") String forca_14,	//
@RequestParam("forca_log") String forca_log,	//

@RequestParam("pressao_1") String pressao_1,	//
@RequestParam("pressao_2") String pressao_2,	//
@RequestParam("pressao_3") String pressao_3,	//
@RequestParam("pressao_4") String pressao_4,	//
@RequestParam("pressao_5") String pressao_5,	//
@RequestParam("pressao_log") String pressao_log,	//

Model model) {

  /////////////////////////
  //Fix empty fields
  if (ava_0.equals("")) ava_0 = "0"; model.addAttribute("ava_0", ava_0);
  if (ava_cpf.equals("")) ava_cpf = "0"; model.addAttribute("ava_cpf", ava_cpf);
  if (ava_1.equals("")) ava_1 = "0"; model.addAttribute("ava_1", ava_1);
  if (ava_2.equals("")) ava_2 = "0"; model.addAttribute("ava_2", ava_2);
  if (ava_3.equals("")) ava_3 = "0"; model.addAttribute("ava_3", ava_3);
  if (ava_4.equals("")) ava_4 = "0"; model.addAttribute("ava_4", ava_4);
  if (ava_5.equals("")) ava_5 = "0"; model.addAttribute("ava_5", ava_5);
  if (ava_6.equals("")) ava_6 = "0"; model.addAttribute("ava_6", ava_6);
  if (ava_7.equals("")) ava_7 = "0"; model.addAttribute("ava_7", ava_7);
  if (ava_8.equals("")) ava_8 = "0"; model.addAttribute("ava_8", ava_8);
  if (ava_9.equals("")) ava_9 = "0"; model.addAttribute("ava_9", ava_9);
  if (ava_10.equals("")) ava_10 = "0"; model.addAttribute("ava_10", ava_10);
  if (ava_11.equals("")) ava_11 = "0"; model.addAttribute("ava_11", ava_11);
  if (ava_12.equals("")) ava_12 = "0"; model.addAttribute("ava_12", ava_12);
  if (ava_13.equals("")) ava_13 = "0"; model.addAttribute("ava_13", ava_13);
  if (ava_14.equals("")) ava_14 = "0"; model.addAttribute("ava_14", ava_14);

  //Fix empty fields
  if (gonio_1.equals("")) gonio_1 = "0"; model.addAttribute("gonio_1", gonio_1);
  if (gonio_2.equals("")) gonio_2 = "0"; model.addAttribute("gonio_2", gonio_2);
  if (gonio_3.equals("")) gonio_3 = "0"; model.addAttribute("gonio_3", gonio_3);
  if (gonio_4.equals("")) gonio_4 = "0"; model.addAttribute("gonio_4", gonio_4);
  if (gonio_5.equals("")) gonio_5 = "0"; model.addAttribute("gonio_5", gonio_5);
  if (gonio_6.equals("")) gonio_6 = "0"; model.addAttribute("gonio_6", gonio_6);
  if (gonio_7.equals("")) gonio_7 = "0"; model.addAttribute("gonio_7", gonio_7);
  if (gonio_8.equals("")) gonio_8 = "0"; model.addAttribute("gonio_8", gonio_8);
  if (gonio_9.equals("")) gonio_9 = "0"; model.addAttribute("gonio_9", gonio_9);
  if (gonio_10.equals("")) gonio_10 = "0"; model.addAttribute("gonio_10", gonio_10);
  if (gonio_11.equals("")) gonio_11 = "0"; model.addAttribute("gonio_11", gonio_11);
  if (gonio_12.equals("")) gonio_12 = "0"; model.addAttribute("gonio_12", gonio_12);
  if (gonio_13.equals("")) gonio_13 = "0"; model.addAttribute("gonio_13", gonio_13);
  if (gonio_14.equals("")) gonio_14 = "0"; model.addAttribute("gonio_14", gonio_14);
  if (gonio_15.equals("")) gonio_15 = "0"; model.addAttribute("gonio_15", gonio_15);
  if (gonio_16.equals("")) gonio_16 = "0"; model.addAttribute("gonio_16", gonio_16);
  if (gonio_17.equals("")) gonio_17 = "0"; model.addAttribute("gonio_17", gonio_17);
  if (gonio_18.equals("")) gonio_18 = "0"; model.addAttribute("gonio_18", gonio_18);
  if (gonio_19.equals("")) gonio_19 = "0"; model.addAttribute("gonio_19", gonio_19);
  if (gonio_20.equals("")) gonio_20 = "0"; model.addAttribute("gonio_20", gonio_20);
  if (gonio_21.equals("")) gonio_21 = "0"; model.addAttribute("gonio_21", gonio_21);
  if (gonio_22.equals("")) gonio_22 = "0"; model.addAttribute("gonio_22", gonio_22);
  if (gonio_23.equals("")) gonio_23 = "0"; model.addAttribute("gonio_23", gonio_23);
  if (gonio_24.equals("")) gonio_24 = "0"; model.addAttribute("gonio_24", gonio_24);
  if (gonio_25.equals("")) gonio_25 = "0"; model.addAttribute("gonio_25", gonio_25);
  if (gonio_26.equals("")) gonio_26 = "0"; model.addAttribute("gonio_26", gonio_26);
  if (gonio_27.equals("")) gonio_27 = "0"; model.addAttribute("gonio_27", gonio_27);
  if (gonio_28.equals("")) gonio_28 = "0"; model.addAttribute("gonio_28", gonio_28);
  if (gonio_29.equals("")) gonio_29 = "0"; model.addAttribute("gonio_29", gonio_29);
  if (gonio_30.equals("")) gonio_30 = "0"; model.addAttribute("gonio_30", gonio_30);
  if (gonio_31.equals("")) gonio_31 = "0"; model.addAttribute("gonio_31", gonio_31);
  if (gonio_32.equals("")) gonio_32 = "0"; model.addAttribute("gonio_32", gonio_32);
  if (gonio_33.equals("")) gonio_33 = "0"; model.addAttribute("gonio_33", gonio_33);
  if (gonio_34.equals("")) gonio_34 = "0"; model.addAttribute("gonio_34", gonio_34);
  if (gonio_35.equals("")) gonio_35 = "0"; model.addAttribute("gonio_35", gonio_35);
  if (gonio_36.equals("")) gonio_36 = "0"; model.addAttribute("gonio_36", gonio_36);
  if (gonio_37.equals("")) gonio_37 = "0"; model.addAttribute("gonio_37", gonio_37);

  ////////
  if (preensao_1.equals("")) preensao_1 = "0"; model.addAttribute("preensao_1", preensao_1);
  if (preensao_2.equals("")) preensao_2 = "0"; model.addAttribute("preensao_2", preensao_2);
  if (preensao_3.equals("")) preensao_3 = "0"; model.addAttribute("preensao_3", preensao_3);
  if (preensao_4.equals("")) preensao_4 = "0"; model.addAttribute("preensao_4", preensao_4);
  if (preensao_5.equals("")) preensao_5 = "0"; model.addAttribute("preensao_5", preensao_5);
  if (preensao_6.equals("")) preensao_6 = "0"; model.addAttribute("preensao_6", preensao_6);
  if (preensao_7.equals("")) preensao_7 = "0"; model.addAttribute("preensao_7", preensao_7);

  ///////////////
  //Fix empty fields
  if (compo_1.equals("")) compo_1 = "0"; model.addAttribute("compo_1", compo_1);
  if (compo_2.equals("")) compo_2 = "0"; model.addAttribute("compo_2", compo_2);
  if (compo_3.equals("")) compo_3 = "0"; model.addAttribute("compo_3", compo_3);
  if (compo_4.equals("")) compo_4 = "0"; model.addAttribute("compo_4", compo_4);
  if (compo_5.equals("")) compo_5 = "0"; model.addAttribute("compo_5", compo_5);
  if (compo_6.equals("")) compo_6 = "0"; model.addAttribute("compo_6", compo_6);
  if (compo_7.equals("")) compo_7 = "0"; model.addAttribute("compo_7", compo_7);
  if (compo_8.equals("")) compo_8 = "0"; model.addAttribute("compo_8", compo_8);
  if (compo_9.equals("")) compo_9 = "0"; model.addAttribute("compo_9", compo_9);
  if (compo_10.equals("")) compo_10 = "0"; model.addAttribute("compo_10", compo_10);
  if (compo_11.equals("")) compo_11 = "0"; model.addAttribute("compo_11", compo_11);
  if (compo_12.equals("")) compo_12 = "0"; model.addAttribute("compo_12", compo_12);
  if (compo_13.equals("")) compo_13 = "0"; model.addAttribute("compo_13", compo_13);
  if (compo_14.equals("")) compo_14 = "0"; model.addAttribute("compo_14", compo_14);
  if (compo_15.equals("")) compo_15 = "0"; model.addAttribute("compo_15", compo_15);
  if (compo_15_2.equals("")) compo_15_2 = "0"; model.addAttribute("compo_15_2", compo_15_2);
  if (compo_16.equals("")) compo_16 = "0"; model.addAttribute("compo_16", compo_16);
  if (compo_16_2.equals("")) compo_16_2 = "0"; model.addAttribute("compo_16_2", compo_16_2);
  if (compo_17.equals("")) compo_17 = "0"; model.addAttribute("compo_17", compo_17);
  if (compo_17_2.equals("")) compo_17_2 = "0"; model.addAttribute("compo_17_2", compo_17_2);
  if (compo_18.equals("")) compo_18 = "0"; model.addAttribute("compo_18", compo_18);
  if (compo_19.equals("")) compo_19 = "0"; model.addAttribute("compo_19", compo_19);
  if (compo_20.equals("")) compo_20 = "0"; model.addAttribute("compo_20", compo_20);
  if (compo_21.equals("")) compo_21 = "0"; model.addAttribute("compo_21", compo_21);
  if (compo_22.equals("")) compo_22 = "0"; model.addAttribute("compo_22", compo_22);
  if (compo_23.equals("")) compo_23 = "0"; model.addAttribute("compo_23", compo_23);
  if (compo_24.equals("")) compo_24 = "0"; model.addAttribute("compo_24", compo_24);
  if (compo_25.equals("")) compo_25 = "0"; model.addAttribute("compo_25", compo_25);
  if (compo_26.equals("")) compo_26 = "0"; model.addAttribute("compo_26", compo_26);
  if (compo_27.equals("")) compo_27 = "0"; model.addAttribute("compo_27", compo_27);
  if (compo_28.equals("")) compo_28 = "0"; model.addAttribute("compo_28", compo_28);
  if (compo_29.equals("")) compo_29 = "0"; model.addAttribute("compo_29", compo_29);
  if (compo_30.equals("")) compo_30 = "0"; model.addAttribute("compo_30", compo_30);
  if (compo_31.equals("")) compo_31 = "0"; model.addAttribute("compo_31", compo_31);
  if (compo_32.equals("")) compo_32 = "0"; model.addAttribute("compo_32", compo_32);
  if (compo_33.equals("")) compo_33 = "0"; model.addAttribute("compo_33", compo_33);
  if (compo_34.equals("")) compo_34 = "0"; model.addAttribute("compo_34", compo_34);
  if (compo_35.equals("")) compo_35 = "0"; model.addAttribute("compo_35", compo_35);
  if (compo_36.equals("")) compo_36 = "0"; model.addAttribute("compo_36", compo_36);

  //Fix empty fields
  if (dor_1.equals("")) dor_1 = "0"; model.addAttribute("dor_1", dor_1);
  if (dor_2.equals("")) dor_2 = "0"; model.addAttribute("dor_2", dor_2);
  if (dor_3.equals("")) dor_3 = "0"; model.addAttribute("dor_3", dor_3);
  if (dor_4.equals("")) dor_4 = "0"; model.addAttribute("dor_4", dor_4);
  if (dor_5.equals("")) dor_5 = "0"; model.addAttribute("dor_5", dor_5);
  if (dor_6.equals("")) dor_6 = "0"; model.addAttribute("dor_6", dor_6);
  if (dor_7.equals("")) dor_7 = "0"; model.addAttribute("dor_7", dor_7);
  if (dor_8.equals("")) dor_8 = "0"; model.addAttribute("dor_8", dor_8);
  if (dor_9.equals("")) dor_9 = "0"; model.addAttribute("dor_9", dor_9);
  if (dor_10.equals("")) dor_10 = "0"; model.addAttribute("dor_10", dor_10);
  if (dor_11.equals("")) dor_11 = "0"; model.addAttribute("dor_11", dor_11);
  if (dor_12.equals("")) dor_12 = "0"; model.addAttribute("dor_12", dor_12);
  if (dor_13.equals("")) dor_13 = "0"; model.addAttribute("dor_13", dor_13);
  if (dor_14.equals("")) dor_14 = "0"; model.addAttribute("dor_14", dor_14);
  if (dor_15.equals("")) dor_15 = "0"; model.addAttribute("dor_15", dor_15);
  if (dor_16.equals("")) dor_16 = "0"; model.addAttribute("dor_16", dor_16);
  if (dor_obs.equals("")) dor_obs = "0"; model.addAttribute("dor_obs", dor_obs);

  //Fix empty fields
  if (evolucaodor_1.equals("")) evolucaodor_1 = "0"; model.addAttribute("evolucaodor_1", evolucaodor_1);
  if (evolucaodor_2.equals("")) evolucaodor_2 = "0"; model.addAttribute("evolucaodor_2", evolucaodor_2);
  if (evolucaodor_3.equals("")) evolucaodor_3 = "0"; model.addAttribute("evolucaodor_3", evolucaodor_3);
  if (evolucaodor_4.equals("")) evolucaodor_4 = "0"; model.addAttribute("evolucaodor_4", evolucaodor_4);
  if (evolucaodor_5.equals("")) evolucaodor_5 = "0"; model.addAttribute("evolucaodor_5", evolucaodor_5);
  if (evolucaodor_6.equals("")) evolucaodor_6 = "0"; model.addAttribute("evolucaodor_6", evolucaodor_6);
  if (evolucaodor_7.equals("")) evolucaodor_7 = "0"; model.addAttribute("evolucaodor_7", evolucaodor_7);
  if (evolucaodor_8.equals("")) evolucaodor_8 = "0"; model.addAttribute("evolucaodor_8", evolucaodor_8);
  if (evolucaodor_9.equals("")) evolucaodor_9 = "0"; model.addAttribute("evolucaodor_9", evolucaodor_9);
  if (evolucaodor_10.equals("")) evolucaodor_10 = "0"; model.addAttribute("evolucaodor_10", evolucaodor_10);
  if (evolucaodor_11.equals("")) evolucaodor_11 = "0"; model.addAttribute("evolucaodor_11", evolucaodor_11);
  if (evolucaodor_12.equals("")) evolucaodor_12 = "0"; model.addAttribute("evolucaodor_12", evolucaodor_12);
  if (evolucaodor_13.equals("")) evolucaodor_13 = "0"; model.addAttribute("evolucaodor_13", evolucaodor_13);
  if (evolucaodor_14.equals("")) evolucaodor_14 = "0"; model.addAttribute("evolucaodor_14", evolucaodor_14);
  if (evolucaodor_15.equals("")) evolucaodor_15 = "0"; model.addAttribute("evolucaodor_15", evolucaodor_15);
  if (evolucaodor_16.equals("")) evolucaodor_16 = "0"; model.addAttribute("evolucaodor_16", evolucaodor_16);
  if (evolucaodor_obs.equals("")) evolucaodor_obs = "0"; model.addAttribute("evolucaodor_obs", evolucaodor_obs);

  //Fix empty fields
  if (gural_1.equals("")) gural_1 = "0"; model.addAttribute("gural_1", gural_1);
  if (gural_2.equals("")) gural_2 = "0"; model.addAttribute("gural_2", gural_2);
  if (gural_3.equals("")) gural_3 = "0"; model.addAttribute("gural_3", gural_3);
  if (gural_4.equals("")) gural_4 = "0"; model.addAttribute("gural_4", gural_4);
  if (gural_5.equals("")) gural_5 = "0"; model.addAttribute("gural_5", gural_5);
  if (gural_6.equals("")) gural_6 = "0"; model.addAttribute("gural_6", gural_6);
  if (gural_7.equals("")) gural_7 = "0"; model.addAttribute("gural_7", gural_7);
  if (gural_8.equals("")) gural_8 = "0"; model.addAttribute("gural_8", gural_8);
  if (gural_9.equals("")) gural_9 = "0"; model.addAttribute("gural_9", gural_9);
  if (gural_10.equals("")) gural_10 = "0"; model.addAttribute("gural_10", gural_10);
  if (gural_11.equals("")) gural_11 = "0"; model.addAttribute("gural_11", gural_11);
  if (gural_12.equals("")) gural_12 = "0"; model.addAttribute("gural_12", gural_12);

  //Fix empty fields
  if (qualidade_1.equals("")) qualidade_1 = "0"; model.addAttribute("qualidade_1", qualidade_1);
  if (qualidade_2.equals("")) qualidade_2 = "0"; model.addAttribute("qualidade_2", qualidade_2);
  if (qualidade_3.equals("")) qualidade_3 = "0"; model.addAttribute("qualidade_3", qualidade_3);
  if (qualidade_4.equals("")) qualidade_4 = "0"; model.addAttribute("qualidade_4", qualidade_4);
  if (qualidade_5.equals("")) qualidade_5 = "0"; model.addAttribute("qualidade_5", qualidade_5);
  if (qualidade_6.equals("")) qualidade_6 = "0"; model.addAttribute("qualidade_6", qualidade_6);
  if (qualidade_7.equals("")) qualidade_7 = "0"; model.addAttribute("qualidade_7", qualidade_7);
  if (qualidade_8.equals("")) qualidade_8 = "0"; model.addAttribute("qualidade_8", qualidade_8);
  if (qualidade_9.equals("")) qualidade_9 = "0"; model.addAttribute("qualidade_9", qualidade_9);
  if (qualidade_10.equals("")) qualidade_10 = "0"; model.addAttribute("qualidade_10", qualidade_10);
  if (qualidade_11.equals("")) qualidade_11 = "0"; model.addAttribute("qualidade_11", qualidade_11);
  if (qualidade_12.equals("")) qualidade_12 = "0"; model.addAttribute("qualidade_12", qualidade_12);
  if (qualidade_13.equals("")) qualidade_13 = "0"; model.addAttribute("qualidade_13", qualidade_13);
  if (qualidade_14.equals("")) qualidade_14 = "0"; model.addAttribute("qualidade_14", qualidade_14);
  if (qualidade_15.equals("")) qualidade_15 = "0"; model.addAttribute("qualidade_15", qualidade_15);
  if (qualidade_16.equals("")) qualidade_16 = "0"; model.addAttribute("qualidade_16", qualidade_16);
  if (qualidade_17.equals("")) qualidade_17 = "0"; model.addAttribute("qualidade_17", qualidade_17);
  if (qualidade_18.equals("")) qualidade_18 = "0"; model.addAttribute("qualidade_18", qualidade_18);

  //Fix empty fields
  if (forca_1.equals("")) forca_1 = "0"; model.addAttribute("forca_1", forca_1);
  if (forca_2.equals("")) forca_2 = "0"; model.addAttribute("forca_2", forca_2);
  if (forca_3.equals("")) forca_3 = "0"; model.addAttribute("forca_3", forca_3);
  if (forca_4.equals("")) forca_4 = "0"; model.addAttribute("forca_4", forca_4);
  if (forca_5.equals("")) forca_5 = "0"; model.addAttribute("forca_5", forca_5);
  if (forca_6.equals("")) forca_6 = "0"; model.addAttribute("forca_6", forca_6);
  if (forca_7.equals("")) forca_7 = "0"; model.addAttribute("forca_7", forca_7);
  if (forca_8.equals("")) forca_8 = "0"; model.addAttribute("forca_8", forca_8);
  if (forca_9.equals("")) forca_9 = "0"; model.addAttribute("forca_9", forca_9);
  if (forca_10.equals("")) forca_10 = "0"; model.addAttribute("forca_10", forca_10);
  if (forca_11.equals("")) forca_11 = "0"; model.addAttribute("forca_11", forca_11);
  if (forca_12.equals("")) forca_12 = "0"; model.addAttribute("forca_12", forca_12);
  if (forca_13.equals("")) forca_13 = "0"; model.addAttribute("forca_13", forca_13);
  if (forca_14.equals("")) forca_14 = "0"; model.addAttribute("forca_14", forca_14);

  //Fix empty fields
  if (pressao_1.equals("")) pressao_1 = "0"; model.addAttribute("pressao_1", pressao_1);
  if (pressao_2.equals("")) pressao_2 = "0"; model.addAttribute("pressao_2", pressao_2);
  if (pressao_3.equals("")) pressao_3 = "0"; model.addAttribute("pressao_3", pressao_3);
  if (pressao_4.equals("")) pressao_4 = "0"; model.addAttribute("pressao_4", pressao_4);
  if (pressao_5.equals("")) pressao_5 = "0"; model.addAttribute("pressao_5", pressao_5);

  //---1.Avaliacao
  //Map<String, Object> m=new HashMap<String, Object>();
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacao where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        stmt.executeUpdate("UPDATE avaliacao SET cpf='"+ava_cpf+"', dataAvaliacao='"+ava_1+"', dataNascimento='"+ava_2+"', idade="+ava_3+", nome='"+ava_4+"' , objetivo='"+ava_5+"', exercicios='"+ava_6+"', outrasAF='"+ava_7+"',  especificar='"+ava_8+"', diagnostico='"+ava_9+"', medicacao='"+ava_10+"', peso="+ava_11+", altura="+ava_12+", imc="+ava_13+", fc="+ava_14+" WHERE id="+ava_0);

        ava_log = "Registro atualizado!";
        model.addAttribute("ava_log", ava_log);

      } else {
        stmt.executeUpdate("insert into avaliacao(cpf,dataAvaliacao,dataNascimento,idade,nome,objetivo,exercicios,outrasAF,especificar,diagnostico,medicacao,peso,altura,imc,fc) values ( '"+ ava_cpf+"', '"+ava_1+"', '"+ava_2+"', "+ava_3+", '"+ava_4+"', '"+ava_5+"', '"+ava_6+"', '"+ava_7+"', '"+ava_8+"', '"+ava_9+"', '"+ava_10+"', "+ava_11+", "+ava_12+", "+ava_13+", "+ava_14+" )");

        ava_log += "[insert into: OK] ";
        model.addAttribute("ava_log", ava_log);
        //fim else
      }

      //ava_0 vem sem nada
      //Retorna o numero da ficha inserida
      rs = stmt.executeQuery("SELECT id FROM avaliacao");
      int numeroFicha=0;
      while (rs.next()) {
        numeroFicha=(Integer)rs.getObject(1);
      }
      model.addAttribute("ava_0",numeroFicha);

    } catch (Exception e){
      ava_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("ava_log", ava_log);
      return "dashboard_ficha";

    }

  } catch (Exception e) {
    ava_log += "[select * from avaliacao: FAIL " + e.getMessage() + "]";
    model.addAttribute("avaliacao_log", ava_log);
    return "dashboard_ficha";
  }

  //--2.Goniometria
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM goniometria where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE goniometria SET ofdir="+gonio_1+", ofesq="+gonio_2+", dor1='"+gonio_3+"', ana1='"+gonio_4+"', local1='"+gonio_5+"' , oredir="+gonio_6+", oreesq="+gonio_7+", dor2='"+gonio_8+"', ana2='"+gonio_9+"', local2='"+gonio_10+"', qfdir="+gonio_11+", qfesq="+gonio_12+", dor3='"+gonio_13+"', ana3='"+gonio_14+"', local3='"+gonio_15+"', qredir="+gonio_16+", qreesq="+gonio_17+", dor4='"+gonio_18+"', ana4='"+gonio_19+"', local4='"+gonio_20+"', jfdir="+gonio_21+", jfesq="+gonio_22+", dor5='"+gonio_23+"', ana5='"+gonio_24+"', local5='"+gonio_25+"', tfddir="+gonio_26+", tfdesq="+gonio_27+", dor6='"+gonio_28+"', ana6='"+gonio_29+"', local6='"+gonio_30+"', tfpdir="+gonio_31+", tfpesq="+gonio_32+", dor7='"+gonio_33+"', ana7='"+gonio_34+"', local7='"+gonio_35+"', obs='"+gonio_36+"', sentar='"+gonio_37+"'"+" WHERE id="+ava_0;

        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        gonio_log = "Registro atualizado!";
        model.addAttribute("gonio_log", gonio_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into goniometria(ofdir,ofesq,dor1,ana1,local1,oredir,oreesq,dor2,ana2,local2,qfdir,qfesq,dor3,ana3,local3,qredir,qreesq,dor4,ana4,local4,jfdir,jfesq,dor5,ana5,local5,tfddir,tfdesq,dor6,ana6,local6,tfpdir,tfpesq,dor7,ana7,local7,obs,sentar) values ( " +gonio_1+", "+gonio_2+", '"+gonio_3+"', '"+gonio_4+"', '"+gonio_5+"', "+gonio_6+", "+gonio_7+", '"+gonio_8+"', '"+gonio_9+"', '"+gonio_10+"', "+gonio_11+", "+gonio_12+", '"+gonio_13+"', '"+gonio_14+"', '"+gonio_15+"', "+gonio_16+", "+gonio_17+", '"+gonio_18+"', '"+gonio_19+"', '"+gonio_20+"', "+gonio_21+", "+gonio_22+", '"+gonio_23+"', '"+gonio_24+"', '"+gonio_25+"', "+gonio_26+", "+gonio_27+", '"+gonio_28+"', '"+gonio_29+"', '"+gonio_30+"', "+gonio_31+", "+gonio_32+", '"+gonio_33+"', '"+gonio_34+"', '"+gonio_35+"', '"+gonio_36+"', '"+gonio_37+"')");

        gonio_log += "[insert into: OK] ";
        model.addAttribute("gonio_log", gonio_log);
      }
    } catch (Exception e){
      gonio_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("gonio_log", gonio_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    gonio_log += "[select * from gonio: FAIL " + e.getMessage() + "]";
    model.addAttribute("gonio_log", gonio_log);
    return "dashboard_ficha";
  }

  //---3.Preensao
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM preensao where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE preensao SET ladodominante='"+preensao_1+"', dir1="+preensao_2+", esq1="+preensao_3+", dir2="+preensao_4+", esq2="+preensao_5+", dir3="+preensao_6+", esq3="+preensao_7+" WHERE id="+ava_0;

        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        preensao_log = "Registro atualizado!";
        model.addAttribute("preensao_log", preensao_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into preensao(ladodominante,dir1,esq1,dir2,esq2,dir3,esq3) values ( '"+preensao_1+"', "+preensao_2+", "+preensao_3+", "+preensao_4+", "+preensao_5+", "+preensao_6+", "+preensao_7+")");

        preensao_log += "[insert into: OK] ";
        model.addAttribute("preensao_log", preensao_log);
      }
    } catch (Exception e){
      preensao_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("preensao_log", preensao_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    pressao_log += "[select * from pressao: FAIL " + e.getMessage() + "]";
    model.addAttribute("pressao_log", pressao_log);
    return "dashboard_ficha";
  }

  //---4.Composicao
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM composicao where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE composicao SET cintura="+compo_1+", prof='"+compo_2+"', dbraco="+compo_3+", ebraco="+compo_4+", quadril="+compo_5+", dpantu="+compo_6+", epantu="+compo_7+", rqc="+compo_8+", dcoxa="+compo_9+", ecoxa="+compo_10+", emediacoxa="+compo_12+", dmediacoxa="+compo_11+", resistencia="+compo_13+", reactancia="+compo_14+", agua="+compo_15+", mmagra="+compo_16+", mgordura="+compo_17+", massacel="+compo_18+", triceps="+compo_19+", subes="+compo_20+", supra="+compo_21+", abd="+compo_22+", coxa="+compo_23+", total="+compo_24+", compo_15_2="+compo_15_2+", compo_16_2="+compo_16_2+", compo_17_2="+compo_17_2+", compo_25="+compo_25+", compo_26="+compo_26+", compo_27="+compo_27+", compo_28="+compo_28+", compo_29="+compo_29+", compo_30="+compo_30+", compo_31='"+compo_31+"', compo_32="+compo_32+", compo_33="+compo_33+", compo_34="+compo_34+", compo_35='"+compo_35+"', compo_36="+compo_36+" WHERE id="+ava_0;

        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        compo_log = "Registro atualizado!";
        model.addAttribute("compo_log", compo_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into composicao(cintura, prof, dbraco, ebraco, quadril, dpantu, epantu, rqc, dcoxa, ecoxa, emediacoxa, dmediacoxa, resistencia, reactancia, agua, mmagra, mgordura, massacel, triceps, subes, supra, abd, coxa, total, compo_15_2, compo_16_2, compo_17_2, compo_25, compo_26, compo_27, compo_28, compo_29, compo_30, compo_31, compo_32, compo_33, compo_34, compo_35, compo_36) values ( "+compo_1+",'"+compo_2+"',"+compo_3+","+compo_4+","+compo_5+","+compo_6+","+compo_7+","+compo_8+","+compo_9+","+compo_10+","+compo_11+","+compo_12+","+compo_13+","+compo_14+","+compo_15+","+compo_16+","+compo_17+","+compo_18+","+compo_19+","+compo_20+","+compo_21+","+compo_22+","+compo_23+","+compo_24+","+compo_15_2+","+compo_16_2+","+compo_17_2+","+compo_25+","+compo_26+","+compo_27+","+compo_28+","+compo_29+","+compo_30+",'"+compo_31+"',"+compo_32+","+compo_33+","+compo_34+",'"+compo_35+"',"+compo_36+")");

        compo_log += "[insert into: OK] ";
        model.addAttribute("compo_log", compo_log);
      }
    } catch (Exception e){
      compo_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("compo_log", compo_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    compo_log += "[select * from composicao: FAIL " + e.getMessage() + "]";
    model.addAttribute("compo_log", compo_log);
    return "dashboard_ficha";
  }

  //---5.Avaliacaodor
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM avaliacaodor where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE avaliacaodor SET dor1='"+dor_1+"', dor2="+dor_2+", dor3='"+dor_3+"', dor4="+dor_4+", dor5='"+dor_5+"', dor6="+dor_6+", dor7='"+dor_7+"', dor8="+dor_8+", dor9='"+dor_9+"', dor10="+dor_10+", dor11='"+dor_11+"', dor12="+dor_12+", dor13='"+dor_13+"', dor14="+dor_14+", dor15='"+dor_15+"', dor16="+dor_16+", dor_obs='"+dor_obs+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        dor_log = "Registro atualizado!";
        model.addAttribute("dor_log", dor_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into avaliacaodor(dor1, dor2, dor3, dor4, dor5, dor6, dor7, dor8, dor9, dor10, dor11, dor12, dor13, dor14, dor15, dor16, dor_obs) values ( '"+dor_1+"',"+dor_2+",'"+dor_3+"',"+dor_4+",'"+dor_5+"',"+dor_6+",'"+dor_7+"',"+dor_8+",'"+dor_9+"',"+dor_10+",'"+dor_11+"',"+dor_12+",'"+dor_13+"',"+dor_14+",'"+dor_15+"',"+dor_16+", '"+dor_obs+"')");

        dor_log += "[insert into: OK] ";
        model.addAttribute("dor_log", dor_log);
      }
    } catch (Exception e){
      dor_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("dor_log", dor_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    dor_log += "[select * from avaliacaodor: FAIL " + e.getMessage() + "]";
    model.addAttribute("dor_log", dor_log);
    return "dashboard_ficha";
  }


  //6.Evolucaodor
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM evolucaodor where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE evolucaodor SET dor1='"+evolucaodor_1+"', dor2='"+evolucaodor_2+"', dor3='"+evolucaodor_3+"', dor4='"+evolucaodor_4+"', dor5='"+evolucaodor_5+"', dor6='"+evolucaodor_6+"', dor7='"+evolucaodor_7+"', dor8='"+evolucaodor_8+"', dor9='"+evolucaodor_9+"', dor10='"+evolucaodor_10+"', dor11='"+evolucaodor_11+"', dor12='"+evolucaodor_12+"', dor13='"+evolucaodor_13+"', dor14='"+evolucaodor_14+"', dor15='"+evolucaodor_15+"', dor16='"+evolucaodor_16+"', dor_obs='"+evolucaodor_obs+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        evolucaodor_log = "Registro atualizado!";
        model.addAttribute("evolucaodor_log", evolucaodor_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into evolucaodor(dor1, dor2, dor3, dor4, dor5, dor6, dor7, dor8, dor9, dor10, dor11, dor12, dor13, dor14, dor15, dor16, dor_obs) values ( '"+evolucaodor_1+"','"+evolucaodor_2+"','"+evolucaodor_3+"','"+evolucaodor_4+"','"+evolucaodor_5+"','"+evolucaodor_6+"','"+evolucaodor_7+"','"+evolucaodor_8+"','"+evolucaodor_9+"','"+evolucaodor_10+"','"+evolucaodor_11+"','"+evolucaodor_12+"','"+evolucaodor_13+"','"+evolucaodor_14+"','"+evolucaodor_15+"', '"+evolucaodor_16+"', '"+evolucaodor_obs+"')");

        evolucaodor_log += "[insert into: OK] ";
        model.addAttribute("evolucaodor_log", evolucaodor_log);

      }
    } catch (Exception e){
      evolucaodor_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("evolucaodor_log", evolucaodor_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    evolucaodor_log += "[select * from evolucaodor: FAIL " + e.getMessage() + "]";
    model.addAttribute("evolucaodor_log", evolucaodor_log);
    return "dashboard_ficha";
  }

  //---7.Gural
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM gural where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE gural SET gural1='"+gural_1+"', gural2='"+gural_2+"', gural3='"+gural_3+"', gural4='"+gural_4+"', gural5='"+gural_5+"', gural6='"+gural_6+"', gural7='"+gural_7+"', gural8='"+gural_8+"', gural9='"+gural_9+"', gural10='"+gural_10+"', gural11='"+gural_11+"', gural12='"+gural_12+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        gural_log = "Registro atualizado!";
        model.addAttribute("gural_log", gural_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into gural(gural1, gural2, gural3, gural4, gural5, gural6, gural7, gural8, gural9, gural10, gural11, gural12) values ( '"+gural_1+"','"+gural_2+"','"+gural_3+"','"+gural_4+"','"+gural_5+"','"+gural_6+"','"+gural_7+"','"+gural_8+"','"+gural_9+"','"+gural_10+"','"+gural_11+"','"+gural_12+"')");

        gural_log += "[insert into: OK] ";
        model.addAttribute("gural_log", gural_log);
      }
    } catch (Exception e){
      gural_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("gural_log", gural_log);
      return "dashboard_ficha";
    }
  } catch (Exception e) {
    pressao_log += "[select * from gural: FAIL " + e.getMessage() + "]";
    model.addAttribute("gural_log", gural_log);
    return "dashboard_ficha";
  }

  //---8.Qualidade
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM qualidade where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE qualidade SET qualidade1='"+qualidade_1+"', qualidade2='"+qualidade_2+"', qualidade3='"+qualidade_3+"', qualidade4='"+qualidade_4+"', qualidade5='"+qualidade_5+"', qualidade6='"+qualidade_6+"', qualidade7='"+qualidade_7+"', qualidade8='"+qualidade_8+"', qualidade9='"+qualidade_9+"', qualidade10='"+qualidade_10+"', qualidade11='"+qualidade_11+"', qualidade12='"+qualidade_12+"', qualidade13='"+qualidade_13+"', qualidade14='"+qualidade_14+"', qualidade15='"+qualidade_15+"', qualidade16='"+qualidade_16+"', qualidade17='"+qualidade_17+"', qualidade18='"+qualidade_18+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        qualidade_log = "Registro atualizado!";
        model.addAttribute("qualidade_log", qualidade_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into qualidade(qualidade1, qualidade2, qualidade3, qualidade4, qualidade5, qualidade6, qualidade7, qualidade8, qualidade9, qualidade10, qualidade11, qualidade12, qualidade13, qualidade14, qualidade15, qualidade16, qualidade17, qualidade18) values ( '"+qualidade_1+"','"+qualidade_2+"','"+qualidade_3+"','"+qualidade_4+"','"+qualidade_5+"','"+qualidade_6+"','"+qualidade_7+"','"+qualidade_8+"','"+qualidade_9+"','"+qualidade_10+"','"+qualidade_11+"','"+qualidade_12+"','"+qualidade_13+"','"+qualidade_14+"','"+qualidade_15+"','"+qualidade_16+"','"+qualidade_17+"','"+qualidade_18+"')");

        qualidade_log += "[insert into: OK] ";
        model.addAttribute("qualidade_log", qualidade_log);
      }
    } catch (Exception e){
      qualidade_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("qualidade_log", qualidade_log);
      return "dashboard_ficha";
    }
  } catch (Exception e) {
    qualidade_log += "[select * from qualidade: FAIL " + e.getMessage() + "]";
    model.addAttribute("qualidade_log", qualidade_log);
    return "dashboard_ficha";
  }

  //---9.Forca
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM forca where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE forca SET forca1='"+forca_1+"', forca2='"+forca_2+"', forca3='"+forca_3+"', forca4='"+forca_4+"', forca5='"+forca_5+"', forca6='"+forca_6+"', forca7='"+forca_7+"', forca8='"+forca_8+"', forca9='"+forca_9+"', forca10='"+forca_10+"', forca11='"+forca_11+"', forca12='"+forca_12+"', forca13='"+forca_13+"', forca14='"+forca_14+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        forca_log = "Registro atualizado!";
        model.addAttribute("forca_log", forca_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into forca(forca1, forca2, forca3, forca4, forca5, forca6, forca7, forca8, forca9, forca10, forca11, forca12, forca13, forca14) values ( '"+forca_1+"','"+forca_2+"','"+forca_3+"','"+forca_4+"','"+forca_5+"','"+forca_6+"','"+forca_7+"','"+forca_8+"','"+forca_9+"','"+forca_10+"','"+forca_11+"','"+forca_12+"','"+forca_13+"','"+forca_14+"')");

        forca_log += "[insert into: OK] ";
        model.addAttribute("forca_log", forca_log);
      }
    } catch (Exception e){
      forca_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("forca_log", forca_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    forca_log += "[select * from forca: FAIL " + e.getMessage() + "]";
    model.addAttribute("forca_log", forca_log);
    return "dashboard_ficha";
  }

  //---10.Pressao
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    try {

      //Faz um select
      //se retornar resultado
      //atualiza o cadastro
      ResultSet rs = stmt.executeQuery("SELECT * FROM pressao where id="+ava_0);
      ArrayList<String> output = new ArrayList<String>();
      //Achou cadastro
      if (rs.next()) {

        String consulta = "UPDATE pressao SET pressao1='"+pressao_1+"', pressao2='"+pressao_2+"', pressao3='"+pressao_3+"', pressao4='"+pressao_4+"', pressao5='"+pressao_5+"' WHERE id="+ava_0;
        System.out.println(consulta);
        stmt.executeUpdate(consulta);

        pressao_log = "Registro atualizado!";
        model.addAttribute("pressao_log", pressao_log);

      } else {
        //Novo registro
        stmt.executeUpdate("insert into pressao(pressao1, pressao2, pressao3, pressao4, pressao5) values ( '"+pressao_1+"','"+pressao_2+"','"+pressao_3+"','"+pressao_4+"','"+pressao_5+"')");

        pressao_log += "[insert into: OK] ";
        model.addAttribute("pressao_log", pressao_log);
      }
    } catch (Exception e){
      pressao_log += "[insert into: FAIL " + e.getMessage() + "] ";
      model.addAttribute("pressao_log", pressao_log);
      return "dashboard_ficha";
    }

  } catch (Exception e) {
    pressao_log += "[select * from pressao: FAIL " + e.getMessage() + "]";
    model.addAttribute("pressao_log", pressao_log);
    return "dashboard_ficha";
  }

  return "dashboard_ficha";

}//fim gravar10

//Precisa do campo 'name' no input do form html
@PostMapping("/finalizar")
public String finalizar(
@RequestParam("ava_1") String ava_1,
@RequestParam("ava_2") String ava_2,
@RequestParam("ava_3") String ava_3,
@RequestParam("ava_4") String ava_4,
@RequestParam("ava_5") String ava_5,
@RequestParam("ava_6") String ava_6,
@RequestParam("ava_7") String ava_7,
@RequestParam("ava_8") String ava_8,
@RequestParam("ava_9") String ava_9,
@RequestParam("ava_10") String ava_10,
@RequestParam("ava_11") String ava_11,
@RequestParam("ava_12") String ava_12,
@RequestParam("ava_13") String ava_13,
@RequestParam("ava_14") String ava_14,

@RequestParam("gonio_1") String gonio_1,
@RequestParam("gonio_2") String gonio_2,
@RequestParam("gonio_3") String gonio_3,
@RequestParam("gonio_4") String gonio_4,
@RequestParam("gonio_5") String gonio_5,
@RequestParam("gonio_6") String gonio_6,
@RequestParam("gonio_7") String gonio_7,
@RequestParam("gonio_8") String gonio_8,
@RequestParam("gonio_9") String gonio_9,
@RequestParam("gonio_10") String gonio_10,
@RequestParam("gonio_11") String gonio_11,
@RequestParam("gonio_12") String gonio_12,
@RequestParam("gonio_13") String gonio_13,
@RequestParam("gonio_14") String gonio_14,
@RequestParam("gonio_15") String gonio_15,
@RequestParam("gonio_16") String gonio_16,
@RequestParam("gonio_17") String gonio_17,
@RequestParam("gonio_18") String gonio_18,
@RequestParam("gonio_19") String gonio_19,
@RequestParam("gonio_20") String gonio_20,
@RequestParam("gonio_21") String gonio_21,
@RequestParam("gonio_22") String gonio_22,
@RequestParam("gonio_23") String gonio_23,
@RequestParam("gonio_24") String gonio_24,
@RequestParam("gonio_25") String gonio_25,
@RequestParam("gonio_26") String gonio_26,
@RequestParam("gonio_27") String gonio_27,
@RequestParam("gonio_28") String gonio_28,
@RequestParam("gonio_29") String gonio_29,
@RequestParam("gonio_30") String gonio_30,
@RequestParam("gonio_31") String gonio_31,
@RequestParam("gonio_32") String gonio_32,
@RequestParam("gonio_33") String gonio_33,
@RequestParam("gonio_34") String gonio_34,
@RequestParam("gonio_35") String gonio_35,
@RequestParam("gonio_36") String gonio_36,
@RequestParam("gonio_37") String gonio_37,
Model model) {

  model.addAttribute("ava_1", ava_1);
  model.addAttribute("ava_2", ava_2);
  model.addAttribute("ava_3", ava_3);
  model.addAttribute("ava_4", ava_4);
  model.addAttribute("ava_5", ava_5);
  model.addAttribute("ava_6", ava_6);
  model.addAttribute("ava_7", ava_7);
  model.addAttribute("ava_8", ava_8);
  model.addAttribute("ava_9", ava_9);
  model.addAttribute("ava_10", ava_10);
  model.addAttribute("ava_11", ava_11);
  model.addAttribute("ava_12", ava_12);
  model.addAttribute("ava_13", ava_13);
  model.addAttribute("ava_14", ava_14);

  model.addAttribute("gonio_1", gonio_1);
  model.addAttribute("gonio_2", gonio_2);
  model.addAttribute("gonio_3", gonio_3);
  model.addAttribute("gonio_4", gonio_4);
  model.addAttribute("gonio_5", gonio_5);
  model.addAttribute("gonio_6", gonio_6);
  model.addAttribute("gonio_7", gonio_7);
  model.addAttribute("gonio_8", gonio_8);
  model.addAttribute("gonio_9", gonio_9);
  model.addAttribute("gonio_10", gonio_10);
  model.addAttribute("gonio_11", gonio_11);
  model.addAttribute("gonio_12", gonio_12);
  model.addAttribute("gonio_13", gonio_13);
  model.addAttribute("gonio_14", gonio_14);
  model.addAttribute("gonio_15", gonio_15);
  model.addAttribute("gonio_16", gonio_16);
  model.addAttribute("gonio_17", gonio_17);
  model.addAttribute("gonio_18", gonio_18);
  model.addAttribute("gonio_19", gonio_19);
  model.addAttribute("gonio_20", gonio_20);
  model.addAttribute("gonio_21", gonio_21);
  model.addAttribute("gonio_22", gonio_22);
  model.addAttribute("gonio_23", gonio_23);
  model.addAttribute("gonio_24", gonio_24);
  model.addAttribute("gonio_25", gonio_25);
  model.addAttribute("gonio_26", gonio_26);
  model.addAttribute("gonio_27", gonio_27);
  model.addAttribute("gonio_28", gonio_28);
  model.addAttribute("gonio_29", gonio_29);
  model.addAttribute("gonio_30", gonio_30);
  model.addAttribute("gonio_31", gonio_31);
  model.addAttribute("gonio_32", gonio_32);
  model.addAttribute("gonio_33", gonio_33);
  model.addAttribute("gonio_34", gonio_34);
  model.addAttribute("gonio_35", gonio_35);
  model.addAttribute("gonio_36", gonio_36);
  model.addAttribute("gonio_37", gonio_37);

  //return "dashboard"; //padrao apos login
  //return "result_dashboard"; //para visualizar os campos crus (teste)
  return "dashboard_ficha";  //para travar os campos e exportar para Excel
}

//Esse tah OK
@PostMapping("/hello")
public String sayHello(@RequestParam("name") String name, Model model) {
  model.addAttribute("name", name);
  return "result";
}

@GetMapping("/")
public String index(Model model) {
  //Faz o mapeamento
  //model.addAttribute("login", new Login());

  return "index";
}

@Bean
public DataSource dataSource() throws SQLException {
  if (dbUrl == null || dbUrl.isEmpty()) {
    return new HikariDataSource();
  } else {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dbUrl);
    return new HikariDataSource(config);
  }
}



/* =====================================================
Aplicativos: Funcao para receber requisicao de login vinda do app
Exemplo: /applogin?email=teste&senha=senha2
Inputs: usuario (String)
Inputs: usuario (String)
Inputs: token (String)
Outpus: XML com ALLOW ou DENY
*/
@GetMapping("/applogin")
@ResponseBody
public String getLogin(
@RequestParam String username,
@RequestParam String password,
Model model) {

  ArrayList<String> output = new ArrayList<String>();
  try {
    Connection connection = dataSource.getConnection();
    //Prepara as variaveis para atuar no BD
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT senha FROM usuarios WHERE email='"+username+"'");

    if (rs.next()) {
      //Verifica a senha do e-mail
      String candidate=password;
      String hashed=(String)rs.getObject(1);
      if (BCrypt.checkpw(candidate, hashed)){
        output.add("Usuario e senha corretos!");
        //return "dashboard_ficha";
        //return "Usuario: " + email + " Senha: " + senha;
        //Retorna um XML para o aplicativo
        AppLogin login = new AppLogin(username);
        String permission = new String("ALLOW");
        output.add(login.getLoginXML(permission));
        return login.getLoginXML(permission);

      } else {
        output.add("Senha invalida!");
      }
    } else
    //Na verdade, apenas o usuario nao foi encontrado
    output.add("Usuário ou Senha invalida!");

    //Se chegou ate aqui, o acesso nao deve ser permitido
    AppLogin login = new AppLogin(username);
    String permissao = new String("DENY");
    output.add(login.getLoginXML(permissao));
    return login.getLoginXML(permissao);

    //model.addAttribute("message", output);
    //return "error";

  } catch (Exception e) {
    output.add("Excecao1: " + e.getMessage());
    model.addAttribute("message", output);
    return "error";
  }
}

@PostMapping("/salvar")
public String salvar(
@RequestParam("id_gel") String id_gel,
@RequestParam("prateleira_ali") String prateleira_ali,
@RequestParam("nome_ali") String nome_ali,
@RequestParam("status_ali ") String status_ali ,
@RequestParam("validade_ali ") String validade_ali ,
@RequestParam("entrada_ali ") String entrada_ali ,
@RequestParam("ava_log") String ava_log, //retorno do login

Model model) {

/////////////////////////
//Fix empty fields
if (id_gel.equals("")) id_gel = "0"; model.addAttribute("id_gel", id_gel);

//---1.Geladeira acesso de dados
//Map<String, Object> m=new HashMap<String, Object>();
try (Connection connection = dataSource.getConnection()) {
Statement stmt = connection.createStatement();
try {

//Faz um select
//se retornar resultado
//atualiza o cadastro
ResultSet rs = stmt.executeQuery("SELECT * FROM geladeira where id_gel="+id_gel);
ArrayList<String> output = new ArrayList<String>();
//Achou cadastro
if (rs.next()) {
// String prateleira=new String("2");
// String nome=new String("1");
// String status=new String("1");
// String validade=new String("11/11/2020");
// String entrada=new String("10/11/2020");

stmt.executeUpdate("insert into alimentos(prateleira_ali,nome_ali,status_ali,validade_ali,entrada_ali,id_gel) values ( '"+ prateleira_ali +"', '"+nome_ali+"', '"+status_ali+"', "+validade_ali+", '"+entrada_ali+"', '"+id_gel+"' )");

ava_log += "[insert into: OK] ";
model.addAttribute("ava_log", ava_log);
}//fim do if

else {
  ava_log = "Não encontrada a geladeira!";
}//else

} catch (Exception e){
ava_log += "[insert into: FAIL " + e.getMessage() + "] ";
model.addAttribute("ava_log", ava_log);
return "cadastro_alimentos";
}

} catch (Exception e) {
ava_log += "[select * from alimentos: FAIL " + e.getMessage() + "]";
model.addAttribute("avaliacao_log", ava_log);
return "cadastro_alimentos";
}
return "cadastro_alimentos";
}//fim salvar

}
