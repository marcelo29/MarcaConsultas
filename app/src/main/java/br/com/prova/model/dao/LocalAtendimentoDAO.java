package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.LocalAtendimento;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de LocalAtendimento
 */
public class LocalAtendimentoDAO {

    //ws
    private String url = ConfiguracoesWS.URL_APLICACAO + "local/";

    public LocalAtendimentoDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public LocalAtendimento selecionarPorId(int id) {
        LocalAtendimento local = new LocalAtendimento();

        try {
            String[] resposta = new WebServiceCliente().get(url + "selecionarPorId/" + id, false);

            if (resposta[0].equals("200")) {
                Gson g = new Gson();
                local = g.fromJson(resposta[1], LocalAtendimento.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return local;
    }

    public List listar() {
        List<LocalAtendimento> locais = new ArrayList<>();

        try {
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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("locais");

                for (int i = 0; i < array.size(); i++) {
                    LocalAtendimento local = g.fromJson(array.get(i), LocalAtendimento.class);
                    locais.add(local);
                }
            }
        } catch (Exception e) {
            Log.e("ErroLocalListar", e.getMessage());
        }

        return locais;
    }

}