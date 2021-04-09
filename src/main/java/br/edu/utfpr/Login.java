package br.edu.utfpr;

public class Login {

    private long id;
    private String nome;
    private String email;
    private String senha;    

    public Login(int id, String nome){
	this.id = id;
	this.nome = nome;
    }
    
    public long getId(){
	return id;
    }

    public void setId(long id){
	this.id = id;
    }

    public String getNome(){
	return nome;
    }

    public void setNome(String nome){
	this.nome = nome;
    }

   public String getEmail(){
	return email;
    }

    public void setEmail(String email){
	this.email = email;
    }

    public String getSenha(){
	return senha;
    }

    public void setSenha(String senha){
	this.senha = senha;
    }
}
