package SIP;


/*
 * Classe dedicata alla gestione del Database.
 * Gestisce l'apertura e la chiusura della connessione col Database
 * Fornisce i metodi per l'esecuzione delle query sul Database
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Vector;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.Friend;

import com.mysql.jdbc.PreparedStatement;

public class DBConnection implements DBEngine{
	
	private String Host			= "localhost";
	private String nomeDB 		= "sip_db";     		// Nome del Database a cui connettersi
	private String nomeUtente	= "prog";   			// Nome utente utilizzato per la connessione al Database
	private String pwdUtente	= "prog";    			// Password usata per la connessione al Database
	private String nomeDriver	= "com.mysql.jdbc.Driver";	//Contiene il nome del driver JDBC
	private String errore		= "";       				// Raccoglie informazioni riguardo l'ultima eccezione sollevata
	
	
	private static DBConnection db_connection = null;
	private Connection db;       // La connessione col Database
	private boolean connesso;    // Flag che indica se la connessione è attiva o meno

	public DBConnection(){}
	
	/**
	 * Apre la connessione con il Database
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 */
	public boolean connetti() {
		connesso = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			db = DriverManager.getConnection("jdbc:mysql://"+Host+"/" + nomeDB + "?user=" + nomeUtente + "&password=" + pwdUtente);
			connesso = true;
		} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
		} catch (InstantiationException ex){} catch (ClassNotFoundException ex){} catch (IllegalAccessException ex){}
		
		return connesso;
	}
   
   public static DBConnection getDBConnection() {
		if(db_connection == null){
			db_connection = new DBConnection();
		}
		return db_connection;
   }

   // Esegue una query di selezione dati sul Database
   // query: una stringa che rappresenta un'istruzione SQL di tipo SELECT da eseguire
   // colonne: il numero di colonne di cui sarà composta la tupla del risultato
   // ritorna un Vector contenente tutte le tuple del risultato
   public Vector eseguiQuery(String query) {
	  connetti();
	  System.out.println("ExecQuery: "+query);
      Vector v = null;
      String [] record;
      int colonne = 0;
      try {
         Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
         ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
         v = new Vector();
         ResultSetMetaData rsmd = rs.getMetaData();
         colonne = rsmd.getColumnCount();

         while(rs.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
            record = new String[colonne];
            for (int i=0; i<colonne; i++) record[i] = rs.getString(i+1);
            v.add( (String[]) record.clone() );
         }
         rs.close();     // Chiudo il ResultSet
         stmt.close();   // Chiudo lo Statement
      } catch (Exception e) { e.printStackTrace(); errore = e.getMessage(); }
      disconnetti();
      return v;
   }

   // Esegue una query di aggiornamento sul Database
   // query: una stringa che rappresenta un'istuzione SQL di tipo UPDATE da eseguire
   // ritorna TRUE se l'esecuzione è adata a buon fine, FALSE se c'è stata un'eccezione
   public boolean eseguiAggiornamento(String query) {
	   connetti();
	  System.out.println("ExecQuery: "+query);
      int numero = 0;
      boolean risultato = false;
      try {
         Statement stmt = db.createStatement();
         numero = stmt.executeUpdate(query);
         risultato = true;
         stmt.close();
      } catch (Exception e) {
         e.printStackTrace();
         errore = e.getMessage();
         risultato = false;
      }
      disconnetti();
      return risultato;
   }
   
   public boolean eseguiAggiornamentoPROVA(String query) throws SQLException {
	      connetti();
	   	  int numero = 0;
	      boolean risultato = false;
	      
	         Statement stmt = db.createStatement();
	         numero = stmt.executeUpdate(query);
	         risultato = true;
	         stmt.close();
	      disconnetti();
	      return risultato;
	   }

   // Chiude la connessione con il Database
   public void disconnetti() {
      try {
         db.close();
         connesso = false;
      } catch (Exception e) { e.printStackTrace(); }
   }
   

   public boolean isConnesso() { return connesso; }   // Ritorna TRUE se la connessione con il Database è attiva
   public String getErrore() { return errore; }       // Ritorna il messaggio d'errore dell'ultima eccezione sollevata


   // Esegue una query di selezione dati sul Database
   // query: una stringa che rappresenta un'istruzione SQL di tipo SELECT da eseguire
   // colonne: il numero di colonne di cui sarà composta la tupla del risultato
   // ritorna una matrice di stringhe contenente tutti i dati del risultato
   public String[][] eseguiQueryStringArray(String query) {
	   	connetti();
	      Vector v = null;
	      String [] record;
	      int colonne = 0;
	      int righe = 0;
	      
	      String[][] queryResult = null;
	      
	      try {
	         Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
	         ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
	         v = new Vector();
	         ResultSetMetaData rsmd = rs.getMetaData();
	         colonne = rsmd.getColumnCount();
	         
	         
	         int k=0;
	         
	         while(rs.next()) k++; 	//Conto quante righe ci sono
	         
	         rs.beforeFirst();		//Torno ad inizio lista
	         
	         queryResult = new String[k][colonne];
	         int riga=0;
	         while(rs.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
		            
		            for (int i=0; i<colonne; i++) {
		            	queryResult[riga][i] = rs.getString(i+1);
		            }
		            riga++;
		         }
	         rs.close();     // Chiudo il ResultSet
	         stmt.close();   // Chiudo lo Statement
	      } catch (Exception e) { e.printStackTrace(); errore = e.getMessage(); }
	      disconnetti();
	      return queryResult;
	   }
   
   public String eseguiQueryOneStringResult(String query){
	   connetti();
	   String tmp = null;
	   try {
	         Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
	         ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
	         ResultSetMetaData rsmd = rs.getMetaData();
	         
	         rs.next();
	         tmp = rs.getString(1);
	         rs.close();     // Chiudo il ResultSet
	         stmt.close();   // Chiudo lo Statement
	      } catch (Exception e) { e.printStackTrace(); errore = e.getMessage(); return null; }
	   	disconnetti();
	    return tmp; 
   }

