package test;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import databaseconnection.DB_connection;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;
import databaseobjects.Paivamaara;
import databaseobjects.Kala;
import databaseobjects.Kalahavainto;
import lib.Operations;

public class Test {

	private static final String DBUSER="user"; 
	private static final String PASSWORD="salasana";
	private static final String HOST="86.50.101.164";
	private static final String DBNAME="tk2";
	
	private static Kayttaja loppukayttaja=null;
	
	public static void main(String[] args) {
		DB_connection connection=new DB_connection(HOST, DBNAME, DBUSER, PASSWORD);
		testikayttaja(connection);
		kirjaudu("Josia Nyman","hyvinSalainen",connection);
                
		testikunta(connection); 
		
	//	etsiLinnut(connection, "ki");
    //  jsonTesti(connection);
	//	lisaaLinnut(lueLinnut(true), connection); //toimii
	//	arrayTest(connection.getConnection()); //toimii
	//	lisaaKunnat(lueKunnat(true), connection); //toimii
	//	lisaaKalat(lueKalat(true),connection);
		lintuhavaintoTesti(connection);
		lintuhavintoHaku(connection);
 //     connection.printInfo();
		kalahavaintotesti(connection);
        System.out.println("Yhteys suljetaan...");
		connection.disconnect();
	}
	
	public static void kirjaudu(String name, String password,DB_connection connection) {
            System.out.println("---Kirjaudutaan sisään tunnuksella "+name+"---");
		loppukayttaja=new Kayttaja(name, password);
		loppukayttaja.setId(connection.logIn(loppukayttaja));
                if(loppukayttaja.getId()>0){
                    System.out.println("Kirjautuminen ok!");
                    System.out.println("Kayttajan "+loppukayttaja.getNimi() +" ID on "+loppukayttaja.getId());
                }else {
                    System.out.println("Kirjautuminen epäonnistui!");
                }
                System.out.println("------------------------");
		
	}
	

