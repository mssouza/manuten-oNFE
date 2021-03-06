/**
 * 
 */
package br.com.samuelweb.nfe;

import javax.xml.bind.JAXBException;

import br.com.samuelweb.nfe.dom.Enum.TipoManifestacao;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.Estados;
import br.com.samuelweb.nfe.util.Tipo;
import br.com.samuelweb.nfe.util.ValidadorCpfCnpj;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema.retConsCad.TRetConsCad;
import br.inf.portalfiscal.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TInutNFe;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsReciNFe.TRetConsReciNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;

/**
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 */
public class Nfe {

	/**
	 * Construtor privado
	 */
	private Nfe() {
	}

	/**
	 * Classe Reponsavel Por Consultar a Distribuiçao da NFE na SEFAZ
	 *
	 * @param tipoCliente
	 *            Informar DistribuicaoDFe.CPF ou DistribuicaoDFe.CNPJ
	 * @param cpfCnpj
	 * @param tipoConsulta
	 *            Informar DistribuicaoDFe.NSU ou DistribuicaoDFe.CHAVE
	 * @param nsuChave
	 * @return
	 * @throws NfeException
	 */
	public static RetDistDFeInt distribuicaoDfe(String tipoCliente, String cpfCnpj, String tipoConsulta,
			String nsuChave) throws NfeException {

		if (ValidadorCpfCnpj.isValidCNPJ(cpfCnpj) == false && ValidadorCpfCnpj.isValidCPF(cpfCnpj) == false) {
			throw new NfeException("CPF OU CNPJ INVÁLIDO!");
		}

		return DistribuicaoDFe.consultaNfe(tipoCliente, cpfCnpj, tipoConsulta, nsuChave);

	}

	/**
	 * Metodo Responsavel Buscar o Status de Serviço do Servidor da Sefaz No
	 * tipo Informar Tipo.NFE.get() ou Tipo.NFCE.get()
	 *
	 * @param tipo
	 * @return
	 * @throws NfeException
	 */
	public static TRetConsStatServ statusServico(String tipo) throws NfeException {

		return Status.statusServico(tipo);

	}

	/**
	 * Classe Reponsavel Por Consultar o status da NFE na SEFAZ No tipo Informar
	 * Tipo.NFE.get() ou Tipo.NFCE.get()
	 *
	 * @param chave
	 * @param tipo
	 * @return TRetConsSitNFe
	 * @throws NfeException
	 */
	public static TRetConsSitNFe consultaXml(String chave, String tipo) throws NfeException {

		return ConsultaXml.consultaXml(chave, tipo);

	}

	/**
	 * Classe Reponsavel Por Consultar o cadastro do Cnpj/CPF na SEFAZ
	 *
	 * @param tipo
	 *            Usar ConsultaCadastro.CNPJ ou ConsultaCadastro.CPF
	 * @param cpfCnpj
	 * @param estado
	 * @return TRetConsCad
	 * @throws NfeException
	 */
	public static TRetConsCad consultaCadastro(String tipo, String cpfCnpj, Estados estado) throws NfeException {

		if (ValidadorCpfCnpj.isValidCNPJ(cpfCnpj) == false && ValidadorCpfCnpj.isValidCPF(cpfCnpj) == false) {
			throw new NfeException("CPF OU CNPJ INVÁLIDO!");
		}

		return ConsultaCadastro.consultaCadastro(tipo, cpfCnpj, estado);

	}

	/**
	 * Classe Reponsavel Por Consultar o retorno da NFE na SEFAZ No tipo
	 * Informar Tipo.NFE.get() ou Tipo.NFCE.get()
	 *
	 * @param recibo
	 * @param tipo
	 * @return
	 * @throws NfeException
	 */
	public static TRetConsReciNFe consultaRecibo(String recibo, Tipo tipo) throws NfeException {

		return ConsultaRecibo.reciboNfe(recibo, tipo);

	}

