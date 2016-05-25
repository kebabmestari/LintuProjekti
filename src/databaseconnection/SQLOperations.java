package databaseconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import databaseobjects.Havainto;
import databaseobjects.Insertable;
import databaseobjects.Kala;
import databaseobjects.Kalahavainto;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Kuva;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;
import databaseobjects.Paivamaara;

import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLOperations {

	/**
	 * Onko akkosellinen merkkijono
	 * @param s, tutkittava merkkijono
	 * @return sisältääkö vain aakkosia
	 */
	public static boolean isAlphabetic(String s){
		if(Pattern.matches("[a-öA-Ö]+", s)){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Onko annettu merkkijono akkosnumeerinen
	 * @param s
	 * @return onko aakkosnumeerinen
	 */
	public static boolean isAlphaNumeric(String s){
		if(Pattern.matches("[a-öA-Ö0-9]+", s)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Etsii listan linnuista, jotka alkavat ennetulla merkkijonolla
	 * @param wordBegin
	 * @param pstm
	 * @return lista linnuista
	 */
	public static ArrayList<Lintu> searchBird(String wordBegin, PreparedStatement pstm){
		ArrayList<Lintu> birdList=new ArrayList<>();
		try {
			pstm.setString(1, wordBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				while(rs.next()){
					String nimi=rs.getString("nimi");
					int id = rs.getInt("id");
					int yleisyys = rs.getInt("yleisyys");
					birdList.add(new Lintu(id, nimi, yleisyys));
				}
				return birdList;
			} catch (SQLException e) {
				pstm.close();
				System.err.println("Kysely lajeista ei toiminut");
				e.printStackTrace();
				return null;
			}
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}

	}

	/**
	 * Palauttaa linnun id:n
	 * @param bird
	 * @param con
	 * @return Linnun id
	 */
	public static int searchBirdId(String bird, PreparedStatement pstm){
		try {
			pstm.setString(1, bird);
			ResultSet rs=pstm.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}else{
				return -5;
				//TODO heitetäänkö poikkeus kun lintua ei löydy????? Kyllä!!!!
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Hae kayttajan ID
	 */
	public static int getUserID(String name, Connection con){
			try {
					String sql = "SELECT kayttaja.id FROM kayttaja WHERE kayttaja.nimi LIKE '"
									+ name + "%';";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next()){
							return rs.getInt("id");
					} else{
							System.err.println("Käyttäjää ei löytynyt");
					}
			} catch (SQLException ex) {
					Logger.getLogger(SQLOperations.class.getName()).log(Level.SEVERE, null, ex);
			}
                        
			return 1;
	}

	/**
	 * Poistaa kalan tai linnun tietokannasta
	 * @param name Lajin nimi
	 */
	public static void removeSpecies(String name, Connection con){
			String sql = "DELETE FROM lintu WHERE lintu.nimi LIKE '" + name + "%';";
			String sql2 = "DELETE FROM kala WHERE kala.nimi LIKE '" + name + "%';";
			try {
							Statement stm=con.createStatement();
							int deletes = stm.executeUpdate(sql);
							deletes += stm.executeUpdate(sql2);
							//Multidelete
							if(deletes > 1){
									Scanner cin = new Scanner(System.in);
									System.out.println("Poistetaan " + deletes + " lajia! Y hyväksyy");
									if(cin.next().toLowerCase() == "y"){
											return;
									} else{
											con.rollback();
									}
							} else if(deletes == 0){
									System.err.println("Lajia ei löytynyt tietokannasta");
							}
			} catch (SQLException e) {
							System.err.println("Lajin poistaminen ei onnistunut");
							System.err.println("Lajista ei saa olla havaintoja ym");
							return;
			}
	}
        
//        public static void deleteLintuHavainto(int id, PreparedStatement stm){
//            deleteHavainto("lintuhavainto", id, stm);
//        }
//        public static void deleteKalaHavainto(int id, PreparedStatement stm){
//            deleteHavainto("kalahavainto", id, stm);
//        }
//        
//        private static void deleteHavainto(String taulu, int id, PreparedStatement stm){
//            try{
//                stm.setString(1, taulu);
//                stm.setInt(2, id);
//                int m = stm.executeUpdate();
//                if(m == 0){
//                    System.err.println("Havaintoa ei löydetty");
//                    return true;
//                } else if(m > 1){
//                    System.err.println("Havainto ei yksiselitteinen!");
//                    return false;
//                }
//            } catch(SQLException e){
//                System.err.println("Ei voida poistaa taulusta " + taulu);
//            }
//            return true;
//        }

	/**
	 * Etsii kalan nimen sen alkukirjaimen perusteella
	 * Ennustavaa syöttöä varten
	 * @param fishBegin
	 * @param con
	 * @return ResultSet kaikista kaloista, jotka alkoivat annetulla merkkijonolla
	 */
	public static ArrayList<Kala> searchFish(String fishBegin, PreparedStatement pstm) {
		ArrayList<Kala> fishlist=new ArrayList<>();
		try {
			pstm.setString(1, fishBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				while(rs.next()){
					String fishName=rs.getString("nimi");
					fishlist.add(new Kala(fishName));
				}
				return fishlist;
			} catch (SQLException e) {
				pstm.close();
				System.err.println("Kysely kaloista ei toiminut");
				e.printStackTrace();
				return null;
			}
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}
	}
	/**
	 * Palauttaa kalan id:n
	 * @param fish
	 * @param con
	 * @return Kalan id tai -5 jos ei löydy tai -1 jos error
	 */
	public static int searchFishId(String fish, PreparedStatement pstm){
		try {
			pstm.setString(1, fish);
			ResultSet rs=pstm.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}else{
				return -5;
				//TODO heitetäänkö poikkeus kun kalaa ei löydy?????
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * Etsii kunnan sen alkuosan perusteella
	 * Ennustavaa syöttöä varten
	 * @param townBegin kunnan alkuosa
	 * @param townSearch kuntahaku
	 * @return lista kunnista, jotka alkoivat parametrina annetulla merkkijonolla
	 */
	public static ArrayList<Kunta> searchTown(String townBegin, PreparedStatement townSearch) {
		ArrayList<Kunta> townList=new ArrayList<>();
		try {
			townSearch.setString(1, townBegin+"%");
			try {
				ResultSet rs=townSearch.executeQuery();
				while(rs.next()){
					String town=rs.getString("nimi");
					townList.add(new Kunta(town));
				}
				return townList;
			} catch (SQLException e) {
				townSearch.close();
				System.err.println("Kysely kunnista ei toiminut");
				e.printStackTrace();
				return null;
			}
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}
	}

	/**
	 * Poistaa käyttäjän tietokannasta
	 * @param user
	 * @param con
	 * @param stm
	 */
	public static void removeUser(String user, Connection con, PreparedStatement stm){
			String sql = "DELETE FROM kayttaja WHERE kayttaja.nimi = '" + user + "';";
			try{
					Statement st = con.createStatement();
					int deletes = st.executeUpdate(sql);
					if(deletes > 1){
							throw new SQLException("Ei voi poistaa useampaa käyttäjää samaan aikaan!");
					} else if(deletes == 0){
							System.err.println("Käyttäjää " + user + " ei löytynyt");
					}
			} catch(SQLException e){
					try{
							con.rollback();
					} catch(SQLException ee){
							System.err.println("Rollback failed");
							ee.printStackTrace();
					}
			}
	}

	/**
	 * Lisää lintu- tai kalahavainnon tietokantaan.
	 * @param havainto
	 * @param con
	 * @return havainnon id, jos error -1
	 */
	public static int insertHavainto(Havainto havainto, Connection con){
		int id=havaintoIdIfAlreadyInTable(havainto, con);
		if(id>0){
			System.out.println("Päivitetään");
			updateHavainto(havainto, id, con);
			return id;
		}else if(id==-1){
			return -1;
		}else{
			System.out.println("Lisätään");
			String sql="INSERT INTO "+havainto.toInsertHeader()+" VALUES "+havainto.toInsertableString()+";";
			try {
				Statement stm=con.createStatement();
				stm.executeUpdate(sql);
				return havaintoIdIfAlreadyInTable(havainto, con);
			} catch (SQLException e) {
				System.err.println("Havainnon lisäys ei onnistu");
				e.printStackTrace();
				return -1;
			}
		}
	}

	/**
	 * Päivittää havainnon tiedot parametrina annettun havainnon tietoihin
	 * @param havainto
	 * @param id
	 */
	public static void updateHavainto(Havainto havainto, int id, Connection con) {
		String sql="UPDATE "+havainto.getTable()+
				" SET "+havainto.getAllUpdatableAttributesWithValues()+
				"WHERE id='"+id+"';";
		try {
			Statement stm=con.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Palauttaa havainnon id:n, jos sellainen on tietokannassa.
	 * Mikäli ei, palautetaan 0
	 * Mikäli poikkeus, -1
	 * @param havainto
	 * @param con
	 * @return havainnon id tai 0 jos ei ole tai -1 jos poikeus
	 */
	public static int havaintoIdIfAlreadyInTable(Havainto havainto, Connection con){
		String sql="SELECT id FROM "+havainto.getTable()+
				" WHERE "+havainto.getUniqueAttributesWithValues()+";";
		try {
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery(sql);
			if(rs.next()){
				int id=rs.getInt("id");
				rs.close();stm.close();
				return id;
			}else{
				rs.close();stm.close();
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * Lisää annetun käyttäjän, jos käyttäjänimi vapaa.
	 * Palauttaa käyttäjän id:n.
	 * @param user
	 * @param insertUser
	 * @param getUserId
	 * @param con
	 * @return käyttäjän id tai -2 jos nimi jo käytössä, -1 jos poikkeus
	 */
	public static int insertUser(Kayttaja user, PreparedStatement insertUser, PreparedStatement getUserId, Connection con) {
		if(!isInsrtableAlreadyInTable(user, con)){
			try{
				insertUser.setString(1, user.getNimi());
				insertUser.setString(2, user.getSalasana());
				insertUser.executeUpdate();
				return logIn(user,getUserId);
			}catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
		}
		return -2;
	}

	/**
	 * Etsii ja palauttaa käyttäjän id:n, mikäli käyttäjänimi ja salasana täsmää
	 * Käytetään kirjautumistilanteessa
	 * @param user
	 * @param getUserId
	 * @return käyttäjän id tai 0 jos ei ole tai -1 poikkeus
	 */
	public static int logIn(Kayttaja user, PreparedStatement getUserId){
		try{
			getUserId.setString(1, user.getNimi());
			getUserId.setString(2, user.getSalasana());
			ResultSet rs=getUserId.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}
			return 0;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
        
        /**
         * Hae kalan tai linnun kuvan URI tietokannasta
         * @param kalaid Kalan id, jos haetaan lintua aseta 0
         * @param lintuid Linnun id, jos kalaa niin 0
         * @param stm Preparedstatement
         * @return Kuva objekti
         */
        public static ArrayList<Kuva> getPicture(int kalaid, int lintuid, PreparedStatement stm){
            ArrayList<Kuva> tulos = new ArrayList<>();
            try{
                if(kalaid == 0 && lintuid != 0){
                    stm.setInt(1, lintuid);
                } else if(kalaid != 0 && lintuid == 0){
                    stm.setInt(1, kalaid);
                } else{
                    System.err.println("Virhe!");
                    return tulos;
                }
                ResultSet rs = stm.executeQuery();
                while(rs.next()){
                    Kuva kuv = new Kuva(rs.getString("fileURI"), lintuid, kalaid);
                    tulos.add(kuv);
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
            return tulos;
        }

	/**
	 * Lisää kaikki ne listan alkiot sitä vastaan tauluun,
	 * joita ei vielä ole taulussa
	 * Lisäksi poistaa listasta sivuvaikutuksena ne alkiot,
	 * jotka olivat jo tietokannassa.
	 * @param insertableArray
	 * @param con
	 */
	public static void insertObject(ArrayList<Insertable> insertableArray, Connection con) {
		if(insertableArray.size()>0){
			insertableArray=removeDuplicateInsertables(insertableArray, con);
			if(insertableArray.size()>0){
				Insertable first=insertableArray.get(0);
				String insertables=first.toInsertableString();
				for (int i=1;i<insertableArray.size();i++){
					insertables=insertables+","+insertableArray.get(i).toInsertableString();
				}
				try {
					String sql="INSERT INTO "+insertableArray.get(0).toInsertHeader()+" VALUES "+insertables+";";
					System.out.println(sql);
					Statement stm=con.createStatement();
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("SQL-ongelma yritettäessä lisätä tyyppiä "+insertableArray.get(0).getTableName());
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki esiintymät olivat jo tietokannassa, ei mitään lisättävää");
			}
		}else{
			System.out.println("Tyhjä lista, ei mitään lisättävää");
		}
	}

	private static ArrayList<Insertable> removeDuplicateInsertables(ArrayList<Insertable> insertableArray, Connection con) {
		for(int i=0;i<insertableArray.size();i++){
			Insertable insertable=insertableArray.get(i);
			if(isInsrtableAlreadyInTable(insertable, con)){
				insertableArray.remove(i);
				i--;
			}
		}
		return insertableArray;
	}
	private static boolean isInsrtableAlreadyInTable(Insertable insertable, Connection con) {
		try{
			String sql="SELECT * FROM "+insertable.getTableName()+" WHERE nimi=?;";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, insertable.getNimi());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				rs.close();
				pstmt.close();
				return true;
			}
			rs.close();
			pstmt.close();
			return false;
		} catch (SQLException e1) {
			System.out.println("SQL-ongelma etsiessä tyyppiä "+insertable.getTableName());
			e1.printStackTrace();
			return true;
		}
	}

	/**
	 * Palauttaa vuoden ja käyttäjän havainnot listana
	 * @param user, jonka havaintoja haetaan
	 * @param vuosi
	 * @param catchData, kysely
	 * @return lista havainnoista
	 */
	public static ArrayList<Kalahavainto> getFishCatchData(Kayttaja user, int vuosi, PreparedStatement catchData) {
		ArrayList<Kalahavainto> catchArray=new ArrayList<>();
		try {
			catchData.setInt(1, user.getId());
			catchData.setInt(2, vuosi);
			ResultSet rs=catchData.executeQuery();
			while(rs.next()){
				try {
					catchArray.add(new Kalahavainto(rs.getInt("id"), rs.getString("paikka"),
							rs.getInt("pituus"),rs.getInt("kalaid"), rs.getInt("havaitsija"), new Paivamaara(rs.getString("paivamaara"))));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Käyttäjän informointi?
			e.printStackTrace();
		}
		return catchArray;
	}

	/**
	 *
	 * @param user
	 * @param vuosi
	 * @param fishIndexSearch
	 * @param fishIndexCheck
	 * @param fishMaxLengthId
	 * @param deleteDuplicate
	 * @return fongauksen indeksi int[0], kalojen lukum##r# int[1] ja pituuksien summa int[2]
	 */
	public static int[] getFongoIndex(Kayttaja user, int vuosi, PreparedStatement fishIndexSearch,
			PreparedStatement fishIndexCheck, PreparedStatement fishMaxLengthId, PreparedStatement deleteDuplicate){
		int[] fongoList=new int[3];
		ArrayList<Integer> kalaidArray=checkForDuplicates(fishIndexCheck, user, vuosi);
		if(kalaidArray.get(0)>0){
			for (int kalaid:kalaidArray){
				if(kalaid>0){
					removeSmallerDuplicates(kalaid, user, vuosi, fishMaxLengthId, deleteDuplicate);
				}
			}
		}else if(kalaidArray.get(0)==-1){
			fongoList[0]=fongoList[1]=fongoList[2]=-1;
			return fongoList;
		}
		try {
			fishIndexSearch.setInt(1, user.getId());
			fishIndexSearch.setInt(2, vuosi);
			ResultSet rs=fishIndexSearch.executeQuery();
			if(rs.next()){
				fongoList[1]=rs.getInt("lkm");
				fongoList[2]=rs.getInt("pituus");
				fongoList[0]=fongoList[1]*fongoList[2];
				return fongoList;
			}else{
				fongoList[0]=fongoList[1]=fongoList[2]=0;
				return fongoList;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fongoList[0]=fongoList[1]=fongoList[2]=-1;
			return fongoList;
		}
	}

	/**
	 * Poistaa tietokannasta indeksin laskemista häiritsevät virheelliset monikot.
	 * Vain pisin lajin edustaja per vuosi per käyttäjä säilytetään (jos kaksi, niin uudempi).
	 * @param kalaid poistettavan havainnon kalalajin id
	 * @param user käyttäjä, jonka fongoja poistetaan
	 * @param vuosi
	 * @param fishMaxLengthId, kysely, joka palauttaa pisimpien yksil#iden id:t aikajärjestyksessä
	 * @param deleteDuplicate, päivitys, joka poistaa havainnot anneulta käyttäjältä, lajilta ja vuodelta, mikäli id on eri
	 */
	public static void removeSmallerDuplicates(int kalaid, Kayttaja user, int vuosi, PreparedStatement fishMaxLengthId, PreparedStatement deleteDuplicate) {
		try{
			fishMaxLengthId.setInt(1, user.getId());
			fishMaxLengthId.setInt(2, kalaid);
			fishMaxLengthId.setInt(3, vuosi);
			fishMaxLengthId.setInt(4, user.getId());
			fishMaxLengthId.setInt(5, kalaid);
			fishMaxLengthId.setInt(6, vuosi);
			ResultSet rs=fishMaxLengthId.executeQuery();
			if(rs.next()){
				int id=rs.getInt("k.id");
				System.out.println(id);
				deleteDuplicate.setInt(1, user.getId());
				deleteDuplicate.setInt(2, kalaid);
				deleteDuplicate.setInt(3, vuosi);
				deleteDuplicate.setInt(4, id);
				deleteDuplicate.executeUpdate();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Tarkistaa, onko virheellisiä havaintoja päässyt tietokantaan.
	 * Vain yksi lajin ilmoitus saa olla vuodessa käyttäjää kohti.
	 * Palauttaa kalalajien id:t, joilla esiintyy virheellisiä.
	 * @param fishIndexCheck kysely, joka palauttaa pisimmän yksilön id:n, jos lajista on virh. duplikaatteja
	 * @param user, käyttäjä, jonka havainnot tarkistetaan
	 * @param vuosi, jota tarkistus koskee
	 * @return virheelliset kalaid:t, jos ei ole, [0]=0, jos poikkeus [0]=-1
	 */
	public static ArrayList<Integer> checkForDuplicates(PreparedStatement fishIndexCheck, Kayttaja user, int vuosi){
		ArrayList<Integer> idArray=new ArrayList<>();
		try {
			fishIndexCheck.setInt(1, user.getId());
			fishIndexCheck.setInt(2, vuosi);
			ResultSet rs=fishIndexCheck.executeQuery();
			if(rs.next()){
				do{
					idArray.add(rs.getInt("kalaid"));
				}while(rs.next());
			}else{
				idArray.add(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try{
				idArray.set(0, -1);
			}catch(IndexOutOfBoundsException e2){
				idArray.add(-1);
			}
		}
		return idArray;

	}

	/**
	 * Poistaa parametrina annetun id:n mukaisen kalahavinnon.
	 * @param id kalahavainnon id
	 * @param preparedFishCatchDeleteById
	 * @return poistettujen rivien määrä tai -1 jos poikkeus
	 */
	public static int deleteFishCatch(int id, Kayttaja user, PreparedStatement preparedFishCatchDeleteById) {
		try{
			preparedFishCatchDeleteById.setInt(1, id);
			preparedFishCatchDeleteById.setInt(2, user.getId());
			int i=preparedFishCatchDeleteById.executeUpdate();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * Palauttaa kalan nimen tai null jos fail
	 * @param kalaid
	 * @param getFishName
	 * @return kalan nimi tai null
	 */
	public static String getFishNameById(int kalaid, PreparedStatement getFishName) {
		try {
			getFishName.setInt(1, kalaid);
			ResultSet rs=getFishName.executeQuery();
			if(rs.next()){
				return rs.getString("nimi");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Palauttaa linnun id vastaavan nimen
	 * Palauttaa null jos error tai lintua ei ole
	 * @param lintuid
	 * @param preparedGetBirdName
	 * @return linnun nimi
	 */
	public static String getBirdNameById(int lintuid, PreparedStatement preparedGetBirdName) {
		try{
			preparedGetBirdName.setInt(1, lintuid);
			ResultSet rs=preparedGetBirdName.executeQuery();
			if(rs.next()){
				String lintu=rs.getString("nimi");
				rs.close();
				return lintu;
			}
		} catch(SQLException e){
			//TODO
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Palauttaa käyttäjän lintuhavaintolistan annetulta aikaväliltä
	 * @param alkuPaivamaara
	 * @param loppuPaivamaara
	 * @param user
	 * @param preparedGetBirdWatchData
	 * @return Lintuhavaintolista
	 */
	public static ArrayList<Lintuhavainto> getBirdWatchData(Paivamaara alkuPaivamaara, Paivamaara loppuPaivamaara, Kayttaja user,
			PreparedStatement preparedGetBirdWatchData) {
		ArrayList<Lintuhavainto> birdWatchArray=new ArrayList<>();
		try{
			preparedGetBirdWatchData.setInt(1, user.getId());
			preparedGetBirdWatchData.setString(2, alkuPaivamaara.toString());
			preparedGetBirdWatchData.setString(3, loppuPaivamaara.toString());
			ResultSet rs=preparedGetBirdWatchData.executeQuery();
			while(rs.next()){
				boolean eko,sponde;
				eko=sponde=false;
				Paivamaara pvm;
				if(rs.getInt("eko")==1){
					eko=true;
				}
				if(rs.getInt("sponde")==1){
					sponde=true;
				}
				try {
					pvm=new Paivamaara(rs.getString("paivamaara"));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				birdWatchArray.add(new Lintuhavainto(rs.getInt("lintuid"), rs.getString("paikka"),
						pvm, rs.getInt("id"), rs.getInt("havaitsija"),
						eko, sponde));
			}
			return birdWatchArray;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
    /**
    * Palauttaa vuodenpinnat aikajarjestyksessa
    * @param user
    * @param vuosi
    * @param preparedGetVuodarit
     * @param preparedBirdNameSearch
    * @return vuodenpinnat lintulistassa
    */
    public static ArrayList<Lintu> getVuodarit(Kayttaja user, int vuosi, PreparedStatement preparedGetVuodarit, PreparedStatement preparedBirdNameSearch) {
        ArrayList<Lintu> vuodarilista=new ArrayList<>();
        try {
                preparedGetVuodarit.setInt(1, user.getId());
                preparedGetVuodarit.setInt(2, vuosi);
            try (ResultSet rs = preparedGetVuodarit.executeQuery()) {
                while(rs.next()){
                    vuodarilista.add(new Lintu(getBirdNameById(rs.getInt("lintuid"), preparedBirdNameSearch)));
                }
            }
                return vuodarilista;
            } catch (SQLException ex) {
                Logger.getLogger(SQLOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
    }
}
