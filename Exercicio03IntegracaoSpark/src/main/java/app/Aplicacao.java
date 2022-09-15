package app;

import static spark.Spark.*;
import service.UsuarioService;


public class Aplicacao {

    private static UsuarioService produtoService = new UsuarioService();

    public static void main(String[] args) {
        port(6789);

        staticFiles.location("/public");

        post("/usuario/insert", (request, response) -> produtoService.insert(request, response));

        get("/usuario/list", (request, response) -> produtoService.getAll(request, response));

        get("/usuario/:id", (request, response) -> produtoService.get(request, response));

        get("/usuario/update/:id", (request, response) -> produtoService.getToUpdate(request, response));

        post("/usuario/update/:id", (request, response) -> produtoService.update(request, response));

        get("/usuario/delete/:id", (request, response) -> produtoService.delete(request, response));


    }
}