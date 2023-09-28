package com.tool.model;

public class ExcelRecord {

	private String action;
	private String DataOraInizio;
	private String DataOraFine;
	private String CodiceEtso;
	private String IdMotivazione;
	private double PsMin;
	private double PsMax;
	private String IdAssetto;
	private double PtMin;
	private double PtMax;
	private int TRrisp;
	private double Gpa;
	private double Gpd;
	private int Tava;
	private int Tara;
	private int Brs;
	
	public ExcelRecord(String action, String dataOraInizio, String dataOraFine, String codiceEtso, String idMotivazione,
			double psMin, double psMax, String idAssetto, double ptMin, double ptMax, int tRrisp, double gpa,
			double gpd, int tava, int tara, int brs) {
		super();
		this.action = action;
		DataOraInizio = dataOraInizio;
		DataOraFine = dataOraFine;
		CodiceEtso = codiceEtso;
		IdMotivazione = idMotivazione;
		PsMin = psMin;
		PsMax = psMax;
		IdAssetto = idAssetto;
		PtMin = ptMin;
		PtMax = ptMax;
		TRrisp = tRrisp;
		Gpa = gpa;
		Gpd = gpd;
		Tava = tava;
		Tara = tara;
		Brs = brs;
	}

	public ExcelRecord() {
		super();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDataOraInizio() {
		return DataOraInizio;
	}

	public void setDataOraInizio(String dataOraInizio) {
		DataOraInizio = dataOraInizio;
	}

	public String getDataOraFine() {
		return DataOraFine;
	}

	public void setDataOraFine(String dataOraFine) {
		DataOraFine = dataOraFine;
	}

	public String getCodiceEtso() {
		return CodiceEtso;
	}

	public void setCodiceEtso(String codiceEtso) {
		CodiceEtso = codiceEtso;
	}

	public String getIdMotivazione() {
		return IdMotivazione;
	}

	public void setIdMotivazione(String idMotivazione) {
		IdMotivazione = idMotivazione;
	}

	public double getPsMin() {
		return PsMin;
	}

	public void setPsMin(double psMin) {
		PsMin = psMin;
	}

	public double getPsMax() {
		return PsMax;
	}

	public void setPsMax(double psMax) {
		PsMax = psMax;
	}

	public String getIdAssetto() {
		return IdAssetto;
	}

	public void setIdAssetto(String idAssetto) {
		IdAssetto = idAssetto;
	}

	public double getPtMin() {
		return PtMin;
	}

	public void setPtMin(double ptMin) {
		PtMin = ptMin;
	}

	public double getPtMax() {
		return PtMax;
	}

	public void setPtMax(double ptMax) {
		PtMax = ptMax;
	}

	public int getTRrisp() {
		return TRrisp;
	}

	public void setTRrisp(int tRrisp) {
		TRrisp = tRrisp;
	}

	public double getGpa() {
		return Gpa;
	}

	public void setGpa(double gpa) {
		Gpa = gpa;
	}

	public double getGpd() {
		return Gpd;
	}

	public void setGpd(double gpd) {
		Gpd = gpd;
	}

	public int getTava() {
		return Tava;
	}

	public void setTava(int tava) {
		Tava = tava;
	}

	public int getTara() {
		return Tara;
	}

	public void setTara(int tara) {
		Tara = tara;
	}

	public int getBrs() {
		return Brs;
	}

	public void setBrs(int brs) {
		Brs = brs;
	}

	@Override
	public String toString() {
		return "ExcelRecord [action=" + action + ", DataOraInizio=" + DataOraInizio + ", DataOraFine=" + DataOraFine
				+ ", CodiceEtso=" + CodiceEtso + ", IdMotivazione=" + IdMotivazione + ", PsMin=" + PsMin + ", PsMax="
				+ PsMax + ", IdAssetto=" + IdAssetto + ", PtMin=" + PtMin + ", PtMax=" + PtMax + ", TRrisp=" + TRrisp
				+ ", Gpa=" + Gpa + ", Gpd=" + Gpd + ", Tava=" + Tava + ", Tara=" + Tara + ", Brs=" + Brs + "]";
	}
	
}
