
package br.edu.utfpr;

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

@Controller
@SpringBootApplication
public class Main {

    //Descomentar as 2 (duas) linhas abaixo para usar BD da nuvem Heroku
    //@Value("${spring.datasource.url}")
    //private String dbUrl;
    //private String dbUrl = "jdbc:postgresql://127.0.0.1:5432/database?user=lucio&password=lucio";

  //@Autowired
  //private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }




    //GET: parameters vem na URL
    //POST: parameters vem no corpo da mensagem

    /*    @RequestMapping(value="/validarlogin", method=RequestMethod.POST)
    @ResponseBody
    String validarlogin(@RequestBody Map<String, Object> model ) {
    try (Connection connection = dataSource.getConnection()) {

	//result eh a URL para direcionar caso o login esteja ok
	String result="db";  //visualizar entrada do form -> post

	//String inputEmail = (String) request.getParameter("inputEmail");
	//String inputPassword = (String) request.getParameter("inputPassword");

	Statement stmt = connection.createStatement();

	ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios");

	//Map<String, Object> model = new HashMap<String, Object>();
	ArrayList<String> output = new ArrayList<String>();
	output = new ArrayList<String>();

	output.add(inputEmail);

	//A chave primaria eh o e-mail
	String email=new String();
	String password=new String();

	while (rs.next()) {
	    email = (String) rs.getObject(2);
	    password = (String) rs.getObject(3);
	    if (email.equals(inputEmail) &&
		password.equals(inputPassword))
		//login = ok -> redireciona para a URL do dashboard
		result = "dashboard";

	}//end while

	model.put("records", output);
	return "db";

      //Redireciona para a URL
      return result;

    } catch (Exception e) {
	//model.put("message", e.getMessage());
      return "error";
    }
    }
    */
  /*@Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }
  */

}
