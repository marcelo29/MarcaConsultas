package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Util;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

public class ConsultaMarcadaDAO {

    //ws
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

                if (json.equals("null")) {
                    return null;
                }

                if (!json.contains("[")) {
                    StringBuilder stringBuilder = new StringBuilder(json);
                    stringBuilder.insert(10, "[");
                    stringBuilder.insert(json.length(), "]");

                    json = stringBuilder.toString();
                }

                Gson g = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                JsonParser parser = new JsonParser();

                JsonArray array = null;

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("consultas");

                for (int i = 0; i < array.size(); i++) {
                    ConsultaMarcada consulta = g.fromJson(array.get(i), ConsultaMarcada.class);
                    consultas.add(consulta);
                }
            }
        } catch (Exception e) {
            Log.e("ErroConsultaListar", e.getMessage());
        }

        return consultas;
    }

    /**
     * @param usuario
     * @return List<ConsultaMarcada>
     * <p/>
     * Método que lista todas as Consultas Marcadas pelo Usuário passado por parâmetro
     */
    public List<ConsultaMarcada> listarMarcadasPorUsuario(Usuario usuario) {
        List<ConsultaMarcada> consultas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarMarcadas/" + usuario.getId(), false);

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

                Gson g = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                JsonParser parser = new JsonParser();

                JsonArray array = null;

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("consultas");

                for (int i = 0; i < array.size(); i++) {
                    ConsultaMarcada consulta = g.fromJson(array.get(i), ConsultaMarcada.class);
                    consultas.add(consulta);
                }
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