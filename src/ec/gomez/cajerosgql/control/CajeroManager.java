package ec.gomez.cajerosgql.control;

public class CajeroManager {
	private String banco;
	private String nombre;
	
	public CajeroManager(String banco,String nombre)
	{
		this.banco = banco;
		this.nombre = nombre;
	}
	
	public String getBanco()
	{
		return this.banco;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
}
