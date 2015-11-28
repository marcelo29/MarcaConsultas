package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public List listar() {
        List<Medico> medicos = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listar", false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new Gson();

                medicos = gson.fromJson(json, new TypeToken<ArrayList<Medico>>() {
                }.getType());
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

                Gson gson = new Gson();

                medicos = gson.fromJson(json, new TypeToken<ArrayList<Medico>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroMedicoListarPorEspecialidade", e.getMessage());
        }

        return medicos;
    }

}