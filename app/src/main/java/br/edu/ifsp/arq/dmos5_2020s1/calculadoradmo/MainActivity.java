package br.edu.ifsp.arq.dmos5_2020s1.calculadoradmo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifsp.arq.dmos5_2020s1.calculadoradmo.constants.Constantes;
import br.edu.ifsp.arq.dmos5_2020s1.calculadoradmo.model.Calculadora;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView resultado;
    private Button[] numericosButton;
    private Button[] operadoresButton;
    private Button apagarButton;

    private Calculadora mCalculadora;

    /* Esta flag se responsabiliza a dizer quando
       sobrescrever um número (novo número no TextView) ou não. */
    private boolean flgNovoNumero = true;

    /* Esta flag se responsabiliza a mostrar qual número utilizar
       quando ocorre um erro ao transformar a entrada em float. */
    private int flgOperacao = Constantes.NULO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultado = findViewById(R.id.textview_saida_resultado);
        apagarButton = findViewById(R.id.button_apagar);

        //Definindo o OnClickListern do botão
        apagarButton.setOnClickListener(this);
        setBotoesNumericos();
        setBotoesOperadores();

        // Instanciando a Calculadora
        mCalculadora = mCalculadora.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v == apagarButton){
            apagar();
        }
    }

    private void setBotoesNumericos () {
        int[] numericos = Constantes.NUMERICOS;
        numericosButton = new Button[numericos.length];

        for(int i = 0; i < numericosButton.length; i++) {
            numericosButton[i] = findViewById(numericos[i]);
            numericosButton[i].setOnClickListener(this::setNumero);
        }
    }

    private void setBotoesOperadores() {
        int[] operadores = Constantes.OPERADORES;
        operadoresButton = new Button[operadores.length];

        for(int i = 0; i < operadoresButton.length; i++) {
            operadoresButton[i] = findViewById(operadores[i]);
            operadoresButton[i].setOnClickListener(this::calcular);
        }
    }

    private void setNumero(View v) {
        // Pegando o valor do TextView.
        String numero = getHint(resultado);

        // Pegando o valor do botão numérico.
        String novoNumero = getValorButton((Button)v);

        if (flgNovoNumero) {
            setHint(String.format("%s", novoNumero));
            flgNovoNumero = false;
        } else {
            setHint(String.format("%s%s", numero, novoNumero));
        }
    }

    private void setHint(String saida) {
        resultado.setHint(saida);
    }

    private String getHint(TextView textView) {
        return textView.getHint().toString();
    }

    private String getValorButton(Button button) {
        return button.getText().toString();
    }

    private int getValorOperador(String operando) {
        int valorOperador;

        switch (operando) {
            case "+": {
                valorOperador = Constantes.ADICAO;
                break;
            } case "-": {
                valorOperador = Constantes.SUBTRACAO;
                break;
            } case "X": {
                valorOperador = Constantes.MULTIPLICACAO;
                break;
            } case "/": {
                valorOperador = Constantes.DIVISAO;
                break;
            } case "=": {
                valorOperador = Constantes.RESULTADO;
                break;
            } default: {
                valorOperador = Constantes.NULO;
                break;
            }
        }

        return valorOperador;
    }

    private int getNumeroSeExcecao() {
        int numero;

        if (flgOperacao == Constantes.MULTIPLICACAO || flgOperacao == Constantes.DIVISAO) {
            /* Se for uma multiplicação ou divisão,
            o número recebe o valor 1 para não afetar o cálculo. */
            numero = 1;
        } else {
            /* Se não, o número recebe o valor 0 para não afetar o cálculo. */
            numero = 0;
        }

        return numero;
    }

    private void calcular(View v) {
        float numero;

        // Pegando o valor do TextView.
        String entrada = getHint(resultado);

        // Pegando o valor do operador.
        String operador = getValorButton((Button)v);
        int valorOperador = getValorOperador(operador);

        // Transformando a string entrada em float
        try{
            numero = Float.parseFloat(entrada);
        }catch (NumberFormatException ex){
            Toast.makeText(this, "Entrada inválida!", Toast.LENGTH_SHORT).show();
            numero = getNumeroSeExcecao();
        }

        // Imprimindo o resultado
        float saida = mCalculadora.calcular(valorOperador,numero);
        setHint(String.valueOf(saida));

        flgOperacao = valorOperador;
        flgNovoNumero = true;
    }

    private void apagar() {
        setHint("0.0");
        mCalculadora.c();
        flgNovoNumero = true;
    }
}