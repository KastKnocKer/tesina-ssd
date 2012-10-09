package SIP;


/*
 * Classe dedicata alla gestione del Database.
 * Gestisce l'apertura e la chiusura della connessione col Database
 * Fornisce i metodi per l'esecuzione delle query sul Database
 */
import java.awt.Toolkit;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import managers.Status;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.ResponseLoginMessage;
import chat.ChatStatusList;
import chat.Contact;

import com.mysql.jdbc.PreparedStatement;

public class DBConnection implements DBConnection_interface{
	
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
		prepSt.setString(3, contact.getEmail());
		prepSt.setString(4, contact.getNickname());
		prepSt.setString(5, contact.getPassword());
		prepSt.execute();
		System.out.println("SIP - Nuovo contatto "+contact.getEmail()+" inserito.");
		return true;
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("SIP - Nuovo contatto "+contact.getEmail()+" NON inserito.");
	}
	
	return completed;
}

public boolean modifyContact(Contact contact) {
	if(!connesso){
		connetti();
	}
	boolean completed = false;
	try {
		System.out.println("MODIFYCONTACT");
		//String query = "INSERT INTO userstatus (idUser, publicIP, localIP, rmiregistryPort, clientPort, lastConnection, status) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?) ON DUPLICATE KEY UPDATE idUser=?, publicIP=?, localIP=?, rmiregistryPort=?, clientPort=?, lastConnection=CURRENT_TIMESTAMP, status=?;";
		
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("UPDATE user SET nickname = ?, avatarurl = ? WHERE idUser = ?;");
		prepSt.setString(1, contact.getNickname());
		prepSt.setString(2, contact.getAvatarURL());
		prepSt.setString(3, Integer.toString(contact.getID()));
		prepSt.execute();
		/*
		prepSt.setString(1, contact.getNome());
		prepSt.setString(2, contact.getCognome());
		prepSt.setString(3, contact.getEmail());
		prepSt.setString(5, contact.getPassword());
		 */
		
		return true;
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return completed;
}



@Override
public ResponseLoginMessage login(RequestLoginMessage rlm) {
	try {
		System.out.println("RemoteServer.getClientHost(): "+RemoteServer.getClientHost());
	} catch (ServerNotActiveException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if(!connesso){
		connetti();
	}
	ResultSet result;
	
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT *,COUNT(*) AS COUNT FROM user WHERE email = ? AND password = ?");
		prepSt.setString(1, rlm.getUsername());
		prepSt.setString(2, rlm.getPassword());
		result = prepSt.executeQuery();
		result.next();
		
		
        if(result.getInt("COUNT") == 0){
        	System.out.println("SIP - Login["+rlm.getUsername()+"] rifiutato. [PublicIP: "+rlm.getRequestorGlobalIP()+" LocalIP: "+rlm.getRequestorLocalIP()+"]");
        	return new ResponseLoginMessage(false, "Login rifiutato", null);
        }else{
        	System.out.println("SIP - Login["+rlm.getUsername()+"] effettuato. [PublicIP: "+rlm.getRequestorGlobalIP()+" LocalIP: "+rlm.getRequestorLocalIP()+"]");
        	Contact contact = new Contact();
        	contact.setID(		Integer.parseInt(result.getString("idUser")));
        	contact.setNome(	result.getString("nome"));
        	contact.setCognome(	result.getString("cognome"));
        	contact.setEmail(	result.getString("email"));
        	contact.setNickname(result.getString("nickname"));
        	contact.setAvatarURL(result.getString("avatarurl"));
        	//TODO aggiungere private e publick key
        	
        	
        	
        	//AGGIORNO I DATI DELLO STATO DI CONNESSIONE DELL'UTENTE
        	System.out.println("ESITO AGGIORNAMENTO: "+this.updateContactConnectionStatus(Integer.parseInt(result.getString(1)), rlm.getRequestorGlobalIP(), rlm.getRequestorLocalIP(), rlm.getRMIRegistryPort(), rlm.getRequestorClientPort(), rlm.getStato()));
        	
        	
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
	// TODO Sviluppi futuri: Introdurre l'autenticazione del richiedente
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	
	if(!connesso){
		connetti();
	}
	
	if(Status.SUPER_DEBUG) 
		System.out.println("SIP - getMyContacts("+msg.getRequestorUserID()+" - "+msg.getRequestorGlobalIP()+" - "+msg.getRequestorLocalIP()+")");
	
	ResultSet result;
	
	try {
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement(
				"SELECT * " +
				"FROM user left outer join  userstatus AS us ON user.idUser = us.idUser " +
				"WHERE user.idUser IN (" +
					"SELECT idUserB " +
					"FROM friendship " +
					"WHERE idUserA = ? AND linkType = 'ATTIVA' " +
				"UNION " +
					"SELECT idUserA " +
					"FROM friendship " +
					"WHERE idUserB = ? AND linkType = 'ATTIVA'" +
				");");
		
		prepSt.setString(1, Integer.toString(msg.getRequestorUserID()));
		prepSt.setString(2, Integer.toString(msg.getRequestorUserID()));

		result = prepSt.executeQuery();

        ResultSetMetaData rsmd = result.getMetaData();
        Contact contact;
        while(result.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
        	contact = new Contact();
        	contact.setID(		Integer.parseInt(result.getString("idUser")));
        	contact.setNome(	result.getString("nome"));
        	contact.setCognome(	result.getString("cognome"));
        	contact.setEmail(	result.getString("email"));
        	contact.setNickname(result.getString("nickname"));
        	contact.setAvatarURL(result.getString("avatarurl"));
        	
        	if(result.getString("status") == null){	//il contatto non ha la tupla della connessione
        		contact.setStatus(ChatStatusList.OFFLINE);
        	}else{
        		// TODO
        		contact.setGlobalIP(result.getString("publicIP"));
        		contact.setLocalIP(result.getString("localIP"));

        		/* Scelto di mettere comunque il contatto Offline, di default: 
        		 * sarà l'utente che ogni volta controllerà se è online. */
        		contact.setStatus(ChatStatusList.OFFLINE);
//        		contact.setStatus(result.getString("status"));
  
        	}
        
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

/**
 * Restituisce un contatto dato il suo indirizzo email. 
 * Questa funzione viene sfruttata durante la fase della 
 * richiesta di amicizia. 
 * 
 * @param email del contatto di cui reperire informazioni da DB 
 * @return
 */
public Contact getContactByEmail(String email) {
	
	Contact contact = new Contact(); 
	
	if(Status.SUPER_DEBUG) 
		System.out.println("SIP - getContactByEmail (" + email + ")");
	
	if(!connesso){
		connetti();
	}
	
	ResultSet result;
	
	try {
		//PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT * FROM user WHERE email != ?");
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("" +
				"SELECT * " +
				"FROM user left outer join userstatus AS us ON user.idUser = us.idUser " +
				"WHERE user.email = ? ;");
		
		prepSt.setString(1, email);
		
		if(Status.SUPER_DEBUG) 
			System.err.println("Query: "+prepSt.getPreparedSql() + " " + email);
		
		result = prepSt.executeQuery();
//		
//		//v = new Vector();
        ResultSetMetaData rsmd = result.getMetaData();
//        Contact contact;
        
        /* Creo il vettore risultato scorrendo tutto il ResultSet */
        while(result.next()) {   
        	contact = new Contact();
        	
             contact.setID(   Integer.parseInt(result.getString("idUser")));
             contact.setNome(  result.getString("nome"));
             contact.setCognome(  result.getString("cognome"));
             contact.setEmail(  result.getString("email"));
             contact.setNickname( result.getString("nickname"));
             contact.setGlobalIP( result.getString("publicIP")); 
             contact.setLocalIP(  result.getString("localIP")); 
        	
        	contact.printInfo(); 
        }
        	
        result.close(); 
    	prepSt.close(); 
	
	} catch (SQLException e) {
		
		if(Status.SUPER_DEBUG) 
			System.err.println("SIP - getContactByEmail (" + email + "): SQLException occurred");
		
		e.printStackTrace();
		return null;
	}
	
	return contact;

}


/**
 * Funzione che aggiorna lo stato della connessione dell'utente per permettere agli altri contatti
 * di poterlo contattare
 */
public boolean updateContactConnectionStatus(int UserID, String PublicIP, String LocalIP, int rmiregistryPort, int clientport, ChatStatusList status) {
	if(!connesso){
		connetti();
	}
	
	if(Status.SUPER_DEBUG) System.out.println("SIP - updateContactConnectionStatus("+UserID+")");
	
	try {
		String query = "INSERT INTO userstatus (idUser, publicIP, localIP, rmiregistryPort, clientPort, lastConnection, status) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?) ON DUPLICATE KEY UPDATE idUser=?, publicIP=?, localIP=?, rmiregistryPort=?, clientPort=?, lastConnection=CURRENT_TIMESTAMP, status=?;";
		PreparedStatement prepSt = (PreparedStatement) db.prepareStatement(query);
		
		prepSt.setString(1, Integer.toString(UserID));
		prepSt.setString(2, PublicIP);
		prepSt.setString(3, LocalIP);
		prepSt.setString(4, Integer.toString(rmiregistryPort));
		prepSt.setString(5, Integer.toString(clientport));
		prepSt.setString(6, status.toString());
		prepSt.setString(7, Integer.toString(UserID));
		prepSt.setString(8, PublicIP);
		prepSt.setString(9, LocalIP);
		prepSt.setString(10, Integer.toString(rmiregistryPort));
		prepSt.setString(11, Integer.toString(clientport));
		prepSt.setString(12, status.toString());
		
		System.out.println("Query: "+prepSt.getNonRewrittenSql());
		
		prepSt.execute();
		
        prepSt.close();   // Chiudo lo Statement
		
	} catch (SQLException e) {
		Toolkit.getDefaultToolkit().beep();
		e.printStackTrace();
		return false;
	}
	
	return true;
}


///**
// * TODO: rimuovi metodo vecchio di Kast!!
// */
//@Override
//public RMISIPBasicResponseMessage requestFriendship(RequestFriendshipMessage msg) {
//	String fromEmail = msg.getRequestorEmail(); 
//	String toEmail	 = msg.getToEmail();
//	String[][] contacts = new String[2][2];
//	String message = "";
//	ResultSet results;
//	System.out.println("SIP - aggiunta amicizia: " + fromEmail + " -> " + toEmail);
//	
//	if(!connesso){
//		connetti();
//	}
//	
//	/* controllo che i due contatti siano reali */
//	boolean result1 = checkContactExistence(fromEmail); 
//	boolean result2 = checkContactExistence(toEmail);
//
//	 if (result1 == false || result2 == false) 
//		 return new RMISIPBasicResponseMessage(false, "Uno dei due contatti non esiste: \n1." + fromEmail + "\n2." + toEmail);  
//	
//	 /* Inserisco la richiesta di amicizia */
//	PreparedStatement prepSt = null;
//	boolean flagAB = Integer.parseInt(contacts[0][0]) < Integer.parseInt(contacts[1][0]);		//Indica se contacts[0][0] (A) < contacts[1][0] (B)
//	boolean flagFromEmail = contacts[0][1].equals(fromEmail);									//Indica se contacts[0][1] (A) è il contatto richiedente
//	try {
//		prepSt = (PreparedStatement) db.prepareStatement("INSERT INTO friendship (`idUserA`, `idUserB`, `linkType`) VALUES (?, ?, ?);");
//		if(flagAB){
//			prepSt.setString(1, contacts[0][0]);
//			prepSt.setString(2, contacts[1][0]);
//			if(flagFromEmail)
//				prepSt.setString(3, FriendshipList.RICHIESTA_AB.toString());
//			else
//				prepSt.setString(3, FriendshipList.RICHIESTA_BA.toString());
//		}else{
//			prepSt.setString(1, contacts[1][0]);
//			prepSt.setString(2, contacts[0][0]);
//			if(flagFromEmail)
//				prepSt.setString(3, FriendshipList.RICHIESTA_BA.toString());
//			else
//				prepSt.setString(3, FriendshipList.RICHIESTA_AB.toString());
//		}
//		
//		prepSt.executeUpdate();
//		return new RMISIPBasicResponseMessage(true, "Richiesta di amicizia effettuata correttamente!");
//		
//	} catch (SQLException e1) {
//		e1.printStackTrace();
//		return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.\nLa richiesta potrebbe già esser stata effettuata in passato.");
//	}
//	
//	
//	
//}

	@Override
	public boolean addFriendship(FriendshipRequest request) {
		
		if(!connesso)
			connetti(); 
		
		/* Inizio transazione */
		try {
			System.err.println("*** INIZIO TRANSAZIONE DB ***");
			db.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if(Status.DEBUG) 
			System.err.println("DBConnection.addFriendship: starting...");
		
		/* Controllo l'esistenza dei due contatti */
		String emailMittente = request.getContattoMittente().getEmail();
		String emailDestinatario = request.getContattoDestinatario().getEmail();
		
		if(checkContactExistence(emailMittente) == false ||
				checkContactExistence(emailDestinatario) == false) {
			System.err.println("DBConnection.addFriendship: uno dei due contatti non esiste.");
			return false; 
		}
		
		FriendshipRequestType requestType = request.getRequestType(); 

		int idMittente = request.getContattoMittente().getID(); 
		int idDestinatario = request.getContattoDestinatario().getID(); 
		
		PreparedStatement prepSt = null;


		/* Recupero da DB lo stato attuale della richiesta di amicizia 
		 * (potrebbe anche essere null, se non hanno alcun tipo di relazione. */
		FriendshipType actualFriendshipType = getFriendshipTypeBetweenContacts_FromDB(request); 
		
		/* Considero il tipo di richiesta da aggiungere sul DB */
		FriendshipType requestedFriendshipType = null;
		
		/* Se su DB non hanno alcuna relazione, allora applico "brutalmente" la 
		 * richiesta come è stata richiesta, tenendo però conto dell'ordine di id 
		 * (idUserA < idUserB deve valere sempre) */
		if(actualFriendshipType == null) {
			
			if(request.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
				
				if(idMittente < idDestinatario) 
					requestedFriendshipType = FriendshipType.RICHIESTA_AB; 
				else if(idDestinatario < idMittente) 
					requestedFriendshipType = FriendshipType.RICHIESTA_BA; 
				
			} else if(request.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
				
					requestedFriendshipType = FriendshipType.ATTIVA; 
			}
			
		/* Se invece fra i due contatti esiste una relazione */
		} else if(actualFriendshipType != null) {
			
			if(request.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
				
				if(idMittente < idDestinatario) 
					requestedFriendshipType = FriendshipType.RICHIESTA_AB; 
				else if(idDestinatario < idMittente) 
					requestedFriendshipType = FriendshipType.RICHIESTA_BA; 
				
				/* Controllo se si verifica una richiesta doppia in entrambi i 
				 * sensi (AB su DB e BA richiesta, o viceversa).
				 * Nel caso, attivo l'amicizia.*/
				if(
					(actualFriendshipType == FriendshipType.RICHIESTA_AB 
					&& requestedFriendshipType == FriendshipType.RICHIESTA_BA) 
					
					|| 
					
					(actualFriendshipType == FriendshipType.RICHIESTA_BA 
					&& requestedFriendshipType == FriendshipType.RICHIESTA_AB) 
					
					|| (actualFriendshipType == FriendshipType.ATTIVA)) 
				{
					requestedFriendshipType = FriendshipType.ATTIVA; 
				}
				
			} else if(request.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
				
					requestedFriendshipType = FriendshipType.ATTIVA; 
			}
			
		}
		
		
		String actualFriendshipType_string = ""; 
		if(actualFriendshipType != null) {
			actualFriendshipType_string = actualFriendshipType.toString(); 
		}
		System.err.println("actualFriendshipType: " + 
				actualFriendshipType_string + "; " +
				"requestedFriendshipType: " + requestedFriendshipType.toString());
		
		/* se per caso arrivati a questo punto è ancora null, 
		 * significa che si è verificato qualche problema nella verifica delle condizioni
		 */
		if(requestedFriendshipType == null) {
			
			return false; 
		}
		
		
		
		/* Inserisco l'amicizia */
		try {
			
			/* Se su DB non era presente nessuna richiesta di amicizia... */
			if(actualFriendshipType == null) {
				System.err.println("Preparo query INSERT...");
				prepSt = (PreparedStatement) db.prepareStatement("" +
						"INSERT INTO friendship (`idUserA`, `idUserB`, `linkType`) " +
						"VALUES (?, ?, ?);");
				
				/* Devo tenere conto che su DB l'inserimento va sempre fatto con idUserA < idUserB */
				if(idMittente < idDestinatario) {
					prepSt.setInt(1, idMittente);
					prepSt.setInt(2, idDestinatario);
					prepSt.setString(3, requestedFriendshipType.toString()); 
					
				/* se idMittente > idDestinatario */
				} else {
					prepSt.setInt(1, idDestinatario);
					prepSt.setInt(2, idMittente);
					prepSt.setString(3, requestedFriendshipType.toString()); 	
				}
			}
			else if (actualFriendshipType == FriendshipType.ATTIVA) {
				System.err.println("*** Attenzione *** I due contatti sono già amici, perciò non faccio nulla.");
				return true; 
			}
			/* se invece su DB era presente già uno stato dell'amicizia, allora
			 * devo solamente eseguirne l'update */
			else if (actualFriendshipType != null) {
				System.err.println("Preparo query UPDATE...");
				prepSt = (PreparedStatement) db.prepareStatement("" +
						"UPDATE friendship " +
						"SET linkType = ? " +
						"WHERE idUserA = ? " +
						"AND idUserB = ? ; "); 
				
				/* Devo tenere conto che su DB l'inserimento va sempre fatto con idUserA < idUserB */
				if(idMittente < idDestinatario) {
					prepSt.setString(1, requestedFriendshipType.toString()); 
					prepSt.setInt(2, idMittente);
					prepSt.setInt(3, idDestinatario);
					
				/* se idMittente > idDestinatario */
				} else {
					prepSt.setString(1, requestedFriendshipType.toString()); 	
					prepSt.setInt(2, idDestinatario);
					prepSt.setInt(3, idMittente);
				}
			}
			
			
		/* Eseguo l'update, che sia INSERT od UPDATE */
		prepSt.executeUpdate();
		
		/* Finisco la transazione */
		System.err.println("*** TERMINO TRANSAZIONE DB ***");
		db.commit(); 
		
		prepSt.close(); 
		
		System.err.println("Richiesta di amicizia da " + request.getContattoMittente().getEmail() + " verso " + request.getContattoDestinatario().getEmail() + " effettuata correttamente!");
//		return new RMISIPBasicResponseMessage(true, "Richiesta di amicizia effettuata correttamente!");
		
		if(Status.DEBUG) 
			System.out.println("DBConnection.addFriendship: ended.");
		
		
		return true; 
		
	} catch (SQLException e1) {
		
//		/******************************************************************************
//		 * INIZIO PROVA
//		 * ***************************************************************************/
//		try {
//			if (actualFriendshipType != null) {
//				System.err.println("Preparo query UPDATE...");
//				prepSt = (PreparedStatement) db.prepareStatement("" +
//						"UPDATE friendship " +
//						"SET linkType = ? " +
//						"WHERE idUserA = ? " +
//						"AND idUserB = ? ; "); 
//				
//				/* Devo tenere conto che su DB l'inserimento va sempre fatto con idUserA < idUserB */
//				if(idMittente < idDestinatario) {
//					prepSt.setString(1, requestedFriendshipType.toString()); 
//					prepSt.setInt(2, idMittente);
//					prepSt.setInt(3, idDestinatario);
//					
//				/* se idMittente > idDestinatario */
//				} else {
//					prepSt.setString(1, requestedFriendshipType.toString()); 	
//					prepSt.setInt(2, idDestinatario);
//					prepSt.setInt(3, idMittente);
//				}
//			}	
//		} catch (SQLException e) {
//			System.err.println("Errore di nuovo eseguendo l'update nel try-catch!");
//			e.printStackTrace(); 
//		}
//		
//		/******************************************************************************
//		 * FINE PROVA
//		 * ***************************************************************************/
		
		System.err.println("Richiesta di amicizia fallita.\nLa richiesta potrebbe già esser stata effettuata in passato.");
		e1.printStackTrace();
		
		if(Status.DEBUG) 
			System.err.println("DBConnection.addFriendship: ended.");
		
//		return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.\nLa richiesta potrebbe già esser stata effettuata in passato.");
		return false; 
	}
		
}
   

	@Override
	public boolean removeFriendship(FriendshipRequest request) {
		
		if(Status.DEBUG) 
			System.err.println("DBConnection.removeFriendship: starting...");
		
		if(!connesso)
			connetti(); 
		
		/* Controllo l'esistenza dei due contatti */
		String emailMittente = request.getContattoMittente().getEmail();
		String emailDestinatario = request.getContattoDestinatario().getEmail();
		
		if(checkContactExistence(emailMittente) == false ||
				checkContactExistence(emailDestinatario) == false) {
			return false; 
		}
		
		/* rimuovo l'amicizia da DB */
		
		PreparedStatement prepSt = null;
		
		int idMittente = request.getContattoMittente().getID(); 
		int idDestinatario = request.getContattoDestinatario().getID(); 
		
		if( idMittente < 0 || idDestinatario < 0 ) {
			System.err.println("Eliminazione di amicizia: l'id di uno o più dei contatti forniti non è valido.");
			System.err.println("DBConnection.removeFriendship: ended 'failing'."); 
			return false; 
		}
			
		try {
			/* preparo la query */
			prepSt = (PreparedStatement) db.prepareStatement("" +
					"DELETE from friendship " +
					"WHERE `idUserA` = ? AND `idUserB` = ? ;" );
			
			/* Poichè nel database idUserA < idUserB sempre, fra due amici,
			 * imposto il valore a seconda di quale è il minore fra 
			 * idMittente ed idDestinatario */
			if( idMittente < idDestinatario ) {
				prepSt.setInt(1, idMittente);
				prepSt.setInt(2, idDestinatario);
			} else {
				prepSt.setInt(1, idDestinatario);
				prepSt.setInt(2, idMittente);
			}
			
			/* Eseguo la query */
			prepSt.executeUpdate();
			
		} catch (SQLException e1) {
			System.err.println("Errore durante l'esecuzione della query.");
			e1.printStackTrace();
			return false; 
//			return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.\nLa richiesta potrebbe già esser stata effettuata in passato.");
		}
		
		
		if(Status.DEBUG) 
			System.err.println("DBConnection.removeFriendship: ended."); 
		
		return true;
	}
	   
	   

	/**
	 * Controllo esistenza di un contatto prima di effettuare operazioni come aggiunta
	 * di amicizia nel database. 
	 * 
	 * @param email
	 * @return
	 */
	private boolean checkContactExistence(String email) {
		
		if(!connesso)
			connetti(); 
		
		ResultSet results;
		String[][] contacts = new String[2][2];
		
		//Cerco l'esistenza dei due contatti
		try {
			PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("SELECT idUser,email FROM user WHERE email = ?;");
			prepSt.setString(1, email);
			results = prepSt.executeQuery();
			
			ResultSetMetaData rsmd = results.getMetaData();
	        int colonne = rsmd.getColumnCount();
	        int index = 0;
	        while(results.next()) {   
	        	// Creo il vettore risultato scorrendo tutto il ResultSet
//	           record = new String[colonne];
//	           for (int i=0; i<colonne; i++) record[i] = rs.getString(i+1);
//	           v.add( (String[]) record.clone() );
	        	for (int i=0; i<colonne; i++){
	        		System.out.println( results.getString(i+1) );
	        		contacts[index][i] = results.getString(i+1);
	        	}
	        	index++;
	        }
	        results.close();     // Chiudo il ResultSet
	        prepSt.close();   // Chiudo lo Statement
			
		} catch (SQLException e) {
			System.err.println("Si è verificato un errore mentre si stava cercando l'esistenza del contatto " + email);
			e.printStackTrace();
			return false; 
		}
		
		System.out.println("contacts[0][0] "+contacts[0][0]);
		System.out.println("contacts[0][1] "+contacts[0][1]);
		
//		System.out.println("contacts[1][0] "+contacts[1][0]);
//		System.out.println("contacts[1][1] "+contacts[1][1]);
		
		// Controllo che esistano i 2 utenti, se non presenti abortisco l'operazione
		if(contacts[0][0] == null || contacts[0][1] == null /* || contacts[1][0] == null || contacts[1][1] == null */ ){
			System.err.println("Contatto " + email + " non valido.");
			return false; 
//			return new RMISIPBasicResponseMessage(false, "Richiesta di amicizia fallita.\nUtenti non validi.");
		}
		
		/* Il contatto esiste */
		return true; 
	}

	public boolean setContactOffline(int userID) {
		if(!connesso)
			connetti(); 
		
		PreparedStatement prepSt = null;
		try {
			/* preparo la query */
			prepSt = (PreparedStatement) db.prepareStatement("DELETE FROM userstatus WHERE idUser = ?;" );
			prepSt.setInt(1, userID);
			/* Eseguo la query */
			prepSt.executeUpdate();
			return true;
		} catch (SQLException e1) {
			System.err.println("Errore durante l'esecuzione della query di Logout.");
			e1.printStackTrace();
			return false; 
		}
	}
	
	
	/**
	 * Funzione che interroga il database per ottenere il tipo di amicizia
	 * fra due contatti. 
	 * 
	 * @param request richiesta di amicizia per esaminare i due contatti coinvolti. 
	 * @return null, se non vi è alcuna relazione; istanza della classe FriendshipRequest_Types se vi è. 
	 */
	private FriendshipType getFriendshipTypeBetweenContacts_FromDB(FriendshipRequest request) {
		
		if(!connesso)
			connetti(); 
		
		int idMittente = request.getContattoMittente().getID(); 
		int idDestinatario = request.getContattoDestinatario().getID(); 
		
		Contact contattoA; 
		Contact contattoB; 
		/* Devo tenere conto che su DB l'inserimento va sempre fatto con idUserA < idUserB */
		if(idMittente < idDestinatario) {
			contattoA = request.getContattoMittente(); 
			contattoB = request.getContattoDestinatario();
		} else {
			contattoA = request.getContattoDestinatario();
			contattoB = request.getContattoMittente(); 
		}
		
		/* Controllo se esiste già l'amicizia tra i due contatti */
		ResultSet result;
		
		try {
				PreparedStatement prepSt = (PreparedStatement) db.prepareStatement("" +
						"SELECT * FROM friendship " +
						"WHERE idUserA = ? AND idUserB = ? ;");
				prepSt.setInt(1, contattoA.getID());
				prepSt.setInt(2, contattoB.getID());
				
				result = prepSt.executeQuery();
				
				/* Apro il primo risultato tramite result.next()
				 * Se restituisce false, significa che non ho trovato alcuna
				 * relazione fra i due contatti, e quindi col metodo restituisco null
				 */
				if(result.next() == false) {
					System.err.println("Non è stata trovata alcuna relazione fra i contatti " + contattoA.getEmail() + " e " + contattoB.getEmail());
					return null; 
				}
				
				/* Se sono arrivato fin qui, significa che qualche relazione è stata trovata */
				String linkType = result.getString("linkType"); 
				
				prepSt.close(); 
				result.close(); 
				
				if(linkType.equals("ATTIVA")) {
					return FriendshipType.ATTIVA; 
				} else if (linkType.equals("RICHIESTA_AB")) {
					return FriendshipType.RICHIESTA_AB;
				} else if (linkType.equals("RICHIESTA_BA")) {
					return FriendshipType.RICHIESTA_BA; 
				}
				
				/* Se sono arrivato fin qua, qualcosa è andato storto */
				return null; 	
		}catch(SQLException e) {
			System.err.println("SQLException durante SELECT * FROM friendship");
			e.printStackTrace(); 
		}
		
		/* Se per qualche motivo dovessi arrivare qua...*/
		return null; 
	}

	public ArrayList<FriendshipRequest> getPendingFriendships(int userID) {
		if(!connesso)
			connetti();
		
		Contact contact = new Contact();
		contact.setID(userID);
		
		ArrayList<FriendshipRequest> friendShipList = new ArrayList<FriendshipRequest>();
		
		PreparedStatement prepSt;
		try {
			prepSt = (PreparedStatement) db.prepareStatement(
					"SELECT * " +
					"FROM user left outer join  userstatus AS us ON user.idUser = us.idUser " +
					"WHERE user.idUser IN (SELECT idUserA FROM friendship WHERE idUserB = ? AND linkType = 'RICHIESTA_AB' UNION SELECT idUserB FROM friendship WHERE idUserA = ? AND linkType = 'RICHIESTA_BA');");
			
			prepSt.setInt(1, userID);
			prepSt.setInt(2, userID);
			
			ResultSet results;
			results = prepSt.executeQuery();
			
			ResultSetMetaData rsmd = results.getMetaData();
	        int colonne = rsmd.getColumnCount();
	        int index = 0;
	        
	        
	        
	        while(results.next()) {
	        	Contact tmpContact = new Contact();
	        	
	        	try {
	        		tmpContact.setID(results.getInt("idUser"));
					tmpContact.setNome(results.getString("nome"));
					tmpContact.setCognome(results.getString("cognome"));
					tmpContact.setNickname(results.getString("nickname"));
					tmpContact.setEmail(results.getString("email"));
					tmpContact.setGlobalIP(results.getString("publicIP"));
					tmpContact.setLocalIP(results.getString("localIP"));
					FriendshipRequest frreq = new FriendshipRequest(FriendshipRequestType.ADD_FRIEND, tmpContact, contact);
					friendShipList.add(frreq);
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        	
	        	
	        	
	        	index++;
	        }
	        results.close();     // Chiudo il ResultSet
	        prepSt.close();   // Chiudo lo Statement
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(	friendShipList.size() == 0	){
			return null;
		}else{
			return friendShipList;
		}
		
		
	}
	
	
	
}