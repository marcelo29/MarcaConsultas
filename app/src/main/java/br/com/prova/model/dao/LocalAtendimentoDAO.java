package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public List listar() {
        List<LocalAtendimento> locais = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listar/", false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                Gson gson = new Gson();

                locais = gson.fromJson(json, new TypeToken<ArrayList<LocalAtendimento>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroLocalListar", e.getMessage());
        }

        return locais;
    }

}