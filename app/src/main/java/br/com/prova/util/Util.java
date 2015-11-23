package br.com.prova.util;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.prova.marcaconsultas.R;

/**
 * Created by Éverdes on 03/10/2015.
 * Classe com métodos uteis, que podem ser chamados em qualquer lugar da aplicação
 */
public class Util {

    public static final String formatoDataBR = "dd/MM/yyyy", formatoDataEUA = "yyyy-MM-dd";

    public static String getToday() {
        return new SimpleDateFormat(formatoDataBR).format(Calendar.getInstance().getTime());
    }

    public static String getDataAtual(Date date) {
        return new SimpleDateFormat(formatoDataEUA).format(date.getTime());
    }

    /**
     * @param data
     * @return String
     * <p/>
     * Método que recebe um objeto do tipo Date, e retorna um String com a data atual, em formato 'dia/mes/ano'
     */
    public static String convertDateToStr(Date data) {
        return new SimpleDateFormat(formatoDataBR).format(data);
    }

    /**
     * @param data
     * @return String
     * <p/>
     * Método que recebe um objeto do tipo Date, e retorna um String com a data atual, em formato 'ano-mes-dia'
     */
    public static String convertDateToStrInvertido(Date data) {
        return new SimpleDateFormat(formatoDataEUA).format(data.getTime());
    }

    /**
     * @param titulo
     * @param mensagem
     * @param contexto Método que exibe uma mensagem na tela, da aplicação, para o usuário
     */
    public static void showMessage(String titulo, String mensagem, Context contexto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setMessage(mensagem);
        builder.setIcon(R.drawable.ic_exclamacao_preto);
        builder.setNegativeButton("Ok", null);

        AlertDialog dialog = builder.create();
        dialog.setTitle(titulo);
        dialog.show();
    }

    public static boolean verificaConexao(Context ctx) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) ctx.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

            builder.setTitle("Aviso");
            builder.setMessage("Sem conexão com a internet, tente novamente mais tarde");
            builder.setNeutralButton("Ok", null);
            builder.setIcon(R.drawable.ic_exclamacao_preto);
            builder.show();

            conectado = false;
        }
        return conectado;
    }
}