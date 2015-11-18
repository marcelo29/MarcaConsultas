package br.com.prova.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.Especialidade;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de Especialidade
 */
public class EspecialidadeDAO {

    //ws ok
    private Banco mBanco;
    private SQLiteDatabase db;
    private String url = ConfiguracoesWS.URL_APLICACAO + "especialidade/";

    public EspecialidadeDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
    }

    /**
     * @param Id
     * @return Especialidade
     * <p/>
     * Método que seleciona uma Especialidade, através de um Id passado por parâmetro
     */
    public Especialidade selecionarPorId(int Id) {
        Especialidade especialidade = new Especialidade();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "selecionarPorId/" + Id, false);

            if (resposta[0].equals("200")) {
                Gson g = new Gson();
                especialidade = g.fromJson(resposta[1], Especialidade.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return especialidade;
    }

    /**
     * @return List<Especialidade>
     * <p/>
     * Método que retorna a lista de todas as Especialidades
     */
    public List listar() {
        List<Especialidade> especialidades = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "listar/", false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                if (!json.contains("[")) {
                    StringBuilder stringBuilder = new StringBuilder(json);
                    stringBuilder.insert(10, "[");
                    stringBuilder.insert(json.length(), "]");

                    json = stringBuilder.toString();
                }

                Gson g = new Gson();

                JsonParser parser = new JsonParser();

                JsonArray array = null;

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("especialidades");

                for (int i = 0; i < array.size(); i++) {
                    Especialidade especialidade = g.fromJson(array.get(i), Especialidade.class);
                    especialidades.add(especialidade);
                }
            }
        } catch (Exception e) {
            Log.e("ErroEspecialidadeListar", e.getMessage());
        }

        return especialidades;
    }

}