public boolean insertContact(Contact contact) {
	if(!connesso){
		connetti();
	}
	boolean completed = false;
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("INSERT INTO user (nome,cognome,email,nickname,password) VALUES (?,?,?,?,?)");
		prepSt.setString(1, contact.getNome());
		prepSt.setString(2, contact.getCognome());
		prepSt.setString(3, contact.geteMail());
		prepSt.setString(4, contact.getNickname());
		prepSt.setString(5, contact.getPassword());
		prepSt.execute();
		System.out.println("SIP - Nuovo contatto "+contact.geteMail()+" inserito.");
		return true;
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("SIP - Nuovo contatto "+contact.geteMail()+" NON inserito.");
	}
	
	return completed;
}

public boolean modifyContact(Contact contact) {
	if(!connesso){
		connetti();
	}
	boolean completed = false;
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("INSERT INTO user (nome,cognome,email,nickname,password) VALUES (?,?,?,?,?)");
		prepSt.setString(1, contact.getNome());
		prepSt.setString(2, contact.getCognome());
		prepSt.setString(3, contact.geteMail());
		prepSt.setString(4, contact.getNickname());
		prepSt.setString(5, contact.getPassword());
		prepSt.execute();
		return true;
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return completed;
}

@Override
public RMISIPBasicResponseMessage requestFriendship(RequestFriendshipMessage msg) {
	String fromEmail = msg.getRequestorEmail(); 
	String toEmail	 = msg.getToEmail();
	String[][] contacts = new String[2][2];
	String message = "";
	ResultSet results;
	System.out.println("SIP - aggiunta amicizia: "+fromEmail+" -> "+toEmail);
	
	if(!connesso){
		connetti();
	}
	boolean completed = false;
	
	//Cerco l'esistenza dei due contatti
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT idUser,email FROM user WHERE email = ? OR email = ?");
		prepSt.setString(1, fromEmail);
		prepSt.setString(2, toEmail);
		results = prepSt.executeQuery();
		
		ResultSetMetaData rsmd = results.getMetaData();
        int colonne = rsmd.getColumnCount();
        int index = 0;
        while(results.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
//           record = new String[colonne];
//           for (int i=0; i<colonne; i++) record[i] = rs.getString(i+1);
//           v.add( (String[]) record.clone() );
        	for (int i=0; i<colonne; i++){
        		System.out.println( results.getString(i+1) );
        		contacts[index][i] = results.getString(i+1);
        	}
        	index++;
        }
        results.close();     // Chiudo il ResultSet
        prepSt.close();   // Chiudo lo Statement
		
	} catch (SQLException e) {
		e.printStackTrace();
		return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.");
	}
	
	//Controllo che esistano i 2 utenti
	if( fromEmail.equals(contacts[0][1]) && toEmail.equals(contacts[1][1]) ){
		
	}else if ( fromEmail.equals(contacts[1][1]) && toEmail.equals(contacts[0][1]) ){
		
	}else{
		return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.\nUtenti non validi.");
	}
	
	
	
	
	return new RMISIPBasicResponseMessage(true, "Richiesta di amicizia effettuata correttamente.");
}

@Override
public ResponseLoginMessage login(String username, String password) {
	if(!connesso){
		connetti();
	}
	ResultSet result;
	
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT *,COUNT(*) AS COUNT FROM user WHERE email = ? AND password = ?");
		prepSt.setString(1, username);
		prepSt.setString(2, password);
		result = prepSt.executeQuery();
		result.next();
		
		
        if(result.getInt("COUNT") == 0){
        	System.out.println("SIP - Login["+username+"] rifiutato.");
        	return new ResponseLoginMessage(false, "Login rifiutato", null);
        }else{
        	System.out.println("SIP - Login["+username+"] effettuato.");
        	Contact contact = new Contact();
        	contact.setID(		Integer.parseInt(result.getString(1)));
        	contact.setNome(	result.getString(2));
        	contact.setCognome(	result.getString(3));
        	contact.seteMail(	result.getString(4));
        	contact.setNickname(result.getString(5));  
        	//TODO aggiungere private e publick key
        	return new ResponseLoginMessage(true, "Login permesso", contact);
        }
	} catch (SQLException e) {
		//e.printStackTrace();
		System.err.println("SIP - login() exception");
		return new ResponseLoginMessage(false, "Login exception", null);
	}
}

/**
 * Funzione per la richiesta del client al SIP dei propri contatti
 */
public ArrayList<Contact> getMyContacts(RMIBasicMessage msg) {
	// TODO Introdurre l'autenticazione del richiedente
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	
	if(!connesso){
		connetti();
	}
	ResultSet result;
	
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT * FROM user WHERE email != ?");
		prepSt.setString(1, msg.getRequestorEmail());	//Mi faccio restituire tutti i contatti tranne me
		//prepSt.setString(2, password);
		result = prepSt.executeQuery();
		
		//v = new Vector();
        ResultSetMetaData rsmd = result.getMetaData();
        Contact contact;
        while(result.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
        	contact = new Contact();
        	contact.setID(		Integer.parseInt(result.getString(1)));
        	contact.setNome(	result.getString(2));
        	contact.setCognome(	result.getString(3));
        	contact.seteMail(	result.getString(4));
        	contact.setNickname(result.getString(5));           
        	contacts.add(contact);
        	contact.printInfo();
        }
        result.close();     // Chiudo il ResultSet
        prepSt.close();   // Chiudo lo Statement
		
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
	
	return contacts;
}

   
   
   
   
   

	   
	   


}