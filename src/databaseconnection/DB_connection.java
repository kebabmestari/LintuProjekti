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
    private final String user; 				//"root"
    private final String password;			//"mysli"

    private Connection con = null;

    /**
     * K#ytt#j#n
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

    /**
     * Kalan
     */
    private PreparedStatement preparedFishNameSearch = null;
    private PreparedStatement preparedFishIdSearch = null;
    private PreparedStatement preparedGetFishName = null;
    private PreparedStatement preparedGetFishPics = null;

    /**
     * Kalahavainnon preparaatit
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

    private PreparedStatement preparedTownSearch = null;

    public DB_connection(String host, String db_name, String user, String password) {
        DB_URI = "jdbc:mysql://" + host.trim() + "/" + db_name.trim();

        this.user = user;
        this.password = password;

        if (!createConnection()) {
            System.err.println("Yhteytt# ei voitu muodostaa");
            //TODO Mit# sitten?
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
     * Poista ehdottomasti, kun kaikki toimii!!!!!!! Vain testausta varten
     *
     * @return
     */
    public Connection getConnection() {
        //TODO poista kun kaikki toimii
        return con;
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

            return true;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
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
     * Muuttaa annetun listan tyypin Insertable mukaisesti, mik#li alkuper#inen
     * tyyppi toteuttaa rajapinnan.
     *
     * @param array
     * @return insertable array
     */
    public ArrayList<Insertable> convertToInsertable(ArrayList<?> array) {
        ArrayList<Insertable> insertableArray = new ArrayList<>();
        try {
            for (int i = 0; i < array.size(); i++) {
                insertableArray.add((Insertable) array.get(i));
            }
            return insertableArray;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     */
    public int getUserID(String username) {
        return SQLOperations.getUserID(username, con);
    }

    /**
     * Commits an SQL-query from a string
     *
     * @param sql
     */
    public ResultSet commitGeneralQuery(String sql) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            System.err.println("Virhe SQL:ss#");
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
     * yleisyysj#rjestyksess#
     *
     * @param wordBegin, linnun nimen alku
     * @return lista sopivista linnuista, pelk#t nimet
     */
    public ArrayList<Lintu> searchBird(String wordBegin) {
        return SQLOperations.searchBird(wordBegin, preparedBirdNameSearch);
    }

    /**
     * Etsii linnun koko nime# vastaavan id:n
     *
     * @param bird koko nimi
     * @return id tai -5 jos ei l#ydy
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
     * Etsii kalan id:n Kun lis#t##n kalahavainto, k#ytt#j# kirjoittaaa kalan
     * nimen, joten tarvitaan my#s id.
     *
     * @param fish
     * @return palauttaa kalan nime# vastaavan id:n
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
     * Lis## parametrina annetun fongatun kalan tiedot tietokantaan. Jos sin#
     * vuonna on jo kyseinen laji saatu, havainnot p#ivitet##n. Palauttaa
     * havainnon id:n
     *
     * @param fishCatch Kalahavainto, joka halutaan lis#t#
     * @return havainnon id
     */
    public int insertFishCatch(Kalahavainto fishCatch) {
        return SQLOperations.insertHavainto(fishCatch, con);
    }

    /**
     * Lis## lintuhavainnon tietokantaan. Mik#li oli n#hty jo laji sin# p#iv#n#,
     * havainto vain p#ivitet##n uusilla tiedoilla. Palauttaa havainnon id:n
     *
     * @param birdWatch, joka halutaan lis#t#
     * @return havainnon id
     */
    public int insertBirdWatch(Lintuhavainto birdWatch) {
        System.out.println(birdWatch.getId() + " " + birdWatch.getHavaitsija() + " " + birdWatch.getLintuId() + " " + birdWatch.getPaikka() + " " + birdWatch.getHavaitsija());
        return SQLOperations.insertHavainto(birdWatch, con);
    }

    /**
     * Etsii havainnon id:n. Kalahavainnoissa riitt##, ett# sin# vuonna on saatu
     * kyseinen laji. Lintuhavainnoissa lintu pit## olla havaittu samana
     * p#iv#n#, koska voidaa my#s p#iv#npinnailla.
     *
     * @param havainto lintuhavainto tai kalahavainto
     * @return 0 jos ei ole havaintoa, -1 jos error, id > 0 jos l#ytyi
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
     * P#ivitt## havainnon annetulla havainnolla
     *
     * @param birdWatch
     */
    public void updateBirdWatch(Lintuhavainto birdWatch) {
        int id = SQLOperations.havaintoIdIfAlreadyInTable(birdWatch, con);
        if (id > 0) {
            SQLOperations.updateHavainto(birdWatch, id, con);
        } else {
            //TODO ei voida p#ivitt##, koska ei ole alunperin havaintoa tai error
        }
    }

    /**
     * P#ivitt## havainnon annetulla havainnolla
     *
     * @param fishCatch uudet tiedot
     */
    public void updateFichCatch(Kalahavainto fishCatch) {
        int id = SQLOperations.havaintoIdIfAlreadyInTable(fishCatch, con);
        if (id > 0) {
            SQLOperations.updateHavainto(fishCatch, id, con);
        } else {
            //TODO ei voida p#ivitt##, koska ei ole alunperin havaintoa tai error
        }
    }

    /**
     * Lintujen lis#ys, id autogeneroidaan Lis#t##n vain ne linnut, jotka
     * puuttuivat tietokannasta
     *
     * @param birdArray on lista lis#tt#vist# linnuista
     */
    public void insertBird(ArrayList<Lintu> birdArray) {
        SQLOperations.insertObject(convertToInsertable(birdArray), con);
    }

    /**
     * Lis## parametrina annetun linnun, mik#li sit# ei ole jo tietokannassa
     *
     * @param lintu, joka aiotaan lis#t#
     */
    public void insertBird(Lintu bird) {
        ArrayList<Insertable> birdArray = new ArrayList<>();
        birdArray.add(bird);
        SQLOperations.insertObject(birdArray, con);
    }

    /**
     * Lis## parametrina annetun k#ytt#j#n, mik#li sit# ei ole jo tietokannassa
     *
     * @param user, joka aiotaan lis#t#
     * @return k#ytt#j#n id tai -2 jos nimi jo k#yt#ss# tai -1 jos poikkeus
     */
    public int insertUser(Kayttaja user) {
        return SQLOperations.insertUser(user, insertUser, getUserId, con);
    }

    /**
     * Palauttaa k#ytt#j#n ID:n, mik#li k#ytt#j#nimi ja salasana ovat oikeat
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
     * Poistaa k#ytt#j#n
     *
     * @param username
     */
    public void removeUser(String username) {
        SQLOperations.removeUser(username, con, deleteUser);
    }

    /**
     * Lis## parametrina annetun kunnan, mik#li sit# ei viel# ole tietokannassa
     *
     * @param town
     */
    public void insertTown(Kunta town) {
        ArrayList<Insertable> townArray = new ArrayList<>();
        townArray.add(town);
        SQLOperations.insertObject(townArray, con);
    }

    /**
     * Lis## parametrina annetut kunnat, mik#li niit# ei viel# ole tietokannassa
     *
     * @param towns
     */
    public void insertTown(ArrayList<Kunta> towns) {
        SQLOperations.insertObject(convertToInsertable(towns), con);
    }

    /**
     * Lis## parametrina annetun kalan, mik#li sit# ei viel# ole tietokannassa
     *
     * @param fish, joka lis#t##n
     */
    public void insertFish(Kala fish) {
        ArrayList<Insertable> fishArray = new ArrayList<>();
        fishArray.add(fish);
        SQLOperations.insertObject(fishArray, con);
    }

    public void insertFish(ArrayList<Kala> fishArray) {
        SQLOperations.insertObject(convertToInsertable(fishArray), con);
    }

    /**
     * Palauttaa fongausindeksin Mik#li tietokannassa on virheellisi# monikoita,
     * ne poistetaan. Yht# k#ytt#j##, kalalajia ja vuotta kohti saa olla vain
     * yksi havainto. Pisin ja pisimmist# uusin j## tietokantaan.
     *
     * @param user, huom id oltava oikea id
     * @param vuosi, vuosi, jolta haetaan indeksi#
     * @return fongausindeksi
     */
    public int[] getFishCatchIndex(Kayttaja user, int vuosi) {
        return SQLOperations.getFongoIndex(user, vuosi, preparedFishIndexSearch,
                preparedFishIndexCheck, preparedFishMaxLengthSearch, preparedFishCatchDuplicateDelete);
    }

    /**
     * Palauttaa parametrina annetun vuoden ja k#ytt#j#n havainnot
     * loppuk#ytt#j#n tarvimassa JSON-formaatissa KESKEN!!
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
     * montako rivi# poistettiin tai poikkeustapauksessa -1
     *
     * @param id kalahavainnon id
     * @return poistettujen rivien m##r# tai -1 poikkeustapauksessa
     */
    public int deleteFishCatch(int id, Kayttaja user) {
        return SQLOperations.deleteFishCatch(id, user, preparedFishCatchDeleteById);
    }

    /**
     * Palauttaa kalan id:t# vastaavan nimen Null, jos exception tai ei kalaa
     *
     * @param kalaid
     * @return kalan nimi tai null
     */
    public String getFishNameById(int kalaid) {
        return SQLOperations.getFishNameById(kalaid, preparedGetFishName);
    }

    /**
     * Palauttaa linnun id:t# vastaavan nimen Null, jos exception tai ei lintua
     *
     * @param lintuid
     * @return linnun nimi tai null
     */
    public String getBirdNameById(int lintuid) {
        return SQLOperations.getBirdNameById(lintuid, preparedGetBirdName);
    }

    /**
     * Palauttaa k#ytt#j#n lintuhavainnot annetulta v#lilt# JSON-muodossa
     *
     * @param alkuPaivamaara
     * @param loppuPaivamaara
     * @param user
     */
    public String getBirdWatchJSON(Paivamaara alkuPaivamaara, Paivamaara loppuPaivamaara, Kayttaja user) {
        ArrayList<Lintuhavainto> birdArray = SQLOperations.getBirdWatchData(alkuPaivamaara, loppuPaivamaara, user, preparedGetBirdWatchData);
        System.out.println(birdArray.size());
        return Operations.arrayToJSON(birdArray, this);
    }
}
