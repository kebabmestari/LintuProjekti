package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
import lib.Operations;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DB_connection {
    private final String DB_URI;
    private final String user;
    private final String password;

    private Connection con = null;

    /**
     * Kayttajan
     */
    private PreparedStatement insertUser = null;
    private PreparedStatement getUserId = null;
    private PreparedStatement deleteUser = null;
    /**
     * Linnun
     */
    private PreparedStatement preparedBirdNameSearch = null;
    private PreparedStatement preparedBirdIdSearch = null;
    private PreparedStatement preparedGetBirdName = null;
    private PreparedStatement preparedGetBirdPics = null;

    /**
     * Lintuhavainnon
     */
    private PreparedStatement preparedBirdWatchByID = null;
    private PreparedStatement preparedGetBirdWatchData = null;
    private PreparedStatement preparedBirdWatchQuery = null;
    private PreparedStatement preparedBirdWatchDeleteById = null;
    private PreparedStatement preparedBirdWatchGetUser = null;
    private PreparedStatement preparedGetVuodarit=null;
    private PreparedStatement preparedDayBirdWatchSearch=null;
    private PreparedStatement preparedMonthBirdWatchSearch=null;

    /**
     * Kalan
     */
    private PreparedStatement preparedFishNameSearch = null;
    private PreparedStatement preparedFishIdSearch = null;
    private PreparedStatement preparedGetFishName = null;
    private PreparedStatement preparedGetFishPics = null;

    /**
     * Kalahavainnon
     */
    private PreparedStatement preparedFishCatchByID = null;
    private PreparedStatement preparedFishCatchDataSearch = null;
    private PreparedStatement preparedFishCatchQuery = null;
    private PreparedStatement preparedFishIndexCheck = null;
    private PreparedStatement preparedFishIndexSearch = null;
    private PreparedStatement preparedFishMaxLengthSearch = null;
    private PreparedStatement preparedFishCatchDuplicateDelete = null;
    private PreparedStatement preparedFishCatchDeleteById = null;
    private PreparedStatement preparedFishCatchGetUser = null;

    /**
     * Kunnan
     */
    private PreparedStatement preparedTownSearch = null;

    public DB_connection(String host, String db_name, String user, String password) {
        DB_URI = "jdbc:mysql://" + host.trim() + "/" + db_name.trim();

        this.user = user;
        this.password = password;

        int i=0;
        while (!createConnection()) {
            System.err.println("Yhteytta ei voitu muodostaa");
            i++;
            if(i==3){
            	System.exit(-1);
            }
        }
    }

    /**
     * Returns true if client is connected to server
     *
     * @return
     */
    public boolean isConnected() {
        return (con != null);
    }


    /**
     * Luodaan yhteysolio tietokantaan sekä luetaan ja valmistellaan kyselyt
     *
     * @return True jos onnistui
     */
    private boolean createConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(DB_URI, user, password);
            
            return this.perpareStatements();
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    /**
     * Sulkee tietokantayhteyden
     *
     * @return onnistuiko sulkeminen
     */
    public boolean disconnect() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lukee kyselyt tekstitiedostosta
     * Palauttaa, onnistuiko lukeminen
     * @return onisstuiko
     */
    private boolean perpareStatements(){
    	try{
    		System.out.println("Luetaan SQL-kyselyjä...");

            preparedBirdNameSearch
                    = con.prepareStatement(Operations.readQuery("preparedBirdNameSearch"));

            preparedBirdIdSearch
                    = con.prepareStatement(Operations.readQuery("preparedBirdIdSearch"));

            preparedGetBirdName
                    = con.prepareStatement(Operations.readQuery("preparedGetBirdName"));

            preparedGetBirdWatchData
                    = con.prepareStatement(Operations.readQuery("preparedGetBirdWatchData"));

            preparedFishNameSearch
                    = con.prepareStatement(Operations.readQuery("preparedFishNameSearch"));

            preparedFishIdSearch
                    = con.prepareStatement(Operations.readQuery("preparedFishIdSearch"));

            preparedBirdWatchDeleteById
                    = con.prepareStatement(Operations.readQuery("preparedBirdWatchDeleteById"));

            preparedGetFishName
                    = con.prepareStatement(Operations.readQuery("preparedGetFishName"));

            preparedTownSearch
                    = con.prepareStatement(Operations.readQuery("preparedTownSearch"));

            preparedFishCatchQuery
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchQuery"));

            preparedBirdWatchQuery
                    = con.prepareStatement(Operations.readQuery("preparedBirdWatchQuery"));

            preparedFishCatchByID
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchByID"));

            preparedBirdWatchByID
                    = con.prepareStatement(Operations.readQuery("preparedBirdWatchByID"));

            preparedFishCatchDataSearch
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchDataSearch"));

            preparedFishCatchGetUser
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchGetUser"));

            preparedBirdWatchGetUser
                    = con.prepareStatement(Operations.readQuery("preparedBirdWatchGetUser"));

            preparedFishIndexCheck
                    = con.prepareStatement(Operations.readQuery("preparedFishIndexCheck"));

            preparedFishIndexSearch
                    = con.prepareStatement(Operations.readQuery("preparedFishIndexSearch"));

            preparedFishMaxLengthSearch
                    = con.prepareStatement(Operations.readQuery("preparedFishMaxLengthSearch"));

            preparedFishCatchDuplicateDelete
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchDuplicateDelete"));

            preparedFishCatchDeleteById
                    = con.prepareStatement(Operations.readQuery("preparedFishCatchDeleteById"));

            insertUser
                    = con.prepareStatement(Operations.readQuery("insertUser"));

            deleteUser
                    = con.prepareStatement(Operations.readQuery("deleteUser"));

            getUserId
                    = con.prepareStatement(Operations.readQuery("getUserId"));

            preparedGetBirdPics
                    = con.prepareStatement(Operations.readQuery("preparedGetBirdPics"));

            preparedGetFishPics
                    = con.prepareStatement(Operations.readQuery("preparedGetFishPics"));
            
            preparedGetVuodarit
                    = con.prepareStatement(Operations.readQuery("preparedGetVuodarit"));
            
            preparedDayBirdWatchSearch
                    =con.prepareStatement(Operations.readQuery("preparedDayBirdWatchSearch"));
            
            preparedMonthBirdWatchSearch
                    =con.prepareStatement(Operations.readQuery("preparedMonthBirdWatchSearch"));
            
            return true;
    	}catch (Exception e){
    		e.printStackTrace();
            return false;
    	}
    }

    /**
     * Output information about the database
     */
    public void printInfo() {
        try {
            DatabaseMetaData meta = con.getMetaData();
            System.out.println(meta.getDatabaseProductName());
            ResultSet res = meta.getTables(null, null, null, null);
            System.out.println("Taulut:");
            while (res.next()) {
                System.out.print(res.getString("TABLE_NAME"));
                ResultSet res2 = commitGeneralQuery("select * from " + res.getString("TABLE_NAME"));
                ResultSetMetaData res3 = res2.getMetaData();
                for (int i = 1; i <= res3.getColumnCount(); i++) {
                    System.out.print("\n");
                    System.out.print("\t" + res3.getColumnName(i) + " "
                            + res3.getColumnTypeName(i));
                }
                System.out.print("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print("\n");
    }


    /**
     * Palauttaa kayttajan id:n
     * Vain adminille
     * @param username
     * @return nimee vastaava id
     */
    public int getUserID(String username) {
        return SQLOperations.getUserID(username, con);
    }

    /**
     * Commits an SQL-query from a string
     *
     * @param sql query string
     * @return relult of the query
     */
    public ResultSet commitGeneralQuery(String sql) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            System.err.println("Virhe SQL:ssa");
            return null;
        }
    }

    /**
     * Executes an SQL-update from a string
     *
     * @param sql
     */
    private int executeGeneralUpdate(String str) {
        try {
            Statement stm = con.createStatement();
            stm.execute(str);
            return stm.getUpdateCount();
        } catch (SQLException e) {
            System.err.println("Virheellinen SQL");
        }
        return 0;
    }

    /**
     * Etsii linnuista vaihtoehdot nimen alun perusteella, palauttaa lajit
     * yleisyysjarjestyksessa
     *
     * @param wordBegin, linnun nimen alku
     * @return lista sopivista linnuista, pelkat nimet
     */
    public ArrayList<Lintu> searchBird(String wordBegin) {
        return SQLOperations.searchBird(wordBegin, preparedBirdNameSearch);
    }

    /**
     * Etsii linnun koko nimea vastaavan id:n
     *
     * @param bird koko nimi
     * @return id tai -5 jos ei loydy
     */
    public int searchBirdId(String bird) {
        return SQLOperations.searchBirdId(bird, preparedBirdIdSearch);
    }

    /**
     * Etsii kalojen nimet, jotka alkavat annetulla merkkijonolla
     *
     * @param wordBegin
     * @return
     */
    public ArrayList<Kala> searchFish(String wordBegin) {
        return SQLOperations.searchFish(wordBegin, preparedFishNameSearch);
    }

    /**
     * Etsii kalan id:n Kun lisataan kalahavainto, kayttaja kirjoittaaa kalan
     * nimen, joten tarvitaan myos id.
     *
     * @param fish
     * @return palauttaa kalan nimea vastaavan id:n
     */
    public int searchFishId(String fish) {
        return SQLOperations.searchBirdId(fish, preparedFishIdSearch);
    }

    /**
     * Get list of fish photos
     *
     * @param fish Fish name
     * @return Arraylist of photos
     */
    public ArrayList<Kuva> getFishPic(String fish) {
        try {
            ArrayList<Kuva> tulos = SQLOperations.getPicture(searchFish(fish).get(0).getId(),
                    0, preparedGetFishPics);
            return tulos;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No such fish :dd");
        }
        return null;
    }

    public Kuva getFishPic(String fish, int id) {
        try {
            ArrayList<Kuva> tulos = SQLOperations.getPicture(searchFish(fish).get(0).getId(),
                    0, preparedGetFishPics);
            return tulos.get(id);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No such fish :dd");
        }
        return null;
    }

    /**
     * Get list of fish photos
     *
     * @param bird Bird name
     * @return Arraylist of photos
     */
    public ArrayList<Kuva> getBirdPic(String bird) {
        try {
            ArrayList<Kuva> tulos = SQLOperations.getPicture(0, searchBird(bird).get(0).getId(),
                    preparedGetBirdPics);
            return tulos;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No such börd :dd");
        }
        return null;
    }

    public Kuva getBirdPic(String bird, int id) {
        try {
            ArrayList<Kuva> tulos = SQLOperations.getPicture(0, searchBird(bird).get(0).getId(),
                    preparedGetBirdPics);
            return tulos.get(id);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No such börd :dd");
        }
        return null;
    }

    /**
     * Etsii kunnan/kunnat jotka vastaavat annettua sanan alkua
     *
     * @param townBegin
     * @return
     */
    public ArrayList<Kunta> searchTown(String townBegin) {
        return SQLOperations.searchTown(townBegin, preparedTownSearch);
    }

    /**
     * Lisaa parametrina annetun fongatun kalan tiedot tietokantaan. Jos sina
     * vuonna on jo kyseinen laji saatu, havainnot paivitetaan. Palauttaa
     * havainnon id:n
     *
     * @param fishCatch Kalahavainto, joka halutaan lisata
     * @return havainnon id
     */
    public int insertFishCatch(Kalahavainto fishCatch) {
        return SQLOperations.insertHavainto(fishCatch, con);
    }

    /**
     * Lisaa lintuhavainnon tietokantaan. Jos oli jo laji sina paivana,
     * havainto vain paivitetaan uusilla tiedoilla. Palauttaa havainnon id:n
     *
     * @param birdWatch, joka halutaan lisata
     * @return havainnon id
     */
    public int insertBirdWatch(Lintuhavainto birdWatch) {
        return SQLOperations.insertHavainto(birdWatch, con);
    }

    /**
     * Etsii havainnon id:n. Kalahavainnoissa riittaa, etta sina vuonna on saatu
     * kyseinen laji. Lintuhavainnoissa lintu pitaa olla havaittu samana
     * paivana, koska voidaa myos paivanpinnailla.
     *
     * @param havainto lintuhavainto tai kalahavainto
     * @return 0 jos ei ole havaintoa, -1 jos error, id > 0 jos loytyi
     */
    public int getHavaintoId(Havainto havainto) {
        return SQLOperations.havaintoIdIfAlreadyInTable(havainto, con);
    }

    /**
     * Poistaa havainnon ID:n perusteella
     *
     * @param id
     */
    public void deleteLintuHavainto(int id) {
//            SQLOperations.deleteBirdWatch(id, preparedBirdWatchDeleteById);
        executeGeneralUpdate("DELETE FROM lintuhavainto WHERE id = " + id);
    }

    public void deleteKalaHavainto(int id) {
//            SQLOperations.deleteFishCatch(id, , preparedFishCatchDeleteById);
        executeGeneralUpdate("DELETE FROM kalahavainto WHERE id = " + id);
    }

    public Lintuhavainto getBirdWatchById(int id) {
        ResultSet rs = commitGeneralQuery("SELECT * FROM lintuhavainto WHERE id = " + id + ";");
        return Operations.RStoLintuHavainto(rs);
    }

    public Kalahavainto getFishCatchById(int id) {
        ResultSet rs = commitGeneralQuery("SELECT * FROM kalahavainto WHERE id = " + id + ";");
        return Operations.RStoKalaHavainto(rs);
    }

    /**
     * Paivittaa havainnon annetulla havainnolla
     *
     * @param birdWatch
     */
    public void updateBirdWatch(Lintuhavainto birdWatch) {
        int id = SQLOperations.havaintoIdIfAlreadyInTable(birdWatch, con);
        if (id > 0) {
            SQLOperations.updateHavainto(birdWatch, id, con);
        } else {
            //TODO ei voida paivittaa, koska ei ole alunperin havaintoa tai error
        }
    }

    /**
     * Paivittaa havainnon annetulla havainnolla
     *
     * @param fishCatch uudet tiedot
     */
    public void updateFichCatch(Kalahavainto fishCatch) {
        int id = SQLOperations.havaintoIdIfAlreadyInTable(fishCatch, con);
        if (id > 0) {
            SQLOperations.updateHavainto(fishCatch, id, con);
        } else {
            //TODO ei voida paivittaa, koska ei ole alunperin havaintoa tai error
        }
    }

    /**
     * Lintujen lisays, id autogeneroidaan Lisataan vain ne linnut, jotka
     * puuttuivat tietokannasta
     *
     * @param birdArray on lista lisattavista linnuista
     */
    public void insertBird(ArrayList<Lintu> birdArray) {
        SQLOperations.insertObject(Operations.convertToInsertable(birdArray), con);
    }

    /**
     * Lisaa parametrina annetun linnun, mikali sita ei ole jo tietokannassa
     *
     * @param bird, joka aiotaan lisata
     */
    public void insertBird(Lintu bird) {
        ArrayList<Insertable> birdArray = new ArrayList<>();
        birdArray.add(bird);
        SQLOperations.insertObject(birdArray, con);
    }

    /**
     * Lisaa parametrina annetun kayttajan, mikali sita ei ole jo tietokannassa
     *
     * @param user, joka aiotaan lisata
     * @return kayttajan id tai -2 jos nimi on jo, tai -1 jos poikkeus
     */
    public int insertUser(Kayttaja user) {
        return SQLOperations.insertUser(user, insertUser, getUserId, con);
    }

    /**
     * Palauttaa kayttajan ID:n, mikali kayttajanimi ja salasana ovat oikeat
     *
     * @param user
     * @return id
     */
    public int logIn(Kayttaja user) {
        return SQLOperations.logIn(user, getUserId);
    }

    /**
     * Poistaa lajin/t tietokannasta
     *
     * @param name
     */
    public void removeSpecies(String name) {
        SQLOperations.removeSpecies(name, con);
    }

    /**
     * Poistaa kayttajan
     *
     * @param username
     */
    public void removeUser(String username) {
        SQLOperations.removeUser(username, con, deleteUser);
    }
    
    /**
     * Poistaa parametrina annetun kayttajan
     * Palauttaa onnistuiko poisto
     * 
     * @param user 
     * @return onnistuiko poisto
     */
    public boolean removeUser(Kayttaja user){
        return SQLOperations.removeUser(user, deleteUser);
    }

    /**
     * Lisaa parametrina annetun kunnan, mikali sita ei viela ole tietokannassa
     *
     * @param town
     */
    public void insertTown(Kunta town) {
        ArrayList<Insertable> townArray = new ArrayList<>();
        townArray.add(town);
        SQLOperations.insertObject(townArray, con);
    }

    /**
     * Lisaa parametrina annetut kunnat, mikali niita ei viela ole tietokannassa
     *
     * @param towns
     */
    public void insertTown(ArrayList<Kunta> towns) {
        SQLOperations.insertObject(Operations.convertToInsertable(towns), con);
    }

    /**
     * Lisaa parametrina annetun kalan, mikali sita ei viela ole tietokannassa
     *
     * @param fish, joka lisataan
     */
    public void insertFish(Kala fish) {
        ArrayList<Insertable> fishArray = new ArrayList<>();
        fishArray.add(fish);
        SQLOperations.insertObject(fishArray, con);
    }

    public void insertFish(ArrayList<Kala> fishArray) {
        SQLOperations.insertObject(Operations.convertToInsertable(fishArray), con);
    }

    /**
     * Palauttaa fongausindeksin Mikali tietokannassa on virheellisia monikoita,
     * ne poistetaan. Yhta kayttajaa, kalalajia ja vuotta kohti saa olla vain
     * yksi havainto. Pisin ja pisimmista uusin jaa tietokantaan.
     *
     * @param user, huom id oltava oikea id
     * @param vuosi, vuosi, jolta haetaan indeksi
     * @return int[0]:indeksi mm, int[1]:laji lkm, int[2]:kokonaispituus mm
     */
    public int[] getFishCatchIndex(Kayttaja user, int vuosi) {
        return SQLOperations.getFongoIndex(user, vuosi, preparedFishIndexSearch,
                preparedFishIndexCheck, preparedFishMaxLengthSearch, preparedFishCatchDuplicateDelete);
    }

    /**
     * Palauttaa parametrina annetun vuoden ja kayttajan havainnot
     * loppukayttajan tarvimassa JSON-formaatissa
     *
     * @param user
     * @param vuosi
     * @return
     */
    public String getFishCatchData(Kayttaja user, int vuosi) {
        ArrayList<Kalahavainto> catchArray = SQLOperations.getFishCatchData(user, vuosi, preparedFishCatchDataSearch);
        return Operations.arrayToJSON(catchArray, this);
    }

    /**
     * Poistaa kalahavainnon, jonka id on parametrina annettu. Palauttaa tiedon,
     * montako rivia poistettiin tai poikkeustapauksessa -1
     *
     * @param id kalahavainnon id
     * @param user
     * @return poistettujen rivien maara tai -1 poikkeustapauksessa
     */
    public int deleteFishCatch(int id, Kayttaja user) {
        return SQLOperations.deleteFishCatch(id, user, preparedFishCatchDeleteById);
    }

    /**
     * Palauttaa kalan id:ta vastaavan nimen.
     * Null, jos exception tai ei kalaa
     *
     * @param kalaid
     * @return kalan nimi tai null
     */
    public String getFishNameById(int kalaid) {
        return SQLOperations.getFishNameById(kalaid, preparedGetFishName);
    }

    /**
     * Palauttaa linnun id:ta vastaavan nimen 
     * Null, jos poikkeus tai ei lintua
     *
     * @param lintuid
     * @return linnun nimi tai null
     */
    public String getBirdNameById(int lintuid) {
        return SQLOperations.getBirdNameById(lintuid, preparedGetBirdName);
    }

    /**
     * Palauttaa kayttajan lintuhavainnot annetulta valilta JSON-muodossa
     *
     * @param alkuPaivamaara
     * @param loppuPaivamaara
     * @param user
     * @return havinnot JSON-muodossa
     */
    public String getBirdWatchJSON(Paivamaara alkuPaivamaara, Paivamaara loppuPaivamaara, Kayttaja user) {
        ArrayList<Lintuhavainto> birdArray = SQLOperations.getBirdWatchData(alkuPaivamaara, loppuPaivamaara, user, preparedGetBirdWatchData);
        return Operations.arrayToJSON(birdArray, this);
    }
    
    /**
     * Hakee kaikki vuodarit eli vuodenpinnat
     * @param user Käyttäjä kenen havainnot haetaan
     * @param vuosi Miltä vuodelta
     * @return 
     */
    public int getVuodarit(Kayttaja user, int vuosi){
        ArrayList<Lintu> vuodarit=
         SQLOperations.getVuodarit(user, vuosi, false, false, preparedGetVuodarit, preparedBirdNameSearch);
        return vuodarit.size();
    } 
    
    /**
     * Plauttaa lintuavavaintolistan annetun kuukauden lajihavainnoista
     * Palautetaan vain yksi havainto per laji
     * Listan pituus on siis kuukausipinnojen maara
     * 
     * @param user kayttaja, jonka havaintoja haetaan
     * @param year vuosi, jonka kuukausi
     * @param month kuukausi, jolta haetaan
     * @param eko, haetaanko vain ekopinnoja
     * @param sponde, haetaanko vain spondepinnoja
     * @return lista pinnakelpoisista havainnoista
     */
    public ArrayList<Lintuhavainto> getMonthBirdWatches(Kayttaja user, int year, int month,
            boolean eko, boolean sponde){
        ArrayList<Lintuhavainto> birdWatch
                =SQLOperations.getMonthBirdWatches(user, year, month,
                        eko, sponde, preparedMonthBirdWatchSearch);
        return birdWatch; 
    }
    
    /**
     * Hakee kaikkien paivien paivanpinnojen summan
     * Voidaan hakea myos vain eko/spondepinnoja
     * @param user
     * @param eko haetaanko vain ekopinnoja
     * @param sponde haetaanko vain spondepinnoja
     * @return paivanpinnojen summa
     */
    public int getCountDayBirdWatch(Kayttaja user, boolean eko, boolean sponde){
        int count=0;
        for(int month=1;month<=12;month++){
            for(int day=1;day<32;day++){
                if(month==2 && day==30){
                    break;
                }
                if((month==4 || month==6 || month==9 || month==11) && day==31){
                    break;
                }
                count+=getDayBirdWatches(user, month, day, eko, sponde).size();
            }
        }
        return count;
    }
    
    /**
     * Palauttaa listan pinnahavainnoista annetulta paivalta
     * Houm! kaikilta vuosilta (paivapinnailun tapa)
     * Vain yksi havainto per laji
     * 
     * @param user
     * @param month
     * @param day
     * @param eko haetaanko vain ekopinnat
     * @param sponde haetaanko vain spondepinnat
     * @return lista pinnahavainnoista
     */
    public ArrayList<Lintuhavainto> getDayBirdWatches(Kayttaja user, int month, int day, boolean eko, boolean sponde){
        return SQLOperations.getDayBirdWatches(user, month, day, eko, sponde, preparedDayBirdWatchSearch);
    }
}
