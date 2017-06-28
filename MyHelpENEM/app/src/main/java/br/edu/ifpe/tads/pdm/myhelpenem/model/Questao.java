package br.edu.ifpe.tads.pdm.myhelpenem.model;

public class Questao {

    /*A key é para que o objeto tenha uma referencia da chave criada no firebase
    com isso é possível manipula-lo no banco*/
    private String key;
    private String pergunta;
    private String categoria;
    private String resposta;
    private String alternativa1;
    private String alternativa2;
    private String alternativa3;
    private String alternativa4;

    public Questao() {
    }

    public Questao(String key, String pergunta, String categoria, String resposta, String alternativa1, String alternativa2, String alternativa3, String alternativa4) {
        this.key = key;
        this.pergunta = pergunta;
        this.categoria = categoria;
        this.resposta = resposta;
        this.alternativa1 = alternativa1;
        this.alternativa2 = alternativa2;
        this.alternativa3 = alternativa3;
        this.alternativa4 = alternativa4;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getAlternativa1() {
        return alternativa1;
    }

    public void setAlternativa1(String alternativa1) {
        this.alternativa1 = alternativa1;
    }

    public String getAlternativa2() {
        return alternativa2;
    }

    public void setAlternativa2(String alternativa2) {
        this.alternativa2 = alternativa2;
    }

    public String getAlternativa3() {
        return alternativa3;
    }

    public void setAlternativa3(String alternativa3) {
        this.alternativa3 = alternativa3;
    }

    public String getAlternativa4() {
        return alternativa4;
    }

    public void setAlternativa4(String alternativa4) {
        this.alternativa4 = alternativa4;
    }
}