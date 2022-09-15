package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;


public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private int CDG;


    public UsuarioService() {
        makeForm();
    }


    public void makeForm() {
        makeForm(FORM_INSERT, new Usuario());
    }


    public void makeForm(int tipo, Usuario usuario) {
        String nomeArquivo = "formulario.html";
        form = "";
        try{
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while(entrada.hasNext()){
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        }  catch (Exception e) { System.out.println(e.getMessage()); }

        String umUsuario = "";
        if(tipo != FORM_INSERT) {
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/usuario/list\">Novo Usuario</a></b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
            umUsuario += "\t<br>";
        }

        if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/usuario/";
            String name, descricao, buttonLabel;
            if (tipo == FORM_INSERT){
                action += "insert";
                name = "Inserir Usuario";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + usuario.getCodigo();
                name = "Atualizar Produto (ID " + usuario.getCodigo() + ")";
                buttonLabel = "Atualizar";
            }
            umUsuario += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td>Login: <input class=\"input--register\" type=\"text\" name=\"login\" value=\""+ usuario.getLogin() +"\"></td>";
            umUsuario += "\t\t\t<td>Senha: <input class=\"input--register\" type=\"password\" name=\"senha\" value=\""+ usuario.getSenha() +"\"></td>";
            umUsuario += "\t\t\t<td>Sexo: <input class=\"input--register\" type=\"text\" name=\"sexo\" value=\""+ usuario.getSexo() +"\"></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
            umUsuario += "\t</form>";
        } else if (tipo == FORM_DETAIL){
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Usuario (ID " + usuario.getCodigo() + ")</b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td>&nbsp;Login: "+ usuario.getLogin() +"</td>";
            umUsuario += "\t\t\t<td>Sexo: "+ usuario.getSexo() +"</td>";
            umUsuario += "\t\t\t<td>Senha: "+ usuario.getSenha() +"</td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-USUARIO>", umUsuario);

        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Usuarios</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Login</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Sexo</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Senha</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";

        List<Usuario> usuarios = usuarioDAO.get();

        if(usuarios.size() > 0) {
            CDG = usuarios.get(usuarios.size() - 1).getCodigo() + 1;
        } else {
            CDG = 0;
        }

        int i = 0;
        String bgcolor = "";
        for (Usuario u : usuarios) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" +
                    "\t<td>" + u.getLogin() + "</td>\n" +
                    "\t<td>" + u.getSexo() + "</td>\n" +
                    "\t<td>" + u.getSenha() + "</td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/" + u.getCodigo() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/update/" + u.getCodigo() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteProduto('"+u.getCodigo()+"', '"+ u.getLogin() + "', '" + u.getSenha() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-USUARIO>", list);
    }


    public Object insert(Request request, Response response) {
        String login = request.queryParams("login");
        String senha = request.queryParams("senha");
        String sexo = request.queryParams("sexo");
        String resp = "";

        Usuario usuario = new Usuario(CDG,login, senha, sexo.charAt(0));

        if(usuarioDAO.insert(usuario)) {
            resp = "Usuario inserido!";
            response.status(201); // 201 Created
        } else {
            resp = "Usuario não inserido!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }


    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = (Usuario) usuarioDAO.get(id);

        if (usuario != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, usuario);
        } else {
            response.status(404); // 404 Not found
            String resp = "Usuario não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
        }

        return form;
    }


    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = (Usuario) usuarioDAO.get(id);

        if (usuario != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, usuario);
        } else {
            response.status(404); // 404 Not found
            String resp = "Usuario não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
        }

        return form;
    }


    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);
        String resp = "";

        if (usuario != null) {
            usuario.setLogin(request.queryParams("login"));
            usuario.setSenha(request.queryParams("senha"));
            usuario.setSexo('M');
            usuarioDAO.update(usuario);
            response.status(200); // success
            resp = "Usuario atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Usuario não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object getAll(Request request, Response response){
        makeForm();
        return form;
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);
        String resp = "";

        System.out.println(id);

        if (usuario != null) {
            usuarioDAO.delete(id);
            response.status(200); // success
            resp = "Usuario excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Usuario não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }
}
