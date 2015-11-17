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
        Usuario usuario = null;
        Cursor cursor;
        String condicao = mBanco.ID_USUARIO + "=" + id + "";

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery("select * from " + mBanco.TB_USUARIO + " where " + condicao, null);

        while (cursor.moveToNext())
            usuario = getUsuario(cursor);

        db.close();
        cursor.close();

        return usuario;
    }

    /**
     * @param cursor
     * @return Usuario
     * <p/>
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de Usuario.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private Usuario getUsuario(Cursor cursor) {
        Usuario usuario = new Usuario();

        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_USUARIO)));
        usuario.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.LOGIN_USUARIO)));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.SENHA_USUARIO)));
        usuario.setPerfil(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.PERFIL_USUARIO)));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.EMAIL_USUARIO)));

        return usuario;
    }
}