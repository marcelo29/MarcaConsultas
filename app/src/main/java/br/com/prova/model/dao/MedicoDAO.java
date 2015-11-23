package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.Especialidade;
import br.com.prova.model.bean.Medico;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de Medico
 */
public class MedicoDAO {

    //ws
    private EspecialidadeDAO mEspecialidadeDAO;
    private String url = ConfiguracoesWS.URL_APLICACAO + "medico/";

    public MedicoDAO() {
        mEspecialidadeDAO = new EspecialidadeDAO();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Medico selecionarPorId(int id) {
        Medico medico = new Medico();

        try {
            String[] resposta = new WebServiceCliente().get(url + "selecionarPorId/" + id, false);

            if (resposta[0].equals("200")) {
                Gson g = new Gson();
                medico = g.fromJson(resposta[1], Medico.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return medico;
    }

    public List listar() {
        List<Medico> medicos = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listar", false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("medicos");

                for (int i = 0; i < array.size(); i++) {
                    Medico medico = g.fromJson(array.get(i), Medico.class);
                    medicos.add(medico);
                }
            }
        } catch (Exception e) {
            Log.e("ErroMedicoListar", e.getMessage());
        }

        return medicos;
    }

    public List<Medico> listarPorEspecialidade(Especialidade especialidade) {
        List<Medico> medicos = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarPorEspecialidade/" + especialidade.getId(), false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("medicos");

                for (int i = 0; i < array.size(); i++) {
                    Medico medico = g.fromJson(array.get(i), Medico.class);
                    medicos.add(medico);
                }
            }
        } catch (Exception e) {
            Log.e("ErroMedicoListarPorEspecialidade", e.getMessage());
        }

        return medicos;
    }

}