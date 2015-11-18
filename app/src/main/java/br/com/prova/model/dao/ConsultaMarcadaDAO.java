package br.com.prova.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Util;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de Consulta Marcada
 */
public class ConsultaMarcadaDAO {

    //ws ok
    private Banco mBanco;
    private SQLiteDatabase db;
    private UsuarioDAO mUsuarioDAO;
    private AgendaMedicoDAO mAgendaMedicoDAO;
    private String url = ConfiguracoesWS.URL_APLICACAO + "consulta/";

    public ConsultaMarcadaDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
        mUsuarioDAO = new UsuarioDAO();
        mAgendaMedicoDAO = new AgendaMedicoDAO(context);
    }

    /**
     * @return List<ConsultaMarcada>
     * <p/>
     * Método que retorna uma lista de ConsultaMarcada onde o valor do campo Situação seja igual a 'M'
     */
    public List listarMarcadas() {
        List<ConsultaMarcada> consultas = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

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

                Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

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
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

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

                Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

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

    /**
     * @param consultaMarcada
     * @param dataCancelamento
     * @return boolean
     * <p/>
     * Método que preenche a data do cancelamento
     * e altera o valor da Situação, da Consulta e da Agenda do Médico, para Cancelada,
     * de acordo com os dados passados por parâmetro e retorna True caso a alteração seja
     * bem-sucedida e False caso não seja.
     */
    public boolean cancelar(ConsultaMarcada consultaMarcada) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String hoje = Util.getDataAtual(new Date());
            String caminho = url + "cancelar/" + consultaMarcada.getId() + "/" + hoje;
            String[] resposta = new WebServiceCliente().get(caminho, false);

            return !resposta[1].isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean inserir(ConsultaMarcada consultaMarcada) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String caminho = url + "inserir/" + consultaMarcada.getIdAgendaMedico() + "/"
                    + consultaMarcada.getUsuario().getId() + "/" +
                    Util.getDataAtual(consultaMarcada.getDataMarcacaoConsulta());

            String[] resposta = new WebServiceCliente().get(caminho, false);

            mAgendaMedicoDAO.alterar(consultaMarcada.getIdAgendaMedico(), Situacao.MARCADA);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}