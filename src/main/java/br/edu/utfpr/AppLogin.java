package br.edu.utfpr;

public class AppLogin {

    private String username;

    public AppLogin(String username){
	     setUsername(username);
    }

   public String getUsername(){
	    return this.username;
    }

    public void setUsername(String username){
	     this.username = username;
    }

    public String getLoginXML(String permissao){
      StringBuffer xml = new StringBuffer();
      xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      xml.append("<project xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");

      xml.append("<username>"+username+"</username>\n");
      xml.append("<permission>"+permissao+"</permission>\n");

      xml.append("</project>");
      return xml.toString();
    }

}
