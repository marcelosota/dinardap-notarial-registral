package ec.gob.dinardap.notarialregistral.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FechaHoraSistema {
	Date fecha = new Date();
	
	private static List<SimpleDateFormat> formatoFecha = new ArrayList<SimpleDateFormat>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7889029038643755249L;

	{
		add(new SimpleDateFormat("dd-MM-yyyy"));
		add(new SimpleDateFormat("dd/MM/yyyy"));
		add(new SimpleDateFormat("dd-MMM-yyyy"));
		add(new SimpleDateFormat("dd.MMM.yyyy"));
		add(new SimpleDateFormat("dd.M.yyyy"));
		add(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a"));
		
		add(new SimpleDateFormat("M-dd-yyyy"));
		add(new SimpleDateFormat("M/dd/yyyy"));
		add(new SimpleDateFormat("M.dd.yyyy"));
		add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("M-dd-yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("MM-dd-yyyy"));
		add(new SimpleDateFormat("MM/dd/yyyy"));
		add(new SimpleDateFormat("MM.dd.yyyy"));
		add(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"));
		add(new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a"));
		
		add(new SimpleDateFormat("yyyy-MM-dd"));
		add(new SimpleDateFormat("yyyy/MM/dd"));
		add(new SimpleDateFormat("yyyy-MMM-dd"));
		add(new SimpleDateFormat("yyyy/MMM/dd"));
		add(new SimpleDateFormat("yyyy-dd-MM"));
		add(new SimpleDateFormat("yyyy/dd/MM"));
		add(new SimpleDateFormat("yyyy-dd-MMM"));
		add(new SimpleDateFormat("yyyy/dd/MMM"));
		add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"));
		add(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a"));
		add(new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a"));
		add(new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss a"));
		
	}};

	public static String obtenerFechaHoraMiliSegundo(){
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
		return formato.format(LocalDateTime.now());
		
	}

	public String obtenerHora(){
		DateFormat formato = new SimpleDateFormat("HHmmss");
		return formato.format(fecha);
	}
	
	public String obtenerFecha(){
		DateFormat formato = new SimpleDateFormat("yyyyMMdd");
		return formato.format(fecha);
	}
	
	public String obtenerFechaHora(){
		DateFormat formato = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return formato.format(fecha);
	}
	
	/*public String obtenerFechaHoraMiliSegundo(){
		DateFormat formato = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		return formato.format(new Timestamp(fecha.getTime()));
	}
	*/
	
	public Date convertirFecha(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = null;
		try {
			fecha = formato.parse(cadena);
			formato.format(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}
	
	public Date convertirFechaEspanol(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date fecha = null;
		try {
			fecha = formato.parse(cadena);
			formato.format(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}
	
	public Date convertirFechaEspanolMesPalabras(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
		Date fecha = null;
		try {
			fecha = formato.parse(cadena);
			formato.format(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}
	
	public Date convertirFechaHora(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fecha = null;
		try {
			fecha = formato.parse(cadena);
			formato.format(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}
	
	public Timestamp convertirTimestamp(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fecha = null;
		try {
			fecha = formato.parse(cadena);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Timestamp timestamp = new Timestamp(fecha.getTime());
	    return timestamp;
	}
	
	public Calendar convertirCalendario(String cadena){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendario = Calendar.getInstance();
		try {
			fecha = formato.parse(cadena);
			calendario.setTimeInMillis(fecha.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendario;
	}
	
	public static Date convertToDate(String cadena) {
		Date date = null;
		if(null == cadena) {
			return null;
		}
		for (SimpleDateFormat format : formatoFecha) {
			try {
				format.setLenient(false);
				date = format.parse(cadena);
			} catch (ParseException e) {
				//Shhh.. try other formats
			}
			if (date != null) {
				break;
			}
		}

		return date;
	}
}