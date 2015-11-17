package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
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
        AgendaMedico agendaMedico = null;
        Cursor cursor;
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.ID_AGENDA_MEDICO + "=" + id;

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext())
            agendaMedico = getAgendaMedico(cursor);

        db.close();

        return agendaMedico;
    }

    /**
     * @param situacao
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, através do valor da Situação, passado por parâmetro
     */
    public List<AgendaMedico> listarPorSituacao(Situacao situacao) {
        Cursor cursor;
        List<AgendaMedico> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + situacao.getNome() + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
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
        String sql = "update " + mBanco.TB_AGENDA_MEDICO +
                " set " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + situacao.getNome() +
                "' where " + mBanco.ID_AGENDA_MEDICO + " = " + idAgendaMedico;

        db = mBanco.getWritableDatabase();

        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
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

                Gson g = new Gson();

                JsonParser parser = new JsonParser();

                JsonArray array = null;

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("agendas");

                for (int i = 0; i < array.size(); i++) {
                    AgendaMedico agenda = g.fromJson(array.get(i), AgendaMedico.class);
                    agendas.add(agenda);
                }
            }
        } catch (Exception e) {
            Log.e("ErroListarAgendas", e.getMessage());
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
        Cursor cursor;
        List<AgendaMedico> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + Situacao.DISPONIVEL.getNome() +
                "' and " + mBanco.MEDICO_AGENDA_MEDICO + " = " + medico.getId();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     * @param data
     * @return List<AgendaMedico>
     * <p/>
     * Método que retorna uma lista de AgendaMedico, através de uma data passada por parâmetro
     */
    public List<AgendaMedico> listarPorData(java.util.Date data) {
        Cursor cursor;
        List<AgendaMedico> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + Situacao.DISPONIVEL.getNome() +
                "' and " + mBanco.DATA_AGENDA_MEDICO + " = '" + Util.convertDateToStrInvertido(data) + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     * @param cursor
     * @return AgendaMedico
     * <p/>
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de AgendaMedico.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private AgendaMedico getAgendaMedico(Cursor cursor) {
        AgendaMedico agendaMedico = new AgendaMedico();

        agendaMedico.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_AGENDA_MEDICO)));
        agendaMedico.setMedico(mMedicoDAO.selecionarPorId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.MEDICO_AGENDA_MEDICO))));
        agendaMedico.setData(Date.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.DATA_AGENDA_MEDICO))));
        agendaMedico.setHora(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.HORA_AGENDA_MEDICO)));
        agendaMedico.setLocalAtendimento(mLocalAtendimentoDAO.selecionarPorId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.LOCAL_ATENDIMENTO_AGENDA_MEDICO))));

        switch (cursor.getString(cursor.getColumnIndexOrThrow(mBanco.SITUACAO_AGENDA_MEDICO))) {
            case "D":
                agendaMedico.setSituacao(Situacao.DISPONIVEL);
                break;
            case "M":
                agendaMedico.setSituacao(Situacao.MARCADA);
                break;
            case "C":
                agendaMedico.setSituacao(Situacao.CANCELADA);
                break;
        }

        return agendaMedico;
    }
}