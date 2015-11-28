package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Util;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

public class ConsultaMarcadaDAO {

    // ws ok
    private UsuarioDAO mUsuarioDAO;
    private AgendaMedicoDAO mAgendaMedicoDAO;
    private String url = ConfiguracoesWS.URL_APLICACAO + "consulta/";

    public ConsultaMarcadaDAO() {
        mUsuarioDAO = new UsuarioDAO();
        mAgendaMedicoDAO = new AgendaMedicoDAO();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public List listarMarcadas() {
        List<ConsultaMarcada> consultas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarMarcadas", false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                consultas = gson.fromJson(json, new TypeToken<ArrayList<ConsultaMarcada>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroConsultaListar", e.getMessage());
        }
        return consultas;
    }

    public List<ConsultaMarcada> listarMarcadasPorUsuario(Usuario usuario) {
        List<ConsultaMarcada> consultas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarMarcadas/" + usuario.getId(), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                consultas = gson.fromJson(json, new TypeToken<ArrayList<ConsultaMarcada>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroConsultaListarPorUsuario", e.getMessage());
        }

        return consultas;
    }

    public boolean cancelar(ConsultaMarcada consultaMarcada) {
        try {
            Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataEUA).create();

            String consultaJson = gson.toJson(consultaMarcada);

            String caminho = url + "cancelar";

            String[] resposta = new WebServiceCliente().post(caminho, consultaJson);

            new AgendaMedicoDAO().alterar(consultaMarcada);

            return !resposta[1].isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean inserir(ConsultaMarcada consultaMarcada) {
        try {
            String caminho = url + "inserir";

            Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataEUA).create();

            String consultaJson = gson.toJson(consultaMarcada);

            String[] resposta = new WebServiceCliente().post(caminho, consultaJson);

            new AgendaMedicoDAO().alterar(consultaMarcada);

            return !resposta[1].isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}