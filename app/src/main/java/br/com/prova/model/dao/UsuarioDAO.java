package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;

import com.google.gson.Gson;

import br.com.prova.model.bean.Usuario;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

public class UsuarioDAO {

    private Banco mBanco;
    private SQLiteDatabase db;
    private String local = ConfiguracoesWS.URL_APLICACAO + "usuario/";

    public UsuarioDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
    }

    public Usuario selecionarPorLogin(final String login) {
        Usuario usuario = new Usuario();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(local + "selecionarPorLogin/" + login, false);

            if (resposta[0].equals("200")) {
                Gson g = new Gson();
                usuario = g.fromJson(resposta[1], Usuario.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

    /**
     * @param id
     * @return Usuario
     * <p/>
     * Método que seleciona um Usuario, através de um Id passado por parâmetro
     */
    public Usuario selecionarPorId(int id) {
        Usuario usuario = new Usuario();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(local + "selecionarPorId/" + id, false);

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