	/**
	 * Classe Reponsavel Por Inutilizar a NFE na SEFAZ No tipo Informar
	 * Tipo.NFE.get() ou Tipo.NFCE.get() Id = Código da UF + Ano (2 posições) +
	 * CNPJ + modelo + série + número inicial e número final precedida do
	 * literal “ID”
	 *
	 * @param id
	 * @param valida
	 * @param tipo
	 * @return
	 * @throws NfeException
	 */
	public static TRetInutNFe inutilizacao(String id, String motivo, Tipo tipo, boolean validar) throws NfeException {

		return Inutilizar.inutiliza(id, motivo, tipo, validar);

	}

	/**
	 * Classe Reponsavel Por criar o Objeto de Inutilização No tipo Informar
	 * Tipo.NFE.get() ou Tipo.NFCE.get() Id = Código da UF + Ano (2 posições) +
	 * CNPJ + modelo + série + número inicial e número final precedida do
	 * literal “ID”
	 *
	 * @param id
	 * @param valida
	 * @param tipo
	 * @return
	 * @throws NfeException
	 */
	public static TInutNFe criaObjetoInutilizacao(String id, String motivo, Tipo tipo)
			throws NfeException, JAXBException {

		TInutNFe inutNFe = Inutilizar.criaObjetoInutiliza(id, motivo, tipo);

		String xml = XmlUtil.objectToXml(inutNFe);
		xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");

		return XmlUtil.xmlToObject(Assinar.assinaNfe(xml, Assinar.INFINUT), TInutNFe.class);

	}

	/**
	 * Metodo para Montar a NFE.
	 *
	 * @param enviNFe
	 * @param valida
	 * @return
	 * @throws NfeException
	 */
	public static TEnviNFe montaNfe(TEnviNFe enviNFe, boolean valida) throws NfeException {

		return Enviar.montaNfe(enviNFe, valida);

	}

	/**
	 * Metodo para Enviar a NFE. No tipo Informar Tipo.NFE.get() ou
	 * Tipo.NFCE.get()
	 *
	 * @param enviNFe
	 * @return
	 * @throws NfeException
	 */
	public static TRetEnviNFe enviarNfe(TEnviNFe enviNFe) throws NfeException {

		Tipo tipo;
		if (enviNFe.getNFe().get(0).getInfNFe().getIde().getMod().equals(55))
			tipo = Tipo.NFE;
		else
			tipo = Tipo.NFCE;

		if (enviNFe.getNFe().get(0).getSignature() == null) {
			throw new NfeException("Nota Fiscal não assinada!");
		}

		return Enviar.enviaNfe(enviNFe, tipo);

	}

	/**
	 * * Metodo para Cancelar a NFE. No tipo Informar Tipo.NFE.get() ou
	 * Tipo.NFCE.get()
	 *
	 * @param envEvento
	 * @return
	 * @throws NfeException
	 */
	public static TRetEnvEvento cancelarNfe(TEnvEvento envEvento, boolean valida, Tipo tipo) throws NfeException {

		return Cancelar.eventoCancelamento(envEvento, valida, tipo);

	}

	/**
	 * * Metodo para Envio da Carta De Correção da NFE. No tipo Informar
	 * Tipo.NFE.get() ou Tipo.NFCE.get()
	 *
	 * @param evento
	 * @param valida
	 * @param tipo
	 * @return
	 * @throws NfeException
	 */
	public static br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento cce(
			br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento evento, boolean valida, Tipo tipo) throws NfeException {

		return CartaCorrecao.eventoCCe(evento, valida, tipo);

	}

	/**
	 * Metodo para Manifestação da NFE.
	 *
	 * @param envEvento
	 * @param valida
	 * @return
	 * @throws NfeException
	 */
	public static br.inf.portalfiscal.nfe.schema.envConfRecebto.TRetEnvEvento manifestacao(String chave,
			TipoManifestacao manifestacao, String cnpj, String motivo, String data) throws NfeException {

		return ManifestacaoDestinatario.eventoManifestacao(chave, manifestacao, cnpj, data, motivo);

	}

}
