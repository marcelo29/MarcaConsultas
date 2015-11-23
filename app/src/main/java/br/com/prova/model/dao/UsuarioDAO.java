package br.com.prova.model.dao;

import android.os.StrictMode;

import com.google.gson.Gson;

import br.com.prova.model.bean.Usuario;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

public class UsuarioDAO {

    //ws ok
    private String url = ConfiguracoesWS.URL_APLICACAO + "usuario/";

    public UsuarioDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Usuario selecionarPorLogin(final String login) {
        Usuario usuario = new Usuario();

        try {
            String[] resposta = new WebServiceCliente().get(url + "selecionarPorLogin/" + login, false);

            if (resposta[0].equals("200")) {
                Gson g = new Gson();
                usuario = g.fromJson(resposta[1], Usuario.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

}