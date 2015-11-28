package br.com.prova.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.model.bean.AgendaMedico;
import br.com.prova.model.bean.ConsultaMarcada;
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

    // ws ok
    private MedicoDAO mMedicoDAO;
    private LocalAtendimentoDAO mLocalAtendimentoDAO;
    private String url = ConfiguracoesWS.URL_APLICACAO + "agenda/";

    public AgendaMedicoDAO() {
        mMedicoDAO = new MedicoDAO();
        mLocalAtendimentoDAO = new LocalAtendimentoDAO();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public AgendaMedico selecionarPorId(int id) {
        AgendaMedico agendaMedico = new AgendaMedico();

        try {
            String[] resposta = new WebServiceCliente().get(url + "selecionarPorId/" + id, false);

            if (resposta[0].equals("200")) {
                Gson g = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();
                agendaMedico = g.fromJson(resposta[1], AgendaMedico.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agendaMedico;
    }

    public List<AgendaMedico> listarPorSituacao(Situacao situacao) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarPorSituacao/" +
                    situacao.getNome(), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                agendas = gson.fromJson(json, new TypeToken<ArrayList<AgendaMedico>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorSituacao", e.getMessage());
        }

        return agendas;
    }

    public List<AgendaMedico> listarPorLocalAtendimento(LocalAtendimento localAtendimento) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarPorLocalAtendimento/" +
                    localAtendimento.getId(), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                agendas = gson.fromJson(json, new TypeToken<ArrayList<AgendaMedico>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorLocalAtendimento", e.getMessage());
        }

        return agendas;
    }

    public List<AgendaMedico> listarPorMedico(Medico medico) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarPorMedico/" +
                    medico.getId(), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                agendas = gson.fromJson(json, new TypeToken<ArrayList<AgendaMedico>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorMedico", e.getMessage());
        }

        return agendas;
    }

    public List<AgendaMedico> listarPorData(java.util.Date data) {
        List<AgendaMedico> agendas = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "listarPorData/" +
                    Util.convertDateToStrInvertido(data), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataBR).create();

                agendas = gson.fromJson(json, new TypeToken<ArrayList<AgendaMedico>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroAgendaListarPorData", e.getMessage());
        }

        return agendas;
    }

    public boolean alterar(ConsultaMarcada consulta) {
        try {
            Gson gson = new GsonBuilder().setDateFormat(Util.formatoDataEUA).create();

            String consultaJson = gson.toJson(consulta);

            String[] resposta = new WebServiceCliente().post(url + "alterar", consultaJson);

            return !resposta[1].isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}