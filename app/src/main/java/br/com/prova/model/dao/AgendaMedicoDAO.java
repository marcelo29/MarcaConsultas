package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.model.bean.AgendaMedico;
import br.com.prova.model.bean.LocalAtendimento;
import br.com.prova.model.bean.Medico;
import br.com.prova.util.Util;
import br.com.prova.ws.ConfiguracoesWS;
import br.com.prova.ws.WebServiceCliente;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de Agenda do Medico
 */
public class AgendaMedicoDAO {

    private Banco mBanco;
    private SQLiteDatabase db;
    private MedicoDAO mMedicoDAO;
    private LocalAtendimentoDAO mLocalAtendimentoDAO;
    private String url = ConfiguracoesWS.URL_APLICACAO + "agenda/";

    public AgendaMedicoDAO(Context context) {

        if (mBanco == null)
            mBanco = new Banco(context);
        mMedicoDAO = new MedicoDAO(context);
        mLocalAtendimentoDAO = new LocalAtendimentoDAO(context);
    }

    /**
     * @param id
     * @return AgendaMedico
     * <p/>
     * Método que seleciona uma AgendaMedico, através de um Id passado por parâmetro
     */
    public AgendaMedico selecionarPorId(int id) {
        AgendaMedico agendaMedico = new AgendaMedico();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "selecionarPorId/" + id, false);

            if (resposta[0].equals("200")) {
                Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                agendaMedico = g.fromJson(resposta[1], AgendaMedico.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agendaMedico;
    }

    /**
     * @param situacao
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, através do valor da Situação, passado por parâmetro
     */
    public List<AgendaMedico> listarPorSituacao(Situacao situacao) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "listarPorSituacao/" +
                    situacao.getNome(), false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("agendas");

                for (int i = 0; i < array.size(); i++) {
                    AgendaMedico agenda = g.fromJson(array.get(i), AgendaMedico.class);
                    agendas.add(agenda);
                }
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorSituacao", e.getMessage());
        }

        return agendas;
    }

    /**
     * @param idAgendaMedico
     * @param situacao
     * @return boolean
     * <p/>
     * Método que altera o valor da Situação, da Agenda Medico, especificados por parâmetro
     * e retorna True caso seja bem-sucedida e False caso não seja.
     */
    public boolean alterar(int idAgendaMedico, Situacao situacao) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "alterar/" + idAgendaMedico + "/" + situacao.getNome(), false);

            return !resposta[1].isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param localAtendimento
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, de um determiando Local de Atendimento
     */
    public List<AgendaMedico> listarPorLocalAtendimento(LocalAtendimento localAtendimento) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "listarPorLocalAtendimento/" +
                    localAtendimento.getId(), false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("agendas");

                for (int i = 0; i < array.size(); i++) {
                    AgendaMedico agenda = g.fromJson(array.get(i), AgendaMedico.class);
                    agendas.add(agenda);
                }
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorLocalAtendimento", e.getMessage());
        }

        return agendas;
    }

    /**
     * @param medico
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, de um determinado medico
     */
    public List<AgendaMedico> listarPorMedico(Medico medico) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "listarPorMedico/" +
                    medico.getId(), false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("agendas");

                for (int i = 0; i < array.size(); i++) {
                    AgendaMedico agenda = g.fromJson(array.get(i), AgendaMedico.class);
                    agendas.add(agenda);
                }
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorMedico", e.getMessage());
        }

        return agendas;
    }

    /**
     * @param data
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, através de uma data passada por parâmetro
     */
    public List<AgendaMedico> listarPorData(java.util.Date data) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            String[] resposta = new WebServiceCliente().get(url + "listarPorData/" +
                    Util.convertDateToStrInvertido(data), false);

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

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("agendas");

                for (int i = 0; i < array.size(); i++) {
                    AgendaMedico agenda = g.fromJson(array.get(i), AgendaMedico.class);
                    agendas.add(agenda);
                }
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorData", e.getMessage());
        }

        return agendas;
    }

}