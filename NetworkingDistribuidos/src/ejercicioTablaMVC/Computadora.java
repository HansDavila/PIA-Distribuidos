package ejercicioTablaMVC;

import java.io.Serializable;

public class Computadora implements Serializable, Comparable<Computadora>
{
	private static final long serialVersionUID = 1L;
	String Cliente;
	String IP;
	String Estado;
	String Procesador;
	String Velocidad;
	String puesto;
	double load = 0.01;
	
	

	int Nucleos;
	String Capacidad;
	String SO;
	String MemoriaTotal;
	

	float UsoMemoria;
	double UsoCpu;
	Double Latencia;
	int puntos;
	int puntosEstaticos;
	
	
	//Constructor
	public Computadora(String cliente, String iP, String estado, String procesador, String velocidad, int nucleos,
			String capacidad, String sO, float UsoMemoria, double UsoCpu, Double Latencia, int puntos, String puesto, double load) 
	{
		this.Cliente = cliente;
		this.IP = iP;
		this.Estado = estado;
		this.Procesador = procesador;
		this.Velocidad = velocidad;
		this.Nucleos = nucleos;
		this.Capacidad = capacidad;
		this.SO = sO;
		this.UsoMemoria = UsoMemoria;
		this.UsoCpu = UsoCpu;
		this.Latencia = Latencia;
		this.puntos = puntos;
		this.puesto = puesto;
		this.load = load;
	}
	
	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	//Constructor Generico
	public Computadora()
	{
		
	}
	
	//Metodo implementado de la Interfaz Comparable
	@Override
	public int compareTo(Computadora e) 
	{
		if(e.getPuntos() < puntos ) //Se indica menor puntuaje es peor 
		{
			return -1;
			
		}else if(e.getPuntos() > puntos)  //Se indica que mayor puntuaje es mejor
		{
			return 0;
			
		}else 
		{
			return 1;
		}
		
	}
	
	//---------------------------Getters y Setters---------------------------
	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	
	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	public String getCliente() {
		return Cliente;
	}

	public int getPuntosEstaticos() {
		return puntosEstaticos;
	}

	public void setPuntosEstaticos(int puntosEstaticos) {
		this.puntosEstaticos = puntosEstaticos;
	}

	public void setCliente(String cliente) {
		Cliente = cliente;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getProcesador() {
		return Procesador;
	}

	public void setProcesador(String procesador) {
		Procesador = procesador;
	}

	public String getVelocidad() {
		return Velocidad;
	}

	public void setVelocidad(String velocidad) {
		Velocidad = velocidad;
	}

	public int getNucleos() {
		return Nucleos;
	}

	public void setNucleos(int nucleos) {
		Nucleos = nucleos;
	}

	public String getCapacidad() {
		return Capacidad;
	}

	public void setCapacidad(String capacidad) {
		Capacidad = capacidad;
	}

	public String getSO() {
		return SO;
	}

	public void setSO(String sO) {
		SO = sO;
	}

	public float getUsoMemoria() {
		return UsoMemoria;
	}

	public void setUsoMemoria(float usoMemoria) {
		UsoMemoria = usoMemoria;
		setPuntos(puntos);
	}

	public double getUsoCpu() {
		return UsoCpu;
	}

	public void setUsoCpu(double d) {
		UsoCpu = d;
	}

	public Double getLatencia() {
		return Latencia;
	}

	public void setLatencia(Double latencia) {
		Latencia = latencia;
	}
	
	public String getMemoriaTotal() {
		return MemoriaTotal;
	}

	public void setMemoriaTotal(String memoriaTotal) {
		MemoriaTotal = memoriaTotal;
	}

	@Override
	public String toString() {
		return "Computadora [Cliente=" + Cliente + ", IP=" + IP + ", Estado=" + Estado + ", Procesador=" + Procesador
				+ ", Velocidad=" + Velocidad + ", Nucleos=" + Nucleos + ", Capacidad=" + Capacidad + ", SO=" + SO
				+ ", UsoMemoria=" + UsoMemoria + ", UsoCpu=" + UsoCpu + ", Latencia=" + Latencia + "]";
	}

	

	
}
