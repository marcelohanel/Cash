package mah.com.br.cash.Diversos;

/**
 * Created by CGI - Marcelo on 12/05/2015.
 */
public class TempValores {

    private int codigo;
    private String nome;
    private String nome_1;
    private Double valor;
    private Double valor_1;
    private String data;
    private int data_inv;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValor_1() {
        return valor_1;
    }

    public void setValor_1(Double valor_1) {
        this.valor_1 = valor_1;
    }

    public String getNome_1() {
        return nome_1;
    }

    public void setNome_1(String nome_1) {
        this.nome_1 = nome_1;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        String[] vData = Funcoes.getData(data);
        this.data_inv = Integer.valueOf(vData[4]);
        this.data = data;
    }

    public int getData_inv() {
        return data_inv;
    }
}