	public static void kalahavaintotesti(DB_connection connection) {
		try {
                    System.out.println("---Kalahavainnon lisäys---");
			Kalahavainto kh=new Kalahavainto("Kaarina", 6500, "hauki",loppukayttaja.getId(),new Paivamaara("3.5.2016"), connection);
			
                        System.out.println("Luotiin kalahavainto \n"+kh.toJSON(connection));
			int havaintoId= connection.insertFishCatch(kh);
			kh.setId(havaintoId);
			System.out.println("Havainnon id:ksi tuli "+havaintoId);
                        System.out.println("------------------------");
                        System.out.println("---Havaintotietojen haku---");
			int[] fongo=connection.getFishCatchIndex(loppukayttaja, 2016);
			System.out.println("Fongausindeksi " +fongo[0]/100+
					" lajeja "+fongo[1]+" pituus senttimetreinä "+fongo[2]/100);
			System.out.println("Kayttajan "+loppukayttaja.getNimi()+" vuoden 2016 kalahavainnot: \n"+connection.getFishCatchData(loppukayttaja, 2016));
                        System.out.println("------------------------");
			//int i=connection.deleteFishCatch(havaintoId, user);
			//System.out.println("Poistettu "+i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void lintuhavaintoTesti(DB_connection connection){
            System.out.println("---Lintuhavainnon lisäys---");
		Lintuhavainto lintuhavainto = null;
		try {
			lintuhavainto = new Lintuhavainto("maakotka", "Parainen", new Paivamaara("17.9.2013"),loppukayttaja.getId(),true,true, connection);
			System.out.println("Luotiin lintuhavainto: "+lintuhavainto.toJSON(connection));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                System.out.println("Lisätään havainto tietokantaan...");
		int id=connection.insertBirdWatch(lintuhavainto);
		lintuhavainto.setId(id);
                System.out.println("Havainnon id:ksi tuli: "+id);
                System.out.println("--------------------------");
	}
	
	public static void lintuhavintoHaku(DB_connection connection){
            System.out.println("---Lintuhavaintojen hakua---");
		String json=null;
		try {
                    Paivamaara p1=new Paivamaara("15.4.2016");
                    Paivamaara p2=new Paivamaara("20-5-2016");
                    System.out.println("Haetaan kaikki havainnot aikaväliltä "+p1.toJSON()+ " - "+p2.toJSON());
			json=connection.getBirdWatchJSON(p1, p2, loppukayttaja);
                        System.out.println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                int vuodarit=connection.getVuodarit(loppukayttaja, 2016);
                System.out.println("Vuodareita: "+vuodarit);
                ArrayList<Lintuhavainto> kuukausipinnat=connection.getMonthBirdWatches(loppukayttaja, 2016, 5, false, true);
                System.out.println("Spondekuukausipinnoja toukokuulta: "+kuukausipinnat.size());
                System.out.println("Tarkemmin: \n"+Operations.arrayToJSON(kuukausipinnat, connection));
                ArrayList<Lintuhavainto> paivyrit=connection.getDayBirdWatches(loppukayttaja, 5, 17, false, false);
                System.out.println("Päivänpinnoja 17.5. on "+paivyrit.size());
                System.out.println("---------------------------");
	}
	
	public static ArrayList<Kala> lueKalat(boolean debug){
		ArrayList<Kala> kalat=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("kalat.csv");
			Scanner scan=new Scanner(inp);
			scan.useDelimiter(";");
			scan.nextLine();
			while(scan.hasNextLine()){
				kalat.add(new Kala(scan.next().toLowerCase()));
				scan.nextLine();
			}
			scan.close();
			inp.close();
			if(debug){
				System.out.println("ArrayListan pituus: "+kalat.size());
				printArray(kalat);
			}
			return kalat;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei löydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Lintu> lueLinnut(boolean debug){
		ArrayList<Lintu> linnut=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("linnut.csv");
			Scanner scan=new Scanner(inp);
			while(scan.hasNextLine()){
				linnut.add(new Lintu(scan.nextLine()));
			}
			scan.close();
			inp.close();
			if(debug)System.out.println("ArrayListan pituus: "+linnut.size());
			return linnut;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei l�ydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Kunta> lueKunnat(boolean debug){
		ArrayList<Kunta> kunnat=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("kunnat.txt");
			Scanner scan=new Scanner(inp);
			scan.useDelimiter(";");
			while(scan.hasNextLine()){
				kunnat.add(new Kunta(scan.next()));
				scan.nextLine();
			}
			scan.close();
			inp.close();
			if(debug){
				System.out.println("ArrayListan pituus: "+kunnat.size());
				printArray(kunnat);
			}
			return kunnat;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei löydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void lisaaKalat(ArrayList<Kala> kalalista, DB_connection connection){
		connection.insertFish(kalalista);
	}

	public static void lisaaLinnut(ArrayList<Lintu> lintulista, DB_connection connection){
		connection.insertBird(lintulista);
	}

	public static void lisaaKunnat(ArrayList<Kunta> kunnat, DB_connection connection){
		connection.insertTown(kunnat);
	}
	
	public static void etsiLinnut(DB_connection connection, String alkuosa) {
			ArrayList<Lintu> linnut= connection.searchBird(alkuosa);
			for (Lintu l:linnut){
				System.out.println(l.getNimi());
			}	
	}
	
	public static void testikayttaja(DB_connection connection) {
                System.out.println("----Käyttäjän lisäys ja poisto----");
		Kayttaja turhake=new Kayttaja("FooBar", "1234");
                System.out.println("Yritetään lisätä kayttaja "+turhake.getNimi());
		int id=connection.insertUser(turhake);
		if(id>0){
			turhake.setId(id);
			System.out.println("Lisätty, ID: " +id);
		}else if(id==-2){
                    System.out.println("Käyttäjänimi on jo olemassa!");
                    System.out.println("Kokeillan kirjautua...");
                    id=connection.logIn(turhake);
                    if(id>0){
                        System.out.println("Senkin tomppeli! Sinulla oli jo tunnukset. ID: " +id);
                    }else{
                        System.out.println("Keksi uusi kayttajatunnus!");
                    }
                    
                }
                
                if(connection.removeUser(turhake)){
                    System.out.println("Poistettu ylimääräisenä käyttäjä "+ turhake.getNimi());
                    id=connection.logIn(turhake);
                    System.out.println("Yritetään kirjautua, id: "+id+ " eli poisto onnistui!");
                }
 		System.out.println("------------------------");
	}
	
        /**
         * Luo kuntalistan Raisio, Helsinki
         * ja yritaa lisata ne tietokantaan
         * @param connection 
         */
	public static void testikunta(DB_connection connection) {
            System.out.println("---Kuntien lisäys---");
		ArrayList<Kunta> kunnat=new ArrayList<>();
		kunnat.add(new Kunta("raisio"));
		kunnat.add(new Kunta("Helsinki"));
                System.out.println("Aiotaan liätä kunnat "+kunnat.get(0)+ ", " +kunnat.get(1));
		connection.insertTown(kunnat);
                System.out.println("-----------------------");
	}
	

	public static void printBirdArray(ArrayList<Lintu> lista) {
		System.out.print("[");
		for(Lintu l: lista){
			System.out.print("("+l.getNimi()+","+l.getYleisyys()+"),");
		}
		System.out.println("] Size:"+lista.size());
	}
	
	public static void printArray(ArrayList<?> lista) {
		System.out.print("[");
		for(Object l: lista){
			System.out.print("("+l+"), ");
		}
		System.out.println("] Size:"+lista.size());
	}

    public static void jsonTesti(DB_connection connection) {
        Kala kala=new Kala(1,"hauki");
        System.out.println(kala.toJSON(connection)+",");
        Lintu lintu = new Lintu (1,"varis", 10);
        System.out.println(lintu.toJSON(connection)+",");
        Kunta kunta=new Kunta("kauhajoki");
        System.out.println(kunta.toJSON(connection));
    }
}
