/*
 * Implementa��o do Bloqueto de Cobran�as do Banco do Brasil
 * - Conv�nio com 6 posi��es
 * 
 * scoelho@stefanini.com
 */
package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public class BloquetoBBConvenio6 extends BloquetoBBImpl implements BloquetoBB {

	@Override
	protected void validaDados() throws ManagerException {

		if (codigoBanco == null || codigoBanco.length() != 3) {
			throw new ManagerException(
					"C�digo do Banco n�o informado ou com tamanho diferente de 3 posi��es");
		}

		if (codigoMoeda == null || codigoMoeda.length() != 1) {
			throw new ManagerException(
					"C�digo de moeda n�o informado ou inv�lido");
		}

		if (dataVencimento == null) {
			throw new ManagerException("Data de vencimento n�o informada");
		}

		if (valor == null) {
			throw new ManagerException(
					"Valor do bloqueto bancário n�o informado");
		}

		if (numeroConvenioBanco == null || numeroConvenioBanco.length() != 6) {
			throw new ManagerException(
					"n�mero de conv�nio n�o informado ou o conv�nio informado � inv�lido. O conv�nio deve ter 6 posi��es");
		}

		if (complementoNumeroConvenioBancoSemDV == null
				&& complementoNumeroConvenioBancoSemDV.length() != 5) {
			throw new ManagerException(
					"Complemento do n�mero do conv�nio n�o informado. O complemento deve ter 5 posi��es");
		}

		if (numeroAgenciaRelacionamento == null
				|| numeroAgenciaRelacionamento.length() != 4) {
			throw new ManagerException(
					"n�mero da ag�ncia de Relacionamento n�o informado. O n�mero da ag�ncia deve ter 4 posi��es");
		}

		if (contaCorrenteRelacionamentoSemDV == null
				|| contaCorrenteRelacionamentoSemDV.length() != 8) {
			throw new ManagerException(
					"Conta corrente de relacionamento n�o informada. O n�mero da conta deve ter 8 posi��es");
		}

		if (tipoCarteira == null || tipoCarteira.length() != 2) {
			throw new ManagerException(
					"Tipo carteira n�o informado ou o valor � inv�lido");
		}
		
		if("21".equals(tipoCarteira) && (complementoNumeroConvenioBancoSemDV.length()) != 17){
			throw new ManagerException(
					"Numero do Convenio do Banco + Complemento do numero do Conv�nio deve ter 17 posi��es para o tipo Convenio igual a 21");
		}
		
		if(!"21".equals(tipoCarteira) && (complementoNumeroConvenioBancoSemDV.length() + numeroConvenioBanco.length()) != 11){
			throw new ManagerException(
					"Numero do Convenio do Banco + Complemento do numero do Conv�nio deve ter 11 posi��es para o tipo Convenio igual a diferente de 21");
		}

		if (dataBase == null) {
			throw new ManagerException("A database n�o foi informada.");
		}

	}

	public BloquetoBBConvenio6(String codigoBanco, String codigoMoeda,
			Date dataVencimento, Date dataBase, BigDecimal valor,
			String numeroConvenioBanco,
			String complementoNumeroConvenioBancoSemDV,
			String numeroAgenciaRelacionamento,
			String contaCorrenteRelacionamentoSemDV, String tipoCarteira)
			throws ManagerException {

		this.codigoBanco = codigoBanco;
		this.codigoMoeda = codigoMoeda;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.numeroConvenioBanco = numeroConvenioBanco;
		this.complementoNumeroConvenioBancoSemDV = complementoNumeroConvenioBancoSemDV;
		this.numeroAgenciaRelacionamento = numeroAgenciaRelacionamento;
		this.contaCorrenteRelacionamentoSemDV = contaCorrenteRelacionamentoSemDV;
		this.tipoCarteira = tipoCarteira;
		this.dataBase = dataBase;

		validaDados();

	}

	@Override
	protected String getLDNumeroConvenio() {
		
		String convenio = String.format("%06d", Long.valueOf(numeroConvenioBanco));
		return String.format("%s.%s", convenio.substring(0, 1), convenio.substring(1, 5));

	}

	@Override
	protected String getCodigoBarrasSemDigito() {

		init();

		StringBuilder buffer = new StringBuilder();
		
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		buffer.append(numeroConvenioBanco);
		buffer.append(complementoNumeroConvenioBancoSemDV);
		
		if(!"21".equals(tipoCarteira)){
			buffer.append(numeroAgenciaRelacionamento);
			buffer.append(contaCorrenteRelacionamentoSemDV);
		}
		
		buffer.append(tipoCarteira);

		return buffer.toString();
	}

	@Override
	public String getCodigoBarras() {

		init();

		StringBuilder buffer = new StringBuilder();

		buffer.append(codigoBanco); // Posi�ao 01 a 03 (3)
		buffer.append(codigoMoeda); // Posi�ao 04 a 04 (1)
		buffer.append(digitoVerificadorCodigoBarras
				(getCodigoBarrasSemDigito())); // Posi�ao 05 a 05 (1)
		
		buffer.append(fatorVencimento); // Posi�ao 06 a 09 (4)
		buffer.append(getValorFormatado()); // Posi�ao 10 a 19 (10)
		buffer.append(numeroConvenioBanco); // Posi�ao 20 a 25 (6)
		
		buffer.append(complementoNumeroConvenioBancoSemDV); // Posi�ao 26 a 30 (5)
		
		if(!"21".equals(tipoCarteira)){
			buffer.append(numeroAgenciaRelacionamento); // Posi�ao 31 a 34 (4)
			buffer.append(contaCorrenteRelacionamentoSemDV); // Posi�ao 35 a 42 (8)
		}
		
		buffer.append(tipoCarteira); // Posi�ao 43 a 44 (2)

		return buffer.toString();
	}

}
