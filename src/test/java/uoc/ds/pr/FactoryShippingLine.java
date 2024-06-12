package uoc.ds.pr;


import uoc.ds.pr.util.DateUtils;

public class FactoryShippingLine {


    public static ShippingLinePR2 getShippingLine() throws Exception {
        ShippingLinePR2 theShippingLine;
        theShippingLine = new ShippingLinePR2Impl();


        ////
        //// SHIPS
        ////

        theShippingLine.addShip("shipId1", "Queen Mary 2", 500, 100, 200, 150, 12 );
        theShippingLine.addShip("shipId2", "Carnival Vista", 600, 100, 200, 150, 23 );
        theShippingLine.addShip("shipId3", "Celebrity Edge", 600, 100, 200, 150, 32 );
        theShippingLine.addShip("shipId4", "Norwegian Escape", 10, 2, 12, 12, 7 );
        theShippingLine.addShip("shipId5", "Harmony of the Seas", 700, 100, 200, 150, 27 );
        theShippingLine.addShip("shipId6", "MSC Meraviglia", 300, 100, 200, 150, 62 );
        theShippingLine.addShip("shipId7", "Costa Diadema", 130, 100, 200, 150, 52 );

        //////
        ////// PORTS
        //////
        theShippingLine.addPort("BCN", "Barcelona", "http://image1", "description1");
        theShippingLine.addPort("DZAAE", "Annaba", "http://image2", "description2");
        theShippingLine.addPort("DZORN", "Oran", "http://image3", "description3");
        theShippingLine.addPort("NOEGD", "Eigersund", "http://image4", "description4");
        theShippingLine.addPort("NOHOY", "Husoy", "http://image5", "description5");
        theShippingLine.addPort("ROME", "Civitavecchia", "http://image6", "description6");
        theShippingLine.addPort("NAP", "Nápoles", "http://image7", "description7");
        theShippingLine.addPort("PAL", "Palermo", "http://image8", "description8");
        theShippingLine.addPort("ATH", "Atenas", "http://image9", "description9");
        theShippingLine.addPort("IBZ", "Ibiza", "http://image10", "description10");
        theShippingLine.addPort("MLG", "Málaga", "http://image10", "description10");
        theShippingLine.addPort("TAN", "Tánger", "http://image11", "description11");
        theShippingLine.addPort("GEN", "Génova", "http://image12", "description12");
        theShippingLine.addPort("EST", "Estambul", "http://image13", "description13");
        theShippingLine.addPort("CAG", "Cagliari", "http://image14", "description14");
        theShippingLine.addPort("MAL", "Malta", "http://image15", "description15");
        theShippingLine.addPort("MSLL", "Marsella", "http://image15", "description15");
        theShippingLine.addPort("TUN", "Túnez", "http://image16", "description16");
        theShippingLine.addPort("SPT", "Split", "http://image17", "description17");
        theShippingLine.addPort("DBK", "Dubrovnik", "http://image18", "description18");
        theShippingLine.addPort("TARR", "Tarragona", "http://image19", "description19");
        theShippingLine.addPort("VLC", "Valencia", "http://image20", "description20");
        theShippingLine.addPort("RAP", "La Ràpita", "http://image21", "description21");


        ////
        //// ROUTES
        ////
        theShippingLine.addRoute("routeId1", "ATH", "ROME", 1048);
        theShippingLine.addRoute("routeId2", "ROME", "TUN", 587);
        theShippingLine.addRoute("routeId3", "NAP", "PAL", 309);
        theShippingLine.addRoute("routeId4", "ATH", "PAL", 674);
        theShippingLine.addRoute("routeId5", "ATH", "IBZ", 674);
        theShippingLine.addRoute("routeId6", "MLG", "TAN", 132);
        theShippingLine.addRoute("routeId7", "GEN", "EST", 1609);
        theShippingLine.addRoute("routeId8", "CAG", "MAL", 505);
        theShippingLine.addRoute("routeId9", "SPT", "DBK", 164);
        theShippingLine.addRoute("routeId10", "BCN", "DBK", 164);
        theShippingLine.addRoute("routeId11", "BCN", "TARR", 100);
        theShippingLine.addRoute("routeId12", "TARR", "RAP", 69);
        theShippingLine.addRoute("routeId13", "RAP", "VLC", 169);
        theShippingLine.addRoute("routeId14", "BCN", "IBZ", 209);
        theShippingLine.addRoute("routeId15", "IBZ", "VLC", 208);



        ///
        /// Clients
        ///
        theShippingLine.addClient("clientId1", "Maria", "Simó");
        theShippingLine.addClient("clientId2", "Àlex", "Lluna ");
        theShippingLine.addClient("clientId3", "Pepet", "Ferra");
        theShippingLine.addClient("clientId4", "Joana", "Quilez");
        theShippingLine.addClient("clientId5", "Armand", "Morata");
        theShippingLine.addClient("clientId6", "Rut", "Paramio");
        theShippingLine.addClient("clientId7", "Miriam", "Navarro");
        theShippingLine.addClient("clientId8", "Pedro", "Tirrano");
        theShippingLine.addClient("clientId9", "Pedro", "Barón");
        theShippingLine.addClient("clientId10", "Emily", "Jones");
        theShippingLine.addClient("clientId11", "Maria", "Pérez");
        theShippingLine.addClient("clientId12", "Josep", "López");
        theShippingLine.addClient("clientId13", "Rafael", "Vidal");
        theShippingLine.addClient("clientId14", "Joel", "Gràcia");
        theShippingLine.addClient("clientId15", "Josep", "Martí");
        theShippingLine.addClient("clientId16", "Pere", "Jila");
        theShippingLine.addClient("clientId17", "Luis", "Blasco");
        theShippingLine.addClient("clientId18", "Jesus", "Linda");


        ///
        /// VOYAGES
        ///
        theShippingLine.addVoyage("voyageId1", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId4", "routeId1");

        theShippingLine.addVoyage("voyageId2", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId2", "routeId2");

        theShippingLine.addVoyage("voyageId3", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId3", "routeId2");



        return theShippingLine;
    }



}